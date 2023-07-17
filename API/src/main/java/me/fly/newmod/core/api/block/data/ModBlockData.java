package me.fly.newmod.core.api.block.data;

import me.fly.newmod.core.api.block.ModBlock;

import java.util.Objects;

/**
 * Represents data stored in a mod block.
 */
public interface ModBlockData {
    /**
     * @return the mod block type of this data.
     */
    ModBlock getType();

    /**
     * Clones this data.
     *
     * @return the cloned data.
     */
    ModBlockData cloneBlock();

    abstract class AbstractModBlockData implements ModBlockData {
        private final ModBlock type;

        protected AbstractModBlockData(ModBlock type) {
            if(type == null) {
                throw new NullPointerException("Null mod type");
            }

            this.type = type;
        }

        @Override
        public ModBlock getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbstractModBlockData that = (AbstractModBlockData) o;
            return Objects.equals(type, that.type);
        }
    }
}
