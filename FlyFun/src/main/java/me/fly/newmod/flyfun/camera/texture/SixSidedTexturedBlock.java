package me.fly.newmod.flyfun.camera.texture;

import com.google.gson.JsonObject;
import me.fly.newmod.core.util.ColorUtil;
import me.fly.newmod.flyfun.camera.Textures;
import org.bukkit.block.data.BlockData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class SixSidedTexturedBlock implements TexturedBlock {
    private final int[] rawColor;
    private final byte[] storedColor;

    public SixSidedTexturedBlock(int[] rawColor, byte[] storedColor) {
        this.rawColor = rawColor;
        this.storedColor = storedColor;
    }

    public static SixSidedTexturedBlock fromModel(JsonObject object, Map<String, String> textureDir) {
        String location = object.getAsJsonObject("textures").get("all").getAsString();

        File file = new File(textureDir.get(location));

        try {
            BufferedImage image = ImageIO.read(file);

            int[] rawColor = new int[256];
            byte[] storedColor = new byte[4096];

            for(int x = 0; x < 16; x++) {
                for(int y = 0; y < 16; y++) {
                    int rgb = image.getRGB(x, y);

                    rawColor[x*16+y] = rgb;

                    int alpha = (rgb >> 24) & 0xff;

                    //If it has more than 25/255 transparency then palette color needs to be recalculated for the individual pixel. Otherwise am not going to bother
                    if(alpha < 230) {
                        for(int i = 0; i < 16; i++) {
                            storedColor[i*256+x*16+y] = -128;
                        }
                    } else {
                        for(int i = 0; i < 16; i++) {
                            int[] ints = ColorUtil.toInts(rgb);

                            double[] Lab = ColorUtil.rgbToOklab(ints[0]*Textures.DARKNESS_MODIFIERS[0][i], ints[1]*Textures.DARKNESS_MODIFIERS[1][i], ints[2]*Textures.DARKNESS_MODIFIERS[2][i]);

                            storedColor[i*256+x*16+y] = ColorUtil.findClosestColor(Lab[0], Lab[1], Lab[2]);
                        }
                    }
                }
            }

            return new SixSidedTexturedBlock(rawColor, storedColor);
        } catch (Exception e) {
            System.err.println("error loading image file " + location);

            return null;
        }
    }

    public short getMapColor(int x, int y, BlockData data, int brightness) {
        return storedColor[brightness*256+x*16+y];
    }
}
