package my.beloved.subject.math;

import java.util.function.Function;

public class Sin implements Function<Double, Double> {
    double precision;

    public Sin(Double precision) {
        this.precision = precision;
    }

    public Double apply(Double x) {
        double result = 0;
        double tmp = 0;
        double prev = Double.MAX_VALUE;
        int k = 0;

        final double PI2 = Math.PI * 2;

        while (x > PI2) {
            x -= PI2;
        }
        while (x < -PI2) {
            x += PI2;
        }

        while (Math.abs(tmp - prev) > this.precision) {
            prev = tmp;
            double a = Math.pow(-1, k);
            double b = Math.pow(x, 2 * k + 1);
            double c = factorial(2 * k + 1);

            tmp = a * (b / c);

            double nextres = result + tmp;
            if (!Double.isFinite(nextres)) {
                break;
            } else {
                result = nextres;
            }
            k++;
        }

        return result;
    }

    private long factorial(long n) {
        long res = 1;
        for (long i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }
}
