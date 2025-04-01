package me.bergenfly.newmod.core.explosives.math;

import java.util.Random;

public class ParticleBox {
    double[][] particles;

    static Random random = new Random();

    public static void main(String[] args) {
        double[][] doubles = new double[10][3];

        for(int i = 0; i < 10; i++) {
            doubles[i][0] = 10*(random.nextDouble()-random.nextDouble());
            doubles[i][1] = 10*(random.nextDouble()-random.nextDouble());
            doubles[i][2] = 10*(random.nextDouble()-random.nextDouble());
        }

        ParticleBox box = new ParticleBox();

        box.particles = doubles;

        box.shuffleEmUp();
    }

    public void shuffleEmUp() {
        // This takes all the particles and shuffles them, as in, simulates a number of perfectly elastic collisions
        // To do this, each particle will start with a velocity. The velocity will then be slightly changed, and the
        // change will depend on the number of particles in the box, as well as the size of the box and number of
        // particles as this simulation is very rough and does not do everything with perfect precision.
        // For now this will be ln(x). Once I read https://www.tec-science.com/thermodynamics/kinetic-theory-of-gases/mean-free-path-collision-frequency/
        // I will change this.

        // After the velocities of the particles are shifted, I will have to do some wizard math to ensure that both
        // momentum in all three dimensions and kinetic energy is conserved. I think that requires some  up
        // multivariable calculus optimization but we will see.

        double modification = Math.log(particles.length * ParticleGrid.TOTAL_FACTOR)/50; //TODO: replace this with something better

        double energyD = 0;
        double vt_x = 0;
        double vt_y = 0;
        double vt_z = 0;

        for(double[] particle : particles) {
            energyD+=particle[0]*particle[0]+particle[1]*particle[1]+particle[2]*particle[2];
            vt_x+=particle[0];
            vt_y+=particle[1];
            vt_z+=particle[2];
        }

        double modX = (vt_x/2-vt_x)/particles.length;
        double modY = (vt_y/2-vt_y)/particles.length;
        double modZ = (vt_z/2-vt_z)/particles.length;

        for(double[] particle : particles) {
            particle[0] = particle[0]*(1+modification*(random.nextDouble()+random.nextDouble()))+modX*random.nextDouble();
            particle[1] = particle[1]*(1+modification*(random.nextDouble()+random.nextDouble()))+modY*random.nextDouble();
            particle[2] = particle[2]*(1+modification*(random.nextDouble()+random.nextDouble()))+modZ*random.nextDouble();
        }

        double energyD_new = 0;
        double vt_x_new = 0;
        double vt_y_new = 0;
        double vt_z_new = 0;

        for(double[] particle : particles) {
            energyD_new+=particle[0]*particle[0]+particle[1]*particle[1]+particle[2]*particle[2];
            vt_x_new+=particle[0];
            vt_y_new+=particle[1];
            vt_z_new+=particle[2];
        }

        double ke_Red = energyD/energyD_new;

        double energyD_fin = 0;
        double vt_x_fin = 0;
        double vt_y_fin = 0;
        double vt_z_fin = 0;

        for(double[] particle : particles) {
            particle[0]*=ke_Red;
            particle[1]*=ke_Red;
            particle[2]*=ke_Red;

            energyD_fin+=particle[0]*particle[0]+particle[1]*particle[1]+particle[2]*particle[2];
            vt_x_fin+=particle[0];
            vt_y_fin+=particle[1];
            vt_z_fin+=particle[2];
        }

        System.out.println("old energy: " + energyD);
        System.out.println("new energy: " + energyD_fin);
        System.out.println("old x: " + vt_x);
        System.out.println("new x: " + vt_x_fin);
        System.out.println("old y: " + vt_y);
        System.out.println("new y: " + vt_y_fin);
        System.out.println("old z: " + vt_z);
        System.out.println("new z: " + vt_z_fin);
    }
}
