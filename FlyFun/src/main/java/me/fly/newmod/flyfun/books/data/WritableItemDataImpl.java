package me.fly.newmod.flyfun.books.data;

import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.data.ModItemData;
import me.fly.newmod.core.api.item.data.ModItemDataSerializer;
import me.fly.newmod.core.util.PersistentDataUtil;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WritableItemDataImpl extends ModItemData.AbstractModItemMeta implements WritableItemData {
    private String[] text;
    private boolean signed;

    public WritableItemDataImpl(ModItem type, String[] text, boolean signed) {
        super(type);

        this.text = text.clone();
        this.signed = signed;
    }

    @Override
    public String[] getText() {
        return text;
    }

    @Override
    public void setText(String[] text) {
        this.text = text.clone();
    }

    @Override
    public boolean isSigned() {
        return signed;
    }

    @Override
    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    @Override
    public ModItemData cloneItem() {
        return new WritableItemDataImpl(getType(), text, signed);
    }

    public static class WritableItemDataSerializer implements ModItemDataSerializer<WritableItemDataImpl> {
        @Override
        public WritableItemDataImpl getData(ItemStack stack) {
            PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();

            //TODO: writable sizes

            String[] text = container.getOrDefault(TEXT, PersistentDataUtil.STRING_ARRAY, new String[1]);
            boolean signed = container.getOrDefault(SIGNED, PersistentDataType.BOOLEAN, false);

            ModItem item = FlyFunPlugin.get().api.itemManager().getType(stack);

            if(item == null) {
                return null;
            }

            return new WritableItemDataImpl(item, text, signed);
        }

        @Override
        public WritableItemDataImpl createDefaultData(ModItem type) {
            return new WritableItemDataImpl(type, new String[1], false);
        }

        @Override
        public void applyData(ItemStack stack, ModItemData data) {
            if(data instanceof WritableItemData wid) {
                ItemMeta meta = stack.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();

                container.set(TEXT, PersistentDataUtil.STRING_ARRAY, wid.getText());
                container.set(SIGNED, PersistentDataType.BOOLEAN, wid.isSigned());

                stack.setItemMeta(meta);
            }
        }

        @Override
        public boolean canSerialize(ModItemData data) {
            return data instanceof WritableItemData;
        }
    }
}
