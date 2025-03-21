package my.beloved.subject.math;

import static org.mockito.ArgumentMatchers.anyDouble;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import com.opencsv.CSVReader;

public class Tables {
    public final static Map<Double, Double> uberTable = mustParseCSV(
        "0,NaN",
        "0.1,-0.74238",
        "0.2,0",
        "0.3,1.151",
        "0.4,16.1529",
        "1,NaN",
        "2,197.2861",
        "7.59709,6.75",
        "7,6.7851",
        "12,7.5696",
        "14,8.13996",
        "-0.5,1.4279",
        "-1,0.4399",
        "-1.575,0",
        "-2,0.2379",
        "-2.35,1.0581",
        "-2.482367,1.325555",
        "-2.633623,0",
        "-3.141592653589793,9.9999338555599552E17",
        "-4,0.9385",
        "-4.73,0",
        "-5.27929,0.17405",
        "-5.5567,0",
        "-6,-1.2008"
    );

    public final static Map<Double, Double> sinTable = mustParseCSV(
        "0,0",
        "-0.004204,-0.004204",
        "-0.429204,-0.416147",
        "-0.5,-0.479426",
        "0.570796,0.540302",
        "-0.779204,-0.702713",
        "-0.911571,-0.790467",
        "-1.062827,-0.873734",
        "1.070796,0.877583",
        "-1,-0.841471",
        "-1.570796,-1",
        "1.570796,1",
        "-1.575,-0.999991",
        "-2,-0.909297",
        "-2.35,-0.711474",
        "-2.429204,-0.653644",
        "-2.482367,-0.612506",
        "-2.633623,-0.486404",
        "-3.141593,0.000001",
        "-3.159204,0.017611",
        "-3.708494,0.53702",
        "-3.985904,0.747512",
        "-4,0.7568",
        "-4.429204,0.960171",
        "-4.73,0.999848",
        "-5.27929,0.843948",
        "-5.5567,0.665357",
        "-6,0.284979",
        "3.570796,-0.416147",
        "6.300796,0.01761",
        "4.053163,-0.790467",
        "3.145796,-0.004204",
        "3.920796,-0.702712",
        "4.712389,-1.000003",
        "5.570796,-0.654815",
        "7.127496,0.747514",
        "7.570796,0.96017",
        "4.204419,-0.873734",
        "2.070796,0.877583",
        "2.570796,0.540303",
        "6.850086,0.53702"
    );

    public final static Map<Double, Double> cosTable = mustParseCSV(
        "0,1",
        "-0.5,0.877583",
        "-1,0.540302",
        "-1.575,-0.004204",
        "-2,-0.416147",
        "-2.35,-0.702713",
        "-2.482367,-0.790467",
        "-2.633623,-0.873734",
        "-3.141593,-1",
        "-4,-0.653644",
        "-4.73,0.017611",
        "-5.27929,0.53702",
        "-5.5567,0.747512",
        "-6,0.960171",
        "0.5,0.877583",
        "1,0.540302",
        "1.575,-0.004204",
        "2,-0.416147",
        "2.35,-0.702713",
        "2.482367,-0.790467",
        "2.633623,-0.873734",
        "3.141593,-1",
        "4,-0.654815",
        "4.73,0.017611",
        "5.27929,0.53702",
        "5.5567,0.747512",
        "6,0.960171"
    );

    public final static Map<Double, Double> tanTable = mustParseCSV(
        "0,0",
        "-0.5,-0.546303",
        "-1,-1.557409",
        "-1.575,237.866556",
        "-2,2.185038",
        "-2.35,1.012467",
        "-2.482367,0.774866",
        "-2.633623,0.556696",
        "-3.141593,-0.000001",
        "-4,-1.157817",
        "-4.73,56.774062",
        "-5.27929,1.571539",
        "-5.5567,0.890095",
        "-6,0.2968"
    );

    public final static Map<Double, Double> cotTable = mustParseCSV(
        "0,Infinity",
        "-0.5,-1.830487",
        "-1,-0.642092",
        "-1.575,0.004204",
        "-2,0.457658",
        "-2.35,0.987686",
        "-2.482367,1.290546",
        "-2.633623,1.796313",
        "-3.141593,-1000000",
        "-4,-0.863695",
        "-4.73,0.017614",
        "-5.27929,0.636319",
        "-5.5567,1.123475",
        "-6,3.369269"
    );

    public final static Map<Double, Double> cscTable = mustParseCSV(
        "0,Infinity",
        "-0.5,-2.085828",
        "-1,-1.188395",
        "-1.575,-1.000009",
        "-2,-1.099751",
        "-2.35,-1.405533",
        "-2.482367,-1.632637",
        "-2.633623,-2.055904",
        "-3.141593,1000000",
        "-4,1.321353",
        "-4.73,1.000152",
        "-5.27929,1.184907",
        "-5.5567,1.502953",
        "-6,3.50903"
    );

    public final static Map<Double, Double> lnTable = mustParseCSV(
        "-0.1,NaN",
        "-1,NaN",
        "0.1,-2.26856",
        "0.2,-1.602126",
        "0.3,-1.201811",
        "0.4,-0.914946",
        "1,0",
        "12,2.485723",
        "14,2.639677",
        "2,0.693397",
        "5,1.60993",
        "7,1.94628",
        "7.59709,2.028047"
    );

    public final static Map<Double, Double> log5Table = mustParseCSV(
        "-0.1,NaN",
        "-1,NaN",
        "0.1,-1.409105",
        "0.2,-0.995153",
        "0.3,-0.746499",
        "0.4,-0.568314",
        "1,0",
        "12,1.543994",
        "14,1.639622",
        "2,0.4307",
        "7,1.208922",
        "7.59709,1.259711"
    );


    public static void runTableTest(Map<Double, Double> table, Function<Double, Double> func, double precision) {
        var errors = new ArrayList<String>();
        for (var entry : table.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();
            double actual = func.apply(x);
            if (!doubleEquals(expected, actual, precision)) {
                errors.add("For value " + x + " expected " + expected + ", but got " + actual + " with precision " + precision);
            }
        }
        if (errors.size() != 0) {
            Assertions.fail(String.join("\n", errors));
        }
    }

    public static <T extends Function<Double, Double>> T mockWithTable(
        T func,
        Map<Double, Double> table,
        double precision
    ) {
        T mockFunc = Mockito.spy(func);
        Mockito.when(mockFunc.apply(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            for (var entry : table.entrySet()) {
                double tableX = entry.getKey();
                if (doubleEquals(tableX, x, precision)) {
                    return entry.getValue();
                }
            }
            // throw new RuntimeException("failed ot find entry for " + x + " in mock table for class " + func.getClass().getName());
            var format = new DecimalFormat("#.######");
            System.out.println("#" + func.getClass().getName() + " \"" + format.format(x) + "," + format.format(func.apply(x)) + "\",");
            return Double.NaN;
        });
        return mockFunc;
    } 

    private static Map<Double, Double> mustParseCSV(String ...csvStrings) {
        try {
            Map<Double, Double> table = new HashMap<>();
            var csvString = String.join("\n", Arrays.asList(csvStrings));
            var reader = new CSVReader(new StringReader(String.join("\n", Arrays.asList(csvString))));
            String[] row;
            while ((row = reader.readNext()) != null) {
                double x = Double.valueOf(row[0]);
                double y = Double.valueOf(row[1]);
                table.put(x, y);
            }
            reader.close(); // Just to silence the warning
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean doubleEquals(double a, double b, double eps) {
        if (Double.isInfinite(a) && Double.isInfinite(b) && (a * b) > 0) {
            return true;
        }
        if (Double.isNaN(a) && Double.isNaN(b)) {
            return true;
        }
        return Math.abs(a - b) < eps;
    }
}
