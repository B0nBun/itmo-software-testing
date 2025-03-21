package my.beloved.subject.math;

import java.util.function.Function;

public class Log5 implements Function<Double, Double> {
    Ln ln; 

    public Log5(Ln ln) {
        this.ln = ln;
    }

    public Double apply(Double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        return this.ln.apply(x) / this.ln.apply(Double.valueOf(5));
    }
}
