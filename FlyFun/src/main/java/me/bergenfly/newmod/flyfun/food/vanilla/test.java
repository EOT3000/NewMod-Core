package me.bergenfly.newmod.flyfun.food.vanilla;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class test {

    public enum Enum {
        OAK_TREE(.6f, 1.0f, .4f, 0.02f,
                .6f, .65f, .45f, 0.02f, 1),
        SPRUCE_TREE(.15f, 1.0f, .65f, 0.025f,
                .6f, .4f, .35f, 0.025f, 1),
        ACACIA_TREE(1.2f, .85f, .3f, 0.025f,
                .2f, .5f, .3f, 0.01f, 1),
        BIRCH_TREE(.55f, .4f, .3f, 0.01f,
                .7f, .4f, .25f, 0.01f, 1),
        DARK_OAK_TREE(.7f, .25f, .15f, 0.01f,
                .8f, .15f, .1f, 0.01f, 1),
        JUNGLE_TREE(.95f, .2f, .15f, 0.03f,
                .85f, .25f, .15f, 0.03f, 1),
        MANGROVE_TREE(.8f, .22f, .12f, 0.04f,
                .9f, .1f, .07f, 0.04f, 1),
        AZALEA_TREE(.65f, .35f, .2f, 0.01f,
                .6f, .45f, .3f, 0.01f, 1),
        CHERRY_TREE(.5f, .4f, .3f, 0.045f,
                .8f, .55f, .3f, 0.045f, 1),
        PALE_OAK_TREE(.7f, .2f, .15f, 0.015f,
                .8f, .15f, .1f, 0.015f, 1),

        BEETROOT_CROP(.7f, .85f, .5f, 0.01f,
                .5f, .4f, .3f, 0.01f, 3),
        WHEAT_CROP(.85f, .6f, .4f, 0.01f,
                .5f, .4f, .3f, 0.01f, 7),
        POTATO_CROP(.75f, .7f, .5f, 0.01f,
                .5f, .35f, .3f, 0.01f, 7),
        CARROT_CROP(.75f, .7f, .5f, 0.01f,
                .5f, .35f, .3f, 0.01f, 7),
        MELON_CROP(1.35f, 1.15f, .85f, 0.01f,
                .55f, .75f, .5f, 0.01f, 8),
        PUMPKIN_CROP(.3f, 1.05f, .8f, 0.01f,
                .55f, .75f, .5f, 0.01f, 8),

        SWEET_BERRY_BUSH(.0f, .75f, .65f, 0.001f,
                .55f, .75f, .5f, 0.00f, 3),

        BAMBOO(.8f, .6f, .45f, 0.005f,
                .7f, .7f, .45f, 0.005f, 12),

        CACTUS(1.7f, .65f, .45f, 0.005f,
                .1f, .35f, .2f, 0.00f, 2),

        SUGAR_CANE(1.3f, 2.5f, 1.5f, 0.01f,
                .5f, .7f, .6f, 0.00f, 2);

        public final TempHumidDistribution data;

        Enum(TempHumidDistribution data, int stages) {
            this.data = data;
        }

        Enum(float idealTemp, float ninetyPercentTempDif, float tenPercentTempDif, float additionalTempSensitivity,
                     float idealHumid, float ninetyPercentHumidDif, float tenPercentHumidDif, float additionalHumidSensitivity, int stages) {
            this(TempHumidDistribution.create(idealTemp, ninetyPercentTempDif, tenPercentTempDif, additionalTempSensitivity,
                    idealHumid, ninetyPercentHumidDif, tenPercentHumidDif, additionalHumidSensitivity), stages);
        }
    }

    public static void main(String[] args) throws Exception {
        double lowestF = 100;
        double highestF = -1;

        //System.out.println("a");

        BufferedImage image = new BufferedImage(218, 82, BufferedImage.TYPE_INT_RGB);

        //0-3
        //3-6
        //6-9
        //9-12
        //12-15
        //15-18

        for(int x = -56; x <= 160; x+=10) {
            for(int y = 0; y <= 80; y+=10) {
                float totalFertility = 0;

                int fertility = 1;

                for(Enum e : new Enum[]{Enum.CHERRY_TREE/*, Enum.MELON_CROP, Enum.CACTUS*/}) {
                    //totalFertility+=(1-e.data.probabilityFinalFailure(x/80.0f,y/80.0f));

                    //double successProb = 1-e.data.probabilityHumid(x/80.0f,y/80.0f);
                    //double successProb2 = 1-e.data.probabilityTemp(x/80.0f,y/80.0f);

                    //if(successProb > .65  && fertility < 1) fertility = 0;

                    //if(successProb > .75  && fertility < 2) fertility = 1;

                    //if(successProb > .85  && fertility < 3) fertility = 2;

                    //System.out.print(((int) (successProb*10) == 10 ? (int) (successProb*10) : (int) (successProb*10) + " ") + "/" + ((int) (successProb2*10) == 10 ? (int) (successProb2*10) : (int) (successProb2*10) + " ") + " ");

                    //if(successProb > .9) fertility += 1;
                }

                if(totalFertility < lowestF) {
                    lowestF = totalFertility;
                }

                if(totalFertility > highestF) {
                    highestF = totalFertility;
                }

                image.setRGB(x+56,y,0xFF0000);

                if(fertility == 0) image.setRGB(x+56,y,0xFF8000);
                if(fertility == 1) image.setRGB(x+56,y,0xFFFF00);
                if(fertility == 2) image.setRGB(x+56,y,0x80FF00);
                if(fertility == 3) image.setRGB(x+56,y,0x00FF00);

                /*if(totalFertility > 0) {
                    image.setRGB(x+56,y,0xFF6000);
                }

                if(totalFertility > .5) {
                    image.setRGB(x+56,y,0xFFAF00);
                }

                if(totalFertility > 1) {
                    image.setRGB(x+56,y,0xFFFF00);
                }

                if(totalFertility > 1.5) {
                    image.setRGB(x+56,y,0xA0FF00);
                }

                if(totalFertility > 2) {
                    image.setRGB(x+56,y,0x00FF00);
                }*/

                //System.out.println(Math.round(totalFertility*1000)/1000.0);
            }

            System.out.println();
        }

        ImageIO.write(image, "png", new File("colors.png"));

        System.out.println(lowestF);
        System.out.println(highestF);
    }
}
