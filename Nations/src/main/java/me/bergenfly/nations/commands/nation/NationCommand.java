package me.bergenfly.nations.commands.nation;

import com.google.common.collect.Sets;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.CommandStem;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.command.requirement.StringCommandArgument;
import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.model.User;
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
                    Town capitalTown = a.getInvokerUser().getCommunity();

                    //Check: no duplicate towns between the two listed and the capital
                    if(invitedOne == invitedTwo || invitedOne == capitalTown || invitedTwo == capitalTown) {
                        return -2;
                    }

                    //Check: is there an outgoing invitation already?
                    for(NationAttempt otherAttempt : attemptsByName.values()) {
                        if(otherAttempt.canJoin(invitedOne)) {
                            return -3;
                        }

                        if(otherAttempt.canJoin(invitedTwo)) {
                            return -4;
                        }
                    }

                    //Check: is the capital already in a nation?
                    if(capitalTown.getNation() != null) {
                        return -5;
                    }

                    //Check: are the other towns already in a nation?
                    if(invitedOne.getNation() != null) {
                        return -6;
                    }

                    if(invitedTwo.getNation() != null) {
                        return -7;
                    }

                    //Check: is the capital already proposing a nation?
                    if(attemptsBySettlement.containsKey(capitalTown)) {
                        return -8;
                    }

                    //Success

                    invitedOne.broadcast((_) -> TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName()));
                    invitedTwo.broadcast((_) -> TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedOne.getName()));

                    String invitedOneMessage = TranslatableString.translate("nations.command.info.nation_creation.mayor", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName());
                    String invitedTwoMessage = TranslatableString.translate("nations.command.info.nation_creation.mayor", a.getInvokerPlayer().getName(), nationName, invitedOne.getName());

                    NationsPlugin.getInstance().addReminder(invitedOne.getLeader().getOfflinePlayer().getUniqueId(), invitedOneMessage);
                    NationsPlugin.getInstance().addReminder(invitedTwo.getLeader().getOfflinePlayer().getUniqueId(), invitedTwoMessage);

                    NationAttempt nationCreationAttempt = new NationAttempt(capitalTown, nationName, invitedOne, invitedTwo);

                    attemptsByName.put(a.getArgument(STRING, 0).toLowerCase(), nationCreationAttempt);
                    attemptsBySettlement.put(capitalTown, nationCreationAttempt);

                    return +1;
                })
                .addMessage(-1, (a) -> TranslatableString.translate("nations.command.error.generic.is_argument", a.getArgument(STRING, 0))) //name taken
                .addMessage(-2, "nations.command.info.nation_creation_help") //there are not three towns in arguments/membership
                .addMessage(-3, (a) -> TranslatableString.translate("nations.command.error.town.already_invited.other", a.getArgument(TOWN, 0).getName())) //town 1 already invited by someone
                .addMessage(-4, (a) -> TranslatableString.translate("nations.command.error.town.already_invited.other", a.getArgument(TOWN, 1).getName())) //town 2 already invited by someone
                .addMessage(-5, "nations.command.error.town.already_member.your") //capital in other nation
                .addMessage(-6, (a) -> TranslatableString.translate("nations.command.error.town.already_member.other", a.getArgument(TOWN, 0).getName())) //town 1 in other nation
                .addMessage(-7, (a) -> TranslatableString.translate("nations.command.error.town.already_member.other", a.getArgument(TOWN, 1).getName())) //town 2 in other nation
                .addMessage(-8, (a) -> TranslatableString.translate("nations.command.error.nation_creation.existing_proposal", attemptsBySettlement.get(a.getInvokerUser().getCommunity()).getName())) //town 2 in other nation
                .addMessage(+1, (a) -> TranslatableString.translate("nations.command.info.nation_creation_sent", a.getArgument(TOWN, 0).getName(), a.getArgument(TOWN, 1).getName())) //success
        );

        create.addBranch("cancel", new CommandFlower()
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                .command((a) -> {
                    if(attemptsBySettlement.containsKey(a.getInvokerUser().getCommunity())) {
                        NationAttempt attemptToDelete = attemptsBySettlement.get(a.getInvokerUser().getCommunity());

                        for(Town town : attemptToDelete.getPotentialMembers()) {
                            NationsPlugin.getInstance().addReminder(town.getLeader().getOfflinePlayer().getUniqueId(), TranslatableString.translate("nations.command.info.nation_creation.cancelled", attemptToDelete.getName()));
                        }

                        a.getInvokerPlayer().sendMessage(TranslatableString.translate("nations.command.info.nation_creation.cancelled", attemptToDelete.getName()));

                        attemptToDelete.kill();

                        attemptsBySettlement.remove(attemptToDelete.getCapital());
                        attemptsByName.remove(attemptToDelete.getName().toLowerCase());

                        return 1;
                    } else {
                        return -1;
                    }
                })
                .addMessage(-1, "nations.command.error.nation_creation.no_proposal")
                .addMessage(1, "nations.general.success"));

        addBranch("join", new CommandFlower()
                .arg(0, STRING)
                .tabCompleter(0, (_) -> concat(NATIONS.keys(), attemptsByName.keySet()))
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                //TODO optional args
                .command((a) -> {
                    String name = a.getArgument(STRING, 0);

                    Town townThatWantsToJoin = a.getInvokerUser().getCommunity();

                    if(townThatWantsToJoin.getNation() != null) {
                        return -1;
                    }

                    if(attemptsByName.containsKey(a.getArgument(STRING, 0).toLowerCase())) {
                        NationAttempt attempt = attemptsByName.get(name.toLowerCase());

                        if(!attempt.canJoin(townThatWantsToJoin)) {
                            return -2;
                        }

                        attempt.addAgreer(a.getInvokerUser().getCommunity());

                        return 2;
                    } else if(NATIONS.get(name) != null) {
                        Nation nation = NATIONS.get(name);

                        if(!Sets.intersection(nation.getOutlaws(), townThatWantsToJoin.getResidents()).isEmpty()) {
                            return -4;
                        }

                        if(nation.getSanctionedTowns().contains(townThatWantsToJoin)) {
                            return -5;
                        }

                        nation.addTown(townThatWantsToJoin);

                        //TODO replace with a broadcast method; add online residents method
                        for(User user : nation.getResidents()) {
                            if(user.getOfflinePlayer().isOnline()) {
                                user.getPlayer().sendMessage(TranslatableString.translate("nations.broadcast.joined.nation", townThatWantsToJoin.getName()));
                            }
                        }

                        return 1;
                    } else {
                        return -3;
                    }
                })
                .addMessage(-1, "nations.command.error.town.already_member.your")
                .addMessage(-2, "nations.command.error.town.not_invited")
                .addMessage(-3, (a) -> TranslatableString.translate("nations.command.error.nation.not_argument", a.getArgument(STRING, 0)))
                .addMessage(-4, "nations.command.error.town_has_nation_outlaw")
                .addMessage(-5, "nations.command.error.town_sanctioned")
                .addMessage(2, "nations.general.success"));
    }

    private static List<String> concat(Collection<String> one, Collection<String> two) {
        List<String> ret = new ArrayList<>();

        ret.addAll(one);
        ret.addAll(two);

        return ret;
    }
}
