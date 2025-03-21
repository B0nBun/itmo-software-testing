package my.beloved.subject;

import java.util.Map;
import java.util.function.Function;

import my.beloved.subject.math.Cos;
import my.beloved.subject.math.Cot;
import my.beloved.subject.math.Csc;
import my.beloved.subject.math.Ln;
import my.beloved.subject.math.Log5;
import my.beloved.subject.math.Sin;
import my.beloved.subject.math.Tan;
import my.beloved.subject.math.UberFunc;

public class UberFuncComp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("USAGE: ./compute <func> <step>");
            return;
        }
        
        var funcTable = getFuncTable(0.001);
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
        for (double x = start; x <= end; x += step) {
            double y = func.apply(x);
            System.out.println(x + "," + y);
        }
    }

    public static Map<String, Function<Double, Double>> getFuncTable(double precision) {
        var sin = new Sin(precision);
        var cos = new Cos(sin);
        var tan = new Tan(sin, cos);
        var cot = new Cot(sin, cos);
        var csc = new Csc(sin);
        var ln = new Ln(precision);
        var log5 = new Log5(ln);
        var uber = new UberFunc(tan, cot, csc, cos, sin, ln, log5);
        return Map.of(
            "sin", sin,
            "cos", cos,
            "tan", tan,
            "cot", cot,
            "csc", csc,
            "ln", ln,
            "log5", log5,
            "uber", uber
        );
    }
}
