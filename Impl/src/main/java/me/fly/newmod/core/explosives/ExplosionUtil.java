package me.fly.newmod.core.explosives;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

public class ExplosionUtil {

    private void runRayOnce(Vector direction, Location center, List<Location> checked) {
        
    }

    private static class ExplosionWorker extends Thread {
        private final ChunkSnapshot[][] snapshots;
        private final int radius;

        ExplosionWorker(Location center, int radius) {
            int chunkRadius = (int) Math.ceil(radius/16.0);
            int fullLength = chunkRadius*2+1;
            World w = center.getWorld();

            ChunkSnapshot[][] snapshots = new ChunkSnapshot[fullLength][];

            for(int x = 0; x < fullLength; x++) {
                ChunkSnapshot[] strip = new ChunkSnapshot[fullLength];

                for(int z = 0; z < fullLength; z++) {
                    strip[z] = w.getChunkAt(center.clone().add((x-chunkRadius)*16, 0, (z-chunkRadius)*16)).getChunkSnapshot();
                }

                snapshots[x] = strip;
            }

            this.snapshots = snapshots;
            this.radius = radius;
        }


        @Override
        public void run() {

        }
    }

}
