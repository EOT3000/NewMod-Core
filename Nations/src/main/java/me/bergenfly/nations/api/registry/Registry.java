package me.bergenfly.nations.api.registry;

import java.util.Collection;
import java.util.Map;

public interface Registry<V, K> {
    V get(K key);

    void set(K key, V value);

    Collection<V> list();

    Map<K, V> map();
}