package me.bergenfly.nations.api.model.organization;

import org.jetbrains.annotations.NotNull;

public interface Named {
    /**
     * Gets an id for this object. This id should be unique among all {@link Named} objects, and persistent between server restarts.
     *
     * @return an id of this object.
     */
    @NotNull String getId();

    /**
     * Gets this object's name. This name is not necessarily unique, depending on the type of object.
     *
     * @return the name of this object.
     */
    @NotNull String getName();

    default void setName(String name) {
        throw new UnsupportedOperationException();
    }
}
