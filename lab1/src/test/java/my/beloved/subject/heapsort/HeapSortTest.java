package my.beloved.subject.heapsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class HeapSortTest {
    private static long seed = 0;

    @ParameterizedTest
    @ValueSource(ints={0, 1, 100, 101})
    public void sortingWithDifferentSizes(int len) {
        List<Integer> range = IntStream.range(0, len).boxed().collect(Collectors.toList());
        List<Integer> toSort = new ArrayList<>(range);
        Collections.shuffle(toSort, new Random(seed));
    
        HeapSort.sort(toSort);

        Assertions.assertIterableEquals(range, toSort);
    }
}
