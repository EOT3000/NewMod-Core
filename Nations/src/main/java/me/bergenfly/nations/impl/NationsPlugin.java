package me.bergenfly.nations.impl;

import me.bergenfly.nations.api.NationsAPI;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.command.settlement.SettlementCommand;
import me.bergenfly.nations.impl.registry.RegistryImpl;
import me.bergenfly.nations.impl.registry.StringRegistryImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class NationsPlugin extends JavaPlugin implements NationsAPI {

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

        this.NATIONS = new StringRegistryImpl<>(Nation.class);
        this.SETTLEMENTS = new StringRegistryImpl<>(Settlement.class);
        this.USERS = new RegistryImpl<>(User.class);

        new SettlementCommand();
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
}
