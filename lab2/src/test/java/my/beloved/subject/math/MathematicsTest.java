package my.beloved.subject.math;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

// TODO: Test UberFunc

@TestInstance(Lifecycle.PER_CLASS)
public class MathematicsTest {
    private final double precision = 0.001;

    private final Map<Double, Double> sinTable = createSinTable();
    private final Map<Double, Double> lnTable = createLnTable();
    private final Mathematics realMath = new Mathematics(this.precision);
    private final Mathematics math = mockMath(this.realMath);

    public Mathematics mockMath(Mathematics realMath) {
        Mathematics math = Mockito.spy(realMath);

        when(math.sin(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (var entry : this.sinTable.entrySet()) {
                var tableX = entry.getKey();
                if (doubleEquals(tableX, x, precision)) {
                    return entry.getValue();
                }
            }
            System.out.println("SIN " + x + ", " + this.realMath.sin(x));
            return Double.NaN;
            // throw new RuntimeException("failed to find a value for sin in the stub table for x=" + x);
        });

        when(math.ln(Mockito.anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (var entry : this.lnTable.entrySet()) {
                var tableX = entry.getKey();
                if (doubleEquals(tableX, x, precision)) {
                    return entry.getValue();
                }
            }
            // throw new RuntimeException("failed to find a value for ln in the stub table for x=" + x);
            System.out.println("LN " + x + ", " + this.realMath.ln(x));
            return Double.NaN;
        });

        return math;
    }

    @Nested
    public class UberFuncTest {
        @ParameterizedTest
        @CsvSource({
            "0.2, 0",
            "0.1, 0",
            "0.4, 0",
            "0.5, 0",
            "0.6, 0",
            "5, 0",
            "7.597, 0",
            "12, 0",
            "100, 0",
            "-0.1, 0",
            "-1, 0",
            "-1.578, 0",
            "-2.2, 0",
            "-2.35, 0",
            "-2.4823, 0",
            "-2.63362, 0",
            "-3.1, 0",
            "-3.1415926, 0",
            "-11, 0",
            "-11.56248, 0",
            "-11.83989, 0",
        })
        public void testPoints(double x, double expected) {
            double actual = UberFunc.compute(math, x);
            System.out.println(x + " -> " + actual);
        }

        @Test
        public void testNaN() {
            double actual = UberFunc.compute(math, 0.0);
            Assertions.assertTrue(Double.isNaN(actual));
        }
    }

    @Nested
    public class Sin {
        @Test
        public void testPoints() {
            for (var entry : sinTable.entrySet()) {
                double x = entry.getKey();
                double expected = entry.getValue();
                double actual = realMath.sin(x);
                Assertions.assertEquals(expected, actual, precision);
            }
        }
    }

    @Nested
    public class Cos {
        @ParameterizedTest
        @CsvSource({
            "0.0,                1",
            "3.14159265359,      -1",
            "2.09439510239,      -0.5",
            "1.5707963267948966, 0",
            "1.0471975511965976, 0.5",
        })
        public void testPoints(double x, double expected) {
            for (double sign : List.of(-1, 1)) {
                double actual = math.cos(sign * x);
                Assertions.assertEquals(expected, actual, precision);
            }
        }
    }

    @Nested
    public class Tan {
        @ParameterizedTest
        @CsvSource({
            "0.0,               0.0",
            "3.141592653589793, 0",
            "0.785398163397,    1",
            "-0.785398163397,   -1",
            "1.0471975512, 1.73205080757",
        })
        public void testPoints(double x, double expected) {
            double actual = math.tan(x);
            Assertions.assertEquals(expected, actual, precision);
        }

        public void testInfinity() {
            double actual = math.tan(Math.PI / 2);
            Assertions.assertTrue(Double.isInfinite(actual));
        }
    }

    @Nested
    public class Cot {
        @ParameterizedTest
        @CsvSource({
            "0.785398163397,    1",
            "-0.785398163397,   -1",
            "1.0471975512, 0.57735026919",
        })
        public void testPoints(double x, double expected) {
            double actual = math.cot(x);
            Assertions.assertEquals(expected, actual, precision);
        }

        public void testInfinity() {
            double actual = math.cot(0);
            Assertions.assertTrue(Double.isInfinite(actual));
        }
    }

    @Nested
    public class Csc {
        @ParameterizedTest
        @CsvSource({
            "1.5707963267948966,   1",
            "4.71238898038,       -1",
            "-1.5707963267948966, -1",
            "-4.71238898038,       1",
            "0.785398163397, 1.41421356237",
            "-0.785398163397, -1.41421356237",
            "2.35619449019, 1.41421356237",
            "-2.35619449019, -1.41421356237",
        })
        public void testPoints(double x, double expected) {
            double actual = math.csc(x);
            Assertions.assertEquals(expected, actual, precision);
        }

        public void testInfinity() {
            double actual = math.csc(Math.PI);
            Assertions.assertTrue(Double.isInfinite(actual));
        }
    }

    @Nested
    public class Ln {
        @Test
        public void testPoints() {
            for (var entry : lnTable.entrySet()) {
                double x = entry.getKey();
                double expected = entry.getValue();
                double actual = realMath.ln(x);
                Assertions.assertEquals(expected, actual, precision);
            }
        }
    }

    @Nested
    public class Log {
        @ParameterizedTest
        @CsvSource({
            "2, 1, 0",
            "2, 0.5, -1",
            "3, 3, 1",
            "3, 9, 2",
            "3, 4, 1.26186",
            "100, 2, 0.15051",
        })
        public void testPoints(int base, double x, double expected) {
            double actual = math.log(x, base);
            Assertions.assertEquals(expected, actual, precision);
        }

        @ParameterizedTest
        @CsvSource({
            "-1, 1",
            "0, 1",
            "1, 1",
            "2, -1",
        })
        public void testNaN(int base, double x) {
            double actual = math.log(x, base);
            Assertions.assertTrue(Double.isNaN(actual));
        }
    }

    private static Map<Double, Double> createSinTable() {
        var map = new HashMap<Double, Double>();
        map.put(0.0, 0.0);
        map.put(0.52359, 0.5);
        map.put(-0.52359, -0.5);
        map.put(0.7854, 0.70710678293655);
        map.put(-0.7854, -0.70710678293655);
        map.put(1.0472, 0.8660254451014825);
        map.put(-1.5708, -1.0);
        map.put(1.5708, 1.0);
        map.put(2.3562, 0.7071070681714239);
        map.put(-2.3562, -0.7071070681714239);
        map.put(2.61799, 0.5);
        map.put(3.66519, -0.5);
        map.put(-3.14159, 0.0);
        map.put(3.14159, 0.0);
        map.put(4.712389, -1.0);
        map.put(-4.712389, 1.0);
        return map;
    }

    private static Map<Double, Double> createLnTable() {
        var map = new HashMap<Double, Double>();
        map.put(1.0, 0.0);
        map.put(2.0, 0.6934);
        map.put(0.5, -0.6928);
        map.put(4.0, 1.3868);
        map.put(9.0, 2.19797);
        map.put(3.0, 1.09893);
        map.put(100.0, 4.60656);
        return map;
    }

    private static boolean doubleEquals(double a, double b, double eps) {
        return Math.abs(a - b) < eps;
    }
}