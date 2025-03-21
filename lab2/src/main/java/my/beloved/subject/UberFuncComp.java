package my.beloved.subject;

import java.util.Map;
import java.util.function.Function;

import my.beloved.subject.math.Mathematics;
import my.beloved.subject.math.UberFunc;

public class UberFuncComp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("USAGE: ./compute <func> <step>");
            return;
        }
        
        var math = new Mathematics(0.001);
        var funcTable = getFuncTable(math);
        Function<Double, Double> func = funcTable.get(args[0]);
        if (func == null) {
            System.out.println("For <func> expected one of: " + String.join(", ", funcTable.keySet()));
            System.out.println("Got " + args[0]);
            return;
        }
        double step = Double.parseDouble(args[1]);
        if (step < 0) {
            System.out.println("step must be positive");
            return;
        }

        double start = -10 * step;
        double end = 10 * step;

        System.out.println("x,y");
        for (double x = start; x < end; x += step) {
            double y = func.apply(x);
            System.out.println(x + "," + y);
        }
    }

    public static Map<String, Function<Double, Double>> getFuncTable(Mathematics math) {
        return Map.of(
            "sin", math::sin,
            "cos", math::cos,
            "tan", math::tan,
            "cot", math::cot,
            "csc", math::csc,
            "ln", math::ln,
            "log5", (x) -> math.log(x, 5),
            "uber", (x) -> UberFunc.compute(math, x)
        );
    }
}
