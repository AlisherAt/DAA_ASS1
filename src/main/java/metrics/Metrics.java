package metrics;

public class Metrics {
    private int comparisons = 0;
    private int allocations = 0;
    private int recursionDepth = 0;
    private int maxDepth = 0;

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementAllocations() {
        allocations++;
    }

    public void enterRecursion() {
        recursionDepth++;
        if (recursionDepth > maxDepth) maxDepth = recursionDepth;
    }

    public void exitRecursion() {
        recursionDepth--;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getAllocations() {
        return allocations;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void reset() {
        comparisons = 0;
        allocations = 0;
        recursionDepth = 0;
        maxDepth = 0;
    }
}