package my.beloved.subject.math;

import java.util.function.Function;

public class UberFunc implements Function<Double, Double> {
    Tan tan;
    Cot cot;
    Csc csc;
    Cos cos;
    Sin sin;
    Ln ln;
    Log5 log5;
    
    public UberFunc(
        Tan tan,
        Cot cot,
        Csc csc,
        Cos cos,
        Sin sin,
        Ln ln,
        Log5 log5
    ) {
        this.tan = tan;
        this.cot = cot;
        this.csc = csc;
        this.cos = cos;
        this.sin = sin;
        this.ln = ln;
        this.log5 = log5;
    }
    
    public Double apply(
        Double x
    ) {
        if (x <= 0) {
            double upper = (tan.apply(x) + cot.apply(x)) * tan.apply(x) - (csc.apply(x) * cos.apply(x) + sin.apply(x));
            double lower = cot.apply(x) + sin.apply(x) / Math.pow(cos.apply(x), 3) + csc.apply(x);
            return upper / tan.apply(x) / lower;
        } else {
            double inner = Math.pow(ln.apply(x) / ln.apply(x) / log5.apply(x), 2);
            return Math.pow(inner + log5.apply(x), 3);
        }
    }   
}
