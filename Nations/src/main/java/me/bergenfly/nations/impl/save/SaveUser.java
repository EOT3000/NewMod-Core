package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class SaveUser {
    public static YamlConfiguration userToMap(User user) {
        YamlConfiguration userSave = new YamlConfiguration();

        userSave.set("name", user.getName());
        userSave.set("uuid", user.getUniqueId().toString());

        return userSave;
    }

    public static void saveUsers() {
        for(User user : NationsPlugin.getInstance().usersRegistry().list()) {
            try {
                userToMap(user).save(new File("plugins/Nations/users/" + user.getUniqueId() + ".yml"));
            } catch (Exception e) {
                NationsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error trying to save user " + user.getName() + "(uuid " + user.getUniqueId() + ")");
                e.printStackTrace();
                NationsPlugin.getInstance().getLogger().log(Level.WARNING, "-----------------------------------");
            }
        }
    }
}
