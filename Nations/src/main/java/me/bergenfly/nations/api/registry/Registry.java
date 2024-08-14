package me.bergenfly.nations.api.registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Registry<V, K> {
    V get(K key);

    void set(K key, V value);

    List<V> list();

    Map<K, V> map();
}