package me.bergenfly.nations.commands.community;

import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.command.requirement.StringCommandArgument;
import me.bergenfly.nations.model.Settlement;
import me.bergenfly.nations.util.ClaimUtil2;

import static me.bergenfly.nations.command.requirement.CommandArgumentType.*;

public class SettlementCommand extends CommandRoot {
    @Override
    public void loadSubcommands() {
        addBranch("info", new CommandFlower()
                .arg(0, SETTLEMENT)
                .commandWithCode((a) -> a.getArgument(SETTLEMENT, 0).sendInfo(a.getInvoker()), 0));

        addBranch("create", new CommandFlower()
                .requirement(CommandRequirement.INVOKER_NOT_IN_COMMUNITY)
                .arg(0, new StringCommandArgument(2, 24, true))
                .command((a) -> Settlement.tryCreate(a.getArgument(STRING, 0), a.getInvokerUser(), a.getInvokerPlayer(), false).rightInt())
                .addMessage(-1, (a) -> TranslatableString.translate("nations.command.error.generic.is_argument", a.getArgument(STRING, 0))) //name taken
                .addMessage(-2, (a) -> TranslatableString.translate("nations.command.error.community.is_member")) //leader in community
                .addMessage(-3, (a) -> TranslatableString.translate("nations.claim.error.not_in_wild.found_settlement")) //homeblock not available
                .addMessage(1, (a) -> TranslatableString.translate("nations.general.success")));

        addBranch("claim", new CommandFlower()
                .requirement(CommandRequirement.INVOKER_LEADER_SETTLEMENT)
                //.arg(0, "one")
                .command((a) -> ClaimUtil2.tryClaimSettlementWithClaimChecks(a.getInvokerUser(), a.getInvokerPlayer()))
                .addMessage(-1, (a) -> TranslatableString.translate("nations.claim.error.not_enough_directly_adjacent", "1")) //<5; 1 directly adjacent
                .addMessage(-2, (a) -> TranslatableString.translate("nations.claim.error.not_enough_adjacent_diagonal", "3")) //>=5; 3 adjacent or diagonal
                .addMessage(-3, (a) -> TranslatableString.translate("nations.claim.error.already_claimed")) //already claimed
                .addMessage(-4, (a) -> TranslatableString.translate("nations.claim.error.not_enough_chunks.settlement")) //not enough chunks
                .addMessage(1, (a) -> TranslatableString.translate("nations.general.success")));

        /*addBranch("unclaim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .tabCompleteOptions(0, "one")
                .player()
                .command((a) -> ClaimUtil.tryUnclaimWithChecksAndArgs(a.invokerUser(), a.settlements()[0], a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.unclaim"))
                .make());*/
    }

    public SettlementCommand() {
        super("settlement");
    }
}
