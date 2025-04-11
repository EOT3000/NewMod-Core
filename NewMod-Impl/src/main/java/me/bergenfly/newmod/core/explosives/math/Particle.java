package me.bergenfly.newmod.core.explosives.math;

public class Particle {
    public vec3 pos;
    public vec3 previousPos;

    public vec3 velocity;

    public int collisionsBlock;
    public int collisionsParticle;

    public long lastCollision;
}
