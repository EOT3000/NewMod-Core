package me.bergenfly.nations.impl.command.plot;

import it.unimi.dsi.fastutil.ints.IntObjectPair;
import me.bergenfly.nations.api.command.*;
import me.bergenfly.nations.api.manager.NationsPermissionManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.DefaultPlotPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.NationImpl;
import org.bukkit.ChatColor;

import static me.bergenfly.nations.api.command.TranslatableString.translate;
import static me.bergenfly.nations.api.manager.NationsPermissionManager.*;

public class PlotCommand extends CommandRoot {

    private static NationsPermissionManager PERMISSION_MANAGER = NationsPlugin.getInstance().permissionManager();

    public PlotCommand() {
        super("plot");
    }

    @Override
    public void loadSubcommands() {
        CommandStem permission = addBranch("permission", null);

        //plot permission set <who?> <permission> ALLOW/DENY

        {
            permission.addBranch("set", new CommandFlower()
                    .addPermissionHolder(0)
                    .addPlotPermission(1)
                    .addBoolean(2)
                    .addSettlement(CommandFlower.CURRENT_LOCATION)
                    .player()
                    .command((a) -> {
                        PlotSection section = a.invokerUser().currentlyAt();

                        if (!checkPlotOwner(section, a)) return false;

                        ((PermissiblePlotSection) section).setPermission(a.plotPermissions()[0], a.permissionHolders()[0], a.booleans()[0]);

                        return true;
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .failureMessage((a) -> TranslatableString.translate("PlotCommand: no permission"))
                    .make()); //TODO: error code return codes for different messages (- returns false, + returns true)
        } //permission set

        {
            addBranch("fs", new CommandFlower()
                    .addSettlement(CommandFlower.CURRENT_LOCATION)
                    .player()
                    .command((a) -> {
                        PlotSection section = a.invokerUser().currentlyAt();

                        if (!checkPlotOwner(section, a)) return false;

                        ((PermissiblePlotSection) section).setClaimable(true);

                        return true;
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .make());
        } //fs (for sale)

        {
            addBranch("claim", new CommandFlower()
                    .addSettlement(CommandFlower.CURRENT_LOCATION)
                    .player()
                    .command((a) -> {
                        if(a.args().length == 0) {
                            PlotSection section = a.invokerUser().currentlyAt();

                            if (!checkFs(section, a)) return false;

                            ((PermissiblePlotSection) section).setOwner(a.invokerUser());

                            return true;
                        } else {
                            IntObjectPair<LandPermissionHolder> m = PERMISSION_MANAGER.get(a.args()[0]);

                            int c = m.keyInt();

                            if(c == VALID) {
                                PlotSection section = a.invokerUser().currentlyAt();

                                if (!checkFs(section, a)) return false;

                                if(m.right().isLandManager(a.invokerUser())) {
                                    ((PermissiblePlotSection) section).setOwner(m.right());
                                    return true;
                                }

                                a.invokerUser().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            } else {
                                ObjectFetchers.sendErrorMessage(c, a.invoker(), a.args()[0]);
                            }

                            return false;
                        }
                    })
                    .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                    .make());
        } //claim

        {
            addBranch("info", new CommandFlower()
                    .addNation(CommandFlower.CURRENT_LOCATION)
                    .player()
                    .command((a) -> {
                        PlotSection section = a.invokerUser().currentlyAt();

                        if(section instanceof PermissiblePlotSection p && p.isClaimable()) {
                            a.invoker().sendMessage(ChatColor.GOLD + "---[ " + ChatColor.YELLOW + "Plot" + ChatColor.DARK_GRAY + "[For Sale]" + ChatColor.GOLD + " ] ---");
                        } else {
                            a.invoker().sendMessage(ChatColor.GOLD + "---[ " + ChatColor.YELLOW + "Plot" + ChatColor.GOLD + " ] ---");
                        }

                        a.invoker().sendMessage(ChatColor.DARK_AQUA + "Administrator: " + ChatColor.AQUA + section.getAdministrator().getFullName());

                        if(section instanceof PermissiblePlotSection) {
                            a.invoker().sendMessage(ChatColor.DARK_AQUA + "Owner: " + ChatColor.AQUA + ((PermissiblePlotSection) section).getOwner().getFullName());
                            a.invoker().sendMessage(ChatColor.DARK_GREEN + "Your allowed permissions: ");

                            for(PlotPermission pp : DefaultPlotPermission.values()) {
                                if(((PermissiblePlotSection) section).hasPermission(pp, a.invokerUser()))
                                    a.invoker().sendMessage(ChatColor.GREEN + " - " + pp.getName());
                            }

                            a.invoker().sendMessage(ChatColor.DARK_RED + "Your disallowed permissions: ");

                            for(PlotPermission pp : DefaultPlotPermission.values()) {
                                if(!((PermissiblePlotSection) section).hasPermission(pp, a.invokerUser()))
                                    a.invoker().sendMessage(ChatColor.RED + " - " + pp.getName());
                            }
                        }

                        return true;
                    })
                    .make());
        }
    }

    private static boolean checkPlotOwner(PlotSection section, CommandFlower.NationsCommandInvocation a) {
        if(section instanceof PermissiblePlotSection s) {
            if (s.getOwner().isLandManager(a.invokerUser())) {
                return true;
            } else {
                a.invokerUser().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                return false;
            }
        } else {
            //Should not happen
            a.invokerUser().sendMessage(TranslatableString.translate("nations.command.error.plot.unsupported"));
            return false;
        }
    }

    private static boolean checkFs(PlotSection section, CommandFlower.NationsCommandInvocation a) {
        if(section instanceof PermissiblePlotSection s) {
            if (s.isClaimable()) {
                return true;
            } else {
                a.invokerUser().sendMessage(TranslatableString.translate("nations.command.error.plot.nfs"));
                return false;
            }
        } else {
            //Should not happen
            a.invokerUser().sendMessage(TranslatableString.translate("nations.command.error.plot.unsupported"));
            return false;
        }
    }
}
