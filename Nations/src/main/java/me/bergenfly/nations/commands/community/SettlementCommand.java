package me.bergenfly.nations.commands.community;

import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.model.Settlement;
import me.bergenfly.nations.permission.DefaultNationPermission;
import me.bergenfly.nations.model.SettlementImpl;
import me.bergenfly.nations.util.ClaimUtil;

public class SettlementCommand extends CommunityCommand {
    @Override
    public void loadSubcommands() {
        super.loadSubcommands();

        addBranch("create", new CommandFlower()
                .communityDoesNotExist(CommandFlower.INVOKER_MEMBER)
                .communityDoesNotExist(0)
                .communityDoesNotExist(CommandFlower.CURRENT_LOCATION)
                .nationDoesNotExist(CommandFlower.CURRENT_LOCATION)
                .cleanName(0)
                .player()
                .command((a) -> Settlement.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.community", a.invoker().getName(), a.args()[0], "settlement"))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("claim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .tabCompleteOptions(0, "quarter", "chunk")
                .player()
                .command((a) -> ClaimUtil.tryClaimWithChecksAndArgs(a.invokerUser(), a.settlements()[0], "settlement", a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());

        addBranch("unclaim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .tabCompleteOptions(0, "quarter", "chunk")
                .player()
                .command((a) -> ClaimUtil.tryUnclaimWithChecksAndArgs(a.invokerUser(), a.settlements()[0], a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.unclaim"))
                .make());
    }

    public SettlementCommand() {
        super("settlement");
    }
}
