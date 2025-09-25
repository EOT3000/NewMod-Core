package me.bergenfly.newmod.flyfun.food.vanilla;

public class TempHumidDistribution {
    /*public static final float NINETY_TEN_DIFFERENT_CONSTANT_LOG2 = -4.450f;
    public static final float LOG2_POINT_TEN = -3.322f;*/

    private float idealHumid;


    private float fullHealthRadiusHumid;
    private float decayRadiusHumid;
    //private float humidFullExtent;
    //private float humidSlope;
    //private float baseHumid;
    //private float powerHumid;
    private float additionalSensitivityHumid;


    private float idealTemp;


    private float fullHealthRadiusTemp;
    private float decayRadiusTemp;
    //private float tempFullExtent;
    //private float tempSlope;
    //private float baseTemp;
    //private float powerTemp;
    private float additionalSensitivityTemp;

    public static TempHumidDistribution create(float idealTemp, float fullHealthRadiusTemp, float decayRadiusTemp, float additionalTempSensitivity,
                                        float idealHumid, float fullHealthRadiusHumid, float decayRadiusHumid, float additionalHumidSensitivity) {
        TempHumidDistribution dist = new TempHumidDistribution();

        if(decayRadiusHumid<0 || fullHealthRadiusTemp<0 || decayRadiusTemp<0 || fullHealthRadiusHumid<0) {
            throw new IllegalArgumentException();
        }

        dist.fullHealthRadiusHumid = fullHealthRadiusHumid;
        dist.decayRadiusHumid = decayRadiusHumid;

        dist.fullHealthRadiusTemp = fullHealthRadiusTemp;
        dist.decayRadiusTemp = decayRadiusTemp;

        //dist.tempSlope = .8f/(ninetyPercentTempDif-tenPercentTempDif);
        //dist.tempFullExtent = tenPercentTempDif-.1f/dist.tempSlope;

        //System.out.println(dist.tempSlope);


        dist.idealTemp = idealTemp;
        //dist.powerTemp = (float) (NINETY_TEN_DIFFERENT_CONSTANT_LOG2/(Math.log(tenPercentTempDif/ninetyPercentTempDif)/Math.log(2)));
        //dist.baseTemp = (float) (Math.pow(2, -LOG2_POINT_TEN*Math.pow(tenPercentTempDif, dist.powerTemp)));
        dist.additionalSensitivityTemp = additionalTempSensitivity;


        //dist.humidSlope = .8f/(ninetyPercentHumidDif-tenPercentHumidDif);
        //dist.humidFullExtent = tenPercentHumidDif-.1f/dist.humidSlope;

        dist.idealHumid = idealHumid;
        //dist.powerHumid = (float) (NINETY_TEN_DIFFERENT_CONSTANT_LOG2/(Math.log(tenPercentHumidDif/ninetyPercentHumidDif)/Math.log(2)));
        //dist.baseHumid = (float) (Math.pow(2, -LOG2_POINT_TEN*Math.pow(tenPercentHumidDif, dist.powerHumid)));
        dist.additionalSensitivityHumid = additionalHumidSensitivity;

        //System.out.println("extent: " + dist.tempFullExtent);

        return dist;
    }

    public float probabilityFinalFailure(float temp, float humid) {
        float pTemp = probabilityFinalFailureFromTemp(temp);
        float pHumid = probabilityFinalFailureFromHumid(humid);

        //System.out.println();
        //System.out.println(pTemp);
        //System.out.println(pHumid);

        float squared = (pTemp*Math.abs(pTemp)+pHumid*pHumid)/2;

        if(squared >= 1) {
            return 1;
        }

        return (float) (Math.sqrt(squared));
    }

    public float probabilityFinalFailureFromTemp(float temp) {
        float distance = Math.abs(temp-idealTemp);

        if(distance <= fullHealthRadiusTemp) {
            return additionalSensitivityTemp;
        }


        return (((distance-fullHealthRadiusTemp)/decayRadiusTemp)*(1-additionalSensitivityTemp)+additionalSensitivityTemp);
    }

    public float probabilityFinalFailureFromHumid(float humid) {
        float distance = Math.abs(humid-idealHumid);

        if(distance <= fullHealthRadiusHumid) {
            return additionalSensitivityHumid;
        }

        if(distance >= (fullHealthRadiusHumid+decayRadiusHumid)) {
            return 1;
        }

        float proportionDecayed = (distance-fullHealthRadiusHumid)/decayRadiusHumid;

        return (proportionDecayed*(1-additionalSensitivityHumid)+additionalSensitivityHumid);
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
                1.4f, .5f, .25f, 0.025f,
                .45f, .425f, .75f, 0.025f);

        System.out.println("jungle: " + d.probabilityFinalFailure(.95f, .9f));
        System.out.println("desert: " + d.probabilityFinalFailure(2.0f, .0f));
        System.out.println("wooded badlands: " + d.probabilityFinalFailure(1.75f, .25f));
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
