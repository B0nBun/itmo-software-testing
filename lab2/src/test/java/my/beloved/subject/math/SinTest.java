package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class SinTest {
    double precision = 0.001;
    Sin sin = new Sin(precision);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.sinTable, this.sin, this.precision);
    }
}
