package me.bergenfly.nations.impl.command.plot;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.CommandStem;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.impl.model.NationImpl;

public class PlotCommand extends CommandRoot {

    public PlotCommand() {
        super("plot");
    }

    @Override
    public void loadSubcommands() {
        CommandStem permission = addBranch("permission", null);

        //plot permission set <who?> <permission> ALLOW/DENY

        permission.addBranch("set", new CommandFlower()
                .addPermissionHolder(0)
                .addPlotPermission(1)
                .addBoolean(2)
                .addSettlement(CommandFlower.CURRENT_LOCATION)
                .player()
                .command((a) -> {
                    PlotSection section = a.invokerUser().currentlyAt();

                    if(section instanceof PermissiblePlotSection s) {
                        if (section.getAdministrator().isUserAdmin(a.invokerUser())) {
                            s.setPermission(a.plotPermissions()[0], a.permissionHolders()[0], a.booleans()[0]);
                            return true;
                        } else {
                            a.invokerUser().sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            return false;
                        }
                    } else {
                        //Should not happen
                        a.invokerUser().sendMessage(TranslatableString.translate("nations.command.error.plot_unsupported"));
                        return false;
                    }
                })
                .successMessage((a) -> TranslatableString.translate("nations.general.success"))
                .make()); //TODO: error code return codes for different messages (- returns false, + returns true)
    }
}
