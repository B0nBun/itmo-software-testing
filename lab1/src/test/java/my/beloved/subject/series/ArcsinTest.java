package my.beloved.subject.series;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ArcsinTest {
    private static long random_seed = 0;
    private static int random_iterations = 100;

    @Test
    public void randomPoints() {
        checkRangeForPrecision(0, 0.95, 0.001);
        checkRangeForPrecision(0.95, 0.985, 0.01);
        checkRangeForPrecision(0.985, 1, 0.1);
    }

    @Test
    public void outOfBounds() {
        Assertions.assertEquals(Double.NaN, Arcsin.compute(-1.1));
        Assertions.assertEquals(Double.NaN, Arcsin.compute(1.1));
    }

    @Test
    public void boundaryPoints() {
        for (double x : List.of(1, -1)) {
            double expected = Math.asin(x);
            double actual = Arcsin.compute(x);
            Assertions.assertEquals(expected, actual, 0.102);
        }
    }

    private void checkRangeForPrecision(double lowerBound, double upperBound, double precision) {
        var rand = new Random(ArcsinTest.random_seed);
        for (int i = 0; i < ArcsinTest.random_iterations; i ++) {
            double randomX = rand.nextDouble(lowerBound, upperBound);
            for (double x : List.of(randomX, -randomX)) {
                double expected = Math.asin(x);
                double actual = Arcsin.compute(x);
                Assertions.assertEquals(expected, actual, precision);
            }
        }
    }
}
