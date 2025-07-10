package me.bergenfly.newmod.core.blockreplacer.nms;

public class ChunkSection {
    byte[] numNotAir;
    byte bitsPerEntry;
    byte[] paletteLength = {};
    int[] palette = {};
    long[] blockData = {};
    byte[] biomeData;

    byte[] fullData;

    boolean flagged;

    boolean dirty = false;

}
