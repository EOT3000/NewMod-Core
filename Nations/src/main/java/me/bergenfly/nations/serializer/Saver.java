package me.bergenfly.nations.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.bergenfly.nations.registry.Registry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class Saver {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T extends Serializable> void saveToFile(T serializable, File file, Class<T> clazz) {
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try(FileOutputStream writer = new FileOutputStream(file)) {
            Object simplified = simplify(serializable);

            String output = gson.toJson(simplified);

            writer.write(output.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> void saveToFile(List<T> serializable, File file, Class<T> clazz) {
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try(FileOutputStream writer = new FileOutputStream(file)) {
            Object simplified = simplify(serializable);

            String output = gson.toJson(simplified);

            writer.write(output.getBytes(StandardCharsets.UTF_8));
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

        S[] list = loadFromFile(file, (Class<S[]>) clazz.arrayType());

        for(S s : list) {
            ret.add(converter.apply(s));
        }

        return ret;
    }



    public static <FINAL extends Serializable, RAW, INTERMEDIATE> Set<FINAL> loadValuesFromFileArray(File file, Class<RAW> clazz, Function<RAW,INTERMEDIATE[]> arrayGetter, Function<INTERMEDIATE, FINAL> converter) {
        Set<FINAL> ret = new HashSet<>();

        if(!file.exists()) {
            return ret;
        }

        INTERMEDIATE[] intermediates = arrayGetter.apply(loadFromFile(file, clazz));

        for(INTERMEDIATE intermediate : intermediates) {
            ret.add(converter.apply(intermediate));
        }

        return ret;
    }


    public static <T extends Serializable, S> Set<T> loadFromDirectory(File dir, Class<S> clazz, Function<S, T> converter) {
        Set<T> ret = new HashSet<>();

        if(!dir.exists()) {
            dir.mkdirs();
        }

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

    public static <S extends Serializable, T extends S, K> Set<T> addToRegistryById(Set<T> set, Registry<S, K> registry, Function<String, K> converter) {
        for(T t : set) {
            registry.set(converter.apply(t.getId()), t);
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
        try(FileReader stream = new FileReader(file)) {
            return gson.fromJson(stream, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //todo https://stackoverflow.com/questions/60291720/java-parse-yaml-to-json to save as yaml
}
