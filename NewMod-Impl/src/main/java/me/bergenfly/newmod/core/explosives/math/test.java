package me.bergenfly.newmod.core.explosives.math;

import me.bergenfly.newmod.core.util.Pair;

public class test {
    public static void main(String[] args) {
        vec3 first = new vec3();
        vec3 second = new vec3();

        System.out.println(getHitPos(second, first));
    }


    private static Pair<vec3, Integer> getHitPos(vec3 first, vec3 second) {
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
