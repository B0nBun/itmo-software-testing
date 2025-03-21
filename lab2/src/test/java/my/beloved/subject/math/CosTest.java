package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class CosTest {
    double precision = 0.001;
    Sin sin = Tables.mockWithTable(new Sin(precision), Tables.sinTable, precision);
    Cos cos = new Cos(sin);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.cosTable, this.cos, this.precision);
    }
}
