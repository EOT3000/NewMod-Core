package me.bergenfly.nations.impl.command.settlement;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.CommandStem;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.model.SettlementImpl;

public class SettlementCommand extends CommandRoot {
    @Override
    public void loadSubcommands() {
        addBranch("create", new CommandStem(new CommandFlower()
                .settlementDoesNotExist(MEMBERSHIP)
                .settlementDoesNotExist(0)
                .cleanName(0)
                .command((a) -> new SettlementImpl(a.args()[0]).register())
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.settlement", a.invoker().getName(), a.args()[0]))
                .failureMessage((_) -> TranslatableString.translate("nations.general.failure"))));
    }
}
