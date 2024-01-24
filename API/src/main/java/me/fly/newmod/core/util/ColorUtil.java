package me.fly.newmod.core.util;

import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ColorUtil {
    public static float rgbtl(float x) {
        if (x <= 0)
            return 0;
        else if (x >= 1)
            return 1;
        else if (x < 0.04045f)
            return x / 12.92f;
        else
            return (float)Math.pow((x + 0.055) / 1.055, 2.4);
    }

    public static float ltrgb(float x) {
        if (x <= 0)
            return 0;
        else if (x >= 1)
            return 1;
        else if (x < 0.0031308f)
            return x * 12.92f;
        else
            return (float)(Math.pow(x, 1 / 2.4) * 1.055 - 0.055);
    }

    private static float[] linear_srgb_to_oklab(float r, float g, float b) {
        float l = 0.4122214708f * r + 0.5363325363f * g + 0.0514459929f * b;
        float m = 0.2119034982f * r + 0.6806995451f * g + 0.1073969566f * b;
        float s = 0.0883024619f * r + 0.2817188376f * g + 0.6299787005f * b;

        float l_ = (float) (Math.cbrt(l));
        float m_ = (float) (Math.cbrt(m));
        float s_ = (float) (Math.cbrt(s));

        float[] returned = { 0.2104542553f * ((float) l_) + 0.7936177850f * ((float) m_) - 0.0040720468f * ((float) s_),
                1.9779984951f * ((float) l_) - 2.4285922050f * ((float) m_) + 0.4505937099f * ((float) s_),
                0.0259040371f * ((float) l_) + 0.7827717662f * ((float) m_) - 0.8086757660f * ((float) s_) };

        return returned;
    }

    private static float[] oklab_to_linear_srgb(float L, float a, float b) {

        float l_ = L + 0.3963377774f * a + 0.2158037573f * b;
        float m_ = L - 0.1055613458f * a - 0.0638541728f * b;
        float s_ = L - 0.0894841775f * a - 1.2914855480f * b;

        float l = l_ * l_ * l_;
        float m = m_ * m_ * m_;
        float s = s_ * s_ * s_;

        float[] returned = { (+4.0767416621f * l - 3.3077115913f * m + 0.2309699292f * s),
                (-1.2684380046f * l + 2.6097574011f * m - 0.3413193965f * s),
                (-0.0041960863f * l - 0.7034186147f * m + 1.7076147010f * s) };

        return returned;
    }

    public static int asInt(int r, int g, int bl) {
        //System.out.println("lab: " + L + "," + a + "," + b);
        //System.out.println("rgb: " + r + "," + g + "," + bl);
        //System.out.println();

        return r << 16 | g << 8 | bl;
    }

    public static int[] toInts(int intt) {
        return new int[]{intt >> 16 & 255, intt >> 8 & 255, intt & 255};
    }

    private interface DistanceCalculator {
        double d(int r1, int g1, int b1, int r2, int g2, int b2);
    }

    public static double distanceLinear(int r1, int g1, int b1, int r2, int g2, int b2) {
        return (rgbtl(r1)-rgbtl(r2))*(rgbtl(r1)-rgbtl(r2))+
                (rgbtl(g1)-rgbtl(g2))*(rgbtl(g1)-rgbtl(g2))+
                (rgbtl(b1)-rgbtl(b2))*(rgbtl(b1)-rgbtl(b2));
    }

    public static double distanceNormal(int r1, int g1, int b1, int r2, int g2, int b2) {
        return (r1-r2)*(r1-r2)+
                (g1-g2)*(g1-g2)+
                (b1-b2)*(b1-b2);
    }

    public static double distanceOkLab(int r1, int g1, int b1, int r2, int g2, int b2) {
        float[] first = linear_srgb_to_oklab(rgbtl(r1), rgbtl(g1), rgbtl(b1));
        float[] secnd = linear_srgb_to_oklab(rgbtl(r2), rgbtl(g2), rgbtl(b2));

        return (first[0]-secnd[0])*(first[0]-secnd[0])+
                (first[1]-secnd[1])*(first[1]-secnd[1])+
                (first[2]-secnd[2])*(first[2]-secnd[2]);
    }

    public static int findBest(int[] input, List<int[]> options, DistanceCalculator calculator) {
        double distance = 10000000;
        int[] bestOption = options.get(0);

        for(int[] option : options) {
            double d = calculator.d(input[0], input[1], input[2], option[0], option[1], option[2]);

            if(d < distance) {
                distance = d;
                bestOption = option;
            }
        }

        return asInt(bestOption[0], bestOption[1], bestOption[2]);
    }

    public static void main(String[] args) throws Exception {
        BufferedImage data = ImageIO.read(new File("data.PNG"));
        BufferedImage image = new BufferedImage(data.getWidth(), data.getHeight()*5, BufferedImage.TYPE_INT_RGB);

        int[] red = {255,0,0};
        int[] orange = {255,127,0};
        int[] yellow = {255,255,0};
        int[] lime = {127,255,0};
        int[] green = {0,255,0};
        int[] aqua = {0,255,255};
        int[] blue = {0,0,255};
        int[] bluemagenta = {127,0,255};
        int[] magenta = {255,0,255};
        int[] redmagenta = {255,0,127};

        List<int[]> colores = Lists.newArrayList(red, orange, yellow, lime, green, aqua, blue, bluemagenta, magenta, redmagenta);

        for(int x = 0; x < data.getWidth(); x++) {
            for(int y = 0; y < data.getHeight(); y++) {
                image.setRGB(x, y, data.getRGB(x, y));
            }
        }

        for(int x = 0; x < data.getWidth(); x++) {
            for(int y = 0; y < data.getHeight(); y++) {
                image.setRGB(x, y+data.getHeight()+2, findBest(toInts(data.getRGB(x,y)), colores, ColorUtil::distanceNormal));
            }
        }

        for(int x = 0; x < data.getWidth(); x++) {
            for(int y = 0; y < data.getHeight(); y++) {
                image.setRGB(x, y+data.getHeight()*2+4, findBest(toInts(data.getRGB(x,y)), colores, ColorUtil::distanceLinear));
            }
        }

        for(int x = 0; x < data.getWidth(); x++) {
            for(int y = 0; y < data.getHeight(); y++) {
                image.setRGB(x, y+data.getHeight()*3+6, findBest(toInts(data.getRGB(x,y)), colores, ColorUtil::distanceOkLab));
            }
        }

        ImageIO.write(image, "png", new File("withprintimagetest.png"));

    }
}
