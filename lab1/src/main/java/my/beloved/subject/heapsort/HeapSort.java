package my.beloved.subject.heapsort;

import java.util.Comparator;
import java.util.List;

// Derived from https://gist.github.com/thmain/7dd000a48bf5ace0ab5855b55c69c09e

public class HeapSort {
    public static <T extends Comparable<T>> void sort(List<T> list) {
        HeapSort.sortBy(list, T::compareTo);
    }

    public static <T> void sortBy(List<T> list, Comparator<T> comp) {
        int size = list.size();

        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(list, size, i, comp);
        }

        for (int i = size - 1; i >= 0; i--) {
            T x = list.get(0);
            list.set(0, list.get(i));
            list.set(i, x);
            heapify(list, i, 0, comp);
        }
    }

    private static <T> void heapify(List<T> list, int heapSize, int i, Comparator<T> comp) {
        int largest = i; 
        int leftChildIdx = 2*i + 1; 
        int rightChildIdx = 2*i + 2; 

        if (leftChildIdx < heapSize && comp.compare(list.get(leftChildIdx), list.get(largest)) > 0) {
            largest = leftChildIdx;
        }

        if (rightChildIdx < heapSize && comp.compare(list.get(rightChildIdx), list.get(largest)) > 0) {
            largest = rightChildIdx;
        }

        if (largest != i) {
            T swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);

            heapify(list, heapSize, largest, comp);
        }
    }
}
