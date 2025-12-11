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
                    if(NATIONS.get(a.getArgument(STRING,0)) != null) {
                        return -1;
                    }

                    if(a.getArgument(TOWN, 0) == a.getArgument(TOWN, 1)) {
                        return -2;
                    }

                    Town capitalTown = a.getInvokerUser().getCommunity();

                    NationAttempt attempt = new NationAttempt(capitalTown, a.getArgument(STRING, 0));

                    attemptsByName.put(a.getArgument(STRING, 0).toLowerCase(), attempt);
                    attemptsBySettlement.put(capitalTown, attempt);

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
