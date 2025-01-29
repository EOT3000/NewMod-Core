package me.bergenfly.nations.impl.model.plot;

import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class _2x2_Chunk implements ClaimedChunk {
    private LandAdministrator[] data;

    private Map<LandAdministrator, PlotSection> admin2Section = new HashMap<>();

    public _2x2_Chunk(int x, int z, World w, LandAdministrator administrator) {
        this.x = x;
        this.z = z;
        this.w = w;

        PlotSection section = administrator.createEmptyPlotSection(this);

        admin2Section.put(administrator, section);

        this.data = new LandAdministrator[] {
                administrator,
                administrator,
                administrator,
                administrator
        };
    }

    public _2x2_Chunk(int x, int z, World w, PlotSection... sections) {
        this.x = x;
        this.z = z;
        this.w = w;

        Set<PlotSection> s = new HashSet<>();
        Set<LandAdministrator> a = new HashSet<>();

        for(int i = 0; i < 4; i++) {
            s.add(sections[i]);
            a.add(sections[i] == null ? null : sections[i].getAdministrator());
        }

        if(s.size() != a.size()) {
            throw new IllegalArgumentException();
        }

        admin2Section.put(sections[0] == null ? null : sections[0].getAdministrator(), sections[0]);
        admin2Section.put(sections[1] == null ? null : sections[1].getAdministrator(), sections[1]);
        admin2Section.put(sections[2] == null ? null : sections[2].getAdministrator(), sections[2]);
        admin2Section.put(sections[3] == null ? null : sections[3].getAdministrator(), sections[3]);

        this.data = new LandAdministrator[] {
                sections[0] == null ? null : sections[0].getAdministrator(),
                sections[1] == null ? null : sections[1].getAdministrator(),
                sections[2] == null ? null : sections[2].getAdministrator(),
                sections[3] == null ? null : sections[3].getAdministrator()
        };
    }

    private final int x;
    private final int z;
    private final World w;

    @Override
    public @Nullable PlotSection getAt(int x0_15, int z0_15) {
        return admin2Section.get(data[(z0_15/8)*2 + (x0_15/8)]);
    }

    private void simplify() {
        Map<LandAdministrator, PlotSection> newMap = new HashMap<>();

        for(LandAdministrator administrator : data) {
            newMap.put(administrator, admin2Section.get(administrator));
        }

        for(LandAdministrator old : admin2Section.keySet()) {
            if(!newMap.containsKey(old)) {
                if(old != null) {
                    old.removeLand(admin2Section.get(old));
                }
            }
        }

        this.admin2Section = newMap;
    }

    @Override
    public @Nullable PlotSection setAt(int x0_15, int z0_15, LandAdministrator admin) {
        if(admin2Section.containsKey(admin)) {
            return setAt(x0_15, z0_15, admin2Section.get(admin));
        } else {
            PlotSection section = admin == null ? null : admin.createEmptyPlotSection(this);

            return setAt(x0_15, z0_15, section);
        }
    }

    @Override
    public @Nullable PlotSection setAt(int x0_15, int z0_15, PlotSection section) {
        int quarterId = (z0_15/8)*2 + (x0_15/8);

        if(section == null) {
            if(data[quarterId] == null) {
                return null;
            }

            admin2Section.put(null, null);

            PlotSection sec = admin2Section.get(data[quarterId]);

            data[quarterId] = null;

            simplify();

            return sec;
        }

        if(section.in() != this) {
            throw new IllegalArgumentException("PlotSection's chunk does not match");
        }

        LandAdministrator newAdmin = section.getAdministrator();

        if(newAdmin == null) {
            throw new IllegalArgumentException("PlotSection's admin is null");
        }

        if(admin2Section.containsKey(newAdmin) && admin2Section.get(newAdmin) != section) {
            throw new UnsupportedOperationException("_2x2_Chunk does not support split administrators");
        }

        PlotSection sec =  admin2Section.get(data[quarterId]);

        admin2Section.put(newAdmin, section);

        data[quarterId] = newAdmin;

        simplify();

        return sec;
    }

    @Override
    public PlotSection[] getSections(boolean a) {
        return a ? admin2Section.values().toArray(new PlotSection[0])
                : admin2Section.values().stream().filter(Objects::nonNull).toArray(PlotSection[]::new);
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
        return 1;
    }

    @Override
    public void unclaim() {
        for(PlotSection section : getSections(false)) {
            section.getAdministrator().removeLand(section);
        }

        NationsPlugin.getInstance().landManager().getPLOTS().set(Plots.getLocationId(x, z, Plots.getWorldId(w)), null);

        data = new LandAdministrator[4];
        admin2Section.clear();
        admin2Section.put(null, null);
    }


}
