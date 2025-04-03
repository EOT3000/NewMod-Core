package me.bergenfly.newmod.core.explosives.math;

import java.util.Random;

public class NextStateCalculator {
    Random random = new Random();



    public void nextState(ParticleGrid grid, ParticleBox box, int minX, int minY, int minZ) {
        box.shuffleEmUp();

        for(vec3 particle : box.particles) {
            vec3 pos = new vec3(minX, minY, minZ).add(new vec3(
                    random.nextDouble() / ParticleGrid.BOXES_PER_BLOCK_LENGTH,
                    random.nextDouble() / ParticleGrid.BOXES_PER_BLOCK_LENGTH,
                    random.nextDouble() / ParticleGrid.BOXES_PER_BLOCK_LENGTH)).add(particle);

            ParticleBox next = grid.getAt(pos);

            next.next.add(particle);
        }
    }
}
