package me.bergenfly.nations.commands.nation;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.CommandStem;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.command.requirement.StringCommandArgument;
import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.registry.Registry;
import org.bukkit.Bukkit;

import java.util.*;

import static me.bergenfly.nations.command.requirement.CommandArgumentType.STRING;
import static me.bergenfly.nations.command.requirement.CommandArgumentType.TOWN;

public class NationCommand extends CommandRoot {

    private Map<String, NationAttempt> attemptsByName = new HashMap<>();
    private Map<Town, NationAttempt> attemptsBySettlement = new HashMap<>();

    private Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();

    @Override
    public void loadSubcommands() {
        CommandStem create = addBranch("create", new CommandFlower()
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                .arg(0, new StringCommandArgument(2, 24, true))
                .arg(1, TOWN, "nations.command.info.nation_creation_help")
                .arg(2, TOWN, "nations.command.info.nation_creation_help")
                .command((a) -> {
                    String nationName = a.getArgument(STRING,0);

                    if(NATIONS.get(nationName) != null || attemptsByName.containsKey(nationName.toLowerCase())) {
                        return -1;
                    }

                    Town invitedOne = a.getArgument(TOWN, 0);
                    Town invitedTwo = a.getArgument(TOWN, 1);

                    if(invitedOne == invitedTwo) {
                        return -2;
                    }

                    Town capitalTown = a.getInvokerUser().getCommunity();

                    invitedOne.broadcast((_) -> TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName()));
                    invitedTwo.broadcast((_) -> TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedOne.getName()));

                    String invitedOneMessage = TranslatableString.translate("nations.command.info.nation_creation.mayor", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName());
                    String invitedTwoMessage = TranslatableString.translate("nations.command.info.nation_creation.mayor", a.getInvokerPlayer().getName(), nationName, invitedOne.getName());

                    NationsPlugin.getInstance().addReminder(invitedOne.getLeader().getOfflinePlayer().getUniqueId(), invitedOneMessage);
                    NationsPlugin.getInstance().addReminder(invitedTwo.getLeader().getOfflinePlayer().getUniqueId(), invitedTwoMessage);

                    NationAttempt nationCreationAttempt = new NationAttempt(capitalTown, nationName, invitedOne, invitedTwo);

                    attemptsByName.put(a.getArgument(STRING, 0).toLowerCase(), nationCreationAttempt);
                    attemptsBySettlement.put(capitalTown, nationCreationAttempt);

                    return 1;
                })
                .addMessage(-1, (a) -> TranslatableString.translate("nations.command.error.generic.is_argument", a.getArgument(STRING, 0))) //name taken
                .addMessage(-2, (a) -> TranslatableString.translate("nations.command.info.nation_creation_help")) //name taken
                .addMessage(1, (a) -> TranslatableString.translate("nations.command.info.nation_creation_sent", a.getArgument(TOWN, 0).getName(), a.getArgument(TOWN, 1).getName())) //success
        );

        create.addBranch("cancel", new CommandFlower());

        addBranch("join", new CommandFlower()
                .arg(0, STRING)
                .tabCompleter(0, (_) -> concat(NATIONS.keys(), attemptsByName.keySet()))
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                //TODO optional args
                .command((a) -> {
                    String name = a.getArgument(STRING, 0);

                    Town townThatWantsToJoin = a.getInvokerUser().getCommunity();

                    if(attemptsByName.containsKey(a.getArgument(STRING, 0).toLowerCase())) {
                        NationAttempt attempt = attemptsByName.get(name.toLowerCase());

                        if(!attempt.canJoin(townThatWantsToJoin)) {
                            return -1;
                        }

                        attempt.addAgreer(a.getInvokerUser().getCommunity());

                    } else if(NATIONS.get(name) != null) {
                        Nation nation = NATIONS.get(name);


                    } else {
                        return -2;
                    }
                }));
    }

    private static List<String> concat(Collection<String> one, Collection<String> two) {
        List<String> ret = new ArrayList<>();

        ret.addAll(one);
        ret.addAll(two);

        return ret;
    }
}
