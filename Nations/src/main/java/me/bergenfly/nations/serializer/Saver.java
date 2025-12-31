package me.bergenfly.nations.serializer;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

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

    public static <T extends Serializable> T loadFromFile(File file, Class<T> clazz) {
        try(FileInputStream stream = new FileInputStream(file)) {
            return yaml.loadAs(stream, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
