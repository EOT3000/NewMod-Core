package me.bergenfly.nations.api.model.organization;

/**
 * Any governmental division or agency that is under a nation.
 */
public interface NationComponent extends Named {
    Nation getNation();

    default void setNation(Nation nation) {
        throw new UnsupportedOperationException();
    }
}
