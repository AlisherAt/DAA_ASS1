# DAA_ASS1
Assignment 1 – Algorithms
Done by: Alisher Akhmet
Design and Implementation Overview
Stack Depth Optimization
MergeSort: Achieves logarithmic recursion depth naturally through perfectly balanced partitioning.
QuickSort: Employs a tail-recursion strategy that always recurses into the smaller subarray first, guaranteeing O(log n) maximum stack depth regardless of pivot quality.
Deterministic Select: Uses the median-of-medians technique to ensure the pivot splits the array sufficiently, leading to controlled recursion depth.
Closest Pair: Recursively divides the point set in half, maintaining Θ(log n) depth due to balanced spatial partitioning.
Memory Usage Strategy
MergeSort utilizes a single pre-allocated auxiliary buffer to avoid repeated memory allocations during merging.
QuickSort and Deterministic Select operate in-place, requiring only O(1) additional space beyond recursion stack.
Closest Pair temporarily allocates an O(n) strip array during the combine phase but releases it immediately after use.
Mathematical Complexity Analysis
MergeSort
Recurrence: T(n) = 2T(n/2) + Θ(n)
Method: Master Theorem (Case 2: f(n) = Θ(n<sup>log<sub>b</sub>a</sup>))
Result: T(n) = Θ(n log n)
QuickSort (Expected Case)
Recurrence: T(n) = T(k) + T(n−k−1) + Θ(n), where E[k] ≈ n/2
Method: Probabilistic analysis shows balanced splits on average
Result: E[T(n)] = Θ(n log n)
Deterministic Select
Recurrence: T(n) ≤ T(n/5) + T(7n/10) + Θ(n)
Method: Akra–Bazzi framework confirms linear behavior since the sum of recursive coefficients is < 1
Result: T(n) = Θ(n) — worst-case linear time
Closest Pair of Points
Recurrence: T(n) = 2T(n/2) + Θ(n)
Method: Master Theorem (Case 2)
Result: T(n) = Θ(n log n)
Empirical Evaluation
Compilation & Execution
bash


# Compile core modules
javac cli/ExperimentRunner.java algorithms/*.java metrics/*.java

# Run benchmark suite
java cli.ExperimentRunner 10000 output/benchmark.csv
Observed Behavior
MergeSort: Predictable Θ(n log n) runtime; slightly higher overhead due to buffer copying.
QuickSort: Faster in practice due to better cache performance and fewer memory writes.
Deterministic Select: Confirmed linear scaling, though with a high constant factor (~3–5× slower than randomized select).
Closest Pair: Matches Θ(n log n) trend, but strip processing adds noticeable overhead for small n.
Practical Performance Considerations
Cache Locality: QuickSort’s sequential access pattern outperforms MergeSort on modern CPUs.
Garbage Collection: MergeSort’s single buffer minimizes GC pressure compared to repeated allocations.
Branch Misprediction: Randomized QuickSort suffers less from unpredictable branching than naive variants.
Benchmarking with JMH
Execution Commands
bash

# Build benchmarks
mvn clean package

# Run all
java -jar target/benchmarks.jar

# Run specific test
mvn exec:java -Dexec.mainClass="org.openjdk.jmh.Main" -Dexec.args="SelectVsSortBenchmark"

# Custom parameters (2 forks, 3 warmup, 5 iterations)
mvn exec:java -Dexec.mainClass="org.openjdk.jmh.Main" -Dexec.args=".* -f 2 -wi 3 -i 5"
Full Workflow Validation
To reproduce the entire experimental pipeline:

bash

# 1. Clean and compile
mvn clean compile

# 2. Generate performance data
mvn exec:java "-Dexec.mainClass=cli.ExperimentRunner" "-Dexec.args=1000 results/data.csv"

# 3. Produce visualizations
mvn exec:java "-Dexec.mainClass=plot.PlotGenerator" "-Dexec.args=results/data.csv"
