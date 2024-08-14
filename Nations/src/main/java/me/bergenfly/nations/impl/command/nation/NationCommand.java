package me.bergenfly.nations.impl.command.nation;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.impl.model.NationImpl;
import me.bergenfly.nations.impl.model.SettlementImpl;

public class NationCommand extends CommandRoot {

    public NationCommand() {
        super("nation");
    }

    @Override
    public void loadSubcommands() {
        addBranch("create", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_MEMBER)
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .nationDoesNotExist(0)
                .nationDoesNotExist(CommandFlower.INVOKER_MEMBER)
                .cleanName(0)
                .player()
                .command((a) -> NationImpl.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.nation", a.invoker().getName(), a.args()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("info", new CommandFlower()
                .addNation(0)
                .player()
                .commandAlwaysSuccess((a) -> a.nations()[0].sendInfo(a.invoker()))
                .make());

        addBranch("claim", new CommandFlower()
                .addNation(CommandFlower.INVOKER_LEADER)
                .player()
                .command((a) -> a.invokerUser().tryClaimChunk(a.nations()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());
    }
}
