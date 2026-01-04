package me.bergenfly.nations.serializer;

import java.util.HashMap;
import java.util.Map;

public interface Serializable {
    Object serialize();

    String getId();

    default String getName() {
        return getId();
    }
}
