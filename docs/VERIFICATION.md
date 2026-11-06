# Verification on 6 November 2026

Environment: Windows, Eclipse Temurin OpenJDK 21.0.13, Maven 3.10.0. Portable tools were scoped to the task workspace; the system Java installation was not changed.

| Command | Actual result |
| --- | --- |
| `mvn --batch-mode --no-transfer-progress verify` | BUILD SUCCESS; 17 tests, 0 failures/errors/skips |
| `java -jar target/java-algorithmlab-1.0.0.jar` | Exit 0; forward list and sorted values printed |
| `java -jar target/java-algorithmlab-1.0.0.jar --benchmark docs/BENCHMARK_RESULTS_2026-11-06.csv` | Exit 0; 27 measured configurations written |

The sort test loops over 128 deterministic array sizes per algorithm. The list model test performs 5,000 mutation steps; these internal checks are not falsely reported as separate JUnit cases. Benchmark CSV contains actual local timings, not estimates. Hosted execution is separate from these local results; inspect the commit-specific runs in [GitHub Actions](https://github.com/PADOALEX/PADO-Java-AlgorithmLab/actions/workflows/ci.yml).
