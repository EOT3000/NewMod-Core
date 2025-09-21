package me.bergenfly.newmod.core.blockreplacer.nms.wrapper;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

//
public class FixedWrappedBlockData extends WrappedBlockData {

    public FixedWrappedBlockData(Object handle) {
        super(handle);
    }

    public FixedWrappedBlockData(Material material, int data) {
        super(CraftMagicNumbers.getBlock(material, (byte) data));
    }

    @Override
    public Material getType() {
        return CraftMagicNumbers.getMaterial(((BlockState) getHandle()).getBlock());
    }

    @Override
    public int getData() {
        return ((Number)CraftMagicNumbers.toLegacyData((BlockState) getHandle())).intValue();
    }

    @Override
    public void setType(Material material) {
        Block block = CraftMagicNumbers.getBlock(material);
        this.setHandle(block.defaultBlockState());
    }

    @Override
    public void setData(int data) {
        this.setTypeAndData(this.getType(), data);
    }

    @Override
    public void setTypeAndData(Material material, int data) {
        this.setHandle(CraftMagicNumbers.getBlock(material, (byte) data));
    }

    @Override
    public Object getHandle() {
        return super.getHandle();
    }

    @Override
    public void setHandle(Object handle) {
        super.setHandle(handle);
    }

    @Override
    public WrappedBlockData deepClone() {
        return new FixedWrappedBlockData(this.handle);
    }
}
