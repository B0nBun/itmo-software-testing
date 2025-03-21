package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class Log5Test {
    double precision = 0.001;
    Ln ln = Tables.mockWithTable(new Ln(precision), Tables.lnTable, precision);
    Log5 log5 = new Log5(ln);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.log5Table, this.log5, this.precision);
    }
}
