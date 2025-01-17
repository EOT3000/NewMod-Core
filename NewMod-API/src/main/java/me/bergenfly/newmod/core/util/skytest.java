package me.bergenfly.newmod.core.util;


import org.bukkit.block.BlockFace;


//Everything, or almost everything in here is based on mojang code
public class skytest {
    public static void main(String[] args) {
        System.out.println((long) (-1.05));


        for(int i = 0; i < 16; i++) {
            double skyBrightness = skytest.getSkyBrightness((float) skytest.timeOfDay(0));

            skytest.vec3 slc = skytest.mix(new skytest.vec3(skyBrightness, skyBrightness, 1.0), new skytest.vec3(1.0,1.0,1.0), .35);

            System.out.println(i + ": " + calculate(0, i/15.0, skyBrightness*.95+.05, 1.5f, 0, slc, 0, 0.0f, 1.0f).multiply(255*10).round().multiply(.1));
        }
    }

    //block- block light 0-15
    //sky- sky light 0-15
    //skyfactor- getSkyBrightness*.95+.05
    //blockfactor- avg of 1.5, std of .03 normal distribution. Causes random flicker in client. Can be set to 1.5
    //UseBrightLightmap- true for the end, otherwise false
    //SkyLightColor- mix(new vec3(getSkyBrightness, getSkyBrightness, 1.0), new vec3(1.0,1.0,1.0), .35)
    //DarkessScale/WorldFactor- used for darkness effect. Otherwise 0
    //BrightnessFactor- def .5, 1.0 is max brightness in vanilla, 0 is moody

    public static vec3  calculate(double block, double sky,
            double SkyFactor, double BlockFactor, int UseBrightLightmap, vec3 SkyLightColor, double DarknessScale, double DarkenWorldFactor, double BrightnessFactor) {
        double block_brightness = get_brightness(Math.floor(block * 16.0) / 15.0) * BlockFactor;
        double sky_brightness = get_brightness(Math.floor(sky * 16.0) / 15.0) * SkyFactor;

        // cubic nonsense, dips to yellowish in the middle, white when fully saturated
        vec3 color = new vec3(
                block_brightness,
                block_brightness * ((block_brightness * 0.6 + 0.4) * 0.6 + 0.4),
                block_brightness * (block_brightness * block_brightness * 0.6 + 0.4)
        );

        if (UseBrightLightmap != 0) {
            color = mix(color, new vec3(0.99, 1.12, 1.0), 0.25f);
            color = clamp(color, 0.0f, 1.0f);
        } else {
            color = color.add(SkyLightColor.multiply(sky_brightness));
            color = mix(color, new vec3(0.75), 0.04f);

            vec3 darkened_color = color.multiply(new vec3(0.7, 0.6, 0.6));
            color = mix(color, darkened_color, DarkenWorldFactor);
        }

        if (UseBrightLightmap == 0) {
            color = clamp(color.subtract(new vec3(DarknessScale)), 0.0f, 1.0f);
        }

        vec3 notGamma = notGamma(color);
        color = mix(color, notGamma, BrightnessFactor);
        color = mix(color, new vec3(0.75), 0.04f);
        color = clamp(color, 0.0f, 1.0f);

        //fragColor = vec4(color, 1.0);

        return color;
    }

    /*
    public static double getSkyBrightness(World world) {
        ServerLevel level = ((CraftWorld) world).getHandle();

        float a = level.getTimeOfDay(1);

        return getSkyBrightness(a);
    }

     */
    public static double getSkyBrightness(float skyAngle) {
        double f = skyAngle;
        double g = 1.0F - ((double) Math.cos(f * 6.2831855F) * 2.0F + 0.2F);
        g = clamp(g, 0.0F, 1.0F);
        g = 1.0F - g;
        return g * 0.8F + 0.2F;
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }

    public static vec3 clamp(vec3 value, double min, double max) {
        return new vec3(clamp(value.a, min, max), clamp(value.b, min, max), clamp(value.c, min, max));
    }

    /*public double getSkyAngle(long time) {
        return timeOfDay(time);
    }*/

    public static double timeOfDay(long time) {
        double d = frac((double)time / 24000.0 - 0.25);
        double e = 0.5 - Math.cos(d * Math.PI) / 2.0;
        return (double)(d * 2.0 + e) / 3.0F;
    }

    public static double frac(double value) {
        return value - (double)lfloor(value);
    }

    public static long lfloor(double value) {
        long l = (long)value;
        return value < (double)l ? l - 1L : l;
    }

    public static double get_brightness(double level) {
        double curved_level = (double) (level / (4.0 - 3.0 * level));
        return mix(curved_level, 1.0f, 0.0f);
    }

    public static double mix(double x, double y, double a) {
        return x*(1-a)+y*a;
    }

    public static vec3 mix(vec3 x, double y, double a) {
        return new vec3(mix(x.a, y, a), mix(x.b, y, a), mix(x.c, y, a));
    }

    public static vec3 mix(vec3 x, vec3 y, double a) {
        return new vec3(mix(x.a, y.a, a), mix(x.b, y.b, a), mix(x.c, y.c, a));
    }

    public static vec3 notGamma(vec3 x) {
        vec3 nx = x.subtractVecFromNum(1.0f);
        return nx.raise(4).subtractVecFromNum(1.0f);
    }

    public static class vec3 {
        public double a;
        public double b;
        public double c;

        public vec3() {}

        public vec3(double a, double b, double c) {
            this.a = (double) a;
            this.b = (double) b;
            this.c = (double) c;
        }

        public vec3(double a) {
            this.a = (double) a;
            this.b = (double) a;
            this.c = (double) a;
        }

        public vec3(BlockFace face) {
            this.a = face.getModX();
            this.b = face.getModY();
            this.c = face.getModZ();
        }

        public double magnitude() {
            return Math.sqrt(a*a+b*b+c*c);
        }

        public vec3 abs() {
            return new vec3(Math.abs(a), Math.abs(b), Math.abs(c));
        }

        public vec3 raise(int pow) {
            vec3 ret = new vec3();

            ret.a = (double) Math.pow(a, pow);
            ret.b = (double) Math.pow(b, pow);
            ret.c = (double) Math.pow(c, pow);

            return ret;
        }

        public vec3 subtract(vec3 other) {
            return new vec3(a-other.a, b-other.b, c-other.c);
        }

        public vec3 add(vec3 other) {
            return new vec3(a+other.a, b+other.b, c+other.c);
        }

        public vec3 divide(vec3 other) {
            return new vec3(a/other.a, b/other.b, c/other.c);
        }

        public vec3 multiply(vec3 other) {
            return new vec3(a*other.a, b*other.b, c*other.c);
        }

        public vec3 multiply(double other) {
            return new vec3(a*other, b*other, c*other);
        }

        public vec3 round() {
            return new vec3(Math.round(a), Math.round(b), Math.round(c));
        }

        public vec3 subtractVecFromNum(double sub) {
            vec3 ret = new vec3();

            ret.a = sub-a;
            ret.b = sub-b;
            ret.c = sub-c;

            return ret;
        }

        @Override
        public String toString() {
            return "vec3{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
    }
}
