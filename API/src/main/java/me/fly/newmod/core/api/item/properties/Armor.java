package me.fly.newmod.core.api.item.properties;

/**
 * The properties of an armor piece.
 */
public interface Armor {
    /**
     * @return the defense of this armor piece.
     */
    int getDefense();

    /**
     * @return the toughness of this armor piece.
     */
    int getToughness();
}
