package me.bergenfly.nations.commands.plot;

import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.listener.PlotWandListener;
import me.bergenfly.nations.model.plot.Lot;

import java.util.ArrayList;
import java.util.UUID;

public class PlotChecks {
    public static final boolean touches(UUID uuid, Lot.Rectangle thisRectangle) {
        boolean touches = false;

        for(Lot.Rectangle otherRectangle : PlotWandListener.rectangles.getOrDefault(uuid, new ArrayList<>())) {
            if(thisRectangle.touches(otherRectangle)) {
                touches = true;
            }
        }

        return touches;
    }

    public static final boolean overlaps(UUID uuid, Lot.Rectangle thisRectangle) {
        for(Lot.Rectangle otherRectangle : PlotWandListener.rectangles.getOrDefault(uuid, new ArrayList<>())) {
            if(thisRectangle.overlapsWith(otherRectangle)) {
                return true;
            }
        }

        return false;
    }
}
