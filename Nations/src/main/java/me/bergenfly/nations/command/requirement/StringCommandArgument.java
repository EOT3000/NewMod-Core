package me.bergenfly.nations.command.requirement;

import me.bergenfly.nations.command.TranslatableString;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public class StringCommandArgument implements CommandArgumentType<String> {

    protected static final Pattern pattern = Pattern.compile("[^a-zA-Z\\d_]|__");

    private final int minLength;
    private final int maxLength;
    private final boolean mustBeClean;

    public StringCommandArgument(int minLength, int maxLength, boolean mustBeClean) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.mustBeClean = mustBeClean;
    }

    @Override
    public boolean isValidArgument(String string) {
        return string.length() <= maxLength && string.length() >= minLength && !(mustBeClean && pattern.matcher(string).find());
    }

    @Override
    public String getErrorMessage(CommandSender invoker, String argument, int position) {
        if(mustBeClean && pattern.matcher(argument).find()) {
            return TranslatableString.translate("nations.command.error.string.non_alphanumeric", argument);
        }

        if(argument.length() > maxLength) {
            return TranslatableString.translate("nations.command.error.string.long", "" + maxLength);
        }

        if(argument.length() < minLength) {
            return TranslatableString.translate("nations.command.error.string.short", "" + minLength);
        }

        return TranslatableString.translate("nations.general.failure");
    }

    @Override
    public String convert(String input) {
        return input;
    }

    @Override
    public CommandArgumentType<String> getParent() {
        return CommandArgumentType.STRING;
    }
}
