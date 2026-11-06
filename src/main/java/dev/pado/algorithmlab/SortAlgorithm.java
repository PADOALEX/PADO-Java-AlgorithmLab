package dev.pado.algorithmlab;

import java.util.Comparator;

public interface SortAlgorithm<T> {
    String name();
    void sort(T[] values, Comparator<? super T> comparator);
}
