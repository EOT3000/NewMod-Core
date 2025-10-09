package me.bergenfly.nations.api.command;

//From Bohemian's answer on stack overflow: https://stackoverflow.com/a/6810409
public class EnglishIntToOrdinal {
    private static final String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

    public static String ordinal(int i) {
        return switch (i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> i + suffixes[i % 10];
        };
    }
}
