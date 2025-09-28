package algorithms;

import metrics.Metrics;
import metrics.RecursionTracker;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {

    public static class Point {
        public final double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double dist(Point q) {
            double dx = this.x - q.x;
            double dy = this.y - q.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public static double computeMinDistance(Point[] pts, Metrics m) {
        if (pts == null || pts.length < 2) {
            throw new IllegalArgumentException("Need at least two points");
        }
        Point[] byX = pts.clone();
        Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
        m.incrementAllocations();
        return solve(byX, 0, byX.length - 1, m);
    }

    private static double solve(Point[] arr, int l, int r, Metrics m) {
        int n = r - l + 1;
        if (n <= 3) {
            return directCheck(arr, l, r, m);
        }

        try (RecursionTracker rt = new RecursionTracker(m)) {
            int mid = (l + r) / 2;
            double xMid = arr[mid].x;

            double d1 = solve(arr, l, mid, m);
            double d2 = solve(arr, mid + 1, r, m);
            double delta = Math.min(d1, d2);

            return Math.min(delta, scanStrip(arr, l, r, xMid, delta, m));
        }
    }

    private static double directCheck(Point[] pts, int start, int end, Metrics m) {
        double best = Double.MAX_VALUE;
        for (int i = start; i <= end; i++) {
            for (int j = i + 1; j <= end; j++) {
                double d = pts[i].dist(pts[j]);
                m.incrementComparisons();
                if (d < best) best = d;
            }
        }
        return best;
    }

    private static double scanStrip(Point[] pts, int l, int r, double centerX, double bound, Metrics m) {
        Point[] candidates = new Point[r - l + 1];
        int count = 0;
        for (int i = l; i <= r; i++) {
            if (Math.abs(pts[i].x - centerX) < bound) {
                candidates[count++] = pts[i];
            }
        }

        Arrays.sort(candidates, 0, count, Comparator.comparingDouble(p -> p.y));

        double min = bound;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count && (candidates[j].y - candidates[i].y) < min; j++) {
                if (j - i > 7) break;
                double d = candidates[i].dist(candidates[j]);
                m.incrementComparisons();
                if (d < min) min = d;
            }
        }
        return min;
    }
}