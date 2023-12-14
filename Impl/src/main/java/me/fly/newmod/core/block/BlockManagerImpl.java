package me.fly.newmod.core.block;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.blockstorage.StoredBlock;
import me.fly.newmod.core.util.PersistentDataUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockManagerImpl implements BlockManager {
    public static final NamespacedKey ID = new NamespacedKey(NewModPlugin.get(), "id");

    private final Map<NamespacedKey, ModBlock> registry = new LinkedHashMap<>();

    @Override
    public void registerBlock(ModBlock block) {
        registry.put(block.getId(), block);
    }

    @Override
    public ModBlock getType(Block block) {
        return getType(block.getLocation());
    }

    @Override
    public ModBlock getType(Location block) {
        BlockStorage storage = NewModPlugin.get().blockStorage();

        if(block == null) {
            return null;
        }

        StoredBlock b = storage.getBlock(block);

        if(!b.hasData(ID, BlockStorage.StorageType.BLOCK_DATA)) {
            return null;
        }

        NamespacedKey id = PersistentDataUtil.namespacedKeyFromPrimitive(b.getData(ID, BlockStorage.StorageType.BLOCK_DATA));

        return registry.get(id);
    }

    @Override
    public ModBlock getType(NamespacedKey key) {
        return registry.get(key);
    }

    @Override
    public void setBlock(Block block, ModBlock type) {
        StoredBlock sb = NewModPlugin.get().blockStorage().getBlock(block.getLocation());

        sb.removeAllData(BlockStorage.StorageType.BLOCK_DATA);
        sb.setData(ID, type.getId().toString(), BlockStorage.StorageType.BLOCK_DATA);
    }
}
