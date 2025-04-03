package me.bergenfly.newmod.core.explosives.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleBox {
    List<vec3> particles = new ArrayList<>();

    List<vec3> next = new ArrayList<>();

    static Random random = new Random();

    /*public static void main(String[] args) {
        vec3[] doubles = new vec3[500000];

        for(int i = 0; i < 500000; i++) {
            doubles[i] = new vec3(10*(random.nextDouble()-random.nextDouble()),
                    100*(random.nextDouble()-random.nextDouble()),
                    100*(random.nextDouble()-random.nextDouble()));
        }

        ParticleBox box = new ParticleBox();

        box.particles = doubles;

        box.shuffleEmUp();
    }*/

    public void shuffleEmUp() {
        // This takes all the particles and shuffles them, as in, simulates a number of perfectly elastic collisions
        // https://www.tec-science.com/thermodynamics/kinetic-theory-of-gases/mean-free-path-collision-frequency/
        // I will change the probability method after reading the above.

        /*double energyD = 0;
        double vt_x = 0;
        double vt_y = 0;
        double vt_z = 0;

        for(vec3 particle : particles) {
            energyD+=particle.magnitudeSquared();
            vt_x+=particle.a;
            vt_y+=particle.b;
            vt_z+=particle.c;
        }*/

        for(int i = 0; i < particles.size(); i++) {
            if(random.nextDouble() < probability(particles.size())) {
                int other = random.nextInt(particles.size());

                if(i == other) {
                    continue;
                }
                //System.out.println("suffled");

                //courtesy of https://exploratoria.github.io/exhibits/mechanics/elastic-collisions-in-3d/index.html

                vec3 v1 = particles.get(i);
                vec3 v2 = particles.get(other);

                vec3 p1 = v1.multiply(-1);
                vec3 p2 = v2.multiply(-1);

                vec3 normal = p1.subtract(p2).normalize();

                vec3 relV = v1.subtract(v2);

                vec3 normV = normal.multiply(relV.dot(normal));

                vec3 v1Prime = v1.subtract(normV);
                vec3 v2Prime = v2.add(normV);

                particles.set(i, v1Prime);
                particles.set(other, v2Prime);
            }
        }

        /*double energyD_fin = 0;
        double vt_x_fin = 0;
        double vt_y_fin = 0;
        double vt_z_fin = 0;

        for(vec3 particle : particles) {
            energyD_fin+=particle.magnitudeSquared();
            vt_x_fin+=particle.a;
            vt_y_fin+=particle.b;
            vt_z_fin+=particle.c;
        }

        System.out.println("old energy: " + energyD);
        System.out.println("new energy: " + energyD_fin);
        System.out.println("old x: " + vt_x);
        System.out.println("new x: " + vt_x_fin);
        System.out.println("old y: " + vt_y);
        System.out.println("new y: " + vt_y_fin);
        System.out.println("old z: " + vt_z);
        System.out.println("new z: " + vt_z_fin);*/
    }

    public double probability(int num) {
        return 2.0/(1+Math.pow(2,-Math.log(num)))-1;
    }
}
