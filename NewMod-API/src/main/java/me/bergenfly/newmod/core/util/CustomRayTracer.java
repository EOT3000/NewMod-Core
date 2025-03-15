package me.bergenfly.newmod.core.util;

import java.util.Random;

public class CustomRayTracer {
    double dirX;
    double dirY;
    double dirZ;

    double x;
    double y;
    double z;

    final double startX = 0;
    final double startY = 0;
    final double startZ = Double.NaN;

    /*private static int floor1(double d) {
        int a = (int) d;

        return a > d ? a-1 : a;
    }

    public static int floor2(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
    }

    //Winner
    public static int floor3(double num) {
        return (int) Math.floor(num);
    }*/

    public static int floor(double num) {
        return (int) Math.floor(num);
    }

    public void moveHalfBlock() {
        int oldBlockX = floor(x);
        int oldBlockY = floor(y);
        int oldBlockZ = floor(z);

        double newX = x+dirX;
        double newY = y+dirY;
        double newZ = z+dirZ;
    }

    /*public static void main(String[] args) {
        benchmark(5_000_000, 0);
        benchmark(5_000_000, 1);
        benchmark(5_000_000, 2);
        benchmark(5_000_000, 3);
    }

    private static void benchmark(int a, int b) {
        int[] first = new int[a];
        int[] second = new int[a];
        int[] third = new int[a];

        Random random = new Random();

        double[] numbers = new double[a];

        for(int i = 0; i < a; i++) {
            numbers[i] = random.nextInt(a*4)-(a*2)+random.nextDouble()+b;
        }

        switch (random.nextInt(6)) {
            case 0: {
                runFirst(a, first, numbers);
                runSecond(a, second, numbers);
                runThird(a, third, numbers);
                break;
            }
            case 1: {
                runFirst(a, first, numbers);
                runThird(a, third, numbers);
                runSecond(a, second, numbers);
                break;
            }
            case 2: {
                runSecond(a, second, numbers);
                runFirst(a, first, numbers);
                runThird(a, third, numbers);
                break;
            }
            case 3: {
                runSecond(a, second, numbers);
                runThird(a, third, numbers);
                runFirst(a, first, numbers);
                break;
            }
            case 4: {
                runThird(a, third, numbers);
                runFirst(a, first, numbers);
                runSecond(a, second, numbers);
                break;
            }
            case 5: {
                runThird(a, third, numbers);
                runSecond(a, second, numbers);
                runFirst(a, first, numbers);
                break;
            }
        }

        for(int i = 0; i < a; i++) {
            if((third[i] != second[i]) || (second[i] != first[i]) || (third[i] != first[i])) {
                System.out.println("Discrepancy: ");
                System.out.println("Unrounded: " + numbers[i]);
                System.out.println("1: " + first[i]);
                System.out.println("2: " + second[i]);
                System.out.println("3: " + third[i]);
                System.out.println();
            }
        }
    }

    private static void runFirst(int a, int[] first, double numbers[]) {
        long start1 = System.currentTimeMillis();

        for(int i = 0; i < a; i++) {
            first[i] = floor1(numbers[i]);
        }

        System.out.println("First took: " + (System.currentTimeMillis() - start1));
    }

    private static void runSecond(int a, int[] second, double[] numbers) {
        long start2 = System.currentTimeMillis();

        for(int i = 0; i < a; i++) {
            second[i] = floor2(numbers[i]);
        }

        System.out.println("Second took: " + (System.currentTimeMillis() - start2));
    }

    private static void runThird(int a, int third[], double numbers[]) {
        long start3 = System.currentTimeMillis();

        for(int i = 0; i < a; i++) {
            third[i] = floor3(numbers[i]);
        }

        System.out.println("Third took: " + (System.currentTimeMillis() - start3));
    }*/
}
