package me.bergenfly.nations.impl.command.nation;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.model.organization.Nation;
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

        addBranch("invite", new CommandFlower()
                .addNation(CommandFlower.INVOKER_LEADER)
                .addSettlement(0)
                .player()
                .command((a) -> {
                    if(a.nations()[0].getSettlements().contains(a.settlements()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.already_member", a.settlements()[0].getName()));
                        return false;
                    }
                    if(a.nations()[0].getInvitations().contains(a.settlements()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.already_invited", a.settlements()[0].getName()));
                        return false;
                    }
                    a.nations()[0].addInvitation(a.settlements()[0]);
                    a.nations()[0].broadcastString(TranslatableString.translate("nations.broadcast.invite.settlement", a.invoker().getName(), a.settlements()[0].getName()));
                    a.settlements()[0].broadcastString(TranslatableString.translate("nations.broadcast.invited.settlement", a.invoker().getName(), a.nations()[0].getName()));
                    return true;
                })
                .make());

        //TODO: uninvite command, and add a join confirmation for settlements w/ a nation
        addBranch("join", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .addNation(0)
                .player()
                .command((a) -> {
                    if(a.nations()[0].getInvitations().contains(a.settlements()[0])) {
                        Nation on = a.settlements()[0].getNation();

                        if(on != null) {
                            if(on.getCapital() == a.settlements()[0]) {
                                a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.is_capital"));
                                return false;
                            }

                            on.broadcastString(TranslatableString.translate("nations.broadcast.left.nation", a.settlements()[0].getName()));
                        }

                        a.settlements()[0].setNation(a.nations()[0]);

                        a.nations()[0].broadcastString(TranslatableString.translate("nations.broadcast.joined.nation", a.settlements()[0].getName()));

                        return true;
                    }

                    a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.not_invited"));
                    return false;
                })
                .make());
    }
}
