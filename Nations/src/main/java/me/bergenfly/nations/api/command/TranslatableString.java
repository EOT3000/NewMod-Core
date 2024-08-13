package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatableString {
    private final IntList ordinals = new IntArrayList();
    private final String input;

    public TranslatableString(String input) {

        for(int i = 1; true; i++) {
            if(input.contains("{" + i + "o}")) {
                ordinals.add(i);
                input = input.replace("{" + i + "o}", "{" + i + "}");
            } else if (!input.contains("{" + i + "}")) {
                break;
            }

            //TODO if goes too far throw exception
        }

        this.input = input;
    }

    static {
        translations = new HashMap<>();

        addTranslations();
    }

    private static void addTranslations() {
        translations.put("nations.command.error.nation.not_member", new TranslatableString("&cYou must be a resident of a nation to do that"));
        translations.put("nations.command.error.nation.not_in_territory", new TranslatableString("&cYou must be within a nation's territory to run that command"));
        translations.put("nations.command.error.nation.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be a nation"));

        translations.put("nations.command.error.nation.is_member", new TranslatableString("&cYou must leave your nation to do that"));
        translations.put("nations.command.error.nation.is_in_territory", new TranslatableString("&cYou cannot be within a nation's territory to do that"));
        translations.put("nations.command.error.nation.is_argument", new TranslatableString("&cThe nation &4{1}&c already exists")); //TODO: alternative message for when a nation exists (not "already"). Current message only really works for the create command

        translations.put("nations.command.error.settlement.not_member", new TranslatableString("You must be a resident of a settlement to do that"));
        translations.put("nations.command.error.settlement.not_in_territory", new TranslatableString("You must be within a territory of a settlement to do that"));
        translations.put("nations.command.error.settlement.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be a settlement"));

        translations.put("nations.command.error.settlement.is_member", new TranslatableString("You must be a resident of a settlement to do that"));
        translations.put("nations.command.error.settlement.is_in_territory", new TranslatableString("You cannot be within a settlement's territory to do that"));
        translations.put("nations.command.error.settlement.is_argument", new TranslatableString("&cThe settlement &4{1}&c already exists")); //TODO same as above

        translations.put("nations.command.error.player.not_argument", new TranslatableString("&4{2}&c is not a valid player (&7{1o} &cargument)"));

        translations.put("nations.command.error.boolean.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be either &7true &cor &7false"));

        //If a float is passed to an int parameter, float will be rounded to an int, only one number type needed
        translations.put("nations.command.error.number.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be a number"));

        translations.put("nations.broadcast.created.settlement", new TranslatableString("&e{1} &ahas created the settlement of &e{2}"));
        translations.put("nations.broadcast.created.nation", new TranslatableString("&e{1} &ahas created the nation of &e{2}"));

        translations.put("nations.general.failure", new TranslatableString("&cSomething went wrong"));
    }

    private static final Map<String, TranslatableString> translations;

    public static String translate(String key) {
        TranslatableString translatableString = translations.get(key);

        if(translatableString == null) {
            return key;
        }

        return translatableString.input;
    }

    public static String translate(String key, String param1) {
        TranslatableString translatableString = translations.get(key);

        if(translatableString == null) {
            return key + ": " + param1;
        }

        IntList ordinals = translatableString.ordinals;
        String input = translatableString.input;

        if(ordinals.contains(1)) {
            return ChatColor.translateAlternateColorCodes('&', input.replace("{1}", EnglishIntToOrdinal.ordinal(Integer.parseInt(param1))));
        }

        return ChatColor.translateAlternateColorCodes('&', input.replace("{1}", param1));
    }

    public static String translate(String key, String param1, String param2) {
        TranslatableString translatableString = translations.get(key);

        if(translatableString == null) {
            return key + ": " + param1 + "," + param2;
        }

        IntList ordinals = translatableString.ordinals;
        String input = translatableString.input;

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

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String translate(String key, String param1, String param2, String param3) {
        TranslatableString translatableString = translations.get(key);

        if(translatableString == null) {
            return key + ": " + param1 + "," + param2 + "," + param3;
        }

        IntList ordinals = translatableString.ordinals;
        String input = translatableString.input;

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

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String translate(String key, String... params) {
        TranslatableString translatableString = translations.get(key);

        if(translatableString == null) {
            return key + ": " + Arrays.toString(params);
        }

        IntList ordinals = translatableString.ordinals;
        String input = translatableString.input;

        for(int i = 0; i < params.length; i++) {
            if(ordinals.contains(i+1)) {
                input = input.replace("{" + (i+1) + "}", EnglishIntToOrdinal.ordinal(Integer.parseInt(params[i])));
            } else {
                input = input.replace("{" + (i+1) + "}", params[i]);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
