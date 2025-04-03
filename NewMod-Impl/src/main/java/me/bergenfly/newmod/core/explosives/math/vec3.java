package me.bergenfly.newmod.core.explosives.math;

import me.bergenfly.newmod.core.util.skytest;
import org.bukkit.block.BlockFace;

public class vec3 {
    public double a;
    public double b;
    public double c;

    public vec3() {}

    public vec3(double a, double b, double c) {
        this.a = (double) a;
        this.b = (double) b;
        this.c = (double) c;
    }

    public vec3(double a) {
        this.a = (double) a;
        this.b = (double) a;
        this.c = (double) a;
    }

    public vec3(BlockFace face) {
        this.a = face.getModX();
        this.b = face.getModY();
        this.c = face.getModZ();
    }

    public double magnitude() {
        return Math.sqrt(a*a+b*b+c*c);
    }

    public double magnitudeSquared() {
        return a*a+b*b+c*c;
    }

    public vec3 abs() {
        return new vec3(Math.abs(a), Math.abs(b), Math.abs(c));
    }

    public vec3 raise(int pow) {
        vec3 ret = new vec3();

        ret.a = (double) Math.pow(a, pow);
        ret.b = (double) Math.pow(b, pow);
        ret.c = (double) Math.pow(c, pow);

        return ret;
    }

    public vec3 normalize() {
        return this.divide(this.magnitude());
    }

    public vec3 subtract(vec3 other) {
        return new vec3(a-other.a, b-other.b, c-other.c);
    }

    public vec3 add(vec3 other) {
        return new vec3(a+other.a, b+other.b, c+other.c);
    }

    public vec3 divide(vec3 other) {
        return new vec3(a/other.a, b/other.b, c/other.c);
    }

    public vec3 divide(double other) {
        return new vec3(a/other, b/other, c/other);
    }

    public vec3 multiply(vec3 other) {
        return new vec3(a*other.a, b*other.b, c*other.c);
    }

    public double dot(vec3 other) {
        return other.a*a+other.b*b+other.c*c;
    }

    public vec3 multiply(double other) {
        return new vec3(a*other, b*other, c*other);
    }

    public vec3 round() {
        return new vec3(Math.round(a), Math.round(b), Math.round(c));
    }

    public vec3 subtractVecFromNum(double sub) {
        vec3 ret = new vec3();

        ret.a = sub-a;
        ret.b = sub-b;
        ret.c = sub-c;

        return ret;
    }

    @Override
    public String toString() {
        return "vec3{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
