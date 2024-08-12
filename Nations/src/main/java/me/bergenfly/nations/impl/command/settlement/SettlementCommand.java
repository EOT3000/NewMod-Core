package me.bergenfly.nations.impl.command.settlement;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.CommandStem;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.model.SettlementImpl;

public class SettlementCommand extends CommandRoot {

    public SettlementCommand() {
        super("settlement");
    }

    @Override
    public void loadSubcommands() {
        addBranch("create", new CommandFlower()
                .settlementDoesNotExist(MEMBERSHIP)
                .settlementDoesNotExist(0)
                .cleanName(0)
                .player()
                .command((a) -> SettlementImpl.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.settlement", a.invoker().getName(), a.args()[0]))
                .failureMessage((_) -> TranslatableString.translate("nations.general.failure")));
    }
}
