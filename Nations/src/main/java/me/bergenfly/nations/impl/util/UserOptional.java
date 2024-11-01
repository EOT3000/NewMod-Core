package me.bergenfly.nations.impl.util;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.PlayerGroup;

import java.util.Collection;
import java.util.function.Predicate;

//Potential memory leak :/    (I think). Try to close these whenever possible
public class UserOptional implements Predicate<> {
    private final Runnable remove;
    private final PlayerGroup group;
    private final User user;

    public UserOptional(User user, Collection<User> list, PlayerGroup group) {

    }


}
