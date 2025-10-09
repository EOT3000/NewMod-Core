package me.bergenfly.nations.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.PlayerGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPlayerGroup implements PlayerGroup {
    private final Set<User> members;

    public AbstractPlayerGroup(Set<User> members) {
        this.members = new HashSet<>(members);
    }

    public AbstractPlayerGroup() {
        this.members = new HashSet<>();
    }

    @Override
    public Set<User> getMembers() {
        return new HashSet<>(members);
    }

    @Override
    public Set<User> getOnlineMembers() {
        return members.stream().filter((a) -> a.getPlayer().isOnline()).collect(Collectors.toSet());
    }

    @Override
    public void addMember(User user) {
        members.add(user);
    }

    @Override
    public void removeMember(User user) {
        members.remove(user);
    }
}
