package me.bergenfly.nations.impl.command.settlement;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.CommandStem;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.model.organization.Nation;
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
                .addCommunity(0)
                .commandAlwaysSuccess((a) -> a.communities()[0].sendInfo(a.invoker()))
                .make());

        addBranch("claim", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .player()
                .command((a) -> a.invokerUser().tryClaimChunk(a.settlements()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());

        addBranch("invite", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .addUser(0)
                .player()
                .command((a) -> {
                    if(a.settlements()[0].getMembers().contains(a.users()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.already_member", a.users()[0].getName()));
                        return false;
                    }
                    if(a.settlements()[0].getInvitations().contains(a.users()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.already_invited", a.users()[0].getName()));
                        return false;
                    }
                    a.settlements()[0].addInvitation(a.users()[0]);
                    a.settlements()[0].broadcastString(TranslatableString.translate("nations.broadcast.invite.user", a.invoker().getName(), a.users()[0].getName()));
                    a.users()[0].sendMessage(TranslatableString.translate("nations.broadcast.invited.user", a.invoker().getName(), a.settlements()[0].getName()));
                    return true;
                })
                .make());

        //TODO: uninvite command, and add a join confirmation for settlements w/ a nation
        addBranch("join", new CommandFlower()
                .addSettlement(0)
                .player()
                .command((a) -> {
                    if(a.settlements()[0].getInvitations().contains(a.users()[0])) {
                        Settlement os = a.invokerUser().getSettlement();

                        if(os != null) { //TODO turn these big if statements into simpler method calls (like requireNonNull)
                            if(os.getLeader() == a.invokerUser()) {
                                a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.is_leader"));
                                return false;
                            }

                            os.broadcastString(TranslatableString.translate("nations.broadcast.left.user", a.invokerUser().getName()));
                        }

                        a.settlements()[0].setNation(a.nations()[0]);

                        a.nations()[0].broadcastString(TranslatableString.translate("nations.broadcast.joined.settlement", a.invokerUser().getName()));

                        return true;
                    }

                    a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.not_invited"));
                    return false;
                })
                .make());
    }
}
