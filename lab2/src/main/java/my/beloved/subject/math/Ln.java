package my.beloved.subject.math;

import java.util.function.Function;

public class Ln implements Function<Double, Double> {
    double precision;

    public Ln(double precision) {
        this.precision = precision;
    }

    public Double apply(Double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        if (x <= 2) {
            return lnHelper(x);
        }
        return this.apply(x / 2) + this.apply(2.0);
    }

    private double lnHelper(double x) {
        double result = 0;
        int k = 1;
        double tmp = 0;
        double prev = Double.MAX_VALUE;

        while (Math.abs(tmp - prev) > this.precision) {
            prev = tmp;
            double a = Math.pow(-1, k + 1) / k;
            double b = Math.pow(x - 1, k);

            tmp = a * b;
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
}
