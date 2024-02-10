package me.fly.newmod.flyfun.camera.texture;

import me.fly.newmod.core.util.ColorUtil;
import me.fly.newmod.flyfun.camera.Textures;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class TextureLoadUtil {
    public static TextureData16x16 load(File file) {
        int[] rawColor = new int[256];
        byte[] storedColor = new byte[4096];

        //File file = new File(textureDir.get(texture));

        try {
            BufferedImage image = ImageIO.read(file);

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

                            double[] Lab = ColorUtil.rgbToOklab(ints[0]* Textures.DARKNESS_MODIFIERS[0][i], ints[1]*Textures.DARKNESS_MODIFIERS[1][i], ints[2]*Textures.DARKNESS_MODIFIERS[2][i]);

                            storedColor[i*256+x*16+y] = ColorUtil.findClosestColor(Lab[0], Lab[1], Lab[2]);
                        }
                    }
                }
            }

            return new TextureData16x16(rawColor, storedColor);
        } catch (Exception e) {
            System.err.println("error loading image file " + file.getPath());

            e.printStackTrace();

            return null;
        }
    }
}
