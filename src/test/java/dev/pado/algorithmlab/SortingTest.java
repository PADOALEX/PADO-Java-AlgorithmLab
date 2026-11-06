package dev.pado.algorithmlab;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class SortingTest {
    static Stream<SortAlgorithm<Integer>> algorithms() {
        return Stream.of(new SelectionSort<>(), new ShellSort<>(), new MergeSort<>());
    }
    @ParameterizedTest @MethodSource("algorithms")
    void matchesReferenceAcrossDeterministicInputs(SortAlgorithm<Integer> algorithm) {
        Random random = new Random(77);
        for (int n = 0; n < 128; n++) {
            Integer[] values = random.ints(n, -12, 13).boxed().toArray(Integer[]::new);
            Integer[] expected = values.clone();
            Arrays.sort(expected);
            algorithm.sort(values, Comparator.naturalOrder());
            assertArrayEquals(expected, values, algorithm.name() + " n=" + n);
        }
    }
    @ParameterizedTest @MethodSource("algorithms")
    void supportsReverseComparatorAndExtremeValues(SortAlgorithm<Integer> algorithm) {
        Integer[] values = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE, 0, -1};
        Integer[] expected = values.clone();
        Arrays.sort(expected, Comparator.reverseOrder());
        algorithm.sort(values, Comparator.reverseOrder());
        assertArrayEquals(expected, values);
    }
    @ParameterizedTest @MethodSource("algorithms")
    void handlesEqualSortedAndReverseData(SortAlgorithm<Integer> algorithm) {
        for (Integer[] values : List.of(new Integer[]{1,1,1}, new Integer[]{1,2,3}, new Integer[]{3,2,1})) {
            Integer[] expected = values.clone(); Arrays.sort(expected);
            algorithm.sort(values, Comparator.naturalOrder());assertArrayEquals(expected, values);
        }
    }
    @ParameterizedTest @MethodSource("algorithms")
    void rejectsMissingArrayAndComparator(SortAlgorithm<Integer> algorithm) {
        assertThrows(NullPointerException.class, () -> algorithm.sort(null, Comparator.naturalOrder()));
        assertThrows(NullPointerException.class, () -> algorithm.sort(new Integer[]{1}, null));
    }
    @Test void mergeSortPreservesOrderForEqualKeys() {
        record Item(int key, String label) { }
        Item[] a = {new Item(2,"a"),new Item(1,"b"),new Item(2,"c"),new Item(1,"d")};
        new MergeSort<Item>().sort(a, Comparator.comparingInt(Item::key));
        assertEquals(List.of("b","d","a","c"),Arrays.stream(a).map(Item::label).toList());
    }
}
