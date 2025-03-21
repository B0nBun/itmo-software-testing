package my.beloved.subject.math;

import java.util.function.Function;

public class Tan implements Function<Double, Double> {
    private Cos cos;
    private Sin sin;

    public Tan(Sin sin, Cos cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public Double apply(Double x) {
        return this.sin.apply(x) / this.cos.apply(x);
    }
}
