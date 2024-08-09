package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatableString {
    private final IntList ordinals = new IntArrayList();

    public TranslatableString(String key) {

        for(int i = 1; true; i++) {
            if(key.contains("{" + i + "o}")) {
                ordinals.add(i);
                key = key.replace("{" + i + "o}", "{" + i + "}");
            } else if (!key.contains("{" + i + "}")) {
                break;
            }

            //TODO if goes too far throw exception
        }
    }

    static {
        translations = new HashMap<>();

        addTranslations();
    }

    private static void addTranslations() {
        translations.put("nations.command.error.location.nation", new TranslatableString("You must be a resident of a nation do run that command"));
        translations.put("nations.command.error.membership.nation", new TranslatableString("You must be within a nation's territory to run that command"));
        translations.put("nations.command.error.argument.nation", new TranslatableString("{1o} argument ({2}) is invalid, must be a nation"));
    }

    private static final Map<String, TranslatableString> translations;

    private String translate(String input, String param1) {
        if(ordinals.contains(1)) {
            return input.replace("{1}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param1)));
        }

        return input.replace("{1}", param1);
    }

    private String translate(String input, String param1, String param2) {
        if(ordinals.contains(1)) {
            input = input.replace("{1}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param1)));
        } else {
            input = input.replace("{1}", param1);
        }

        if(ordinals.contains(2)) {
            input = input.replace("{2}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param2)));
        } else {
            input = input.replace("{2}", param2);
        }

        return input;
    }

    private String translate(String input, String param1, String param2, String param3) {
        if(ordinals.contains(1)) {
            input = input.replace("{1}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param1)));
        } else {
            input = input.replace("{1}", param1);
        }

        if(ordinals.contains(2)) {
            input = input.replace("{2}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param2)));
        } else {
            input = input.replace("{2}", param2);
        }

        if(ordinals.contains(3)) {
            input = input.replace("{3}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param3)));
        } else {
            input = input.replace("{3}", param3);
        }

        return input;
    }
}
