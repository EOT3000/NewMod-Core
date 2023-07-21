package me.fly.newmod.flyfun.books.data;

import me.fly.newmod.core.api.item.data.ModItemData;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.NamespacedKey;

/**
 * Item data for writable mod items.
 */
public interface WritableItemData extends ModItemData {
    NamespacedKey TEXT = new NamespacedKey(FlyFunPlugin.get(), "text");
    NamespacedKey SIGNED = new NamespacedKey(FlyFunPlugin.get(), "signed");

    /**
     * @return the text of this data.
     */
    String[] getText();

    /**
     * Sets the text of this data.
     *
     * @param text the text.
     */
    void setText(String[] text);

    /**
     * @return if this item was signed (if it is signed, it should not be further edited in game).
     */
    boolean isSigned();

    /**
     * Sets this item to be signed, or unsign it.
     *
     * @param signed if this item should be signed.
     */
    void setSigned(boolean signed);
}
