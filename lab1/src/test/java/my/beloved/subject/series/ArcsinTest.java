package my.beloved.subject.series;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArcsinTest {
    private static long random_seed = 0;
    private static int random_iterations = 500;

    @Test
    public void testPointsForPrecision() {
        var rand = new Random(ArcsinTest.random_seed);
        for (int i = 0; i < ArcsinTest.random_iterations; i ++) {
            double randomX = rand.nextDouble(0, 1);
            for (double x : List.of(randomX, -randomX)) {
                double expected = Math.asin(x);
                double actual = Arcsin.compute(x);
                double precision = Arcsin.precisionForX(x);
                Assertions.assertEquals(expected, actual, precision);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(doubles={-1.1, 1.1})
    public void outOfBoundsReturnsNaN(double x) {
        Assertions.assertEquals(Double.NaN, Arcsin.compute(x));
    }

    @ParameterizedTest
    @ValueSource(doubles={-1.1, 1.1})
    public void boundaryPointsPrecision(double x) {
        double expected = Math.asin(x);
        double actual = Arcsin.compute(x);
        Assertions.assertEquals(expected, actual, 0.102);
    }
}
