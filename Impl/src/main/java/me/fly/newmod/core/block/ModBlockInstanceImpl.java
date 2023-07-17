package me.fly.newmod.core.block;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.block.data.ModBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ModBlockInstanceImpl implements ModBlockInstance {
    private final Block representation;

    private final ModBlock type;
    private ModBlockData data;

    public ModBlockInstanceImpl(Location representation) {
        this(representation.getBlock());
    }

    //TODO: null checks

    public ModBlockInstanceImpl(Block representation) {
        BlockManager manager = NewModPlugin.get().blockManager();

        this.representation = representation;

        this.type = manager.getType(representation);
        this.data = manager.getData(representation);
    }

    public ModBlockInstanceImpl(ModBlock type) {
        BlockManager manager = NewModPlugin.get().blockManager();

        this.representation = null;

        this.type = type;
        this.data = manager.createDefaultData(type);
    }

    public ModBlockInstanceImpl(ModBlock type, ModBlockData data, Block representation) {
        this.representation = representation;

        this.type = type;
        this.data = data;
    }

    @Override
    public Block create(Location location) {
        if(representation == null || !location.equals(representation.getLocation())) {
            BlockManager manager = NewModPlugin.get().blockManager();
            Block block = location.getBlock();

            type.place(block, this);

            manager.applyData(block, data);

            return block;
        } else {
            return representation;
        }
    }

    @Override
    public boolean exists() {
        return representation != null;
    }

    @Override
    public ModBlock getType() {
        return type;
    }

    @Override
    public ModBlockData getData() {
        return data.cloneBlock();
    }

    @Override
    public void setData(ModBlockData data) {
        this.data = data;
    }

    @Override
    public void update() {
        if(exists()) {
            BlockManager manager = NewModPlugin.get().blockManager();

            manager.applyData(representation, data);
        }
    }

    @Override
    public String toString() {
        return "ModBlockInstanceImpl{" +
                "representation=" + representation +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}
