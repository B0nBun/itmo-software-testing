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
        "-10.0,5.967841977329944",
        "-9.0,-3.98995997",
        "-8.0,0.022073",
        "-7.0,0.974577",
        "-6.0,-1.2008",
        "-5.0,0.07624",
        "-4.0,0.938537",
        "-3.0,-551.13555",
        "-2.0,0.23794123",
        "-1.0,0.4399025",
        "0.0,NaN",
        "1.0,NaN",
        "2.0,197.286126",
        "3.0,22.6367638",
        "4.0,10.7805455",
        "5.0,8.0",
        "6.0,7.07922154",
        "7.0,6.785126",
        "8.0,6.7627815",
        "9.0,6.878065"
    );

    public final static Map<Double, Double> sinTable = mustParseCSV(
        "0,0",
        "-0.429204,-0.416147",
        "0.570796,0.540302",
        "-10,0.544021",
        "-1,-0.841471",
        "-1.429204,-0.989992",
        "1.570796,1",
        "-2,-0.909297",
        "-2.429204,-0.653644",
        "-3,-0.14112",
        "-3.429204,0.283662",
        "-4,0.7568",
        "-4.429204,0.960171",
        "-5,0.959045",
        "-5.429204,0.754585",
        "-6,0.284979",
        "-6.429204,-0.1455",
        "-7,-0.656987",
        "-7.429204,-0.91113",
        "-8,-0.989358",
        "-8.429204,-0.839072",
        "-9,-0.412118",
        "3.570796,-0.416147",
        "9.570796,-0.145502",
        "10.570796,-0.911131",
        "4.570796,-0.989994",
        "2.570796,0.540303",
        "5.570796,-0.654815",
        "6.570796,0.283662",
        "7.570796,0.96017",
        "8.570796,0.753902"
    );

    public final static Map<Double, Double> cosTable = mustParseCSV(
        "-10,-0.83907",
        "-9,-0.91113",
        "-8,-0.14550",
        "-7,0.754585",
        "-6,0.96017",
        "-5,0.28366",
        "-4,-0.653644",
        "-3,-0.98999",
        "-2,-0.416147",
        "-1,0.5403023",
        "0,0.9999999",
        "1,0.5403034",
        "2,-0.4161466",
        "3,-0.98999",
        "4,-0.654815",
        "5,0.283662",
        "6,0.9601703",
        "7,0.7539024",
        "8,-0.145502",
        "9,-0.911131"
    );

    public final static Map<Double, Double> tanTable = mustParseCSV(
        "0,0",
        "-10,-0.648362",
        "-1,-1.557408",
        "-2,2.185038",
        "-3,0.142547",
        "-4,-1.157817",
        "-5,3.380967",
        "-6,0.296801",
        "-7,-0.87066",
        "-8,6.799711",
        "-9,0.452315"
    );

    public final static Map<Double, Double> cotTable = mustParseCSV(
        "0,Infinity",
        "-10,-1.542349",
        "-1,-0.642093",
        "-2,0.457658",
        "-3,7.015235",
        "-4,-0.863695",
        "-5,0.295773",
        "-6,3.369266",
        "-7,-1.148554",
        "-8,0.147065",
        "-9,2.210847"
    );

    public final static Map<Double, Double> cscTable = mustParseCSV(
        "0,Infinity",
        "-10,1.838164",
        "-1,-1.188395",
        "-2,-1.099751",
        "-3,-7.086168",
        "-4,1.321353",
        "-5,1.042704",
        "-6,3.50903",
        "-7,-1.5221",
        "-8,-1.010756",
        "-9,-2.42649"
    );

    public final static Map<Double, Double> lnTable = mustParseCSV(
        "1,0",
        "2,0.693397",
        "3,1.098929",
        "4,1.386794",
        "5,1.60993",
        "6,1.792326",
        "7,1.94628",
        "8,2.080191",
        "9,2.197968"
    );

    public final static Map<Double, Double> log5Table = mustParseCSV(
        "1,0",
        "2,0.4307",
        "3,0.682594",
        "4,0.8614",
        "5,1",
        "6,1.113294",
        "7,1.208922",
        "8,1.2921",
        "9,1.365257"
    );


    public static void runTableTest(Map<Double, Double> table, Function<Double, Double> func, double precision) {
        var errors = new ArrayList<String>();
        for (var entry : table.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();
            double actual = func.apply(x);
            if (!doubleEquals(expected, actual, precision)) {
                errors.add("Expected " + expected + ", but got " + actual + " with precision " + precision);
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
            // TODO: Throw
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
