package algorithms;

import metrics.Metrics;
import metrics.RecursionTracker;
import util.ArrayUtils;

import java.util.Random;

public class QuickSort {
    private static final Random RNG = new Random();

    public static void executeSort(int[] data, Metrics m) {
        if (data == null || data.length <= 1) return;
        ArrayUtils.shuffle(data, RNG);
        tailRecursiveSort(data, 0, data.length - 1, m);
    }

    private static void tailRecursiveSort(int[] arr, int start, int end, Metrics m) {
        while (start < end) {
            try (RecursionTracker rt = new RecursionTracker(m)) {
                int p = placePivot(arr, start, end, m);
                int leftLen = p - start;
                int rightLen = end - p;

                if (leftLen <= rightLen) {
                    tailRecursiveSort(arr, start, p - 1, m);
                    start = p + 1;
                } else {
                    tailRecursiveSort(arr, p + 1, end, m);
                    end = p - 1;
                }
            }
        }
    }

    private static int placePivot(int[] arr, int low, int high, Metrics m) {
        int randomPos = low + RNG.nextInt(high - low + 1);
        int pivot = arr[randomPos];
        ArrayUtils.swap(arr, randomPos, high);
        int marker = low;
        for (int i = low; i < high; i++) {
            m.incrementComparisons();
            if (arr[i] <= pivot) {
                ArrayUtils.swap(arr, marker, i);
                marker++;
            }
        }
        ArrayUtils.swap(arr, marker, high);
        return marker;
    }
}