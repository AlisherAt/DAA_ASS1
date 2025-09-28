package metrics;

public class RecursionTracker implements AutoCloseable {
    private final Metrics metrics;

    public RecursionTracker(Metrics m) {
        this.metrics = m;
        metrics.enterRecursion();
    }

    @Override
    public void close() {
        metrics.exitRecursion();
    }
}