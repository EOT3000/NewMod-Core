package me.bergenfly.nations.model.plot;

import io.papermc.paper.plugin.PermissionManager;
import me.bergenfly.nations.NationsPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public enum DefaultPlotPermission implements PlotPermission {
    PLACE("place"),                                 //Place most blocks
    DESTROY("destroy"),                             //Destroy most blocks
    //TODO note, placing chest next to other chest can be a bug to access the other chest if you don't have perms in the other chunk
    CONTAINER("container"),                         //Open and modify containers
    SIGN("sign"),                                   //Modify signs
    VEHICLE("vehicle"),                             //Place boats and minecarts
    LEVER("lever"),                                 //Click buttons and flick levers
    DOOR("door"),                                   //Open/close trapdoors, doors, fence gates
    INTERACT("interact");                           //Other interactions

    //TODO move this
    private static Map<NamespacedKey, DefaultPlotPermission> permissions = new HashMap<>();

    private final NamespacedKey key;

    DefaultPlotPermission(String key) {
        this.key = new NamespacedKey(NationsPlugin.getInstance(), key);
    }

    //private final Set<Function<PlotPermissionContext, Event.Result>> overrideFunctions = new HashSet<>();

    /* *
     * Sets an override function for a plugin.
     *
     * @param predicate the predicate used
     */
    /*public void setPluginOverrideFunction(JavaPlugin plugin, Function<PlotPermissionContext, Event.Result> predicate) {
        plugin.getPluginMeta().getPluginSoftDependencies();
    }*/

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull String getName() {
        return name();
    }

    public static DefaultPlotPermission of(String s) {
        if(permissions.isEmpty()) {
            for(DefaultPlotPermission p : values()) {
                permissions.put(p.getKey(), p);
            }
        }

        return permissions.get(NamespacedKey.fromString(s));
    }
}
