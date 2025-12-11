package me.bergenfly.nations.command.requirement;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.TranslatableString;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public interface CommandRequirement {
    boolean passes(CommandSender sender);

    String getErrorMessage(CommandSender invoker);


    CommandRequirement INVOKER_PLAYER = new BasicCommandRequirement((a) -> a instanceof Player, "nations.command.error.not_player");
    CommandRequirement INVOKER_NOT_IN_COMMUNITY = new BasicCommandRequirement((a) -> !NationsPlugin.getInstance().usersRegistry().get(((Player) a).getUniqueId()).hasCommunity(), "nations.command.error.town.is_member", CommandRequirement.INVOKER_PLAYER);
    CommandRequirement INVOKER_IN_TOWN = new BasicCommandRequirement((a) -> NationsPlugin.getInstance().usersRegistry().get(((Player) a).getUniqueId()).getCommunity().isSettlement(), "nations.command.error.town.not_member", CommandRequirement.INVOKER_PLAYER);
    CommandRequirement INVOKER_LEADER_TOWN = new BasicCommandRequirement((a) -> NationsPlugin.getInstance().usersRegistry().get(((Player) a).getUniqueId()).isCommunityLeader(), "nations.general.no_permission", CommandRequirement.INVOKER_IN_TOWN);


    class BasicCommandRequirement implements CommandRequirement {
        private final Predicate<CommandSender> check;
        private final String errorKey;
        private CommandRequirement parent;

        public BasicCommandRequirement(Predicate<CommandSender> check, String errorKey) {
            this.check = check;
            this.errorKey = errorKey;
        }

        public BasicCommandRequirement(Predicate<CommandSender> check, String errorKey, CommandRequirement parent) {
            this.check = check;
            this.errorKey = errorKey;
            this.parent = parent;
        }

        @Override
        public boolean passes(CommandSender sender) {
            if(!parent.passes(sender)) {
                return false;
            }

            return check.test(sender);
        }

        @Override
        public String getErrorMessage(CommandSender invoker) {
            if(!parent.passes(invoker)) {
                return parent.getErrorMessage(invoker);
            }

            return TranslatableString.translate(errorKey);
        }
    }
}
