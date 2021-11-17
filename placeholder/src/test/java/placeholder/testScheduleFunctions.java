package placeholder;

import Functionality.scheduleFunctions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import kotlin.Pair;
import model.Availability;
import model.TaskList;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class testScheduleFunctions {
    scheduleFunctions temp = new scheduleFunctions();
    Availability aval = new Availability();
    TaskList list = new TaskList("first list");

    final String URI = "jdbc:sqlite:./JBApp.db";
    ConnectionSource connectionSource;

    @Test
    public void checkTestSchedule() {
        try {
            String listname = "l1";
            String dt = "2021-11-20";  // Start date
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
            String dt2 = "2021-11-22";  // Start date
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(dt2);
            Double duration = 3.0;
            Pair<TaskList, Availability> result = temp.scheduleOneTest(list,date1,date2, listname, duration, aval);
            Availability a = result.getSecond();
            /*for (Map.Entry<String, List<Pair<Double, Double>>> k: a.getThisMap().entrySet()) {
                if (k.getValue().get(0).getFirst().equals(12.0)) {
                    assertEquals(1,1);
                    return;
                }
            }*/
            Pair<Double, Double> curr = a.getThisMap().get("2021-11-20").get(0);
            assertEquals(12.0,curr.component1());
        } catch (SQLException e) {
            assertEquals(11.0,12.0);
            e.printStackTrace();
        } catch (ParseException e) {
            assertEquals(11.0,12.0);
            e.printStackTrace();
        }
    }

    @Test
    public void checkTestSchedule_carryOver() {
        try {
            String listname = "l1";
            String dt = "2021-11-20";  // Start date
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
            String dt2 = "2021-11-22";  // Start date
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(dt2);
            Double duration = 20.0;
            Pair<TaskList, Availability> result = temp.scheduleOneTest(list,date1,date2, listname, duration, aval);
            Availability a = result.getSecond();
            Pair<Double, Double> curr = a.getThisMap().get("2021-11-21").get(0);
            System.out.println(curr.component1());
            assertEquals(17.0,curr.component1());
        } catch (SQLException e) {
            assertEquals(11.0,12.0);
            e.printStackTrace();
        } catch (ParseException e) {
            assertEquals(11.0,12.0);
            e.printStackTrace();
        }
    }

    @Test
    public void checkTestSchedule_complexCase() {
        //this case adds 2 tasks, each with different durations. notice the availability is changed
        //when adding consecutive tasks
        try {
            String listname = "l1";
            String dt = "2021-11-20";  // Start date
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
            String dt2 = "2021-11-22";  // Start date
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(dt2);
            Double duration = 20.0;
            Pair<TaskList, Availability> result = temp.scheduleOneTest(list,date1,date2, listname, duration, aval);
            TaskList l1 = result.component1();
            Availability a1 = result.component2();
            String listname1 = "l2";
            String dt3 = "2021-11-20";  // Start date
            Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(dt3);
            String dt4 = "2021-11-25";  // Start date
            Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(dt4);
            Double duration2 = 6.0;
            Pair<TaskList, Availability> result1 = temp.scheduleOneTest(l1,date3,date4, listname1, duration2, a1);
            TaskList l2 = result1.component1();
            Availability a2 = result1.component2();
            Pair<Double, Double> curr = a2.getThisMap().get("2021-11-22").get(0);
            assertEquals(11.0,curr.component1());
            List<TaskList.Task> lis = l1.getTaskList();

        } catch (SQLException e) {
            assertEquals(11.0,12.0);
            e.printStackTrace();
        } catch (ParseException e) {
            assertEquals(11.0,12.0);
            e.printStackTrace();
        }
    }
}
