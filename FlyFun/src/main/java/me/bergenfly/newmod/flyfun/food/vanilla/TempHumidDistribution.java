package me.bergenfly.newmod.flyfun.food.vanilla;

public class TempHumidDistribution {
    /*public static final float NINETY_TEN_DIFFERENT_CONSTANT_LOG2 = -4.450f;
    public static final float LOG2_POINT_TEN = -3.322f;*/

    private float idealHumid;

    private float humidFullExtent;
    private float humidSlope;
    //private float baseHumid;
    //private float powerHumid;
    private float additionalSensitivityHumid;


    private float idealTemp;

    private float tempFullExtent;
    private float tempSlope;
    //private float baseTemp;
    //private float powerTemp;
    private float additionalSensitivityTemp;

    public static TempHumidDistribution create(float idealTemp, float ninetyPercentTempDif, float tenPercentTempDif, float additionalTempSensitivity,
                                        float idealHumid, float ninetyPercentHumidDif, float tenPercentHumidDif, float additionalHumidSensitivity) {
        TempHumidDistribution dist = new TempHumidDistribution();

        if(tenPercentTempDif>ninetyPercentTempDif) {
            throw new IllegalArgumentException();
        }

        if(tenPercentHumidDif>ninetyPercentHumidDif) {
            throw new IllegalArgumentException();
        }

        dist.tempSlope = .8f/(ninetyPercentTempDif-tenPercentTempDif);
        dist.tempFullExtent = tenPercentTempDif-.1f/dist.tempSlope;

        //System.out.println(dist.tempSlope);


        dist.idealTemp = idealTemp;
        //dist.powerTemp = (float) (NINETY_TEN_DIFFERENT_CONSTANT_LOG2/(Math.log(tenPercentTempDif/ninetyPercentTempDif)/Math.log(2)));
        //dist.baseTemp = (float) (Math.pow(2, -LOG2_POINT_TEN*Math.pow(tenPercentTempDif, dist.powerTemp)));
        dist.additionalSensitivityTemp = additionalTempSensitivity;


        dist.humidSlope = .8f/(ninetyPercentHumidDif-tenPercentHumidDif);
        dist.humidFullExtent = tenPercentHumidDif-.1f/dist.humidSlope;

        dist.idealHumid = idealHumid;
        //dist.powerHumid = (float) (NINETY_TEN_DIFFERENT_CONSTANT_LOG2/(Math.log(tenPercentHumidDif/ninetyPercentHumidDif)/Math.log(2)));
        //dist.baseHumid = (float) (Math.pow(2, -LOG2_POINT_TEN*Math.pow(tenPercentHumidDif, dist.powerHumid)));
        dist.additionalSensitivityHumid = additionalHumidSensitivity;

        //System.out.println("extent: " + dist.tempFullExtent);

        return dist;
    }

    public float probabilityFinalFailure(float temp, float humid) {
        float pTemp;

        if(temp <= idealTemp+tempFullExtent && temp >= idealTemp-tempFullExtent) {
            pTemp = 0;
        } else {
            float fromIdeal = Math.abs(temp-idealTemp);

            /*System.out.println("temp: " + temp);
            System.out.println("ideal: " + idealTemp);
            System.out.println("from ideal: " + fromIdeal);
            System.out.println("extent: " + tempFullExtent);*/

            float fromExtent = Math.abs(fromIdeal-tempFullExtent);

            //System.out.println("from extend: " + fromExtent);

            pTemp = Math.min(fromExtent*tempSlope, 1);
        }

        float pHumid;

        if(humid <= idealHumid+humidFullExtent && humid >= idealHumid-humidFullExtent) {
            pHumid = 0;
        } else {
            float fromIdeal = Math.abs(humid-idealHumid);

            float fromExtent = Math.abs(fromIdeal-humidFullExtent);

            pHumid = Math.min(fromExtent*humidSlope, 1);
        }

        pTemp = pTemp*(1-additionalSensitivityTemp)+additionalSensitivityTemp;
        pHumid = pHumid*(1-additionalSensitivityHumid)+additionalSensitivityHumid;

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

    public static void main(String[] args) {
        /*TempHumidDistribution d = create(
                .0f, .95f, .8f, 0.01f,
                .55f, .45f, .3f, 0.01f);*/

        TempHumidDistribution d = create(
                1.3f, 2.5f, 1.5f, 0.01f,
                .5f, .7f, .6f, 0.00f);

        System.out.println("jungle: " + d.probabilityFinalFailure(.95f, .9f));
        System.out.println("desert: " + d.probabilityFinalFailure(2.0f, .0f));
        System.out.println("jagged peaks: " + d.probabilityFinalFailure(-.7f, .9f));
        System.out.println("snowy taiga: " + d.probabilityFinalFailure(-.5f, .4f));
        System.out.println("plains: " + d.probabilityFinalFailure(.8f, .4f));
        System.out.println("cherry grove: " + d.probabilityFinalFailure(.5f, .8f));
        System.out.println("forest: " + d.probabilityFinalFailure(.7f, .8f));
        System.out.println("meadow: " + d.probabilityFinalFailure(.5f, .8f));
        System.out.println("swamp: " + d.probabilityFinalFailure(.6f, .9f));
        System.out.println("mangrove swamp: " + d.probabilityFinalFailure(.8f, .9f));
        System.out.println("savanna: " + d.probabilityFinalFailure(1.2f, .2f));
        System.out.println("frozen river: " + d.probabilityFinalFailure(.0f, .5f));
        System.out.println("dark forest: " + d.probabilityFinalFailure(.7f, .8f));
        System.out.println("birch forest: " + d.probabilityFinalFailure(.6f, .6f));

        System.out.println("spruce 1: " + d.probabilityFinalFailure(-.5f, .8f));
        System.out.println("spruce 2: " + d.probabilityFinalFailure(-.5f, .3f));
        System.out.println("spruce 2: " + d.probabilityFinalFailure(.3f, .8f));
        System.out.println("spruce 2: " + d.probabilityFinalFailure(.3f, .3f));
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
