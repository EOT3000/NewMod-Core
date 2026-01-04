package me.bergenfly.nations.serializer;

import me.bergenfly.nations.registry.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.function.Function;

public class Saver {
    private static final Yaml yaml = new Yaml();

    public static <T extends Serializable> void saveToFile(T serializable, File file, Class<T> clazz) {
        try(FileWriter writer = new FileWriter(file)) {
            Object object = serializable.serialize();

            Object simplified = simplify(object);

            yaml.dump(simplified, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object simplify(Object serializable) {
        if(serializable instanceof Serializable s) {
            return simplify(s.serialize());
        }

        if(serializable instanceof Collection<?> c) {
            List<Object> refined = new ArrayList<>();

            for(Object element : c) {
                refined.add(simplify(element));
            }

            return refined;
        }

        if(serializable instanceof Map<?,?> m) {
            Map<Object, Object> refined = new HashMap<>();

            for(Object key : m.keySet()) {
                refined.put(key, simplify(m.get(key)));
            }

            return refined;
        }

        return serializable;
    }

    public static <T extends Serializable, S> Set<T> loadValuesFromFile(File file, Class<S> clazz, Function<S, T> converter) {
        Set<T> ret = new HashSet<>();

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for(String key : configuration.getKeys(false)) {
            ConfigurationSection chunk = configuration.getSerializable(key);


        }
    }

    public static <T extends Serializable, S> Set<T> loadFromDirectory(File dir, Class<S> clazz, Function<S, T> converter) {
        Set<T> ret = new HashSet<>();

        for(File file : dir.listFiles()) {
            T t = converter.apply(loadFromFile(file, clazz));

            ret.add(t);
        }

        return ret;
    }

    public static <S extends Serializable, T extends S> Set<T> addToRegistryById(Set<T> set, Registry<S, String> registry) {
        for(T t : set) {
            registry.set(t.getId(), t);
        }

        return set;
    }

    public static <S extends Serializable, T extends S> Set<T> addToRegistryByName(Set<T> set, Registry<S, String> registry) {
        for(T t : set) {
            registry.set(t.getName(), t);
        }

        return set;
    }

    public static <T> T loadFromFile(File file, Class<T> clazz) {
        try(FileInputStream stream = new FileInputStream(file)) {
            return yaml.loadAs(stream, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
