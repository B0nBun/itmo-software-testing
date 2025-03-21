package my.beloved.subject.math;

import java.util.function.Function;

public class Cos implements Function<Double, Double> {
    Sin sin;
    
    public Cos(Sin sin) {
        this.sin = sin;
    }
    
    public Double apply(Double x) {
        return this.sin.apply(Math.PI / 2 + x);
    }
}
