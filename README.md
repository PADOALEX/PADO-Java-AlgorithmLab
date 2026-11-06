# PADO Java AlgorithmLab

A small Java learning project: a generic doubly linked list, three manually implemented sorting algorithms, correctness tests and reproducible timing experiments. It develops ideas from [PADO AlgorithmLab in C#](https://github.com/PADOALEX/PADO-AlgorithmLab) in a separate implementation. No web framework or database is needed.

## Requirements and commands

Use JDK 21 and Apache Maven 3.9 or later. Tested with Eclipse Temurin 21.0.13 and Maven 3.10.0 on Windows. Java 21 is an LTS baseline compatible with the accompanying Spring project; no preview features are used. Set `JAVA_HOME` to the JDK and put Maven's `bin` directory on PATH. A wrapper is intentionally omitted; `java -version` and `mvn -version` should report these prerequisites before building.

```sh
mvn --batch-mode --no-transfer-progress verify
java -jar target/java-algorithmlab-1.0.0.jar
java -jar target/java-algorithmlab-1.0.0.jar --benchmark results/benchmark.csv
```

## Structure

- `DoublyLinkedList<T>`: private owned nodes; indexed insertion/removal; endpoint operations; forward/reverse iterators; explicit invariant diagnostics. Null values are allowed. Iterators detect structural modifications. The list is not thread-safe.
- `SortAlgorithm<T>` and three implementations: selection, gap-halving Shell and stable top-down merge sort. Comparators control ordering. A comparator must support the values supplied; natural order does not accept null elements.
- `Main`: a concise CLI demo and a CSV timing experiment.
- `src/test`: seeded comparisons against Java's reference sort, merge stability, invalid inputs and 5,000 model-based list mutations with checks in both traversal directions.

No `Arrays.sort` or `Collections.sort` implements the production algorithms. Reference sorting is confined to tests; `Arrays.toString` is only display formatting.

## Complexity

| Operation | Time | Extra space | Stable |
| --- | --- | --- | --- |
| Selection sort | O(n²) in all cases | O(1) | No |
| Shell sort, halved gaps | Gap/data dependent; O(n²) worst-case bound | O(1) | No |
| Merge sort | O(n log n) | O(n) buffer + O(log n) stack | Yes |
| Add/remove at either end | O(1) | O(1) per new node | N/A |
| Indexed lookup/insert/remove | O(n), O(1) at endpoints | O(1) plus inserted node | N/A |
| Traversal/invariant check | O(n) | O(1) | N/A |

## Experiments and verification

The experiment runs 3 algorithms × 3 sizes (100, 1,000, 10,000) × 3 orders (random, sorted, reverse). Each configuration uses five warm-ups and five measured samples. Input generation is seeded; cloning and correctness checks are outside the timed interval. CSV records Java/OS and mean/minimum nanoseconds. Merge's internal buffer allocation is part of its timed work. Boxed `Integer` values and comparator calls affect these measurements.

This is an educational `System.nanoTime` experiment, not JMH. It lacks fork isolation, GC profiling and confidence intervals. JVM compilation, caches, CPU scheduling and background work affect timings. Do not compare it directly with the C# BenchmarkDotNet table or infer a language speed advantage.

[Actual 27-row run](docs/BENCHMARK_RESULTS_2026-11-06.csv) and [verification](docs/VERIFICATION.md) distinguish local execution from prepared CI. GitHub Actions is configured but has not run for this unpublished repository.

## Learning and provenance

[Java vs C#](docs/JAVA_VS_CSHARP.md) · [Explain the code](docs/EXPLAIN_THE_CODE.md) · [Dependencies](docs/DEPENDENCIES_AND_LICENSES.md)

This Java implementation was prepared with Codex assistance as a learning deliverable. It is not evidence that the owner independently wrote or already understands every line. Review, modify and explain the code before claiming it in a CV. Standard algorithm ideas come from the C# predecessor; Shell uses gapped insertion rather than reproducing its swap loop. This repository does not change the C# source. Source is MIT-licensed; dependency notices apply separately.
