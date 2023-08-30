package me.fly.newmod.villagers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntSet;

public class AMemory {
    public Int2ObjectMap<MemoryAssociation> associated;

    public enum MemoryAssociation {
        PROPERTY_OF,
        EVENT_AT,

    }
}
