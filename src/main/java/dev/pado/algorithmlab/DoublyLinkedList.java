package dev.pado.algorithmlab;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/** Mutable, non-thread-safe list. Nodes are private so callers cannot corrupt links. */
public final class DoublyLinkedList<T> implements Iterable<T> {
    private static final class Node<E> {
        final E value;
        Node<E> previous;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int version;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public void addFirst(T value) { insert(0, value); }
    public void addLast(T value) { insert(size, value); }
    public T get(int index) { return nodeAt(index).value; }

    /** Insertion accepts 0..size; lookup/removal accept 0..size-1. */
    public void insert(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException(index);
        Node<T> next = index == size ? null : nodeAt(index);
        Node<T> previous = next == null ? tail : next.previous;
        Node<T> node = new Node<>(value);
        node.previous = previous;
        node.next = next;
        if (previous == null) head = node; else previous.next = node;
        if (next == null) tail = node; else next.previous = node;
        size++;
        version++;
    }

    public T removeFirst() {
        if (head == null) throw new NoSuchElementException("List is empty");
        return unlink(head);
    }

    public T removeLast() {
        if (tail == null) throw new NoSuchElementException("List is empty");
        return unlink(tail);
    }

    public T removeAt(int index) { return unlink(nodeAt(index)); }

    public boolean remove(T value) {
        for (Node<T> node = head; node != null; node = node.next) {
            if (Objects.equals(value, node.value)) { unlink(node); return true; }
        }
        return false;
    }

    public void clear() {
        if (size == 0) return;
        for (Node<T> node = head; node != null;) {
            Node<T> next = node.next;
            node.previous = null;
            node.next = null;
            node = next;
        }
        head = tail = null;
        size = 0;
        version++;
    }

    private T unlink(Node<T> node) {
        if (node.previous == null) head = node.next; else node.previous.next = node.next;
        if (node.next == null) tail = node.previous; else node.next.previous = node.previous;
        node.next = node.previous = null;
        size--;
        version++;
        return node.value;
    }

    private Node<T> nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(index);
        Node<T> node;
        if (index < size / 2) {
            node = head;
            for (int i = 0; i < index; i++) node = node.next;
        } else {
            node = tail;
            for (int i = size - 1; i > index; i--) node = node.previous;
        }
        return node;
    }

    public Iterator<T> descendingIterator() { return iterator(true); }
    @Override public Iterator<T> iterator() { return iterator(false); }

    private Iterator<T> iterator(boolean descending) {
        return new Iterator<>() {
            private final int expectedVersion = version;
            private Node<T> current = descending ? tail : head;
            private void checkVersion() {
                if (expectedVersion != version) throw new ConcurrentModificationException();
            }
            @Override public boolean hasNext() { checkVersion(); return current != null; }
            @Override public T next() {
                checkVersion();
                if (current == null) throw new NoSuchElementException();
                T value = current.value;
                current = descending ? current.previous : current.next;
                return value;
            }
        };
    }

    /** O(n) structural diagnostic; bounded traversal also detects cycles. */
    public void assertInvariants() {
        if (size < 0 || (head == null) != (tail == null) || (head == null) != (size == 0))
            throw new IllegalStateException("Invalid endpoints or size");
        int count = 0;
        Node<T> previous = null;
        for (Node<T> node = head; node != null; node = node.next) {
            if (++count > size || node.previous != previous) throw new IllegalStateException("Invalid forward link");
            previous = node;
        }
        if (count != size || previous != tail) throw new IllegalStateException("Forward count mismatch");
        count = 0;
        Node<T> next = null;
        for (Node<T> node = tail; node != null; node = node.previous) {
            if (++count > size || node.next != next) throw new IllegalStateException("Invalid reverse link");
            next = node;
        }
        if (count != size || next != head) throw new IllegalStateException("Reverse count mismatch");
    }
}
