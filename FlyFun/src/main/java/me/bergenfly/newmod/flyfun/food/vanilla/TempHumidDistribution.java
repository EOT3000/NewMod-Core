package me.bergenfly.newmod.flyfun.food.vanilla;

public class TempHumidDistribution {
    public static final float NINETY_TEN_DIFFERENT_CONSTANT_LOG2 = -4.450f;
    public static final float LOG2_POINT_TEN = -3.322f;

    private float idealHumid;
    private float baseHumid;
    private float powerHumid;
    private float additionalSensitivityHumid;


    private float idealTemp;
    private float baseTemp;
    private float powerTemp;
    private float additionalSensitivityTemp;

    public static TempHumidDistribution create(float idealTemp, float ninetyPercentTempDif, float tenPercentTempDif, float additionalTempSensitivity,
                                        float idealHumid, float ninetyPercentHumidDif, float tenPercentHumidDif, float additionalHumidSensitivity) {
        TempHumidDistribution dist = new TempHumidDistribution();

        dist.idealTemp = idealTemp;
        dist.powerTemp = (float) (NINETY_TEN_DIFFERENT_CONSTANT_LOG2/(Math.log(tenPercentTempDif/ninetyPercentTempDif)/Math.log(2)));
        dist.baseTemp = (float) (Math.pow(2, -LOG2_POINT_TEN*Math.pow(tenPercentTempDif, dist.powerTemp)));
        dist.additionalSensitivityTemp = additionalTempSensitivity;

        dist.idealHumid = idealHumid;
        dist.powerHumid = (float) (NINETY_TEN_DIFFERENT_CONSTANT_LOG2/(Math.log(tenPercentHumidDif/ninetyPercentHumidDif)/Math.log(2)));
        dist.baseHumid = (float) (Math.pow(2, -LOG2_POINT_TEN*Math.pow(tenPercentHumidDif, dist.powerHumid)));
        dist.additionalSensitivityHumid = additionalHumidSensitivity;

        return dist;
    }

    public float probabilityFinalFailure(float temp, float humid) {
        float fromIdealTemp = Math.abs(temp-idealTemp);
        float fromIdealHumid = Math.abs(humid-idealHumid);

        double pTemp = (1-additionalSensitivityTemp) * Math.pow(baseTemp, -(1.0/(Math.pow(fromIdealTemp, powerTemp)))) + additionalSensitivityTemp;
        double pHumid = (1-additionalSensitivityHumid) * Math.pow(baseHumid, -(1.0/(Math.pow(fromIdealHumid, powerHumid)))) + additionalSensitivityHumid;

        return (float) (Math.sqrt((pTemp*pTemp+pHumid*pHumid)/2));
    }

    public float probabilityImmediateFailure(float temp, float humid) {
        float finalP = probabilityFinalFailure(temp, humid);

        if(finalP == 0) return 0;

        return (float) ((Math.sqrt(1-2*finalP+2*finalP*finalP) + finalP - 1)/finalP);
    }

    public float probabilityReroll(float temp, float humid) {
        float immediateFailP = probabilityImmediateFailure(temp, humid);

        return (float) (.5+.5*immediateFailP*immediateFailP-immediateFailP);
    }

    /*public static void main(String[] args) {
        TempHumidDistribution d = create(0,1,.5f, 0, 0,0,0,0);

        System.out.println(d.baseTemp);
        System.out.println(d.powerTemp);

        for(double x = 0; x < 1; x+=.05) {
            double fail = probabilityImmediateFailure((float) x);
            double reroll = probabilityReroll((float) x);
            double success = 1-fail-reroll;

            for(int i = 0; i < 10; i++) {
                fail+=reroll*fail;
                success+=success*reroll;
                reroll=reroll*reroll;
            }

            System.out.println(c(x) + ": \t" + c(success) + ", \t" + c(reroll) + ", \t" + c(fail));
        }
    }

    private static String c(double x) {
        return Double.toString(Math.round(x*1000)/1000.0);
    }

    public static float probabilityImmediateFailure(float finalP) {
        if(finalP == 0) return 0;

        return (float) ((Math.sqrt(1-2*finalP+2*finalP*finalP) + finalP - 1)/finalP);
    }

    public static float probabilityReroll(float finalP) {
        float immediateFailP = probabilityImmediateFailure(finalP);

        return (float) (.5+.5*immediateFailP*immediateFailP-immediateFailP);
    }*/
}
