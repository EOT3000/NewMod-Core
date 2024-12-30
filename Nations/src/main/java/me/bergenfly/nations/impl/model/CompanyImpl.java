package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Company;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CompanyImpl extends AbstractLedPlayerGroup implements Company {
    private String name;
    private String firstName;

    public CompanyImpl(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void sendInfo(CommandSender user) {

    }

    @Override
    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getId() {
        return "company_" + firstName.toLowerCase();
    }


}
