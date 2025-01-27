package me.bergenfly.nations.impl.model.plot;

import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class _1x1_Chunk implements ClaimedChunk {
    private PlotSection data;

    public _1x1_Chunk(int x, int z, World w, LandAdministrator administrator) {
        this.x = x;
        this.z = z;
        this.w = w;

        this.data = administrator.createEmptyPlotSection(this);
    }

    public _1x1_Chunk(int x, int z, World w) {
        this.x = x;
        this.z = z;
        this.w = w;
    }

    private final int x;
    private final int z;
    private final World w;

    @Override
    public @Nullable PlotSection getAt(int x0_15, int z0_15) {
        return data;
    }

    @Override
    public PlotSection[] getSections(boolean a) {
        return data == null && !a
                ? new PlotSection[0]
                : new PlotSection[] {
                        data
        };
    }

    @Override
    public @Nullable PlotSection setAt(int x0_15, int z0_15, LandAdministrator admin) {
        if(data != null && data.getAdministrator().equals(admin)) {
            return data;
        }

        PlotSection old = data;

        this.data = admin == null ? null : admin.createEmptyPlotSection(this);

        return old;
    }

    @Override
    public @Nullable PlotSection setAt(int x0_15, int z0_15, PlotSection section) {
        if(data == section) {
            return data;
        }

        PlotSection old = data;

        this.data = section;

        return old;
    }

    @Override
    public int getChunkX() {
        return x;
    }

    @Override
    public int getChunkZ() {
        return z;
    }

    @Override
    public @NotNull World getWorld() {
        return w;
    }

    @Override
    public int getDivision() {
        return 0;
    }

    @Override
    public void unclaim() {
        if(data != null) {
            data.getAdministrator().removeLand(data);
        }

        NationsPlugin.getInstance().landManager().getPLOTS().set(Plots.getLocationId(x, z, Plots.getWorldId(w)), null);

        data = null;
    }


}
