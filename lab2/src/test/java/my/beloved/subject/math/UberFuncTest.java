package my.beloved.subject.math;

import org.junit.jupiter.api.Test;

public class UberFuncTest {
    double precision = 0.001;
    Sin sin = Tables.mockWithTable(new Sin(precision), Tables.sinTable, precision);
    Cos cos = Tables.mockWithTable(new Cos(sin), Tables.cosTable, precision);
    Tan tan = Tables.mockWithTable(new Tan(sin, cos), Tables.tanTable, precision);
    Cot cot = Tables.mockWithTable(new Cot(sin, cos), Tables.cotTable, precision);
    Csc csc = Tables.mockWithTable(new Csc(sin), Tables.cscTable, precision);
    Ln ln = Tables.mockWithTable(new Ln(precision), Tables.lnTable, precision);
    Log5 log5 = Tables.mockWithTable(new Log5(ln), Tables.log5Table, precision);
    UberFunc uber = new UberFunc(tan, cot, csc, cos, sin, ln, log5);

    @Test
    public void tableTest() {
        Tables.runTableTest(Tables.uberTable, uber, precision);
    }
}
