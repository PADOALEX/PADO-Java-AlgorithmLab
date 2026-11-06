package dev.pado.algorithmlab;

import java.util.Comparator;
import java.util.Objects;

public final class SelectionSort<T> implements SortAlgorithm<T> {
    @Override public String name() { return "selection"; }
    @Override public void sort(T[] values, Comparator<? super T> comparator) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(comparator);
        for (int i = 0; i < values.length - 1; i++) {
            int minimum = i;
            for (int j = i + 1; j < values.length; j++)
                if (comparator.compare(values[j], values[minimum]) < 0) minimum = j;
            if (minimum != i) {
                T tmp = values[i]; values[i] = values[minimum]; values[minimum] = tmp;
            }
        }
    }
}
