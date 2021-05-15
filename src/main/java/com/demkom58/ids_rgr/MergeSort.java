package com.demkom58.ids_rgr;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class MergeSort extends RecursiveAction {
    private static final int THRESHOLD = 8192;
    private final int[] array;
    private final int left;
    private final int right;

    public MergeSort(int[] array) {
        this(array, 0, array.length - 1);
    }

    public MergeSort(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        // Якщо змінна яка вказує початок більша або є кінцем, завершуємо метод
        if (left >= right)
            return;

        int center = (left + right) / 2;

        // Створимо дві нові підзадачі
        if (right - left < THRESHOLD) {
            new MergeSort(array, left, center).compute();
            new MergeSort(array, center + 1, right).compute();
        } else {
            invokeAll(new MergeSort(array, left, center), new MergeSort(array, center + 1, right));
        }

        // Відсортуємо рівень
        sort(left, center, right);
    }

    private void sort(int left, int mid, int right) {
        int[] temp = new int[right - left + 1];

        int lCurrent = left;
        int rCurrent = mid + 1;

        int writes = 0;

        while (lCurrent <= mid && rCurrent <= right) {
            if (array[lCurrent] <= array[rCurrent]) {
                temp[writes] = array[lCurrent];
                writes++; // Збільшуємо кількість записів
                lCurrent++; // Збільшуємо ітерацію лівої частини
            } else {
                temp[writes] = array[rCurrent];
                writes++; // Збільшуємо кількість записів
                rCurrent++; // Збільшуємо ітерацію правої частини
            }
        }

        // Записуємо відсортовані дані
        while (rCurrent <= right)
            temp[writes++] = array[rCurrent++];

        while (lCurrent <= mid)
            temp[writes++] = array[lCurrent++];

        // Записуємо фінальний результат в результуючий масив
        for (writes = 0; writes < temp.length; writes++)
            array[left + writes] = temp[writes];
    }
}