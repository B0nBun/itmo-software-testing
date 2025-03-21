package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class TanTest {
    double precision = 0.001;
    Sin sin = Tables.mockWithTable(new Sin(precision), Tables.sinTable, precision);
    Cos cos = Tables.mockWithTable(new Cos(sin), Tables.cosTable, precision);
    Tan tan = new Tan(sin, cos);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.tanTable, this.tan, this.precision);
    }
}
