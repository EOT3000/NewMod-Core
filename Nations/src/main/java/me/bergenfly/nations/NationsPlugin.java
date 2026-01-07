package me.bergenfly.nations;


import me.bergenfly.nations.commands.community.TownCommand;
import me.bergenfly.nations.commands.nation.NationCommand;
import me.bergenfly.nations.commands.plot.PlotCommand;
import me.bergenfly.nations.listener.PlotWandListener;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.manager.NationsPermissionManager;
import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.registry.Registry;
import me.bergenfly.nations.registry.RegistryImpl;
import me.bergenfly.nations.registry.StringRegistry;
import me.bergenfly.nations.serializer.Saver;
import me.bergenfly.nations.serializer.Serializable;
import me.bergenfly.nations.serializer.type.*;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;

public class NationsPlugin extends JavaPlugin implements Listener {

    private List<Triple<UUID, String, BooleanSupplier>> reminders = new ArrayList<>();

    //private static final org.slf4j.Logger log = LoggerFactory.getLogger(NationsPlugin.class);
    private static NationsPlugin instance = null;

    private boolean enabled = false;

    private StringRegistry<Nation> NATIONS;
    private StringRegistry<Town> COMMUNITIES;
    private Registry<User, UUID> USERS;
    //private Registry<Company, String> COMPANIES;
    private StringRegistry<Serializable> PERMISSION_HOLDER;

    private NationsPermissionManager permissionManager;

    private NationsLandManager landManager;

    public NationsPlugin() {
        if(instance != null) {
            throw new RuntimeException("Can't make another NationsPlugin instance");
        }

        instance = this;
    }

    @Override
    public void onEnable() {
        if(enabled) {
            throw new RuntimeException("Can't enable NationsPlugin twice");
        }

        this.enabled = true;

        Logger logger = getLogger();

        logger.info(ChatColor.DARK_AQUA + "---------------------------------------------");
        logger.info(ChatColor.RED + "Starting Factionals!");
        logger.info(ChatColor.DARK_AQUA + "---------------------------------------------");

        this.NATIONS = new StringRegistry<>(Nation.class);
        this.COMMUNITIES = new StringRegistry<>(Town.class);
        this.USERS = new RegistryImpl<>(User.class);
        //this.COMPANIES = new RegistryImpl<>(Company.class);
        //this.PERMISSION_HOLDERS_ID = new RegistryImpl<>(LandPermissionHolder.class);
        this.landManager = new NationsLandManager();
        this.permissionManager = new NationsPermissionManager();

        try {
            Set<Town> loadedTowns = Saver.loadFromDirectory(new File("./plugins/Nations/towns"), TownDeserialized.class, Town::new);
            Saver.addToRegistryById(Saver.addToRegistryByName(Saver.addToRegistryById(loadedTowns, COMMUNITIES), COMMUNITIES), PERMISSION_HOLDER);

            Set<Nation> loadedNations = Saver.loadFromDirectory(new File("./plugins/Nations/nations"), NationDeserialized.class, Nation::new);
            Saver.addToRegistryById(Saver.addToRegistryByName(Saver.addToRegistryById(loadedNations, NATIONS), NATIONS), PERMISSION_HOLDER);

            Set<ClaimedChunk> loadedChunks = Saver.loadValuesFromFileArray(new File("./plugins/Nations/chunks.json"), ChunkListDeserialized.class, ChunkListDeserialized::chunks, ClaimedChunk::new);
            Saver.addToRegistryById(loadedChunks, landManager.chunksRegistry(), Integer::parseInt);

            Set<User> loadedUsers = Saver.loadFromDirectory(new File("./plugins/Nations/users"), UserDeserialized.class, User::new);
            Saver.addToRegistryById(loadedUsers, USERS, UUID::fromString);


        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            logger.severe("Encountered an error loading nations plugin, must disable: " + e.toString());
            return;
        }

        Bukkit.getPluginCommand("town").setExecutor(new TownCommand());
        //Bukkit.getPluginCommand("settlement").setExecutor(new SettlementCommand());
        //Bukkit.getPluginCommand("tribe").setExecutor(new TribeCommand());
        Bukkit.getPluginCommand("nation").setExecutor(new NationCommand());
        Bukkit.getPluginCommand("plot").setExecutor(new PlotCommand());
        //Bukkit.getPluginCommand("company").setExecutor(new CompanyCommand());

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlotWandListener(), this);
    }

    @Override
    public void onDisable() {
        //SavePlot.savePlots();
        //SaveNation.saveNations();
        //SaveCommunity.saveCommunities();
        //SaveUser.saveUsers();

        for(Nation nation : NATIONS.list()) {
            Saver.saveToFile(nation, new File("./plugins/Nations/nations/" + nation.getId() + ".json"), Nation.class);
        }

        for(Town town : COMMUNITIES.list()) {
            Saver.saveToFile(town, new File("./plugins/Nations/towns/" + town.getId() + ".json"), Town.class);
        }

        for(User user : USERS.list()) {
            Saver.saveToFile(user, new File("./plugins/Nations/users/" + user.getId() + ".json"), User.class);
        }

        Saver.saveToFile(landManager.chunksRegistry().list(), new File("./plugins/Nations/chunks/chunks.json"), ClaimedChunk.class);
    }

    //Don't use this. Only internal code can use this
    @Deprecated
    public static NationsPlugin getInstance() {
        return instance;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        User user = USERS.get(uuid);

        if(user == null) {
            USERS.set(uuid, new User(uuid, event.getPlayer().getName()));
        } else {
            user.updateName();
        }

        for(Triple<UUID, String, BooleanSupplier> reminder : new ArrayList<>(reminders)) {
            if(reminder.getLeft().equals(uuid)) {
                if(!reminder.getRight().getAsBoolean()) {
                    reminders.remove(reminder);
                    continue;
                }

                event.getPlayer().sendMessage(reminder.getMiddle());

                Bukkit.getScheduler().runTaskLater(this, () -> {
                    if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
                        reminders.remove(reminder);
                    }
                }, 30*20);
            }
        }
    }

    /*@EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        PlotSection from = landManager.getPlotSectionAtLocation(event.getFrom());
        PlotSection to = landManager.getPlotSectionAtLocation(event.getTo());

        if (to == from) {
            return;
        }

        if (to == null) {
            event.getPlayer().sendTitle(ChatColor.DARK_GREEN + "Entering Wilderness", ChatColor.GREEN + "It's dangerous to go alone", 5, 25, 5);
        } else {
            LandAdministrator admin = to.getAdministrator();

            if(from != null) {
                LandAdministrator adminOld = from.getAdministrator();

                if(adminOld == admin) {
                    return;
                }
            }

            event.getPlayer().sendTitle(ChatColor.GOLD + "Entering " + ChatColor.YELLOW + admin.getName(), ChatColor.YELLOW + "oooo", 5, 25, 5);
        }
    }*/

    public void addReminder(UUID uuid, String string) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if(player.isOnline()) {
            player.getPlayer().sendMessage(string);
        } else {
            reminders.add(Triple.of(uuid, string, () -> true));
        }
    }

    public void addReminder(UUID uuid, String string, BooleanSupplier predicate) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if(player.isOnline()) {
            player.getPlayer().sendMessage(string);
        } else {
            reminders.add(Triple.of(uuid, string, predicate));
        }
    }

    public Registry<Town, String> communitiesRegistry() {
        return COMMUNITIES;
    }

    public Registry<Serializable, String> idHaverRegistry() {
        return PERMISSION_HOLDER;
    }

    public StringRegistry<Nation> nationsRegistry() {
        return NATIONS;
    }

    public NationsPermissionManager permissionManager() {
        return permissionManager;
    }

    public Registry<User, UUID> usersRegistry() {
        return USERS;
    }

    public NationsLandManager landManager() {
        return landManager;
    }
}

