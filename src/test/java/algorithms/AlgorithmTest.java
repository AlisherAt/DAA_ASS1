package algorithms;

import metrics.Metrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmTest {

    private static final Random RNG = new Random(42); // deterministic tests

    @BeforeEach
    void resetMetrics() {
    }

    @Test
    void testQuickSortCorrectnessAndDepth() {
        int[] original = generateRandomArray(5000);
        int[] toSort = original.clone();
        Metrics m = new Metrics();

        QuickSort.executeSort(toSort, m);

        int[] expected = original.clone();
        Arrays.sort(expected);
        assertArrayEquals(expected, toSort, "QuickSort output should be sorted");

        int maxDepth = m.getMaxDepth();
        int theoreticalMax = 2 * (int) (Math.log(toSort.length) / Math.log(2)) + 10;
        assertTrue(maxDepth <= theoreticalMax,
                "QuickSort depth too high. Actual: " + maxDepth + ", Expected ≤ " + theoreticalMax);
    }

    @Test
    void testMergeSortCorrectnessAndStability() {
        int[] original = {5, 2, 8, 2, 9, 1, 5, 4};
        int[] toSort = original.clone();
        Metrics m = new Metrics();

        MergeSort.performSort(toSort, m);

        int[] expected = {1, 2, 2, 4, 5, 5, 8, 9};
        assertArrayEquals(expected, toSort, "MergeSort should sort correctly");

        assertTrue(m.getComparisons() > 0, "Should perform comparisons");
    }

    @Test
    void testDeterministicSelectCorrectness() {
        for (int size : new int[]{100, 500, 1000}) {
            int[] original = generateRandomArray(size);
            int k = RNG.nextInt(size); // 0-based index

            int[] copy = original.clone();
            int expected = Arrays.stream(copy).sorted().toArray()[k];
            Metrics m = new Metrics();
            int actual = DeterministicSelect.kthSmallest(original, k, m);

            assertEquals(expected, actual,
                    "Select(k=" + k + ") failed for size=" + size);
            assertTrue(m.getComparisons() > 0, "Should perform comparisons");
        }
    }

    @Test
    void testClosestPairAgainstBruteForce() {
        for (int n = 2; n <= 50; n += 5) {
            ClosestPair.Point[] points = generateRandomPoints(n);
            Metrics m = new Metrics();

            double fastResult = ClosestPair.computeMinDistance(points, m);
            double bruteResult = bruteForceClosest(points);

            assertEquals(bruteResult, fastResult, 1e-9,
                    "ClosestPair failed for n=" + n);
            assertTrue(m.getComparisons() > 0, "Should perform comparisons");
        }
    }


    private int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RNG.nextInt(10000);
        }
        return arr;
    }

    private ClosestPair.Point[] generateRandomPoints(int n) {
        ClosestPair.Point[] points = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new ClosestPair.Point(RNG.nextInt(1000), RNG.nextInt(1000));
        }
        return points;
    }

    private double bruteForceClosest(ClosestPair.Point[] points) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = points[i].dist(points[j]);
                if (dist < min) min = dist;
            }
        }
        return min;
    }
}