package me.bergenfly.nations.impl.command.community;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.impl.model.SettlementImpl;
import me.bergenfly.nations.impl.util.ClaimUtil;

public class SettlementCommand extends CommunityCommand {
    @Override
    public void loadSubcommands() {
        super.loadSubcommands();

        addBranch("create", new CommandFlower()
                .communityDoesNotExist(CommandFlower.INVOKER_MEMBER)
                .communityDoesNotExist(0)
                .cleanName(0)
                .player()
                .command((a) -> SettlementImpl.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.community", a.invoker().getName(), a.args()[0], "settlement"))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("claim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .player()
                .command((a) -> ClaimUtil.tryClaimWithChecksAndArgs(a.invokerUser(), a.settlements()[0], "settlement", a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());
    }

    public SettlementCommand() {
        super("settlement");
    }
}
