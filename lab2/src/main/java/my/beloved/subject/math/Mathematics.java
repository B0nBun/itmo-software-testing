package my.beloved.subject.math;

public class Mathematics {
    private double precision;

    public Mathematics(double precision) {
        this.precision = precision;
    }


    public double sin(double x) {
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

            result += tmp;
            k++;
        }

        return result;
    }

    public double cos(double x) {
        return this.sin(Math.PI / 2 + x);
    }

    public double tan(double x) {
        return sin(x) / cos(x);
    }

    public double cot(double x) {
        return cos(x) / sin(x);
    }

    public double csc(double x) {
        return 1 / sin(x);
    }

    public double ln(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        if (x <= 2) {
            return lnHelper(x);
        }
        return this.ln(x / 2) + this.ln(2);
    }

    public double log(double x, int base) {
        if (base <= 0 || base == 1) {
            return Double.NaN;
        }
        if (x <= 0) {
            return Double.NaN;
        }
        return this.ln(x) / this.ln(base);
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
            result += tmp;
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
