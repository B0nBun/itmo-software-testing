package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class CscTest {
    double precision = 0.001;
    Sin sin = Tables.mockWithTable(new Sin(precision), Tables.sinTable, precision);
    Csc csc = new Csc(sin);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.cscTable, this.csc, this.precision);
    }
}
