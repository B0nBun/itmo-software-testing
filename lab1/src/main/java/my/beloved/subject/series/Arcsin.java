package my.beloved.subject.series;

public class Arcsin {
    private final static int terms = 300;
    public static double compute(double x) {
        if (x < -1 || x > 1) {
            return Double.NaN;
        }
        double arcsin = 0;
        for (int n = 0; n < terms; n ++) {
            double term = 1 / Math.pow(2, 2 * n) * nCr(2 * n, n) * Math.pow(x, 2 * n + 1) / (2 * n + 1);
            arcsin += term;
        }
        return arcsin;
    }

    private static long nCr(int n, int r) {
        if(r > n - r) {
            r = n - r;
        }
        long ans = 1;
        for(int i = 1; i <= r; i++) {
            ans *= n - r + i;
            ans /= i;
        }
        return ans;
    }
}
