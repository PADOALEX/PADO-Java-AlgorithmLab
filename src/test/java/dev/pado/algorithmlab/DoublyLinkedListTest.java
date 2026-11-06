package dev.pado.algorithmlab;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {
    private static <T> List<T> forward(DoublyLinkedList<T> list) {
        List<T> result = new ArrayList<>(); list.forEach(result::add); return result;
    }
    @Test void randomizedOperationsMatchArrayListAndPreserveBothLinkDirections() {
        var actual = new DoublyLinkedList<Integer>(); var expected = new ArrayList<Integer>();
        Random random = new Random(99);
        for (int step = 0; step < 5000; step++) {
            if (expected.isEmpty() || random.nextBoolean()) {
                int index = random.nextInt(expected.size()+1), value = random.nextInt(20);
                expected.add(index,value); actual.insert(index,value);
            } else {
                int index = random.nextInt(expected.size());
                assertEquals(expected.remove(index),actual.removeAt(index));
            }
            actual.assertInvariants();assertEquals(expected.size(),actual.size());
            assertEquals(expected,forward(actual));
            List<Integer> backwards=new ArrayList<>();actual.descendingIterator().forEachRemaining(backwards::add);
            assertEquals(expected.reversed(),backwards);
            for (int i=0;i<expected.size();i++) assertEquals(expected.get(i),actual.get(i));
        }
    }
    @Test void endpointsNullsAndClear() {
        var list = new DoublyLinkedList<String>();
        assertTrue(list.isEmpty());list.assertInvariants();
        assertThrows(NoSuchElementException.class,list::removeFirst);
        assertThrows(NoSuchElementException.class,list::removeLast);
        list.addLast("b");list.addFirst("a");list.addLast(null);
        assertTrue(list.remove(null));assertFalse(list.remove("missing"));
        assertEquals("a",list.removeFirst());assertEquals("b",list.removeLast());
        list.assertInvariants();list.addLast("again");list.clear();list.clear();list.assertInvariants();
        assertEquals(0,list.size());
    }
    @Test void rejectsInvalidIndicesWithoutChangingList() {
        var list=new DoublyLinkedList<Integer>();list.addLast(5);
        assertThrows(IndexOutOfBoundsException.class,()->list.insert(-1,0));
        assertThrows(IndexOutOfBoundsException.class,()->list.insert(2,0));
        assertThrows(IndexOutOfBoundsException.class,()->list.get(1));
        assertThrows(IndexOutOfBoundsException.class,()->list.removeAt(-1));
        assertEquals(List.of(5),forward(list));list.assertInvariants();
    }
    @Test void iteratorsDetectMutationAndReportExhaustion() {
        var list=new DoublyLinkedList<Integer>();list.addLast(1);
        var iterator=list.iterator();assertEquals(1,iterator.next());
        assertFalse(iterator.hasNext());assertThrows(NoSuchElementException.class,iterator::next);
        var reverse=list.descendingIterator();list.addFirst(2);
        assertThrows(ConcurrentModificationException.class,iterator::hasNext);
        assertThrows(ConcurrentModificationException.class,reverse::next);
    }
}
