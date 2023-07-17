package me.fly.newmod.core.impl.blockstorage;

import me.fly.newmod.core.api.util.IntTriple;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

// A region of 32 by 32 chunks
public class RegionBlockStorage {
    public final int x;
    public final int z;
    public final World world;

    private boolean dirty = false;

    //public static final ImmutableList<String> ILLEGAL_KEYS = ImmutableList.<String>builder().add("x", "z").build();

    public RegionBlockStorage(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    //TODO: private this
    public final Map<IntTriple, MarkingHashMapWrapper> data = new HashMap<>();

    public void remove(IntTriple vector) {
        data.remove(vector);
    }

    public Set<NamespacedKey> getKeys(IntTriple vector) {
        return data.getOrDefault(vector, new MarkingHashMapWrapper()).keySet();
    }

    public Map<NamespacedKey, String> getValues(IntTriple vector) {
        data.putIfAbsent(vector, new MarkingHashMapWrapper());

        return data.get(vector);
    }

    public String getByKey(IntTriple vector, NamespacedKey key) {
        return data.getOrDefault(vector, new MarkingHashMapWrapper()).get(key);
    }

    public void modifyKey(IntTriple vector, NamespacedKey key, String value) {
        data.putIfAbsent(vector, new MarkingHashMapWrapper());

        data.get(vector).put(key, value);
    }

    public void removeKey(IntTriple vector, NamespacedKey key) {
        if(data.containsKey(vector)) {
            data.get(vector).remove(key);
        }
    }

    public Set<IntTriple> getAllLocations() {
        return data.keySet();
    }

    public void load(YamlConfiguration configuration, String file) {
        ConfigurationSection b = configuration.getConfigurationSection("blocks");

        if(b == null) {
            Bukkit.getLogger().severe("Error loading file " + file);
            return;
        }

        for(String rk : b.getKeys(false)) {
            ConfigurationSection sec = b.getConfigurationSection(rk);

            int x = sec.getInt("x");
            int y = sec.getInt("y");
            int z = sec.getInt("z");

            MarkingHashMapWrapper map = new MarkingHashMapWrapper();

            for(String data : sec.getKeys(false)) {
                String[] spl = data.split(":");

                NamespacedKey key = new NamespacedKey(spl[0], spl[1]);

                map.put(key, sec.getString(data));
            }

            this.data.put(new IntTriple(x, y, z), map);
        }
    }

    public void saveIfDirty() {
        if(dirty) {
            save();
        }
    }

    public void save() {
        File dir = new File("/plugins/NewMod/save/" + world.getName());
        File file = new File("/plugins/NewMod/save/" + world.getName() + "/r." + x + "." + z + ".yml");

        try {
            dir.mkdirs();
            file.createNewFile();
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error saving region " + x + "," + z + ":" + world.getName());

            e.printStackTrace();
        }

        YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("x", x);
        configuration.set("z", z);
        configuration.set("world", world.getName());

        Map<String, Map<String, String>> blocks = new HashMap<>();

        for(Map.Entry<IntTriple, MarkingHashMapWrapper> map : data.entrySet()) {
            if(map.getKey() == null) {
                Bukkit.getLogger().severe("Error saving null block in region " + x + "," + z + ":" + world.getName());
                continue;
            }

            if(map.getValue() == null) {
                Bukkit.getLogger().severe("Error saving block " + map.getKey().toString() + " in region " + x + "," + z + ":" + world.getName());
                continue;
            }

            String key = map.getKey().x + "," + map.getKey().y + "," + map.getKey().z;

            Map<String, String> keys = new HashMap<>(nkToS(map.getValue()));

            keys.put("n:x", Integer.toString(map.getKey().x));
            keys.put("n:y", Integer.toString(map.getKey().y));
            keys.put("n:z", Integer.toString(map.getKey().z));

            blocks.put(key, keys);
        }

        configuration.set("blocks", blocks);

        try {
            configuration.save(file);
            dirty = false;
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error saving region " + x + "," + z + ":" + world.getName());

            e.printStackTrace();
        }
    }

    private Map<String, String> nkToS(MarkingHashMapWrapper map) {
        Map<String, String> ret = new HashMap<>();

        for(Map.Entry<NamespacedKey, String> entry : map.entrySet()) {
            ret.put(entry.getKey().toString(), entry.getValue());
        }

        return ret;
    }

    //TODO: private this too
    public class MarkingHashMapWrapper implements Map<NamespacedKey, String> {
        private final HashMap<NamespacedKey, String> wrapped = new HashMap<>();

        @Override
        public String getOrDefault(Object key, String defaultValue) {
            return wrapped.getOrDefault(key, defaultValue);
        }

        @Override
        public void forEach(BiConsumer<? super NamespacedKey, ? super String> action) {
            wrapped.forEach(action);
        }

        @Override
        public void replaceAll(BiFunction<? super NamespacedKey, ? super String, ? extends String> function) {
            RegionBlockStorage.this.dirty = true;
            wrapped.replaceAll(function);
        }

        @Nullable
        @Override
        public String putIfAbsent(NamespacedKey key, String value) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.putIfAbsent(key, value);
        }

        @Override
        public boolean remove(Object key, Object value) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.remove(key, value);
        }

        @Override
        public boolean replace(NamespacedKey key, String oldValue, String newValue) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.replace(key, oldValue, newValue);
        }

        @Nullable
        @Override
        public String replace(NamespacedKey key, String value) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.replace(key, value);
        }

        @Override
        public String computeIfAbsent(NamespacedKey key, @NotNull Function<? super NamespacedKey, ? extends String> mappingFunction) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.computeIfAbsent(key, mappingFunction);
        }

        @Override
        public String computeIfPresent(NamespacedKey key, @NotNull BiFunction<? super NamespacedKey, ? super String, ? extends String> remappingFunction) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.computeIfPresent(key, remappingFunction);
        }

        @Override
        public String compute(NamespacedKey key, @NotNull BiFunction<? super NamespacedKey, ? super String, ? extends String> remappingFunction) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.compute(key, remappingFunction);
        }

        @Override
        public String merge(NamespacedKey key, @NotNull String value, @NotNull BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.merge(key, value, remappingFunction);
        }

        @Override
        public int size() {
            return wrapped.size();
        }

        @Override
        public boolean isEmpty() {
            return wrapped.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return wrapped.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return wrapped.containsValue(value);
        }

        @Override
        public String get(Object key) {
            return wrapped.get(key);
        }

        @Nullable
        @Override
        public String put(NamespacedKey key, String value) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.put(key, value);
        }

        @Override
        public String remove(Object key) {
            RegionBlockStorage.this.dirty = true;
            return wrapped.remove(key);
        }

        @Override
        public void putAll(@NotNull Map<? extends NamespacedKey, ? extends String> m) {
            RegionBlockStorage.this.dirty = true;
            wrapped.putAll(m);
        }

        @Override
        public void clear() {
            RegionBlockStorage.this.dirty = true;
            wrapped.clear();
        }

        @NotNull
        @Override
        public Set<NamespacedKey> keySet() {
            return wrapped.keySet();
        }

        @NotNull
        @Override
        public Collection<String> values() {
            return wrapped.values();
        }

        @NotNull
        @Override
        public Set<Entry<NamespacedKey, String>> entrySet() {
            return wrapped.entrySet();
        }
    }
}
