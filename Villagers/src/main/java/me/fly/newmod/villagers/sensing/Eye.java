package me.fly.newmod.villagers.sensing;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Eye {
    private static final int threads = 2;
    private static final ExecutorService eyeThreadPool = Executors.newFixedThreadPool(threads);

    public static int lookForCropsAtLevel(Location villager, Location eye, int distance) {
        Vector start = villager.clone().add(0,0.5,0).toVector();

        Vector dir = eye.getDirection().setY(0);
        Vector farthestStraight = start.clone().add(dir);

        Vector left = farthestStraight.clone().subtract(new Vector(dir.getZ()/2.0, 0, dir.getX()/2.0));
        Vector right = farthestStraight.clone().add(new Vector(dir.getZ()/2.0, 0, dir.getX()/2.0));

        List<Vector> rayDirections = new ArrayList<>();

        for(double i = 0; i <= distance-1; i++) {
            rayDirections.add(start.clone().subtract(intermediate(left, right, i/(distance-1))).normalize());
        }

        //for()

        return 0;
    }

    private static Vector intermediate(Vector a, Vector b, double p) {
        return new Vector(a.getX()*p+b.getX()*(p-1), a.getY()*p+b.getY()*(p-1), a.getZ()*p+b.getZ()*(p-1));
    }
}
