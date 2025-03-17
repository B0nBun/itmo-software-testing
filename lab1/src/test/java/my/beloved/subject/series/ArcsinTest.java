package my.beloved.subject.series;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Assertions;

public class ArcsinTest {
    @ParameterizedTest
    @CsvSource({
        "0.0,                0.0",
        "1.5707963267948966, 0.12",   // pi / 2
        "1.0471975511965976, 0.001",  // pi / 3
        "0.7853981633974483, 0.0001", // pi / 4
        "0.5235987755982988, 0.0001"  // pi / 6
    })
    public void testPoints(double expectedY, double precision) {
        for (double sign : List.of(1, -1)) {
            double x = Math.sin(sign * expectedY);
            double actual = Arcsin.compute(x);
            Assertions.assertEquals(sign * expectedY, actual, precision);
        }
    }

    private static long random_seed = 1;
    private static int random_iterations = 1000;

    @ParameterizedTest
    @CsvSource({
        "0, 0.95, 0.001",
        "0.95, 0.985, 0.015",
        "0.985, 1, 0.1",
    })
    public void testRandomPointsForPrecision(double lowerBound, double upperBound, double precision) {
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

    @ParameterizedTest
    @ValueSource(doubles={-1.1, 1.1})
    public void outOfBoundsReturnsNaN(double x) {
        Assertions.assertEquals(Double.NaN, Arcsin.compute(x));
    }
}
