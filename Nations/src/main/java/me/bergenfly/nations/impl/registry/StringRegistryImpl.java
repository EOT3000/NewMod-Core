package me.bergenfly.nations.impl.registry;

import me.bergenfly.nations.api.registry.Registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A wrapper to allow for a case-insensitive string registry
 *
 * @param <V> the value type
 */
public class StringRegistryImpl<V> implements Registry<V, String> {
    private RegistryImpl<V, String> registry;

    public StringRegistryImpl(Class<V> clazz) {
        registry = new RegistryImpl<>(clazz);
    }

    @Override
    public V get(String key) {
        return registry.get(key.toLowerCase());
    }

    @Override
    public void set(String key, V value) {
        registry.set(key.toLowerCase(), value);
    }

    @Override
    public List<V> list() {
        return registry.list();
    }

    @Override
    public Map<String, V> map() {
        return registry.map();
    }

    @Override
    public void addAll(Map<String, V> map) {
        registry.addAll(map);
    }
}
