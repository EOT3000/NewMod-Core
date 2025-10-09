package me.bergenfly.nations.api.permission;

import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum DefaultNationPermission implements NationPermission {

    INVITE_SETTLEMENT("invite_settlement"),             //Invite a settlement to the nation

    KICK_SETTLEMENT("kick_settlement"),                 //Kick a settlement from the nation

    DIPLOMACY("diplomacy"),                             //Change relations between your and other nations

    MANAGEMENT("management"),                           //Modify ranks and departments within the nation

    BAN("ban"),                                         //Ban a user from the nation

    TERRITORY("territory")                              //Claim or unclaim **nation** land

    ;
    //TODO move this
    private static Map<NamespacedKey, DefaultNationPermission> permissions = new HashMap<>();

    private final NamespacedKey key;

    DefaultNationPermission(String key) {
        this.key = new NamespacedKey(NationsPlugin.getInstance(), key);
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull String getName() {
        return name();
    }
}
