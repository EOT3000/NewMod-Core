package me.bergenfly.newmod.core.util;

import it.unimi.dsi.fastutil.ints.*;
import org.bukkit.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class ColorUtil {
    private static Int2ObjectMap<int[]> colors = new Int2ObjectArrayMap<>();
    private static List<IntObjectPair<double[]>> colorsLab = new ArrayList<>();

    static {
        for (MapColor color : MapColor.values()) {
            if (color.equals(MapColor.NONE)) {
                continue;
            }

            colors.put(color.id * 4 + 0, toInts(color.var0));
            colors.put(color.id * 4 + 1, toInts(color.var1));
            colors.put(color.id * 4 + 2, toInts(color.color));
            colors.put(color.id * 4 + 3, toInts(color.var3));
        }

        for (Int2ObjectMap.Entry<int[]> color : colors.int2ObjectEntrySet()) {
            double[] lab = rgbToOklab(color.getValue()[0], color.getValue()[1], color.getValue()[2]);

            colorsLab.add(new IntObjectImmutablePair<>(color.getIntKey(), lab));
        }

        for(IntObjectPair<double[]> x : colorsLab) {
            System.out.println(x.firstInt() + " " + idToName(x.keyInt()) + " Lab: " + Arrays.toString(x.value()) + " rgb: " + Arrays.toString(colors.get(x.keyInt())));
        }
    }

    public static int dim(int color, double brightness) {
        if(brightness == 1) {
            return color;
        }
        if(brightness == 0) {
            return 0;
        }

        Color c = Color.fromARGB(color);

        double[] Lab = rgbToOklab(c.getRed(), c.getGreen(), c.getBlue());

        Lab[0] = Lab[0]*brightness;

        int[] rgb = oklabToRGB(Lab[0], Lab[1], Lab[2]);

        return asInt(rgb[0], rgb[1], rgb[2]);
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(Math.min(value, max), min);
    }

    //gammaToLinear, linearToGamma, rgbToOklab and oklabToRGB adopted from https://gist.github.com/earthbound19/e7fe15fdf8ca3ef814750a61bc75b5ce

    public static double gammaToLinear(double c) {
        return c >= 0.04045 ? Math.pow((c + 0.055) / 1.055, 2.4) : c / 12.92;
    }

    public static double linearToGamma(double c) {
        return c >= 0.0031308 ? 1.055 * Math.pow(c, 1 / 2.4) - 0.055 : 12.92 * c;
    }

    public static double[] rgbToOklab(int color) {
        int[] rgb = toInts(color);

        return rgbToOklab(rgb[0], rgb[1], rgb[2]);
    }


    public static double[] rgbToOklab(double r, double g, double b) {
        r = gammaToLinear(r / 255);
        g = gammaToLinear(g / 255);
        b = gammaToLinear(b / 255);
        var l = 0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b;
        var m = 0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b;
        var s = 0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b;
        l = Math.cbrt(l);
        m = Math.cbrt(m);
        s = Math.cbrt(s);
        return new double[]{
                l * +0.2104542553 + m * +0.7936177850 + s * -0.0040720468,
                l * +1.9779984951 + m * -2.4285922050 + s * +0.4505937099,
                l * +0.0259040371 + m * +0.7827717662 + s * -0.8086757660
        };
    }

    public static int[] oklabToRGB(double L, double a, double b) {
        var l = L + a * +0.3963377774 + b * +0.2158037573;
        var m = L + a * -0.1055613458 + b * -0.0638541728;
        var s = L + a * -0.0894841775 + b * -1.2914855480;
        l = l * l * l;
        m = m * m * m;
        s = s * s * s;
        var r = l * +4.0767416621 + m * -3.3077115913 + s * +0.2309699292;
        var g = l * -1.2684380046 + m * +2.6097574011 + s * -0.3413193965;
        var bl = l * -0.0041960863 + m * -0.7034186147 + s * +1.7076147010;

        r = 255 * linearToGamma(r);
        g = 255 * linearToGamma(g);
        bl = 255 * linearToGamma(bl);

        r = clamp(r, 0, 255);
        g = clamp(g, 0, 255);
        bl = clamp(bl, 0, 255);

        r = Math.round(r);
        g = Math.round(g);
        bl = Math.round(bl);

        return new int[]{(int) r, (int) g, (int) bl};
    }

    public static int asInt(int r, int g, int bl) {
        //System.out.println("lab: " + L + "," + a + "," + b);
        //System.out.println("rgb: " + r + "," + g + "," + bl);
        //System.out.println();

        return r << 16 | g << 8 | bl;
    }

    public static int asInt(int r, int g, int b, int a) {
        //System.out.println("lab: " + L + "," + a + "," + b);
        //System.out.println("rgb: " + r + "," + g + "," + bl);
        //System.out.println();

        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int[] toInts(int intt) {
        return new int[]{intt >> 16 & 255, intt >> 8 & 255, intt & 255};
    }

    private interface DistanceCalculator {
        double d(int r1, int g1, int b1, int r2, int g2, int b2);
    }

    public static double gtl(double a) {
        return gammaToLinear(a);
    }

    public static double distanceLinear(int r1, int g1, int b1, int r2, int g2, int b2) {
        return (gtl(r1 / 255.0) - gtl(r2 / 255.0)) * (gtl(r1 / 255.0) - gtl(r2 / 255.0)) +
                (gtl(g1 / 255.0) - gtl(g2 / 255.0)) * (gtl(g1 / 255.0) - gtl(g2 / 255.0)) +
                (gtl(b1 / 255.0) - gtl(b2 / 255.0)) * (gtl(b1 / 255.0) - gtl(b2 / 255.0));
    }

    public static double distanceNormal(int r1, int g1, int b1, int r2, int g2, int b2) {
        return (r1 - r2) * (r1 - r2) +
                (g1 - g2) * (g1 - g2) +
                (b1 - b2) * (b1 - b2);
    }

    //TODO: use better color diff algorithm
    public static double distanceRgbUsingOkLab(int r1, int g1, int b1, int r2, int g2, int b2) {
        double[] first = rgbToOklab(r1, g1, b1);
        double[] secnd = rgbToOklab(r2, g2, b2);

        return (first[0] - secnd[0]) * (first[0] - secnd[0]) +
                (first[1] - secnd[1]) * (first[1] - secnd[1]) * 2 +
                (first[2] - secnd[2]) * (first[2] - secnd[2]) * 2;
    }

    public static double distanceSquaredOkLab(double L1, double a1, double b1, double L2, double a2, double b2) {
        return (L1-L2)*(L1-L2)+
                (a1-a2)*(a1-a2)+
                (b1-b2)*(b1-b2);
    }

    public static int findBest(int[] input, List<int[]> options, DistanceCalculator calculator) {
        double distance = 10000000;
        int[] bestOption = options.get(0);

        for (int[] option : options) {
            double d = calculator.d(input[0], input[1], input[2], option[0], option[1], option[2]);

            if (d < distance) {
                distance = d;
                bestOption = option;
            }
        }

        return asInt(bestOption[0], bestOption[1], bestOption[2]);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static byte findClosestColor(double L, double a, double b) {
        //Use the naive approach ):

        double closest = 1000000;
        int closestColor = 4;

        /*if(p) {
            System.out.println("Trying to find best color for " + L + "," + a + "," + b);
        }*/

        for(IntObjectPair<double[]> color : colorsLab) {
            double dist = distanceSquaredOkLab(color.second()[0], color.second()[1], color.second()[2], L, a, b);
            /*if(p && dist < 0.003) {
                System.out.println("Testing " + idToName(color.keyInt())  + " (" + Arrays.toString(color.value()) + ") (rgb:" +
                        Arrays.toString(colors.get(color.keyInt())) +
                        ") at distance " + dist);
            }*/
            if(dist < closest) {
                /*if(p) {
                    System.out.println("Old color: " + idToName(closestColor) + " at distance " + closest
                            + ", replaced with " + idToName(color.keyInt())  + " (Lab:" + Arrays.toString(color.value()) + ") (rgb:" +
                            Arrays.toString(colors.get(color.keyInt())) +
                            ") at distance " + dist);
                }*/
                closest = dist;
                closestColor = (color.keyInt());
            }
        }

        return (byte) closestColor;
    }

    public static String idToName(int key) {
        MapColor color = MapColor.values()[(int) Math.floor(key / 4.0)];
        int mod = key - color.id * 4;

        return switch (mod) {
            case 0 -> "Color " + color.name() + " (moderately darkened): ";
            case 1 -> "Color " + color.name() + " (normal): ";
            case 2 -> "Color " + color.name() + " (slightly darkened): ";
            case 3 -> "Color " + color.name() + " (very darkened): ";
            default -> "error";
        };
    }

    public static void main(String[] args) throws Exception {
        BufferedImage data = ImageIO.read(new File("data.jpg"));
        BufferedImage image = new BufferedImage(74, 6, BufferedImage.TYPE_INT_RGB);

        int[] red = {255, 0, 0};
        int[] orange = {255, 127, 0};
        int[] yellow = {255, 255, 0};
        int[] lime = {127, 255, 0};
        int[] green = {0, 255, 0};
        int[] aqua = {0, 255, 255};
        int[] blue = {0, 0, 255};
        int[] bluemagenta = {127, 0, 255};
        int[] magenta = {255, 0, 255};
        int[] redmagenta = {255, 0, 127};

        int[] black = {0, 0, 0};
        int[] grey1 = {64, 64, 64};
        int[] grey2 = {128, 128, 128};
        int[] grey3 = {191, 191, 191};
        int[] white = {255, 255, 255};

        List<Pair<Integer, int[]>> colores = new ArrayList<>();
        Map<Integer, Integer> coloresInt = new HashMap<>();
        List<Pair<Integer, double[]>> coloresLab = new ArrayList<>();

        /*colores.add(red);
        colores.add(orange);
        colores.add(yellow);
        colores.add(lime);
        colores.add(green);
        colores.add(aqua);
        colores.add(blue);
        colores.add(bluemagenta);
        colores.add(magenta);
        colores.add(redmagenta);*/

        /*colores.add(black);
        colores.add(grey1);
        colores.add(grey2);
        colores.add(grey3);
        colores.add(white);*/

        for (MapColor color : MapColor.values()) {
            if (color.equals(MapColor.NONE)) {
                continue;
            }

            colores.add(new Pair<>(color.id * 4 + 0, toInts(color.var0)));
            colores.add(new Pair<>(color.id * 4 + 1, toInts(color.var1)));
            colores.add(new Pair<>(color.id * 4 + 2, toInts(color.color)));
            colores.add(new Pair<>(color.id * 4 + 3, toInts(color.var3)));

            coloresInt.put(color.id * 4 + 0, color.var0);
            coloresInt.put(color.id * 4 + 1, color.var1);
            coloresInt.put(color.id * 4 + 2, color.color);
            coloresInt.put(color.id * 4 + 3, color.var3);
        }

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getHeight(); y++) {
                //image.setRGB(x, y, data.getRGB(x, y));
            }
        }

        for (Pair<Integer, int[]> color : colores) {
            double[] lab = rgbToOklab(color.getValue()[0], color.getValue()[1], color.getValue()[2]);

            coloresLab.add(new Pair<>(color.getKey(), lab));
        }

        Map<Integer, Double> sumDistancesGravity = new HashMap<>();

        Map<Integer, Set<Integer>> duplicateColors = new HashMap<>();

        int dupledcount = 0;

        for (Pair<Integer, double[]> color1 : coloresLab) {
            double sumDistanceGravity = 0;

            for (Pair<Integer, double[]> color2 : coloresLab) {
                double[] c1 = color1.getValue();
                double[] c2 = color2.getValue();

                /*System.out.print(Math.round(100*Math.sqrt(
                        (c1[0]-c2[0])*(c1[0]-c2[0])+
                                (c1[1]-c2[1])*(c1[1]-c2[1])+
                                (c1[2]-c2[2])*(c1[2]-c2[2])))/100.0);

                if(Math.round(100*Math.sqrt(
                        (c1[0]-c2[0])*(c1[0]-c2[0])+
                                (c1[1]-c2[1])*(c1[1]-c2[1])+
                                (c1[2]-c2[2])*(c1[2]-c2[2])))/100.0 == 0.0) {
                    System.out.print("aa");
                }

                System.out.println();*/

                /*if(Math.sqrt(
                        (c1[0]-c2[0])*(c1[0]-c2[0])+
                                (c1[1]-c2[1])*(c1[1]-c2[1])+
                                (c1[2]-c2[2])*(c1[2]-c2[2])) <= 0.017 && !color1.getKey().equals(color2.getKey())) {
                    MapColor mcolor1 = MapColor.values()[(int) Math.floor(color1.getKey()/4.0)];
                    int mod = color1.getKey()-mcolor1.id*4;

                    String name1 = switch (mod) {
                        case 0 -> "Color " + mcolor1.name() + " (moderately darkened): ";
                        case 1 -> "Color " + mcolor1.name() + " (normal): ";
                        case 2 -> "Color " + mcolor1.name() + " (slightly darkened): ";
                        case 3 -> "Color " + mcolor1.name() + " (very darkened): ";
                        default -> "error";
                    };

                    MapColor mcolor2 = MapColor.values()[(int) Math.floor(color2.getKey()/4.0)];
                    int mod2 = color2.getKey()-mcolor2.id*4;

                    String name2 = switch (mod2) {
                        case 0 -> "Color " + mcolor2.name() + " (moderately darkened): ";
                        case 1 -> "Color " + mcolor2.name() + " (normal): ";
                        case 2 -> "Color " + mcolor2.name() + " (slightly darkened): ";
                        case 3 -> "Color " + mcolor2.name() + " (very darkened): ";
                        default -> "error";
                    };

                    System.out.println(name1 + " and " + name2 + " are very similar");

                    if(color1.getKey() < color2.getKey()) {
                        duplicateColors.putIfAbsent(color1.getKey(), new HashSet<>());

                        if(duplicateColors.get(color1.getKey()).add(color2.getKey())) {
                            dupledcount++;
                        }
                    } else {
                        duplicateColors.putIfAbsent(color2.getKey(), new HashSet<>());

                        if(duplicateColors.get(color2.getKey()).add(color1.getKey())) {
                            dupledcount++;
                        }
                    }
                }*/

                double d = Math.sqrt(
                        (c1[0] - c2[0]) * (c1[0] - c2[0]) +
                                (c1[1] - c2[1]) * (c1[1] - c2[1]) +
                                (c1[2] - c2[2]) * (c1[2] - c2[2]));

                if (d < 0.0001) {
                    continue;
                }

                sumDistanceGravity += 1.0 / d;
            }

            sumDistancesGravity.put(color1.getKey(), sumDistanceGravity);
        }

        System.out.println(dupledcount);


        int count = 0;

        for (Map.Entry<Integer, Set<Integer>> entry : duplicateColors.entrySet()) {
            for (Integer i : entry.getValue()) {
                image.setRGB(count, 0, coloresInt.get(entry.getKey()));
                image.setRGB(count, 1, coloresInt.get(entry.getKey()));
                image.setRGB(count, 2, coloresInt.get(entry.getKey()));

                image.setRGB(count, 3, coloresInt.get(i));
                image.setRGB(count, 4, coloresInt.get(i));
                image.setRGB(count, 5, coloresInt.get(i));

                count++;
            }
        }


        sumDistancesGravity = sortByValue(sumDistancesGravity);

        for (int key : sumDistancesGravity.keySet()) {
            double sum = sumDistancesGravity.get(key);

            MapColor color = MapColor.values()[(int) Math.floor(key / 4.0)];
            int mod = key - color.id * 4;

            String name = switch (mod) {
                case 0 -> "Color " + color.name() + " (moderately darkened): ";
                case 1 -> "Color " + color.name() + " (normal): ";
                case 2 -> "Color " + color.name() + " (slightly darkened): ";
                case 3 -> "Color " + color.name() + " (very darkened): ";
                default -> "error";
            };

            StringBuilder builder = new StringBuilder(name);

            for (int i = 0; i < 64 - name.length(); i++) {
                builder.append(" ");
            }

            name = builder.toString();

            name += Math.round(sum / 200.0);

            System.out.println(name);
        }

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getHeight(); y++) {
                //image.setRGB(x, y+data.getHeight()+2, findBest(toInts(data.getRGB(x,y)), colores, ColorUtil::distanceNormal));
            }
        }

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getHeight(); y++) {
                //image.setRGB(x, y+data.getHeight()*2+4, findBest(toInts(data.getRGB(x,y)), colores, ColorUtil::distanceLinear));
            }
        }

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getHeight(); y++) {
                //image.setRGB(x, y+data.getHeight()+2, findBest(toInts(data.getRGB(x,y)), colores, ColorUtil::distanceOkLab));
            }
        }

        ImageIO.write(image, "png", new File("duplicated.png"));

    }
}
