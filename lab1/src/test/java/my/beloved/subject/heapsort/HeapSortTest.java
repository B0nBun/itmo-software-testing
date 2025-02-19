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
        var toSort = new ArrayList<Integer>(range);
        var toSortBy = new ArrayList<Integer>(range);
        
        var rand = new Random(seed);
        Collections.shuffle(toSort, rand);
        Collections.shuffle(toSort, rand);
    
        HeapSort.sort(toSort);
        Assertions.assertIterableEquals(range, toSort);

        HeapSort.sortBy(toSortBy, (a, b) -> b - a);
        Collections.reverse(range);
        Assertions.assertIterableEquals(range, toSortBy);
    }
}
