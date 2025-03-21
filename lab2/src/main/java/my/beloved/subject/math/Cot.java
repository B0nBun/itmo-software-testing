package my.beloved.subject.math;

import java.util.function.Function;

public class Cot implements Function<Double, Double> {
    private Cos cos;
    private Sin sin;

    public Cot(Sin sin, Cos cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public Double apply(Double x) {
        return this.cos.apply(x) / this.sin.apply(x);
    }
}
