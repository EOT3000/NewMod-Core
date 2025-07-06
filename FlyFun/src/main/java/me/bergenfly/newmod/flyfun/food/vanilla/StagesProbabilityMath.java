package me.bergenfly.newmod.flyfun.food.vanilla;

public class StagesProbabilityMath {

    //OEIS A157963
    /*private static final int[][] COEFFICIENT_NUMERATORS =
            {
                    {1},
                    {1, -1},
                    {2, -3, 1},
                    {8, -14, 7, -1},
                    {64, -120, 70, -15, 1},
                    {1024, -1984, 1240, -310, 31, -1},
                    {32768, -64512, 41664, -11160, 1302, -63, 1},
                    {2097152, -4161536, 2731008, -755904, 94488, -5334, 127, -1},
                    {268435456, -534773760, 353730560, -99486720, 12850368, -777240, 21590, -255, 1}
            };*/


    public float probabilityImmediateFailure(double finalFailure) {
        double finalP = finalFailure;

        if(finalP == 0) return 0;

        return (float) ((Math.sqrt(1-2*finalP+2*finalP*finalP) + finalP - 1)/finalP);
    }

    public float probabilityReroll(double immediateFailure) {
        double immediateFailP = immediateFailure;

        return (float) (.5+.5*immediateFailP*immediateFailP-immediateFailP);
    }

    /**
     * Determines the likelihood of a stage failing, to achieve a desired final failure probability.
     *
     * @param endLikelihoodFailure the likelihood the plant fails to reach its final stage.
     * @param numberStages number of stages <i>before</i> the final stage. Sapling is 1 for example, because there is one sapling stage, and 1 final, tree stage (ignoring the sapling's substages).
     * @return the likelihood of failure of each individual stage prior to the final stage, necessary to achieve the final endLikelihoodFailure.
     */
    public static double stageLikelihoodFailure(double endLikelihoodFailure, int numberStages) {
        return 1-Math.pow(1-endLikelihoodFailure, 1.0/numberStages);
    }
}
