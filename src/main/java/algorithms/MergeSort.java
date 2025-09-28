package algorithms;

import metrics.Metrics;
import metrics.RecursionTracker;

public class MergeSort {
    private static final int THRESHOLD = 8;

    public static void performSort(int[] input, Metrics m) {
        if (input == null || input.length <= 1) return;
        int[] temp = new int[input.length];
        m.incrementAllocations();
        recursiveSort(input, temp, 0, input.length - 1, m);
    }

    private static void recursiveSort(int[] src, int[] dest, int begin, int end, Metrics m) {
        if (end - begin + 1 <= THRESHOLD) {
            simpleSort(src, begin, end, m);
            return;
        }

        try (RecursionTracker rt = new RecursionTracker(m)) {
            int middle = (begin + end) / 2;
            recursiveSort(src, dest, begin, middle, m);
            recursiveSort(src, dest, middle + 1, end, m);
            combine(src, dest, begin, middle, end, m);
        }
    }

    private static void simpleSort(int[] arr, int start, int finish, Metrics m) {
        for (int i = start + 1; i <= finish; i++) {
            int current = arr[i];
            int j = i - 1;
            while (j >= start) {
                m.incrementComparisons();
                if (arr[j] > current) {
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = current;
        }
    }

    private static void combine(int[] original, int[] buffer, int low, int mid, int high, Metrics m) {
        System.arraycopy(original, low, buffer, low, high - low + 1);
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                original[k] = buffer[j++];
            } else if (j > high) {
                original[k] = buffer[i++];
            } else {
                m.incrementComparisons();
                original[k] = (buffer[i] <= buffer[j]) ? buffer[i++] : buffer[j++];
            }
        }
    }
}