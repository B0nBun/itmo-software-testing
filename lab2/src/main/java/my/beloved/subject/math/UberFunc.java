package my.beloved.subject.math;

public class UberFunc {
    public static double compute(
        Mathematics math,
        double x
    ) {
        if (x <= 0) {
            double upper = (math.tan(x) + math.cot(x)) * math.tan(x) - (math.csc(x) * math.cos(x) + math.sin(x));
            double lower = math.cot(x) + math.sin(x) / Math.pow(math.cos(x), 3) + math.csc(x);
            return upper / math.tan(x) / lower;
        } else {
            double inner = Math.pow(math.ln(x) / math.ln(x) / math.log(x, 5), 2);
            return Math.pow(inner + math.log(x, 5), 3);
        }
    }   
}
