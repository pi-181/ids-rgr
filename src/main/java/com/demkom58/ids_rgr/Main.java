package com.demkom58.ids_rgr;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static final int PARALLELISM = 6;
    public static final int ARRAY_SIZE = 500_000;
    public static final int TESTS = 100;

    public static void main(String[] args) {
        {
            final int[] rand = randomArray(ARRAY_SIZE, 1000);
            final long l = System.currentTimeMillis();
            Arrays.sort(rand);
            System.out.println("Done for " + (System.currentTimeMillis() - l) + "ms");
        }

        long singleTime = 0;
        for (int i = 1; i <= PARALLELISM; i++) {
            long time = 0;
            for (int j = 0; j < TESTS; j++) {
                Runtime.getRuntime().gc();
                time += benchmark(i, ARRAY_SIZE, 1000);
            }
            time = time / TESTS;

            if (i == 1)
                singleTime = time;

            double boost = singleTime / ((double) time);
            double efficiency = boost / i * 100;

            System.out.println("Array size " + ARRAY_SIZE + "; parallelism " + i + "; " +
                    "done for " + time + "ms; boost " + boost + "; efficiency " + efficiency);
        }
    }

    private static long benchmark(int parallelism, int arraySize, int seed) {
        ForkJoinPool pool = new ForkJoinPool(parallelism);

        int[] array = randomArray(arraySize, seed);
        MergeSort mergeSort = new MergeSort(array);

        long startTime = System.currentTimeMillis();
        pool.invoke(mergeSort);

        return System.currentTimeMillis() - startTime;
    }

    private static int[] randomArray(final int size, final int seed) {
        final int[] result = new int[size];
        final Random rand = new Random(seed);

        for (int i = 0; i < size; i++)
            result[i] = rand.nextInt(1000);

        return result;
    }
}
