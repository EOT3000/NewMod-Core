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

    int numberErrors = 1001;

    public void nextState(ParticleGrid grid, ParticleBox box, int minX, int minY, int minZ) {
        box.shuffleEmUp();

        for(Particle particle : box.particles) {
            vec3 pos = particle.pos.add(particle.velocity);

            vec3 block = pos.block();

            loadBlock(block);

            if(blockCache.getInt(block) == -1) {
                double ke0 = particle.velocity.magnitudeSquared();
                double kef = ke0/2; //TODO choose proper elasticities for collisions

                Pair<vec3, Integer> hitPos = getHitPos(particle.pos, particle.pos.add(particle.velocity));

                //This should not happen
                if(hitPos.getValue() == 0) {
                    if(numberErrors++ > 1000) {
                        numberErrors = 0;
                        //TODO make this log properly

                        long a = System.currentTimeMillis();

                        System.err.println(a + ": Error: particle is at " + particle.pos + " and was previously at " + particle.previousPos);
                        System.err.println(a + ": It has a velocity of " + particle.velocity);
                        System.err.println(a + ": It attempted to move to " + particle.pos.add(particle.velocity));
                        System.err.println(a + ": In world " + w.getName());
                    }
                }

                if(hitPos.getValue() == 1) {
                    vec3 movement = hitPos.getKey().subtract(particle.pos);

                    double proportionLeft = 1-movement.magnitude()/particle.velocity.magnitude();

                    vec3 newVelocity = particle.velocity.multiply(-1);

                    vec3 newPosition = hitPos.getKey().add(newVelocity);
                }
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

    private static Pair<vec3, Integer> getHitPos(vec3 first, vec3 second) {
        //This can probably be done better ):

        vec3 intermediates = new vec3(
                intermediateInt(first.a, second.a),
                intermediateInt(first.b, second.b),
                intermediateInt(first.c, second.c));

        vec3 axis = intermediates.subtract(first).NaN_to_0_otherwise_1();

        vec3 fromFirstToSecond = second.subtract(first);

        if(axis.abs().sum() == 0) {
            return new Pair<>(null, -1);
        }

        if(axis.abs().sum() > 1) {
            return new Pair<>(null, 1);
        }

        double fromFirstToBlock = intermediates.subtract(first).multiply(axis).NaN_to_0().sum();

        double fromFirstToSecondAxis = fromFirstToSecond.multiply(axis).sum();

        double proportionMove = fromFirstToBlock/fromFirstToSecondAxis;

        return new Pair<>(first.add(fromFirstToSecond.multiply(proportionMove)), 0);
    }

    private static double intermediateInt(double first, double next) {
        double inter = Math.round((first+next)/2);

        double dist = Math.abs(first-next);

        if(Math.abs(inter-first) > dist || Math.abs(inter-next) > dist) {
            return Double.NaN;
        }

        return inter;
    }
}
