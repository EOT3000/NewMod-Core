package me.fly.newmod.core.block;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.block.data.ModBlockData;
import me.fly.newmod.core.api.block.data.ModBlockDataSerializer;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.blockstorage.StoredBlock;
import me.fly.newmod.core.util.PersistentDataUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockManagerImpl implements BlockManager {
    public static final NamespacedKey ID = new NamespacedKey(NewModPlugin.get(), "id");

    private final Map<Class<? extends ModBlockData>, ModBlockDataSerializer<?>> serializers = new HashMap<>();
    private final Map<NamespacedKey, ModBlock> registry = new LinkedHashMap<>();

    @Override
    public void registerBlock(ModBlock block) {
        registry.put(block.getId(), block);
    }

    @Override
    public ModBlock getType(Block block) {
        BlockStorage storage = NewModPlugin.get().blockStorage();

        if(block == null) {
            return null;
        }

        StoredBlock b = storage.getBlock(block.getLocation());

        System.out.println("looked up block " + block.getLocation() + " it is a " + b + " and does it have an ID? " + b.hasData(ID, BlockStorage.StorageType.BLOCK_DATA));

        if(!b.hasData(ID, BlockStorage.StorageType.BLOCK_DATA)) {
            return null;
        }

        NamespacedKey id = PersistentDataUtil.namespacedKeyFromPrimitive(b.getData(ID, BlockStorage.StorageType.BLOCK_DATA));

        System.out.println("the ID is " + id + ", it is a " + registry.get(id));

        System.out.println();

        return registry.get(id);
    }

    @Override
    public ModBlock getType(NamespacedKey key) {
        return registry.get(key);
    }

    @Override
    public <T extends ModBlockData> void registerSerializer(ModBlockDataSerializer<T> serializer, Class<T> clazz) {
        serializers.put(clazz, serializer);
    }

    @Override
    public <T extends ModBlockData> T createDefaultData(ModBlock type) {
        if(type.getDataType() == null) {
            return null;
        }

        //noinspection unchecked
        return (T) serializers.get(type.getDataType()).createDefaultData(type);
    }

    @Override
    public ModBlockData getData(Block block) {
        ModBlock type = getType(block);

        if(type == null || type.getDataType() == null) {
            return null;
        }

        Class<? extends ModBlockData> clazz = type.getDataType();

        return serializers.get(clazz).getData(block);
    }

    @Override
    public boolean applyData(Block block, ModBlockData data) {
        ModBlock type = getType(block);

        if(type == null) {
            return false;
        }

        ModBlockDataSerializer<?> serializer = serializers.get(type.getDataType());

        if(serializer == null || !serializer.canSerialize(data)) {
            return false;
        }

        serializer.applyData(block, data);

        return true;
    }

    @Override
    public void setBlock(Block block, ModBlock type) {
        StoredBlock sb = NewModPlugin.get().blockStorage().getBlock(block.getLocation());

        sb.removeAllData(BlockStorage.StorageType.BLOCK_DATA);
        sb.setData(ID, type.getId().toString(), BlockStorage.StorageType.BLOCK_DATA);
    }

    @Override
    public ModBlockInstance from(Block block) {
        return new ModBlockInstanceImpl(block);
    }
}
