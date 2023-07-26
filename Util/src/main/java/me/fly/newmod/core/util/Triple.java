package me.fly.newmod.core.util;

import java.util.Objects;

public class Triple<X, Y, Z> {
    private final X key;
    private final Y value1;
    private final Z value2;

    public Triple(X key, Y value1, Z value2) {
        this.key = key;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(key, triple.key) && Objects.equals(value1, triple.value1) && Objects.equals(value2, triple.value2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value1, value2);
    }

    @Override
    public String toString() {
        return "Triple{" +
                "key=" + key +
                ", value1=" + value1 +
                ", value2=" + value2 +
                '}';
    }
}
