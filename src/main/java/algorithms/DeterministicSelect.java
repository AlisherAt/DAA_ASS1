package algorithms;

import metrics.Metrics;
import metrics.RecursionTracker;
import util.ArrayUtils;

public class DeterministicSelect {

    public static int kthSmallest(int[] data, int index, Metrics m) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input array cannot be empty");
        }
        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("Index out of valid range");
        }
        return findKth(data, 0, data.length - 1, index, m);
    }

    private static int findKth(int[] arr, int lo, int hi, int target, Metrics m) {
        if (lo == hi) return arr[lo];

        try (RecursionTracker rt = new RecursionTracker(m)) {
            int pivotPos = choosePivot(arr, lo, hi, m);
            pivotPos = rearrange(arr, lo, hi, pivotPos, m);

            if (target == pivotPos) {
                return arr[target];
            } else if (target < pivotPos) {
                return findKth(arr, lo, pivotPos - 1, target, m);
            } else {
                return findKth(arr, pivotPos + 1, hi, target, m);
            }
        }
    }

    private static int choosePivot(int[] arr, int left, int right, Metrics m) {
        int len = right - left + 1;
        if (len <= 5) {
            return getMedianOfSmall(arr, left, right, m);
        }

        int groupCount = (len + 4) / 5;
        int[] medians = new int[groupCount];
        m.incrementAllocations();

        for (int g = 0; g < groupCount; g++) {
            int gStart = left + g * 5;
            int gEnd = Math.min(gStart + 4, right);
            medians[g] = getMedianOfSmall(arr, gStart, gEnd, m);
        }

        return findKth(medians, 0, groupCount - 1, groupCount / 2, m);
    }

    private static int getMedianOfSmall(int[] a, int s, int e, Metrics m) {
        for (int i = s + 1; i <= e; i++) {
            int val = a[i];
            int j = i - 1;
            while (j >= s) {
                m.incrementComparisons();
                if (a[j] > val) {
                    a[j + 1] = a[j];
                    j--;
                } else {
                    break;
                }
            }
            a[j + 1] = val;
        }
        return s + (e - s) / 2;
    }

    private static int rearrange(int[] arr, int l, int r, int pIdx, Metrics m) {
        int pivotVal = arr[pIdx];
        ArrayUtils.swap(arr, pIdx, r);
        int writePos = l;
        for (int i = l; i < r; i++) {
            m.incrementComparisons();
            if (arr[i] <= pivotVal) {
                ArrayUtils.swap(arr, writePos, i);
                writePos++;
            }
        }
        ArrayUtils.swap(arr, writePos, r);
        return writePos;
    }
}