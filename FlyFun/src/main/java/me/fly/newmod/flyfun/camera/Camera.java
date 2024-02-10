package me.fly.newmod.flyfun.camera;

import me.fly.newmod.core.util.GeometryUtil;
import me.fly.newmod.flyfun.camera.model.BlockModel;
import me.fly.newmod.flyfun.camera.model.BlockStates;
import me.fly.newmod.flyfun.camera.texture.GetImagePixelFromRotationAndLocation;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Camera {
    public byte[][] run(Location location) {
        byte[][] data = new byte[128][128];

        for(double x = 0; x < 128; x++) {
            for(double y = 0; y < 128; y++) {
                Vector vector = GeometryUtil.getRelative(location, new Vector(-0.5+x/128.0, 0, -0.5+y/128.0));

                vector.normalize();

                RayTraceResult result = location.getWorld().rayTraceBlocks(location, vector, 512, FluidCollisionMode.ALWAYS, false);

                double x_ = result.getHitPosition().getX()-result.getHitPosition().getBlockX();
                double y_ = result.getHitPosition().getY()-result.getHitPosition().getBlockY();

                int xInt = (int)(x_*16);
                int yInt = (int)(y_*16);

                BlockStates.BlockState state = Textures.me.getStates(result.getHitBlock().getType()).getState(result.getHitBlock().getBlockData());

                GetImagePixelFromRotationAndLocation.getFace()
            }
        }
    }
}
