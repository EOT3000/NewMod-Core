package me.bergenfly.newmod.flyfun.camera.model;

import me.fly.newmod.flyfun.camera.texture.TextureData16x16;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class AllSidesBlockModel implements BlockModel {


    private final TextureData16x16 data;

    public AllSidesBlockModel(TextureData16x16 data) {
        this.data = data;
    }

    @Override
    public byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        if(brightness*256+x*16+y > 4095) {
            System.out.println("x" + x);
            System.out.println("y" + y);
            System.out.println("b" + brightness);
        }
        return this.data.storedColor()[brightness*256+x*16+y];
    }
}
