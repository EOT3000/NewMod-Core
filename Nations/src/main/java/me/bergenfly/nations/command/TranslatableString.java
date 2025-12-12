package me.bergenfly.nations.command;

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

    //TODO reorganize translation keys- look at other projects
    private static void addTranslations() {
        /*
            public static final int VALID;
    public static final int INVALID_TYPE;
    public static final int INVALID_NATION;
    public static final int NONE_MATCH_SPECIFIC;
    public static final int NONE_MATCH_GENERAL;
    public static final int MULTIPLE_MATCH;
    public static final int TOO_MANY_PARAMETERS;
        */

        translations.put("nations.command.error.not_player", new TranslatableString("&4You must be a player to run that command"));

        translations.put("nations.command.error.holder.invalid_type", new TranslatableString("&4{1} &cis not a valid type (run '/nation help types' for more information)"));
        translations.put("nations.command.error.holder.invalid_nation", new TranslatableString("&4{1} &cis not a valid nation (run '/nation help types' for more information)"));
        translations.put("nations.command.error.holder.none_match_specific", new TranslatableString("&cThere are no matches found for &4{1}&c. Check that all parameters are correct (run '/nation help types' for more information)"));
        translations.put("nations.command.error.holder.none_match_general", new TranslatableString("&cThere are no players or organizations named&4{1}"));
        translations.put("nations.command.error.holder.multiple_match", new TranslatableString("&cThere are multiple entities that match&4{1}&c. Use parameters to narrow the search down (run '/nation help types' for more information)"));
        translations.put("nations.command.error.holder.too_many_parameters", new TranslatableString("&cThe parameters provided are malformed (run '/nation help types' for more information)"));


        translations.put("nations.command.error.town.is_capital", new TranslatableString("&cThe capital may not leave its nation"));
        translations.put("nations.command.error.user.is_leader", new TranslatableString("&cThe leader may not leave their {1}"));

        translations.put("nations.command.error.not_in_territory", new TranslatableString("&cYou must be within a nation or town's territory to do that"));


        translations.put("nations.command.error.nation.not_member", new TranslatableString("&cYou must be a resident of a nation to do that"));
        translations.put("nations.command.error.nation.not_in_territory", new TranslatableString("&cYou must be within a nation's territory to do that"));
        translations.put("nations.command.error.nation.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be the name of a nation"));

        translations.put("nations.command.error.nation.is_member", new TranslatableString("&cYou must leave your nation to do that"));
        translations.put("nations.command.error.nation.is_in_territory", new TranslatableString("&cYou cannot be within a nation's territory to do that"));
        translations.put("nations.command.error.nation.is_argument", new TranslatableString("&cThe nation &4{1}&c already exists")); //TODO: alternative message for when a nation exists (not "already"). Current message only really works for the create command

        translations.put("nations.command.error.town.not_member", new TranslatableString("You must be a resident of a town to do that"));
        translations.put("nations.command.error.town.not_in_territory", new TranslatableString("You must be within a territory of a town to do that"));
        translations.put("nations.command.error.town.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be the name of a town"));

        translations.put("nations.command.error.community.is_member", new TranslatableString("You must leave your current community, town or town to do that"));

        translations.put("nations.command.error.town.is_member", new TranslatableString("You must leave your town to do that"));
        translations.put("nations.command.error.town.is_in_territory", new TranslatableString("You cannot be within a town's territory to do that"));
        translations.put("nations.command.error.town.is_argument", new TranslatableString("&cThe town &4{1}&c already exists")); //TODO same as above


        translations.put("nations.command.error.generic.is_argument", new TranslatableString("&4{1}&c already exists")); //TODO same as above

        translations.put("nations.command.error.town.already_invited", new TranslatableString("&cThe town &4{1}&c has already been invited to the nation"));
        translations.put("nations.command.error.town.already_member", new TranslatableString("&cThe town &4{1}&c is already a member of the nation"));

        translations.put("nations.command.error.rank.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be a rank"));
        translations.put("nations.command.error.rank.is_argument", new TranslatableString("&cThe rank &4{1}&c already exists"));

        translations.put("nations.command.error.town.not_invited", new TranslatableString("&cYour town has not been invited to join this nation"));
        translations.put("nations.command.error.user.not_invited", new TranslatableString("&cYou have not been invited to join this town"));

        translations.put("nations.command.error.user.already_invited", new TranslatableString("&cThe user &4{1}&c has already been invited to the {2}"));
        translations.put("nations.command.error.user.already_member", new TranslatableString("&cThe user &4{1}&c is already a member of the {2}"));
        translations.put("nations.command.error.user.not_member", new TranslatableString("&cThe user &4{1}&c is not a member of the {2}"));

        translations.put("nations.command.error.player.not_argument", new TranslatableString("&4{2}&c is not a valid player (&7{1o} &cargument)"));

        translations.put("nations.command.error.boolean.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be either &7true &cor &7false"));


        translations.put("nations.command.error.integer.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be an integer"));
        translations.put("nations.command.error.number.not_argument", new TranslatableString("&7{1o} &cargument (&4{2}&c) is invalid, must be a number"));

        translations.put("nations.command.error.string.non_alphanumeric", new TranslatableString("&4{1}&c contains invalid characters."));
        translations.put("nations.command.error.string.long", new TranslatableString("&4{1}&c is too long (maximum length &e{2}&c)"));
        translations.put("nations.command.error.string.short", new TranslatableString("&4{1}&c is too short (minimum length &e{2}&c)"));

        translations.put("nations.command.error.plot.unsupported", new TranslatableString("&cThis plot does not support this"));
        translations.put("nations.command.error.plot.nfs", new TranslatableString("&cThis plot is not for sale"));

        translations.put("nations.command.error.plot.split", new TranslatableString("&cThis plot is already split"));
        translations.put("nations.command.error.plot.not_split", new TranslatableString("&cThis plot is not split"));

        translations.put("nations.command.info.nation_creation", new TranslatableString("&e{1} &dhas invited your town to help found &e{2}&d, along with &e{3}"));
        translations.put("nations.command.info.nation_creation.mayor", new TranslatableString("&e{1} &dhas invited your town to help found &e{2}&d, along with &e{3}&d. Run &e/n join {2} &dto accept."));

        translations.put("nations.command.info.nation_creation_sent", new TranslatableString("&aProposed nation creation to &e{1} &aand &e{2}"));

        translations.put("nations.command.info.nation_creation_help", new TranslatableString("&eTo create a nation two other towns within range must agree to join. Those towns must be listed in the command, with the format &d/n create <name> <town> <town>"));

        //translations.put("nations.message.invitation.user", new TranslatableString("&e{1} &ahas created the {3} of &e{2}"));

        translations.put("nations.broadcast.created.community", new TranslatableString("&e{1} &ahas created the {3} of &e{2}"));
        translations.put("nations.broadcast.created.nation", new TranslatableString("&e{1} &ahas created the nation of &e{2}"));

        translations.put("nations.broadcast.invite.town", new TranslatableString("&e{1} &ahas invited the town of &e{2} to the nation."));
        translations.put("nations.broadcast.invited.town", new TranslatableString("&e{1} &ahas invited your town to the nation of &e{2}"));

        translations.put("nations.broadcast.invite.user.town", new TranslatableString("&e{1} &ahas invited &e{2} to the town"));
        translations.put("nations.broadcast.invited.user.town", new TranslatableString("&e{1} &ahas invited you to the town of &e{2}&a. Run &e/t join {2} &eto join them."));

        translations.put("nations.broadcast.kick.user", new TranslatableString("&e{1} &ahas kicked &e{2} from the {3}"));
        translations.put("nations.broadcast.kicked.user", new TranslatableString("&e{1} &ahas kicked you from the {3} of &e{2}"));

        translations.put("nations.broadcast.left.nation", new TranslatableString("&aThe town of &e{1} &ahas left your nation"));
        translations.put("nations.broadcast.joined.nation", new TranslatableString("&aThe town of &e{1} &ahas joined your nation"));

        translations.put("nations.broadcast.left.community", new TranslatableString("&e{1} &ahas left your {2}"));
        translations.put("nations.broadcast.joined.community", new TranslatableString("&e{1} &ahas joined your {2}"));

        translations.put("nations.general.failure", new TranslatableString("&cSomething went wrong"));
        translations.put("nations.general.success", new TranslatableString("&eSuccess"));
        translations.put("nations.general.no_permission", new TranslatableString("&cYou don't have permission to do that")); //TODO better message for different plot permissions

        translations.put("nations.claim.error.not_adjacent", new TranslatableString("&cYour new claim must be adjacent to an existing {1} claim"));

        translations.put("nations.claim", new TranslatableString("&eClaimed")); // TODO add .success to the end of the key
        translations.put("nations.unclaim", new TranslatableString("&eUnclaimed")); // TODO add .success to the end of the key

        translations.put("nations.claim.error.not_in_wild.found_town", new TranslatableString("&cYou must be standing in wilderness to found a town"));
        translations.put("nations.claim.error.already_claimed", new TranslatableString("&cThis {1} has already been claimed"));
        translations.put("nations.claim.error.not_claimed", new TranslatableString("&cThis {1} is not claimed"));


        translations.put("nations.claim.error.not_enough_adjacent_diagonal", new TranslatableString("&cThere must be at least &4{1}&c chunks adjacent or diagonal to this chunk to claim it")); //todo plurals :cry:
        translations.put("nations.claim.error.not_enough_directly_adjacent", new TranslatableString("&cThere must be at least &4{1}&c chunk directly adjacent to this chunk to claim it"));

        translations.put("nations.claim.error.not_enough_chunks.town", new TranslatableString("&cYour town does not have enough available chunks to claim more."));



        translations.put("nations.selection.list.title", new TranslatableString("&6Current Selections:"));
        translations.put("nations.selection.list.element", new TranslatableString("&e- Selection (&6{1}&ex&6{2}&6 blocks)"));

        translations.put("nations.selection.error.wrong_world", new TranslatableString("&cA previous selection is in a different world. Try &e/plot tract selection clear"));
        translations.put("nations.selection.error.no_selection", new TranslatableString("&cThere is no selection"));
        translations.put("nations.selection.error.overlap", new TranslatableString("&cThis selection overlaps with another selection"));
        translations.put("nations.selection.error.separated", new TranslatableString("&cThis selection does not touch another selection"));

        translations.put("nations.selection.pos1.success", new TranslatableString("&dSuccessfully set pos1!"));
        translations.put("nations.selection.pos2.success", new TranslatableString("&dSuccessfully set pos2!"));


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
