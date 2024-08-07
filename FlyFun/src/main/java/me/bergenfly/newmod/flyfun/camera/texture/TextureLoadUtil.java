package me.bergenfly.newmod.flyfun.camera.texture;

import me.bergenfly.newmod.core.util.ColorUtil;
import me.bergenfly.newmod.flyfun.camera.Textures;

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

            //System.out.println(image.getType());

            //BufferedImage porters = new BufferedImage(16, 16, BufferedImage.TYPE_BYTE_GRAY);

            //System.out.println(file.getName() + ": " + image.getType());

            for(int x = 0; x < 16; x++) {
                for(int y = 0; y < 16; y++) {
                    int rgb = image.getRGB(x, y);

                    if(image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                        byte[] g = (byte[]) image.getRaster().getDataElements(x, y, null);

                        rgb = ColorUtil.asInt(g[0]&0xff,g[0]&0xff,g[0]&0xff, 255);

                        //System.out.println(file.getName() + " " + Arrays.toString(g) + " (g), " + rgb + " (rgb) , " + Arrays.toString(ColorUtil.toInts(rgb)) + " (rgb array) " + ((rgb >> 24) & 0xff) + " (alpha)");
                    }

                    /*if(file.getName().equals("stone.png")) {
                        System.out.println(x + "," + y + " (rgb mine): " + Arrays.toString(ColorUtil.toInts(rgb)));
                        System.out.println(x + "," + y + " (rgb raw): " + rgb);
                        System.out.println(x + "," + y + " (rgb bukkit argb): " + Color.fromARGB(rgb).getRed() + "," + Color.fromARGB(rgb).getGreen() + "," + Color.fromARGB(rgb).getBlue());
                        //System.out.println(x + "," + y + " (rgb bukkit rgb): " + Color.fromRGB(rgb).getRed() + "," + Color.fromRGB(rgb).getGreen() + "," + Color.fromRGB(rgb).getBlue());
                    }*/


                    rawColor[x*16+y] = rgb;

                    int alpha = (rgb >> 24) & 0xff;

                    //If it has more than 25/255 transparency then palette color needs to be recalculated for the individual pixel. Otherwise am not going to bother
                    if(alpha < 230) {
                        for(int i = 0; i < 16; i++) {
                            storedColor[i*256+x*16+y] = 0;
                        }
                    } else {
                        for(int i = 0; i < 16; i++) {
                            int[] ints = ColorUtil.toInts(rgb);

                            double[] Lab = ColorUtil.rgbToOklab(ints[0]* Textures.DARKNESS_MODIFIERS[i][0], ints[1]*Textures.DARKNESS_MODIFIERS[i][1], ints[2]*Textures.DARKNESS_MODIFIERS[i][2]);

                            /*if(file.getName().equals("stone.png") && i == 15) {
                                System.out.println(x + "," + y + " (Lab): " + Arrays.toString(Lab));
                                System.out.println(x + "," + y + " (rgb): " + ints[0]* Textures.DARKNESS_MODIFIERS[i][0] + "," + ints[1]*Textures.DARKNESS_MODIFIERS[i][1] + "," + ints[2]*Textures.DARKNESS_MODIFIERS[i][2]);
                            }*/

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
