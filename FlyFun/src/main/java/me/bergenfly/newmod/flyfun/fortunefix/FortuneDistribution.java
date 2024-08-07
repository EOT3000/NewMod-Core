package me.fly.newmod.flyfun.fortunefix;

import java.util.Random;

public interface FortuneDistribution {
    Random random = new Random();

    FortuneDistribution oneOrTwo = FortuneDistribution::oneOrTwoDistribution;
    FortuneDistribution oneToThree = fortune -> upToDistribution(fortune, 3);
    FortuneDistribution oneToThreeAddOneThirdFortune = fortune -> upToDistribution(fortune+0.333, 3);
    FortuneDistribution twoTo_ThreePlusFortune_AddOneHalfFortune = fortune -> upToDistribution(fortune+0.5, 2+fortune)+1;
    //FortuneDistribution oneToThreeAddOneThirdFortune = fortune -> upToDistribution(fortune+0.333, 3);
    FortuneDistribution fourToSixteenAddOneTenthFortune = fortune -> upToDistribution(fortune+1, 13)+3;

    static int oneOrTwoDistribution(int fortune) {
        if(add(fortune)) {
            return 2;
        } else {
            return 1;
        }
    }

    static int upToDistribution(double fortune, int max) {
        int ret = 1;

        for(int i = 0; i < max-1; i++) {
            if(add(fortune/(max/35.0+2.5))) {
                ret++;
            }
        }

        return ret;
    }

    static boolean add(double fortune) {
        return random.nextDouble() > 1/(1.0+(fortune));
    }



    int generateRandomItemCount(int fortune);

    public static void main(String[] args) {
        for(int m = 0; m <= 7; m+=1) {
            for (int i = 0; i < 10000; i++) {
                System.out.print(upToDistribution(m+0.5, 2+m)+1);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
