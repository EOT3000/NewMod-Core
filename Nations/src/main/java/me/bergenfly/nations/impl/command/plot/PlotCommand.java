package me.bergenfly.nations.impl.command.plot;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.CommandStem;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.impl.model.NationImpl;

public class PlotCommand extends CommandRoot {

    public PlotCommand() {
        super("plot");
    }

    @Override
    public void loadSubcommands() {
        CommandStem permission = addBranch("permission", null);

        //plot permission set <who?> <permission> ALLOW/DENY

        permission.addBranch("set", new CommandFlower()
                .add);
    }
}
