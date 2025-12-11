package me.bergenfly.nations.commands.nation;

import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.CommandStem;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.command.requirement.StringCommandArgument;
import me.bergenfly.nations.model.Town;

import java.util.HashMap;
import java.util.Map;

import static me.bergenfly.nations.command.requirement.CommandArgumentType.TOWN;

public class NationCommand extends CommandRoot {

    private Map<String, NationAttempt> attemptsByName = new HashMap<>();
    private Map<Town, NationAttempt> attemptsBySettlement = new HashMap<>();

    @Override
    public void loadSubcommands() {
        CommandStem create = addBranch("create", new CommandFlower()
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                .arg(0, new StringCommandArgument(2, 24, true))
                .arg(1, TOWN)
                .arg(2, TOWN)
                .command((a) -> {

                }));

        create.addBranch("cancel", new CommandFlower());

        addBranch("join", new CommandFlower()
                .arg(0, STRING)
                .requirement(CommandRequirement.INVOKER_LEADER_TOWN)
                //TODO optional args
                .command((a) -> {
                    String name = a.getArgument(STRING, 0);

                    if(!attemptsByName.contains()) {
                        return -1;
                    }

                    NationAttempt attempt = attemptsByName.get(name).addAgreer(a.getInvokerUser().getCommunity());
                }));
    }
}
