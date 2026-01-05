package me.bergenfly.nations.commands.plot;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.CommandFlower;
import me.bergenfly.nations.command.CommandRoot;
import me.bergenfly.nations.command.CommandStem;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.listener.PlotWandListener;
import me.bergenfly.nations.model.plot.Lot;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

import static me.bergenfly.nations.commands.plot.PlotChecks.*;

public class PlotCommand extends CommandRoot {
    public PlotCommand() {
        super("plot");
    }

    @Override
    public void loadSubcommands() {
        {
            CommandStem tract = addBranch("tract");

            {
                CommandStem selection = addBranch("selection");

                selection.addBranch("clear", new CommandFlower()
                        .requirement(CommandRequirement.INVOKER_PLAYER)
                        .command((a) -> {
                            Bukkit.getScheduler().runTaskLater(NationsPlugin.getInstance(), () -> {
                                UUID uuid = a.getInvokerPlayer().getUniqueId();

                                PlotWandListener.worlds.remove(uuid);
                                PlotWandListener.lefts.remove(uuid);
                                PlotWandListener.rights.remove(uuid);
                                PlotWandListener.rectangles.remove(uuid);
                            }, 1);

                            return 1;
                        })
                        .addMessage(1, (a) -> TranslatableString.translate("nations.general.success")));

                selection.addBranch("add", new CommandFlower()
                        .requirement(CommandRequirement.INVOKER_PLAYER)
                        .command((a) -> {
                            UUID uuid = a.getInvokerPlayer().getUniqueId();

                            if(!PlotWandListener.lefts.containsKey(uuid) && !PlotWandListener.rights.containsKey(uuid)) {
                                return -1;
                            }

                            IntIntPair leftPair = PlotWandListener.lefts.get(uuid);
                            IntIntPair rightPair = PlotWandListener.rights.get(uuid);

                            Lot.Rectangle thisRectangle = new Lot.Rectangle(leftPair.firstInt(), leftPair.secondInt(), rightPair.firstInt(), rightPair.secondInt());

                            if(overlaps(uuid, thisRectangle)) {
                                return -2;
                            }

                            if(!touches(uuid, thisRectangle)) {
                                return -3;
                            }

                            PlotWandListener.rectangles.putIfAbsent(uuid, new ArrayList<>());
                            PlotWandListener.rectangles.get(uuid).add(thisRectangle);

                            a.getInvokerPlayer().sendMessage(TranslatableString.translate("nations.selection.list.title"));

                            for(Lot.Rectangle rectangleForList : PlotWandListener.rectangles.get(uuid)) {
                                int xLength = rectangleForList.xMax-rectangleForList.xMin;
                                int zLength = rectangleForList.zMax-rectangleForList.zMin;

                                a.getInvokerPlayer().sendMessage(TranslatableString.translate("nations.selection.list.element", ""+xLength, ""+zLength));
                            }

                            return 1;
                        })
                        .addMessage(-1, (a) -> TranslatableString.translate("nations.selection.error.no_selection"))
                        .addMessage(-2, (a) -> TranslatableString.translate("nations.selection.error.overlap"))
                        .addMessage(-3, (a) -> TranslatableString.translate("nations.selection.error.separated"))
                );

            }
        }
    }

}
