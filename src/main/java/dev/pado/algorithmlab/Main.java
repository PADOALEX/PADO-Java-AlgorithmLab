package dev.pado.algorithmlab;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class Main {
    private Main() { }
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("--benchmark")) {
            benchmark(Path.of(args.length > 1 ? args[1] : "results/benchmark.csv"));
            return;
        }
        if (args.length > 0) throw new IllegalArgumentException("Usage: java -jar ... [--benchmark [output.csv]]");
        var list = new DoublyLinkedList<String>();
        list.addLast("React"); list.addFirst("Java"); list.insert(1, "HTTP");
        list.assertInvariants();
        System.out.print("List: "); list.forEach(x -> System.out.print(x + " "));
        Integer[] values = {8, -2, 5, 5, 0, 1};
        new MergeSort<Integer>().sort(values, Comparator.naturalOrder());
        System.out.println("\nMerge sort: " + Arrays.toString(values));
    }

    /** Educational timings, not JMH or a cross-language performance comparison. */
    static void benchmark(Path output) throws IOException {
        Path parent = output.toAbsolutePath().getParent();
        Files.createDirectories(parent);
        List<SortAlgorithm<Integer>> algorithms = List.of(new SelectionSort<>(), new ShellSort<>(), new MergeSort<>());
        try (var writer = new PrintWriter(Files.newBufferedWriter(output, StandardCharsets.UTF_8))) {
            writer.println("algorithm,size,order,seed,warmups,samples,mean_ns,min_ns,java_version,os");
            for (var algorithm : algorithms) for (int size : new int[]{100, 1000, 10000})
                for (String order : List.of("random", "sorted", "reverse")) {
                    Integer[] input = dataset(size, order);
                    for (int i = 0; i < 5; i++) algorithm.sort(input.clone(), Comparator.naturalOrder());
                    long total = 0, minimum = Long.MAX_VALUE;
                    for (int i = 0; i < 5; i++) {
                        Integer[] copy = input.clone(); // Excluded from timed interval.
                        long start = System.nanoTime();
                        algorithm.sort(copy, Comparator.naturalOrder());
                        long elapsed = System.nanoTime() - start;
                        for (int j = 1; j < copy.length; j++)
                            if (copy[j - 1] > copy[j]) throw new IllegalStateException("Incorrect sort");
                        total += elapsed; minimum = Math.min(minimum, elapsed);
                    }
                    writer.printf(Locale.ROOT, "%s,%d,%s,20261106,5,5,%.1f,%d,%s,%s%n", algorithm.name(), size, order,
                        total / 5.0, minimum, System.getProperty("java.version"), System.getProperty("os.name"));
                }
        }
        System.out.println("Wrote 27 measured configurations to " + output);
    }

    static Integer[] dataset(int size, String order) {
        Integer[] a = new Integer[size];
        Random random = new Random(20261106L + size);
        for (int i = 0; i < size; i++) a[i] = switch (order) {
            case "sorted" -> i;
            case "reverse" -> size - i;
            default -> random.nextInt(size * 2 + 1) - size;
        };
        return a;
    }
}
