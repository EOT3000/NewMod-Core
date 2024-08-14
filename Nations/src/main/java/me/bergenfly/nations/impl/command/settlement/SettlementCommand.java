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
                .settlementDoesNotExist(CommandFlower.INVOKER_MEMBER)
                .settlementDoesNotExist(0)
                .cleanName(0)
                .player()
                .command((a) -> SettlementImpl.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.settlement", a.invoker().getName(), a.args()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("info", new CommandFlower()
                .addSettlement(0)
                .commandAlwaysSuccess((a) -> a.settlements()[0].sendInfo(a.invoker()))
                .make());

        addBranch("claim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .player()
                .command((a) -> a.invokerUser().tryClaimChunk(a.settlements()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());
    }
}
