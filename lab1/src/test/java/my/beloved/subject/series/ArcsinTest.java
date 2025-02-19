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
        "0.0,   0.0",
        "0.5,   0.523",
        "0.25,  0.252",
        "0.75,  0.848",
        "0.125, 0.125",
        "0.625, 0.675",
        "0.875, 1.065",
        "1.0,   1.468"
    })
    public void testPoints(double x, double expectedY) {
        for (double sign : List.of(1, -1)) {
            double actual = Arcsin.compute(sign * x);
            Assertions.assertEquals(sign * expectedY, actual, 0.001);
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
