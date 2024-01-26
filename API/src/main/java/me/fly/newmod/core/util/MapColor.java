package me.fly.newmod.core.util;

public enum MapColor {
    NONE(0, 0),
    GRASS(1, 8368696),
    SAND(2, 16247203),
    WOOL(3, 13092807),
    FIRE(4, 16711680),
    ICE(5, 10526975),
    METAL(6, 10987431),
    PLANT(7, 31744),
    SNOW(8, 16777215),
    CLAY(9, 10791096),
    DIRT(10, 9923917),
    STONE(11, 7368816),
    WATER(12, 4210943),
    WOOD(13, 9402184),
    QUARTZ(14, 16776437),
    COLOR_ORANGE(15, 14188339),
    COLOR_MAGENTA(16, 11685080),
    COLOR_LIGHT_BLUE(17, 6724056),
    COLOR_YELLOW(18, 15066419),
    COLOR_LIGHT_GREEN(19, 8375321),
    COLOR_PINK(20, 15892389),
    COLOR_GRAY(21, 5000268),
    COLOR_LIGHT_GRAY(22, 10066329),
    COLOR_CYAN(23, 5013401),
    COLOR_PURPLE(24, 8339378),
    COLOR_BLUE(25, 3361970),
    COLOR_BROWN(26, 6704179),
    COLOR_GREEN(27, 6717235),
    COLOR_RED(28, 10040115),
    COLOR_BLACK(29, 1644825),
    GOLD(30, 16445005),
    DIAMOND(31, 6085589),
    LAPIS(32, 4882687),
    EMERALD(33, 55610),
    PODZOL(34, 8476209),
    NETHER(35, 7340544),
    TERRACOTTA_WHITE(36, 13742497),
    TERRACOTTA_ORANGE(37, 10441252),
    TERRACOTTA_MAGENTA(38, 9787244),
    TERRACOTTA_LIGHT_BLUE(39, 7367818),
    TERRACOTTA_YELLOW(40, 12223780),
    TERRACOTTA_LIGHT_GREEN(41, 6780213),
    TERRACOTTA_PINK(42, 10505550),
    TERRACOTTA_GRAY(43, 3746083),
    TERRACOTTA_LIGHT_GRAY(44, 8874850),
    TERRACOTTA_CYAN(45, 5725276),
    TERRACOTTA_PURPLE(46, 8014168),
    TERRACOTTA_BLUE(47, 4996700),
    TERRACOTTA_BROWN(48, 4993571),
    TERRACOTTA_GREEN(49, 5001770),
    TERRACOTTA_RED(50, 9321518),
    TERRACOTTA_BLACK(51, 2430480),
    CRIMSON_NYLIUM(52, 12398641),
    CRIMSON_STEM(53, 9715553),
    CRIMSON_HYPHAE(54, 6035741),
    WARPED_NYLIUM(55, 1474182),
    WARPED_STEM(56, 3837580),
    WARPED_HYPHAE(57, 5647422),
    WARPED_WART_BLOCK(58, 1356933),
    DEEPSLATE(59, 6579300),
    RAW_IRON(60, 14200723),
    GLOW_LICHEN(61, 8365974);

    public final int id, color;
    public final int var0, var1, var3;

    public static final double VAR0 = 180/255.0;
    public static final double VAR1 = 220/255.0;
    public static final double VAR3 = 135/255.0;

    MapColor(int id, int color) {
        int r = toInts(color)[0];
        int g = toInts(color)[1];
        int b = toInts(color)[2];

        this.id = id;
        this.color = color;

        this.var0 = asInt((int) (r*VAR0), (int) (g*VAR0), (int) (b*VAR0));
        this.var1 = asInt((int) (r*VAR1), (int) (g*VAR1), (int) (b*VAR1));
        this.var3 = asInt((int) (r*VAR3), (int) (g*VAR3), (int) (b*VAR3));
    }

    int[] toInts(int intt) {
        return new int[]{intt >> 16 & 255, intt >> 8 & 255, intt & 255};
    }

    int asInt(int r, int g, int bl) {
        return r << 16 | g << 8 | bl;
    }

}
