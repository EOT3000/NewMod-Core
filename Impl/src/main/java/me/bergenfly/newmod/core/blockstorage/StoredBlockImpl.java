package me.bergenfly.newmod.core.blockstorage;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.blockstorage.StoredBlock;
import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.util.PersistentDataUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StoredBlockImpl implements StoredBlock {
    public static final PersistentDataType<PersistentDataContainer, Map<NamespacedKey, String>> TYPE = DataType.asMap(PersistentDataUtil.NAMESPACED_KEY, DataType.STRING);

    public static final NamespacedKey ENVIR = new NamespacedKey(NewModPlugin.get(), "environment");
    public static final NamespacedKey BLOCK = new NamespacedKey(NewModPlugin.get(), "block");

    public StoredBlockImpl(Location location) {
        this.location = location;
    }

    private final Location location;

    private Map<NamespacedKey, String> get(BlockStorage.StorageType type) {
        return type == null ? null :
                new CustomBlockData(location.getBlock(), NewModPlugin.get()).getOrDefault(switch (type) {
                    case BLOCK_DATA -> BLOCK;
                    case ENVIRONMENTAL -> ENVIR;
                }, TYPE, new HashMap<>());
    }

    private void put(BlockStorage.StorageType type, Map<NamespacedKey, String> map) {
        new CustomBlockData(location.getBlock(), NewModPlugin.get()).set(switch (type) {
            case BLOCK_DATA -> BLOCK;
            case ENVIRONMENTAL -> ENVIR;
        }, TYPE, map);
    }

    @Override
    public boolean hasData(NamespacedKey key, BlockStorage.StorageType type) {
        return get(type).containsKey(key);
    }

    @Override
    public Set<NamespacedKey> getKeys(BlockStorage.StorageType type) {
        return new HashSet<>(get(type).keySet());
    }

    @Override
    public String getData(NamespacedKey key, BlockStorage.StorageType type) {
        return get(type).get(key);
    }

    @Override
    public void setData(NamespacedKey key, String value, BlockStorage.StorageType type) {
        Map<NamespacedKey, String> map = get(type);

        map.put(key, value);

        put(type, map);
    }

    @Override
    public void removeData(NamespacedKey key, BlockStorage.StorageType type) {
        Map<NamespacedKey, String> map = get(type);

        map.remove(key);

        put(type, map);
    }

    @Override
    public void removeAllData(BlockStorage.StorageType type) {
        new CustomBlockData(location.getBlock(), NewModPlugin.get()).set(switch (type) {
            case BLOCK_DATA -> BLOCK;
            case ENVIRONMENTAL -> ENVIR;
        }, TYPE, new HashMap<>());
    }
}
