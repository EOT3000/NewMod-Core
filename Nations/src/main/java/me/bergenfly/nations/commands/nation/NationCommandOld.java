package me.bergenfly.nations.commands.nation;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.CommandStem;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.organization.*;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.permission.DefaultNationPermission;
import me.bergenfly.nations.permission.NationPermission;
import me.bergenfly.nations.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.model.NationImpl;
import me.bergenfly.nations.model.RankImpl;
import me.bergenfly.nations.model.SettlementImpl;
import me.bergenfly.nations.util.ClaimUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NationCommandOld extends CommandRoot {

    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();
    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();

    private NationsLandManager landManager = NationsPlugin.getInstance().landManager();

    public NationCommandOld() {
        super("nation");
    }

    @Override
    public void loadSubcommands() {
        //addBranch()

        addBranch("create", new CommandFlower()
                .addSettlement(CommandFlower.INVOKER_LEADER)
                .nationDoesNotExist(0)
                .nationDoesNotExist(CommandFlower.INVOKER_MEMBER)
                .cleanName(0)
                .nameLength(0, 3, 24)
                .player()
                .command((a) -> Nation.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.nation", a.invoker().getName(), a.args()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("info", new CommandFlower()
                .addNation(0)
                .player()
                .commandAlwaysSuccess((a) -> a.nations()[0].sendInfo(a.invoker()))
                .make());

        addBranch("claim", new CommandFlower()
                .addNation(CommandFlower.INVOKER_MEMBER)
                .nationPermission(DefaultNationPermission.TERRITORY)
                .tabCompleteOptions(0, "quarter", "chunk")
                .player()
                .command((a) -> ClaimUtil.tryClaimWithChecksAndArgs(a.invokerUser(), a.nations()[0], "nation", a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.claim"))
                .make());

        addBranch("unclaim", new CommandFlower()
                .addNation(CommandFlower.INVOKER_MEMBER)
                .nationPermission(DefaultNationPermission.TERRITORY)
                .tabCompleteOptions(0, "quarter", "chunk")
                .player()
                .command((a) -> ClaimUtil.tryUnclaimWithChecksAndArgs(a.invokerUser(), a.nations()[0], a.args()))
                .successMessage((a) -> TranslatableString.translate("nations.unclaim"))
                .make());

        CommandStem community = addBranch("community", null);

        {
            community.addBranch("invite", new CommandFlower()
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .nationPermission(DefaultNationPermission.INVITE_SETTLEMENT)
                    .addCommunity(0)
                    .player()
                    .command((a) -> {
                        if (a.nations()[0].getCommunities().contains(a.communities()[0])) {
                            //TODO: message change to community/tribe/settlement variable on which it is
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.already_member", a.communities()[0].getName()));
                            return false;
                        }
                        if (a.nations()[0].getInvitations().contains(a.communities()[0])) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.already_invited", a.communities()[0].getName()));
                            return false;
                        }
                        a.nations()[0].addInvitation(a.communities()[0]);
                        a.nations()[0].broadcastString(TranslatableString.translate("nations.broadcast.invite.settlement", a.invoker().getName(), a.communities()[0].getName()));
                        a.communities()[0].broadcastString(TranslatableString.translate("nations.broadcast.invited.settlement", a.invoker().getName(), a.nations()[0].getName()));
                        return true;
                    })
                    .make());
        } //invite

        //TODO: uninvite command, and add a join confirmation for settlements w/ a nation
        {
            community.addBranch("join", new CommandFlower()
                    .addCommunity(CommandFlower.INVOKER_LEADER)
                    .addNation(0)
                    .player()
                    .command((a) -> {
                        if (!a.nations()[0].getInvitations().contains(a.communities()[0])) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.not_invited"));
                            return false;
                        }

                        Nation on = a.communities()[0].getNation();

                        if (on != null) {
                            if (on.getCapital() == a.communities()[0]) {
                                a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.is_capital"));
                                return false;
                            }

                            on.broadcastString(TranslatableString.translate("nations.broadcast.left.nation", a.communities()[0].getName()));
                        }

                        a.settlements()[0].setNation(a.nations()[0]);

                        a.nations()[0].broadcastString(TranslatableString.translate("nations.broadcast.joined.nation", a.communities()[0].getName()));

                        return true;
                    })
                    .make());
        } //join

        {
            community.addBranch("leave", new CommandFlower()
                    .player()
                    .addCommunity(CommandFlower.INVOKER_LEADER)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .command((a) -> {
                        Nation n = a.nations()[0];
                        Community s = a.communities()[0];

                        if (n.getCapital() == s) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.is_capital"));
                            return false;
                        }

                        s.setNation(null);
                        return true;
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .successBroadcast((a) -> TranslatableString.translate("NationCommandOld: community left", a.communities()[0].getName()), (a) -> a.nations()[0])
                    .successBroadcast((a) -> TranslatableString.translate("NationCommandOld: community left", a.communities()[0].getName()), (a) -> a.communities()[0])
                    .make());
        } //leave

        {
            community.addBranch("kick", new CommandFlower()
                    .player()
                    .addCommunity(0)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .nationPermission(DefaultNationPermission.KICK_SETTLEMENT)
                    .command((a) -> {
                        Nation n = a.nations()[0];
                        Community s = a.communities()[0];

                        if (n.getCapital() == s) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.is_capital"));
                            return false;
                        }

                        s.setNation(null);
                        return true;
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .successBroadcast((a) -> TranslatableString.translate("NationCommandOld: community kicked", a.communities()[0].getName()), (a) -> a.nations()[0])
                    .successBroadcast((a) -> TranslatableString.translate("NationCommandOld: community kicked", a.communities()[0].getName()), (a) -> a.communities()[0])
                    .make());
        } //kick

        {
            addBranch("map", new CommandFlower()
                    .player()
                    .commandAlwaysSuccess((a) -> sendMap((Player) a.invoker(), a.invokerUser()))
                    .make());
        } //map

        {
            CommandStem rank = addBranch("rank", null);

            rank.addBranch("create", new CommandFlower()
                    .cleanName(0)
                    .player()
                    .nameLength(0, 3,24)
                    .requirement(0, (a,b,c)-> {
                        Nation n = USERS.get(((Player) c).getUniqueId()).getNation();

                        return n != null && !n.hasRankWithName(a[b]);
                    }, (a) -> TranslatableString.translate("nations.command.error.rank.is_argument", a[0]))
                    //TODO built in permission requirements
                    .nationPermission(DefaultNationPermission.MANAGEMENT)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .commandAlwaysSuccess((a) -> a.nations()[0].addRank(new RankImpl(a.args()[0], a.nations()[0])))
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .make());

            rank.addBranch("info", new CommandFlower()
                    .addNation(0)
                    .argsLength(2)
                    .requirement(1, (a,b,c)-> {
                        Nation n = NATIONS.get(a[0]);

                        return n.hasRankWithName(a[b]);
                    }, (a) -> TranslatableString.translate("nations.command.error.rank.not_argument", a[1]))
                    .player()
                    .commandAlwaysSuccess((a) -> a.nations()[0].getRank(a.args()[1]).sendInfo(a.invoker()))
                    .make());

            rank.addBranch("add", new CommandFlower()
                    .addUser(1)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .argsLength(2)
                    .player()
                    .command((a) -> {
                        Nation n = a.nations()[0];
                        Rank r = n.getRank(a.args()[0]);

                        //Make all these if statements single lines

                        if(r == null) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.rank.not_argument", "2", a.args()[0]));
                            return false;
                        }

                        if(a.users()[0].getNation() != a.nations()[0]) {
                            a.invoker().sendMessage(TranslatableString.translate("NationCommandOld: user not in faction", a.users()[0].getName()));
                            return false;
                        }

                        if(n.getLeader() != a.invokerUser() || r.getLeader() != a.invokerUser()) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            return false;
                        }

                        //TODO broadcast results of these somewhere, send update to people actually being added

                        r.addMember(a.users()[0]);
                        return true;
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .make());

            rank.addBranch("kick", new CommandFlower()
                    .addUser(1)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .argsLength(2)
                    .player()
                    .command((a) -> {
                        Nation n = a.nations()[0];
                        Rank r = n.getRank(a.args()[0]);

                        //Make all these if statements single lines

                        if(r == null) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.rank.not_argument", a.args()[0]));
                            return false;
                        }

                        if(!r.getMembers().contains(a.users()[0])) {
                            a.invoker().sendMessage(TranslatableString.translate("NationCommandOld: user not in rank", a.users()[0].getName()));
                            return false;
                        }

                        if(n.getLeader() != a.invokerUser() || r.getLeader() != a.invokerUser()) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            return false;
                        }

                        //TODO broadcast results of these somewhere, send update to people actually being added

                        r.removeMember(a.users()[0]);
                        return true;
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .make());

            CommandStem set = rank.addBranch("set");

            set.addBranch("leader", new CommandFlower()
                    .addUser(1)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .argsLength(2)
                    .player()
                    .command((a) -> {
                        Nation n = a.nations()[0];
                        Rank r = a.nations()[0].getRank(a.args()[0]);

                        if(r == null) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.rank.not_argument", a.args()[0]));
                            return false;
                        }

                        if(a.users()[0].getNation() != a.invokerUser().getNation()) {
                            a.invoker().sendMessage(TranslatableString.translate("NationCommandOld: user not in faction", a.users()[0].getName()));
                            return false;
                        }

                        if(n.getLeader() != a.invokerUser()) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            return false;
                        }

                        r.setLeader(a.users()[0]);
                        return true;
                    })
                    .make());

            set.addBranch("permission", new CommandFlower()
                    .addNationPermission(1)
                    .addBoolean(2)
                    .addNation(CommandFlower.INVOKER_MEMBER)
                    .argsLength(3)
                    .nationPermission(DefaultNationPermission.MANAGEMENT)
                    .player()
                    .command((a) -> {
                        Nation n = a.nations()[0];
                        Rank r = a.nations()[0].getRank(a.args()[0]);

                        if(r == null) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.command.error.rank.not_argument", a.args()[1]));
                            return false;
                        }

                        if(!n.hasPermission(a.invokerUser(), a.nationPermissions()[0])) {
                            a.invoker().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            return false;
                        }

                        if(a.booleans()[0]) {
                            r.setPermission(a.nationPermissions()[0]);

                            a.invoker().sendMessage("added the permission (NationCommandOld)");

                            //TODO command return codes, so messages can be taken outside the logic of the command
                        } else {
                            r.unsetPermission(a.nationPermissions()[0]);

                            a.invoker().sendMessage("removed the permission (NationCommandOld)");
                        }

                        return true;
                    })
                    .make());

        } //rank subcommand
    }

    private String chars = "ABCDFEGHKLMNPRSUWXZ";

    private void sendMap(Player player, User user) {
        int minX = player.getChunk().getX() - 5;
        int minZ = player.getChunk().getZ() - 5;

        int maxX = minX + 11;
        int maxZ = minZ + 11;

        int cur = 0;

        Map<Object, TextComponent> map = new HashMap<>();

        map.put(user.getCommunity(), Component.text('O').color(TextColor.color(0x154000)));
        map.put(user.getNation(), Component.text('O').color(TextColor.color(0xccffb3)));

        for (int z = minZ; z < maxZ; z++) {
            TextComponent line = Component.empty();

            for (int x = minX; x < maxX; x++) {
                ClaimedChunk chunk = landManager.getClaimedChunkAtChunk(player.getWorld(), x, z);

                if(chunk == null) {
                    line = line.append(Component.text('-').color(NamedTextColor.GRAY));
                    continue;
                }

                if(chunk.getDivision() > 0 && chunk.getSections(true).length > 1) {
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    int c = 0;

                    for(PlotSection section : chunk.getSections(true)) {
                        c++;

                        TextColor color;

                        if(section == null) {
                            color = NamedTextColor.GRAY;
                        } else {
                            LandAdministrator admin = section.getAdministrator();

                            if (!map.containsKey(admin)) {
                                if (admin instanceof Nation) {
                                    map.put(admin, Component.text(chars.charAt(cur)).color(TextColor.color(NamedTextColor.DARK_RED)));
                                } else {
                                    map.put(admin, Component.text(chars.charAt(cur)).color(TextColor.color(NamedTextColor.RED)));
                                }

                                cur++;
                            }

                            color = map.get(admin).color();
                        }

                        r+=color.red();
                        g+=color.green();
                        b+=color.blue();
                    }

                    line = line.append(Component.text('#').color(TextColor.color(r/c, g/c, b/c)));
                } else {

                    LandAdministrator admin = chunk.getAt(0, 0).getAdministrator();

                    if (!map.containsKey(admin)) {
                        if (admin instanceof Nation) {
                            map.put(admin, Component.text(chars.charAt(cur)).color(TextColor.color(NamedTextColor.DARK_RED)));
                        } else {
                            map.put(admin, Component.text(chars.charAt(cur)).color(TextColor.color(NamedTextColor.RED)));
                        }

                        cur++;
                    }

                    line = line.append(map.get(admin));
                }
            }

            player.sendMessage(line);
        }
    }
}
