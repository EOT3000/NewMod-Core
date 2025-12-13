package me.bergenfly.nations.registry;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface Registry<V, K> {
    V get(K key);

    void set(K key, V value);

    List<V> list();

    default List<K> keys() {
        return new ArrayList<>(map().keySet());
    }

    Map<K, V> map();

    void addAll(Map<K, V> map);
}