package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;

import java.util.Set;

/**
 * Represents a group of players.
 */
public interface PlayerGroup {
    /**
     * Sends a message to all online members of the group.
     *
     * @param s the message to send.
     */
    void broadcastString(String s);

    //TODO: void broadcastTranslatable(String s);

    //TODO: void broadcastComponent();

    /**
     * Gets all the members of this group, including those offline.
     *
     * @return a set of {@link User}s who are members of this group.
     */
    Set<User> getMembers();

    /**
     * Gets all the members of this group who are currently online.
     *
     * @return a set of {@link User}s who are members of this group and currently online.
     */
    Set<User> getOnlineMembers();
}
