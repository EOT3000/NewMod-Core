package me.bergenfly.nations.serializer.type;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.plot.Lot;
import me.bergenfly.nations.registry.Registry;

import java.util.List;
import java.util.UUID;

//TODO try with UUIDs
public record TownDeserialized(String name, String leader, String[] residents, String[] outlaws, String initialName, long creationTime, String founder, Lot[] lots) {
    private static Registry<User, UUID> USERS;

    public User getLeader() {
        if(USERS == null) {
            USERS = NationsPlugin.getInstance().usersRegistry();
        }

        return USERS.get(UUID.fromString(leader));
    }
}
