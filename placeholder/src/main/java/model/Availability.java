package model;

import kotlin.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Availability {
    private Map<String, List<Pair<Double,Double>>> thisMap;
    public Availability() {
<<<<<<< HEAD
        String dt = "2021-10-10";  // Start date

=======
        String dt = "2021-10-01";  // Start date
>>>>>>> parent of 548813a (Merge remote-tracking branch 'origin/alex2' into dev_Zhenyong1)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, List<Pair<Double, Double>>> new_map = new LinkedHashMap<>();
        for (int i = 0;i<= 100; i++) {
            Calendar cal = Calendar.getInstance();
            c.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date
            Pair<Double, Double> newPair = new Pair<>(9.0, 21.0);
            List<Pair<Double, Double>> newList = new ArrayList();
            newList.add(newPair);
            new_map.put(dt, newList);

        }
        this.thisMap = new_map;
        //c.add(Calendar.DATE, 1);  // number of days to add
        //dt = sdf.format(c.getTime());  // dt is now the new date
    }
    public Availability(Map<String, List<Pair<Double, Double>>> thisMap) {
        this.thisMap = thisMap;
    }
    public Map<String, List<Pair<Double, Double>>> getThisMap() {
        return thisMap;
    }

    public void setThisMap(Map<String, List<Pair<Double, Double>>> thisMap) {
        this.thisMap = thisMap;
    }





}
