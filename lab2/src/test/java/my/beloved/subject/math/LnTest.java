package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class LnTest {
    double precision = 0.001;
    Ln ln = new Ln(precision);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.lnTable, this.ln, this.precision);
    }
}
