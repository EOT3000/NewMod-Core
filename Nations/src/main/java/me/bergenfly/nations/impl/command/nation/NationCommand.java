package me.bergenfly.nations.impl.command.nation;

import it.unimi.dsi.fastutil.objects.Object2CharArrayMap;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.NationImpl;
import me.bergenfly.nations.impl.model.SettlementImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NationCommand extends CommandRoot {

    private NationsLandManager landManager = NationsPlugin.getInstance().landManager();

    public NationCommand() {
        super("nation");
    }

    @Override
    public void loadSubcommands() {
        addBranch("create", new CommandFlower()
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
                    if(!a.nations()[0].getInvitations().contains(a.settlements()[0])) {
                        a.invoker().sendMessage(TranslatableString.translate("nations.command.error.settlement.not_invited"));
                        return false;
                    }

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
                })
                .make());

        addBranch("map", new CommandFlower()
                .player()
                .commandAlwaysSuccess((a) -> sendMap((Player) a.invoker(), a.invokerUser()))
                .make());
    }

    private String chars = "ABCDFEGHKLMNOPRSUWXZ";

    private void sendMap(Player player, User user) {
        int minX = player.getChunk().getX() - 5;
        int minZ = player.getChunk().getZ() - 5;

        int maxX = minX + 11;
        int maxZ = minZ + 11;

        int cur = 0;

        Map<LandAdministrator, TextComponent> map = new HashMap<>();

        map.put(user.getSettlement(), Component.text('#').color(TextColor.color(0x154000)));
        map.put(user.getNation(), Component.text('#').color(TextColor.color(0xccffb3)));

        for (int z = minZ; z < maxZ; z++) {
            TextComponent line = Component.empty();

            for (int x = minX; x < maxX; x++) {
                ClaimedChunk chunk = landManager.getClaimedChunkAtLocation(player.getWorld(), x, z);

                LandAdministrator admin = chunk.getAt(0, 0).getAdministrator();

                if (!map.containsKey(admin)) {
                    if (admin instanceof Nation) {
                        map.put(admin, Component.text(chars.charAt(cur)).color(TextColor.color(NamedTextColor.DARK_GRAY)));
                    } else {
                        map.put(admin, Component.text(chars.charAt(cur)).color(TextColor.color(NamedTextColor.GRAY)));
                    }

                    cur++;
                }

                line = line.append(map.get(admin));
            }

            player.sendMessage(line);
        }
    }
}
