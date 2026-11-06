package dev.pado.algorithmlab;

import java.util.Comparator;
import java.util.Objects;

/** Gap-halving Shell sort using gapped insertion. Not stable. */
public final class ShellSort<T> implements SortAlgorithm<T> {
    @Override public String name() { return "shell"; }
    @Override public void sort(T[] values, Comparator<? super T> comparator) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(comparator);
        for (int gap = values.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < values.length; i++) {
                T value = values[i];
                int j = i;
                while (j >= gap && comparator.compare(values[j - gap], value) > 0) {
                    values[j] = values[j - gap];
                    j -= gap;
                }
                values[j] = value;
            }
        }
    }
}
