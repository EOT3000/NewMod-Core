package me.bergenfly.newmod.core.explosives.math;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.newmod.core.util.Pair;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Random;

public class NextStateCalculator {
    Random random = new Random();
    World w;

    //blast resistances multiplied by 100
    Object2IntMap<vec3> blockCache = new Object2IntOpenHashMap<>();
    Object2IntMap<vec3> halfTickValuesForce = new Object2IntOpenHashMap<>();
    Object2IntMap<vec3> energy = new Object2IntOpenHashMap<>();

    public void nextState(ParticleGrid grid, ParticleBox box, int minX, int minY, int minZ) {
        box.shuffleEmUp();

        for(Particle particle : box.particles) {
            vec3 pos = particle.pos.add(particle.velocity);

            vec3 block = pos.block();

            loadBlock(block);

            if(blockCache.getInt(block) == -1) {
                double ke0 = particle.velocity.magnitudeSquared();
                double kef = ke0/2;

                //vec3 hitPos = getHitPos();
            }

            ParticleBox next = grid.getAt(pos);

            next.next.add(particle);

            particle.previousPos = particle.pos;
            particle.pos = pos;
        }
    }

    private void loadBlock(vec3 block) {
        if(!blockCache.containsKey(block)) {
            Block b = w.getBlockAt((int) block.a, (int) block.b, (int) block.c);

            float br = b.getType().getBlastResistance();

            if(br > 1_000_000) {
                br = -1;
            } else if(br < .6) {
                if(b.getType().equals(Material.AIR)
                        || b.getType().equals(Material.CAVE_AIR)) {
                    br = 0;
                } else {
                    br = 60;
                }
            } else {
                br = br*100;
            }

            blockCache.put(block, (int) br);
        }
    }

    private Pair<vec3, Integer> getHitPos(vec3 first, vec3 second) {
        vec3 axis = new vec3(
                first.a-intermediateInt(first.a, second.a),
                first.b-intermediateInt(first.b, second.b),
                first.c-intermediateInt(first.c, second.c)).floor();

        vec3 fromFirstToSecond = first.subtract(second);

        if(axis.abs().sum() == 0) {
            return new Pair<>(null, -1);
        }

        if(axis.abs().sum() > 1) {
            return new Pair<>(null, 1);
        }

        double fromFirstToBlock = intermediateInt(first.multiply(axis).sum(), second.multiply(axis).sum());

        double fromFirstToSecondAxis = fromFirstToSecond.multiply(axis).sum();

        double proportionMove = fromFirstToBlock/fromFirstToSecondAxis;

        return new Pair<>(first.add(fromFirstToSecond.multiply(proportionMove)), 0);
    }

    private double intermediateInt(double first, double next) {
        double inter = Math.round((first+next)/2);

        double dist = Math.abs(first-next);

        if(Math.abs(inter-first) > dist || Math.abs(inter-next) > dist) {
            return (first+next)/2;
        }

        return dist;
    }
}
