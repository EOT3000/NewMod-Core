package me.bergenfly.nations.impl;

import me.bergenfly.nations.api.NationsAPI;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.command.settlement.SettlementCommand;
import me.bergenfly.nations.impl.model.UserImpl;
import me.bergenfly.nations.impl.registry.RegistryImpl;
import me.bergenfly.nations.impl.registry.StringRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;

public class NationsPlugin extends JavaPlugin implements NationsAPI, Listener {

    private static NationsPlugin instance = null;

    private boolean enabled = false;

    private Registry<Nation, String> NATIONS;
    private Registry<Settlement, String> SETTLEMENTS;
    private Registry<User, UUID> USERS;

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
        this.SETTLEMENTS = new StringRegistryImpl<>(Settlement.class);
        this.USERS = new RegistryImpl<>(User.class);

        Bukkit.getPluginCommand("settlement").setExecutor(new SettlementCommand());

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public Registry<Nation, String> nationsRegistry() {
        return NATIONS;
    }

    @Override
    public Registry<Settlement, String> settlementsRegistry() {
        return SETTLEMENTS;
    }

    @Override
    public Registry<User, UUID> usersRegistry() {
        return USERS;
    }

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
}