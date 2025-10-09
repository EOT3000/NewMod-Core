package me.bergenfly.nations.api.model.organization;

import org.jetbrains.annotations.Nullable;

/**
 * Any governmental division or agency that may be under a nation.
 */
public interface NationComponent extends Named {
    @Nullable Nation getNation();

    default void setNation(Nation nation) {
        throw new UnsupportedOperationException();
    }
}
