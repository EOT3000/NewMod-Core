package me.bergenfly.nations.commands.community;

import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.command.requirement.CommandArgumentType;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.command.requirement.StringCommandArgument;
import me.bergenfly.nations.model.Settlement;
import me.bergenfly.nations.permission.DefaultNationPermission;
import me.bergenfly.nations.model.SettlementImpl;
import me.bergenfly.nations.util.ClaimUtil;
import org.bukkit.entity.Player;

import static me.bergenfly.nations.command.requirement.CommandArgumentType.*;

public class SettlementCommand extends CommandRoot {
    @Override
    public void loadSubcommands() {
        addBranch("info", new CommandFlower()
                .arg(0, SETTLEMENT)
                .commandWithCode((a) -> a.getArgument(SETTLEMENT, 0).sendInfo(a.getInvoker()), 0));

        addBranch("create", new CommandFlower()
                .requirement(CommandRequirement.INVOKER_PLAYER)
                .requirement(CommandRequirement.INVOKER_NOT_IN_COMMUNITY)
                .arg(0, new StringCommandArgument(2, 24, true))
                .command((a) -> Settlement.tryCreate(a.getArgument(STRING, 0), a.getInvokerUser(), a.getInvokerPlayer(), false).rightInt())

                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.community", a.invoker().getName(), a.args()[0], "settlement"))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("claim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .tabCompleteOptions(0, "one")
                .player()
                .command((a) -> ClaimUtil.tryClaimWithChecksAndArgs(a.invokerUser(), a.settlements()[0], "settlement", a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());

        addBranch("unclaim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .tabCompleteOptions(0, "one")
                .player()
                .command((a) -> ClaimUtil.tryUnclaimWithChecksAndArgs(a.invokerUser(), a.settlements()[0], a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.unclaim"))
                .make());
    }

    public SettlementCommand() {
        super("settlement");
    }
}
