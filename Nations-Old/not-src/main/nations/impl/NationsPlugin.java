package me.bergenfly.nations.impl;

import me.bergenfly.nations.api.NationsAPI;
import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.manager.NationsPermissionManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.*;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.command.community.SettlementCommand;
import me.bergenfly.nations.command.company.CompanyCommand;
import me.bergenfly.nations.command.nation.NationCommand;
import me.bergenfly.nations.command.plot.PlotCommand;
import me.bergenfly.nations.command.community.CommunityCommand;
import me.bergenfly.nations.command.community.TribeCommand;
import me.bergenfly.nations.listener.PlotListener;
import me.bergenfly.nations.model.UserImpl;
import me.bergenfly.nations.registry.RegistryImpl;
import me.bergenfly.nations.registry.StringRegistryImpl;
import me.bergenfly.nations.save.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NationsPlugin extends JavaPlugin implements NationsAPI, Listener, PlayerGroup {

    //private static final org.slf4j.Logger log = LoggerFactory.getLogger(NationsPlugin.class);
    private static NationsPlugin instance = null;

    private boolean enabled = false;

    private Registry<Nation, String> NATIONS;
    private Registry<Community, String> COMMUNITIES;
    private Registry<User, UUID> USERS;
    private Registry<Company, String> COMPANIES;
    private Registry<LandPermissionHolder, String> PERMISSION_HOLDERS_ID;

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

        this.NATIONS = new StringRegistryImpl<>(Nation.class);
        this.COMMUNITIES = new StringRegistryImpl<>(Community.class);
        this.USERS = new RegistryImpl<>(User.class);
        this.COMPANIES = new RegistryImpl<>(Company.class);
        this.PERMISSION_HOLDERS_ID = new RegistryImpl<>(LandPermissionHolder.class);
        this.landManager = new NationsLandManager();
        this.permissionManager = new NationsPermissionManager();

        try {
            LoadUser.loadUsers();
            LoadCommunity.loadCommunities();
            LoadNation.loadNations();
            LoadPlot.loadPlots();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            logger.severe("Encountered an error loading nations plugin, must disable: " + e.toString());
            return;
        }

        Bukkit.getPluginCommand("community").setExecutor(new CommunityCommand("community"));
        Bukkit.getPluginCommand("settlement").setExecutor(new SettlementCommand());
        Bukkit.getPluginCommand("tribe").setExecutor(new TribeCommand());
        Bukkit.getPluginCommand("nation").setExecutor(new NationCommand());
        Bukkit.getPluginCommand("plot").setExecutor(new PlotCommand());
        Bukkit.getPluginCommand("company").setExecutor(new CompanyCommand());

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlotListener(), this);
    }

    @Override
    public void onDisable() {
        SavePlot.savePlots();
        SaveNation.saveNations();
        SaveCommunity.saveCommunities();
        SaveUser.saveUsers();
    }

    @Override
    public Registry<Nation, String> nationsRegistry() {
        return NATIONS;
    }

    @Override
    public Registry<Community, String> communitiesRegistry() {
        return COMMUNITIES;
    }

    @Override
    public Registry<User, UUID> usersRegistry() {
        return USERS;
    }

    @Override
    public Registry<Company, String> companiesRegistry() {
        return COMPANIES;
    }

    @Override
    public Registry<LandPermissionHolder, String> permissionHoldersByIdRegistry() {
        return PERMISSION_HOLDERS_ID;
    }

    @Override
    public NationsLandManager landManager() {
        return landManager;
    }

    @Override
    public NationsPermissionManager permissionManager() {
        return permissionManager;
    }

    @Override
    public void sendInfo(CommandSender user) {

    }

    @Override
    public Set<User> getMembers() {
        return new HashSet<>(USERS.list());
    }

    @Override
    public Set<User> getOnlineMembers() {
        return USERS.list().stream().filter(User::isOnline).collect(Collectors.toSet());
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
            USERS.set(uuid, new UserImpl(uuid));
        } else {
            user.updateName();
        }
    }

    @EventHandler
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
    }

}
