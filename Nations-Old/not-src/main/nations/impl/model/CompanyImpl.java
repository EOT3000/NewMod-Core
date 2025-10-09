package me.bergenfly.nations.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Company;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.util.IdUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CompanyImpl extends AbstractLedPlayerGroup implements Company {
    private String name;
    private final String firstName;
    private final long creationTime;

    private final String id;

    private static Registry<Company, String> COMPANIES;

    public CompanyImpl(String name, String firstName, User leader, long creationTime) {
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;

        this.id = IdUtil.settlementId1(firstName, creationTime);

        setLeader(leader);
    }

    public static CompanyImpl tryCreate(String name, User leader) {
        if(COMPANIES == null) {
            COMPANIES = NationsPlugin.getInstance().companiesRegistry();
        }

        CompanyImpl s = new CompanyImpl(name, name, leader, System.currentTimeMillis());

        if(COMPANIES.get(name) != null) {
            return null;
        }

        COMPANIES.set(name, s);

        NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return s;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public String getFullName() {
        return "Company " + name.replaceAll("_", " ");
    }

    @Override
    public void sendInfo(CommandSender user) {
        user.sendMessage(ChatColor.GOLD + "--- [ " + ChatColor.YELLOW + getFullName() + ChatColor.GOLD +" ] ---");
        user.sendMessage(ChatColor.DARK_AQUA + "Leader: " + ChatColor.AQUA + leader.getName());

        String members = "Leader " + leader.getName();

        for(User member : getMembers()) {
            if(!member.equals(leader)) {
                members += (", " + member.getName());
            }
        }

        String membersOnline = "Leader " + leader.getName();

        for(User member : getOnlineMembers()) {
            if(!member.equals(leader)) {
                membersOnline += (", " + member.getName());
            }
        }

        //TODO: for all player groups, include a count of members as well

        user.sendMessage(ChatColor.DARK_AQUA + "Members: " + ChatColor.AQUA + members);
        user.sendMessage(ChatColor.DARK_AQUA + "Online Members: " + ChatColor.AQUA + membersOnline);
    }

    @Override
    public boolean setName(String newName) {
        String oldName = this.name;

        if(COMPANIES == null) {
            COMPANIES = NationsPlugin.getInstance().companiesRegistry();
        }

        if(COMPANIES.get(newName) != null) {
            return false;
        }

        COMPANIES.set(oldName, null);

        this.name = newName;

        COMPANIES.set(newName, this);

        NationsPlugin.getInstance().permissionManager().registerHolder(this, oldName);

        return true;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }


}
