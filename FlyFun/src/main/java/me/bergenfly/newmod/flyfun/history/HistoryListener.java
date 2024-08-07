package me.bergenfly.newmod.flyfun.history;

import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HistoryListener implements Listener {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();

    public static NamespacedKey LINE_1_NAMESPACE = new NamespacedKey(plugin, "line1");
    public static NamespacedKey LINE_2_NAMESPACE = new NamespacedKey(plugin, "line2");
    public static NamespacedKey LINE_3_NAMESPACE = new NamespacedKey(plugin, "line3");
    public static NamespacedKey LINE_4_NAMESPACE = new NamespacedKey(plugin, "line4");
    public static NamespacedKey COLOR_NAMESPACE = new NamespacedKey(plugin, "color");
    public static NamespacedKey GLOW_NAMESPACE = new NamespacedKey(plugin, "glow");

    public static NamespacedKey FRONT_NAMESPACE = new NamespacedKey(plugin, "front");
    public static NamespacedKey BACK_NAMESPACE = new NamespacedKey(plugin, "back");

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        //TODO: silk touch only
        if(Tag.ALL_SIGNS.isTagged(event.getBlock().getType())) {
            ItemStack stack = new ItemStack(event.getBlock().getDrops().iterator().next());
            ItemMeta meta = stack.getItemMeta();
            Sign sign = (Sign) event.getBlock().getState();

            event.setDropItems(false);

            PersistentDataContainer container = meta.getPersistentDataContainer();
            PersistentDataContainer frontContainer = container.getAdapterContext().newPersistentDataContainer();
            PersistentDataContainer backContainer = container.getAdapterContext().newPersistentDataContainer();

            saveSide(frontContainer, sign.getSide(Side.FRONT));
            saveSide(backContainer, sign.getSide(Side.BACK));

            container.set(FRONT_NAMESPACE, PersistentDataType.TAG_CONTAINER, frontContainer);
            container.set(BACK_NAMESPACE, PersistentDataType.TAG_CONTAINER, backContainer);

            stack.setItemMeta(meta);

            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
        }
    }

    private void saveSide(PersistentDataContainer container, SignSide side) {
        container.set(LINE_1_NAMESPACE, PersistentDataType.STRING, LegacyComponentSerializer.legacySection().serialize(side.line(0)));
        container.set(LINE_2_NAMESPACE, PersistentDataType.STRING, LegacyComponentSerializer.legacySection().serialize(side.line(1)));
        container.set(LINE_3_NAMESPACE, PersistentDataType.STRING, LegacyComponentSerializer.legacySection().serialize(side.line(2)));
        container.set(LINE_4_NAMESPACE, PersistentDataType.STRING, LegacyComponentSerializer.legacySection().serialize(side.line(3)));

        container.set(COLOR_NAMESPACE, PersistentDataType.STRING, side.getColor() != null ? side.getColor().name() : "none");
        container.set(GLOW_NAMESPACE, PersistentDataType.BOOLEAN, side.isGlowingText());
    }
}
