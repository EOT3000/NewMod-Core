package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.UserImpl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LoadUser {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    public static User mapToUser(YamlConfiguration configuration, File file) {
        return new UserImpl(UUID.fromString(configuration.getString("uuid")), configuration.getString("name"));
    }

    public static void loadUsers() {
        File dir = new File("plugins/Nations/users");

        if(!dir.exists()) {
            return;
        }

        for(File file : dir.listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            User user = mapToUser(config, file);

            api.usersRegistry().set(user.getUniqueId(), user);
            api.permissionManager().registerHolder(user, null);
        }
    }
}
