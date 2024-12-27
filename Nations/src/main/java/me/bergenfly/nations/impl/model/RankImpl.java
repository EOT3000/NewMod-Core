package me.bergenfly.nations.impl.model;

import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.*;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RankImpl extends AbstractLedPlayerGroup implements Rank {
    private final Nation nation;
    private final String name;
    private final String id;

    private final long creationTime;

    public RankImpl(String name, Nation nation) {
        this(name, nation, System.currentTimeMillis());
    }

    public RankImpl(String name, Nation nation, long creationTime) {
        this(name, null, nation, creationTime, IdUtil.rankId1(name, nation.getId(), creationTime));
    }

    public RankImpl(String name, User leader, Nation nation, long creationTime, String id) {
        this.nation = nation;
        this.name = name;
        this.id = id;
        this.creationTime = creationTime;

        if(leader != null) {
            setLeader(leader);
        }
    }

    private final Set<NationPermission> permissions = new HashSet<>();

    @Override
    public void setPermission(NationPermission permission) {
        permissions.add(permission);
    }

    @Override
    public void unsetPermission(NationPermission permission) {
        permissions.remove(permission);
    }

    @Override
    public boolean hasPermission(NationPermission permission) {
        return permissions.contains(permission);
    }

    @Override
    public Set<NationPermission> getPermissions() {
        return new HashSet<>(permissions);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @Nullable Nation getNation() {
        return nation;
    }

    @Override
    public boolean isLandManager(User user) {
        return Rank.super.isLandManager(user);
    }

    @Override
    public int effectivePriority(boolean allowed) {
        return Rank.super.effectivePriority(allowed);
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void sendInfo(CommandSender user) {
        //TODO convert to translation keys
        user.sendMessage(ChatColor.GOLD + "--- [ " + ChatColor.YELLOW + name.replaceAll("_", " ") + ChatColor.GOLD +" ] ---");
        user.sendMessage(ChatColor.DARK_AQUA + "Leader: " + ChatColor.AQUA + (getLeader() == null ? ChatColor.GRAY + "None" : getLeader().getName()));
        user.sendMessage(ChatColor.DARK_AQUA + "Nation: " + ChatColor.AQUA + getNation().getName());

        if(senderHasMoreInfo(user)) {
            String members = "";

            for(User member : getMembers()) {
                if(!member.equals(getLeader())) {
                    members += (", " + member.getName());
                }
            }

            members = getLeader() != null ? ("Leader " + getLeader().getName()) + members : members.replaceFirst(", ", "");

            String permissions = "";

            for(NationPermission permission : this.permissions) {
                permissions += ", " + permission.getName();
            }

            permissions = permissions.replaceFirst(", ", "");

            user.sendMessage(ChatColor.DARK_AQUA + "Members: " + ChatColor.AQUA + members);
            user.sendMessage(ChatColor.DARK_AQUA + "Permissions: " + ChatColor.AQUA + permissions);
        } else {
            user.sendMessage(ChatColor.DARK_AQUA + "Members: " + ChatColor.DARK_GRAY + "Hidden");
            user.sendMessage(ChatColor.DARK_AQUA + "Permissions: " + ChatColor.DARK_GRAY + "Hidden");
        }
    }

    private boolean senderHasMoreInfo(CommandSender user) {
        if(user instanceof Player) {
            User u = NationsPlugin.getInstance().usersRegistry().get(((Player) user).getUniqueId());

            return nation.getLeader() == u || getMembers().contains(u);
        }

        return user instanceof ConsoleCommandSender;
    }


    //DELETABLE

    private final Set<DeletionSubscriber> subscribers = new HashSet<>();
    private boolean deleted = false;

    @Override
    public void delete() {
        this.deleted = true;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void subscribeToDeletion(DeletionSubscriber subscriber) {
        subscribers.add(subscriber);
    }
}
