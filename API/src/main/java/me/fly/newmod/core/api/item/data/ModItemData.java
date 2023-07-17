package me.fly.newmod.core.api.item.data;

import me.fly.newmod.core.api.item.ModItem;

import java.util.Objects;

/**
 * Represents data stored in a mod item.
 */
public interface ModItemData {
    /**
     * @return the mod item type of this data.
     */
    ModItem getType();

    /**
     * Clones this data.
     *
     * @return the cloned data.
     */
    ModItemData cloneItem();

    abstract class AbstractModItemMeta implements ModItemData {
        private final ModItem type;

        protected AbstractModItemMeta(ModItem type) {
            if(type == null) {
                throw new NullPointerException("Null mod type");
            }

            this.type = type;
        }

        @Override
        public ModItem getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbstractModItemMeta that = (AbstractModItemMeta) o;
            return Objects.equals(type, that.type);
        }
    }
}
