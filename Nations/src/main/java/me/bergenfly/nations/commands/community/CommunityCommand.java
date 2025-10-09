package me.bergenfly.nations.commands.community;

import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.model.organization.Community;

public class CommunityCommand extends CommandRoot {

    public CommunityCommand(String word) {
        super(word);
    }

    @Override
    public void loadSubcommands() {
        addBranch("info", new CommandFlower()
                .addCommunity(0)
                .commandAlwaysSuccess((a) -> a.communities()[0].sendInfo(a.invoker()))
                .make());

        addBranch("invite", new CommandFlower()
                .addCommunity(CommandFlower.INVOKER_LEADER)
                .addUser(0)
                .player()
                .command((a) -> {
                    if(a.communities()[0].getMembers().contains(a.users()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.already_member", a.users()[0].getName(), word(a.communities()[0])));
                        return false;
                    }
                    if(a.communities()[0].getInvitations().contains(a.users()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.already_invited", a.users()[0].getName(), word(a.communities()[0])));
                        return false;
                    }
                    a.communities()[0].addInvitation(a.users()[0]);
                    a.communities()[0].broadcastString(TranslatableString.translate("nations.broadcast.invite.user", a.invoker().getName(), a.users()[0].getName(), word(a.communities()[0])));
                    a.users()[0].sendMessage(TranslatableString.translate("nations.broadcast.invited.user", a.invoker().getName(), a.communities()[0].getName(), word(a.communities()[0])));
                    return true;
                })
                .make());

        addBranch("kick", new CommandFlower()
                .addCommunity(CommandFlower.INVOKER_LEADER)
                .addUser(0)
                .player()
                .command((a) -> {
                    if(!a.communities()[0].getMembers().contains(a.users()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.not_member", a.users()[0].getName(), word(a.communities()[0])));
                        return false;
                    }
                    a.communities()[0].removeInvitation(a.users()[0]);
                    a.communities()[0].removeMember(a.users()[0]);
                    a.communities()[0].broadcastString(TranslatableString.translate("nations.broadcast.kick.user", a.invoker().getName(), a.users()[0].getName(), word(a.communities()[0])));
                    a.users()[0].sendMessage(TranslatableString.translate("nations.broadcast.kicked.user", a.invoker().getName(), a.communities()[0].getName(), word(a.communities()[0])));
                    return true;
                })
                .make());

        //TODO: uninvite command, and add a join confirmation for communities w/ a nation
        addBranch("join", new CommandFlower()
                .addCommunity(0)
                .player()
                .command((a) -> {
                    if(a.communities()[0].getInvitations().contains(a.invokerUser())) {
                        Community os = a.invokerUser().getCommunity();

                        if(os != null) { //TODO turn these big if statements into simpler method calls (like requireNonNull)
                            if(os.getLeader() == a.invokerUser()) {
                                a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.is_leader", word(a.communities()[0])));
                                return false;
                            }

                            os.broadcastString(TranslatableString.translate("nations.broadcast.left.community", a.invokerUser().getName(), word(a.communities()[0])));
                        }

                        a.invokerUser().setCommunity(a.communities()[0]);
                        a.communities()[0].removeInvitation(a.invokerUser());

                        a.communities()[0].broadcastString(TranslatableString.translate("nations.broadcast.joined.community", a.invokerUser().getName(), word(a.communities()[0])));

                        return true;
                    }

                    a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.not_invited"));
                    return false;
                })
                .make());

        addBranch("leave", new CommandFlower()
                .addCommunity(CommandFlower.INVOKER_MEMBER)
                .player()
                .command((a) -> {
                    Community os = a.invokerUser().getCommunity();

                    if (os.getLeader() == a.invokerUser()) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.user.is_leader", word(a.communities()[0])));
                        return false;
                    }

                    os.broadcastString(TranslatableString.translate("nations.broadcast.left.community", a.invokerUser().getName(), word(a.communities()[0])));

                    a.invokerUser().setCommunity(a.communities()[0]);
                    a.communities()[0].removeInvitation(a.invokerUser());

                    a.communities()[0].broadcastString(TranslatableString.translate("nations.broadcast.joined.community", a.invokerUser().getName(), word(a.communities()[0])));

                    return true;

                })
                .make());
    }

    public static String word(Community c) {
        return c.isSettlement() ? "settlement" : "tribe";
    }
}
