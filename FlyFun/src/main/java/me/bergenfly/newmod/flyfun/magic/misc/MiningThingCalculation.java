package me.bergenfly.newmod.flyfun.magic.misc;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.Material;

import java.util.Map;
import java.util.Random;

// A Thing, no name yet that I could think of, that spawns rarely when breaking blocks
// This is specifically for mining- so stone like blocks.
// Ores and rarer blocks are more likely to drop The Item than stone
// To prevent just farming one block, mining one block a lot decreases the likelyhood that block drops The Thing
// Mining a diversity of blocks is more effective
public class MiningThingCalculation {
    //Start with material- change with custom blocks if necessary
    public Object2IntMap<Material> scores;

    public void add(Material material) {
        int reducer = scores.put(material, scores.getOrDefault(material, 0)+1)+1;

        for(Material m : scores.keySet()) {
            if(m == material) {
                continue;
            }

            int reduce = scores.getOrDefault(m, 0);

            double rf = reductionFraction(reduce, reducer);

            int newReduced = reduce-randomRound(rf*reduce);

            scores.put(m, newReduced);
        }
    }

    public static double reductionFraction(int scoreToReduce, int scoreOfReducer) {
        double reducedFraction = fractionFunction_Big2Small(.05, .05, 128, scoreToReduce);
        double reducerFraction = fractionFunction_Big2Small(.6, .4, 5, scoreOfReducer);

        return reducedFraction*reducerFraction;
    }

    public static double fractionFunction_Big2Small(double min, double range, int halfPoint, int x) {
        return range-(range*x)/(x+halfPoint)+min;
    }

    public static double frac(double value) {
        return value - Math.floor(value);
    }

    public static final Random random = new Random();

    //I don't think this works for negative numbers
    public static int randomRound(double value) {
        double frac = frac(value);

        if(random.nextDouble() < frac) {
            return (int) Math.ceil(value);
        }

        return (int) Math.floor(value);
    }
}
