package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class CotTest {
    double precision = 0.001;
    Sin sin = Tables.mockWithTable(new Sin(precision), Tables.sinTable, precision);
    Cos cos = Tables.mockWithTable(new Cos(sin), Tables.cosTable, precision);
    Cot cot = new Cot(sin, cos);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.cotTable, this.cot, this.precision);
    }
}
