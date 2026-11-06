package dev.pado.algorithmlab;

import java.util.Comparator;
import java.util.Objects;

public final class MergeSort<T> implements SortAlgorithm<T> {
    @Override public String name() { return "merge"; }
    @Override public void sort(T[] values, Comparator<? super T> comparator) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(comparator);
        mergeRange(values, values.clone(), 0, values.length, comparator);
    }
    private void mergeRange(T[] a, T[] buffer, int left, int right, Comparator<? super T> c) {
        if (right - left < 2) return;
        int middle = left + (right - left) / 2;
        mergeRange(a, buffer, left, middle, c);
        mergeRange(a, buffer, middle, right, c);
        int i = left, j = middle, k = left;
        while (i < middle && j < right)
            buffer[k++] = c.compare(a[i], a[j]) <= 0 ? a[i++] : a[j++];
        while (i < middle) buffer[k++] = a[i++];
        while (j < right) buffer[k++] = a[j++];
        System.arraycopy(buffer, left, a, left, right - left);
    }
}
