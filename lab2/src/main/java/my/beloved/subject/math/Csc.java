package my.beloved.subject.math;

import java.util.function.Function;

public class Csc implements Function<Double, Double> {
    Sin sin;

    public Csc(Sin sin) {
        this.sin = sin;
    }

    public Double apply(Double x) {
        return 1 / this.sin.apply(x);
    }
}
