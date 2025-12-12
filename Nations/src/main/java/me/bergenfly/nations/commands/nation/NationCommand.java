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

import java.util.HashMap;
import java.util.Map;

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

                    if(NATIONS.get(nationName) != null) {
                        return -1;
                    }

                    Town invitedOne = a.getArgument(TOWN, 0);
                    Town invitedTwo = a.getArgument(TOWN, 1);

                    if(invitedOne == invitedTwo) {
                        return -2;
                    }

                    Town capitalTown = a.getInvokerUser().getCommunity();

                    NationAttempt attempt = new NationAttempt(capitalTown, nationName);

                    invitedOne.broadcast((_) -> TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName()));
                    invitedTwo.broadcast((_) -> TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedOne.getName()));

                    Bukkit.getScheduler().runTaskLater(NationsPlugin.getInstance(), () -> {
                        if(invitedOne.getLeader().getOfflinePlayer().isOnline() && attempt.isActive()) {
                            invitedOne.getLeader().getPlayer().sendMessage(TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName()));
                        } else {

                        }

                        if(invitedOne.getLeader().getOfflinePlayer().isOnline()) {
                            invitedOne.getLeader().getPlayer().sendMessage(TranslatableString.translate("nations.command.info.nation_creation", a.getInvokerPlayer().getName(), nationName, invitedTwo.getName()));
                        } else {

                        }
                    }, 1);


                    attemptsByName.put(a.getArgument(STRING, 0).toLowerCase(), attempt);
                    attemptsBySettlement.put(capitalTown, attempt);

                    a.getArgument(TOWN, 0).getLeader().getOfflinePlayer();

                    return 1;
                })
                .addMessage(-1, (a) -> TranslatableString.translate("nations.command.error.generic.is_argument", a.getArgument(STRING, 0))) //name taken
                .addMessage(-2, (a) -> TranslatableString.translate("nations.command.info.nation_creation_help")) //name taken
                .addMessage(1, (a) -> TranslatableString.translate("nations.command.info.nation_creation_sent", a.getArgument(TOWN, 0).getName(), a.getArgument(TOWN, 1).getName())) //success
        );

        create.addBranch("cancel", new CommandFlower());

        addBranch("join", new CommandFlower()
                .arg(0, STRING)
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                //TODO optional args
                .command((a) -> {
                    String name = a.getArgument(STRING, 0);

                    if(!attemptsByName.containsKey(a.getArgument(STRING, 0).toLowerCase())) {
                        return -1;
                    }

                    NationAttempt attempt = attemptsByName.get(name);

                    attempt.addAgreer(a.getInvokerUser().getCommunity());
                }));
    }
}
