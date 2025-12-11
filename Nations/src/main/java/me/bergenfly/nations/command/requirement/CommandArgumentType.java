package me.bergenfly.nations.command.requirement;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.model.User;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public interface CommandArgumentType<T> {
    boolean isValidArgument(String string);

    String getErrorMessage(CommandSender invoker, String argument, int position);

    default List<String> getTabCompletions() {
        return new ArrayList<>();
    }

    default CommandArgumentType<T> getParent() {
        return this;
    }

    T convert(String input);




    CommandArgumentType<Integer> INTEGER = BasicCommandArgumentType.createExceptionCheckType(Integer.TYPE, Integer::parseInt, "nations.command.error.integer.not_argument");
    CommandArgumentType<Float> NUMBER = BasicCommandArgumentType.createExceptionCheckType(Float.TYPE, Float::parseFloat, "nations.command.error.number.not_argument");
    CommandArgumentType<Town> TOWN = BasicCommandArgumentType.createNullCheckType(Town.class, NationsPlugin.getInstance().communitiesRegistry()::get, "nations.command.error.town.not_argument", NationsPlugin.getInstance().communitiesRegistry()::keys);
    CommandArgumentType<String> STRING = BasicCommandArgumentType.createExceptionCheckType(String.class, (a) -> a, "");
    CommandArgumentType<User> USER = BasicCommandArgumentType.createNullCheckType(User.class, (a) -> USERS.get(Bukkit.getOfflinePlayer(a).getUniqueId()) , "nations.command.error.player.not_argument", (b) -> Bukkit.getOnlinePlayers().stream().map((a) -> a.getName()).collect(Collectors.list));


    class BasicCommandArgumentType<T> implements CommandArgumentType<T> {
        private final Predicate<String> isValidArgument;
        private final TriFunction<CommandSender, String, Integer, String> getErrorMessage;
        private final Function<String, T> convert;
        private final Supplier<List<String>> getTabComplete;

        private BasicCommandArgumentType(Predicate<String> isValidArgument, TriFunction<CommandSender, String, Integer, String> getErrorMessage, Function<String, T> convert, Supplier<List<String>> getTabComplete) {
            this.isValidArgument = isValidArgument;
            this.getErrorMessage = getErrorMessage;
            this.convert = convert;
            this.getTabComplete = getTabComplete;
        }

        public static <A> BasicCommandArgumentType<A> createExceptionCheckType(Class<A> clazz, Function<String, A> convert, TriFunction<CommandSender, String, Integer, String> getErrorMessage) {
            return new BasicCommandArgumentType<>((a) -> {
                try {
                    convert.apply(a);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }, getErrorMessage, convert, ArrayList::new);
        }

        public static <A> BasicCommandArgumentType<A> createExceptionCheckType(Class<A> clazz, Function<String, A> convert, String errorMessageKey) {
            return new BasicCommandArgumentType<>((a) -> {
                try {
                    convert.apply(a);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }, (a,b,c) -> TranslatableString.translate(errorMessageKey, c.toString(), b), convert, ArrayList::new);
        }

        public static <A> BasicCommandArgumentType<A> createNullCheckType(Class<A> clazz, Function<String, A> convert, TriFunction<CommandSender, String, Integer, String> getErrorMessage, Supplier<List<String>> getTabComplete) {
            return new BasicCommandArgumentType<>((a) -> convert.apply(a) != null, getErrorMessage, convert, getTabComplete);
        }

        public static <A> BasicCommandArgumentType<A> createNullCheckType(Class<A> clazz, Function<String, A> convert, String errorMessageKey, Supplier<List<String>> getTabComplete) {
            return new BasicCommandArgumentType<>((a) -> convert.apply(a) != null, (a,b,c) -> TranslatableString.translate(errorMessageKey, c.toString(), b), convert, getTabComplete);
        }


        @Override
        public boolean isValidArgument(String string) {
            return isValidArgument.test(string);
        }

        @Override
        public String getErrorMessage(CommandSender invoker, String argument, int position) {
            return getErrorMessage.apply(invoker, argument, position);
        }

        @Override
        public T convert(String input) {
            return convert.apply(input);
        }

        @Override
        public List<String> getTabCompletions() {
            return getTabComplete.get();
        }
    }

    /*class NumberArgument<T extends Number> implements CommandArgumentType<T> {
        private final CommandArgumentType<T> parent;
        private final int min;
        private final int max;

        public NumberArgument(CommandArgumentType<T> parent, int min, int max) {
            this.parent = parent;
            this.min = min;
            this.max = max;
        }

        @Override
        public boolean isValidArgument(String string) {
            if(!parent.isValidArgument(string)) {
                return false;
            }

            T converted = convert(string);

            if(converted.doubleValue() < min || converted.doubleValue() > max) {

            }

            return false;
        }

        @Override
        public String getErrorMessage(CommandSender invoker, String argument, int position) {
            return "";
        }

        @Override
        public T convert(String input) {
            return null;
        }
    }*/ //Got ahead of myself- not needed (yet)
}
