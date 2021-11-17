package placeholder;

import kotlin.Pair;
import model.Availability;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class testAvailability {
    Availability aval = new Availability();
    @Test
    public void checkDates(){

        String dt = "2021-11-18";
        Pair<Double,Double> thispair = aval.getThisMap().get(dt).get(0);
        assertEquals(thispair.component1(),9.0);
        assertEquals(thispair.component2(),21.0);

        String dt2 = "2021-12-01";
        Pair<Double,Double> thispair2 = aval.getThisMap().get(dt2).get(0);
        assertEquals(thispair2.component1(),9.0);
        assertEquals(thispair2.component2(),21.0);

        String dt3 = "2022-01-05";
        Pair<Double,Double> thispair3 = aval.getThisMap().get(dt3).get(0);
        assertEquals(thispair3.component1(),9.0);
        assertEquals(thispair3.component2(),21.0);

    }
}
