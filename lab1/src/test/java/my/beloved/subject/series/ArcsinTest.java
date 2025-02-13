package my.beloved.subject.series;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Assertions;

public class ArcsinTest {
    private static long random_seed = 0;
    private static int random_iterations = 100;

    @ParameterizedTest
    @CsvSource({
        "0, 0.95, 0.001",
        "0.95, 0.985, 0.01",
        "0.985, 1, 0.1",
    })
    public void testPointsForPrecision(double lowerBound, double upperBound, double precision) {
        // TODO: Express precision as a function
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

    @ParameterizedTest
    @ValueSource(doubles={-1, 1})
    public void boundaryPointsPrecision(double x) {
        double expected = Math.asin(x);
        double actual = Arcsin.compute(x);
        Assertions.assertEquals(expected, actual, 0.102);
    }
}
