package Functionality;
import kotlin.Pair;
import model.TaskList;
import model.Availability;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;
import com.j256.ormlite.dao.Dao;

public class scheduleFunctions {

    public Pair<TaskList, Availability> scheduleOne(TaskList curr, Date to_add_date,Date to_add_duedate,
                                                    String to_add_name,Double duration, Availability user, Dao<TaskList, Integer> d) throws SQLException, ParseException {
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        int subtask = 1;
        Iterator<Map.Entry<String, List<Pair<Double, Double>>>> iterator = avaliable.entrySet().iterator();
        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {

            String ent_date_string = ent.getKey();
            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
                List<Double> starttime = new ArrayList<>();
                List<Double> endtime = new ArrayList<>();
                for (Pair<Double, Double> val_pair : ent.getValue()) {
                    starttime.add(val_pair.getFirst());
                    endtime.add(val_pair.getSecond());
                }
                for (int i = 0; i < starttime.size(); i++) {
                    double time_range = endtime.get(i) - starttime.get(i);
                    if (time_range == 0 ) {
                        continue;
                    }
                    if (time_range > duration) {
                        double start = starttime.get(i) + duration;
                        double end = endtime.get(i);
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(start, end);
                        ent.getValue().remove(to_rem);
                        ent.getValue().add(to_add);
                        avaliable.put(ent_date_string, ent.getValue());
                        curr.addTask(to_add_name+" part "+subtask,to_add_duedate,ent_date,duration,d);

                        return new Pair<>(curr, new Availability(avaliable));
                    } else {
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i),endtime.get(i));
                        ent.getValue().remove(to_rem);
                        ent.getValue().add(to_add);
                        curr.addTask(to_add_name+" part "+subtask,
                                to_add_duedate,ent_date,time_range,d);
                        subtask++;
                        duration = duration - time_range;
                        /*to_add_date = ent_date;*/
                        avaliable.put(ent_date_string, ent.getValue());
                    }
                }

            }

        }
        return new Pair<>(curr, new Availability(avaliable));
    }

    public Pair<TaskList, Availability> addBackTask(TaskList curr, Date to_add_date,Date to_add_duedate,
                                                    String to_add_name,Double duration, Availability user, Dao<TaskList, Integer> d) throws SQLException, ParseException {
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {

            String ent_date_string = ent.getKey();
            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
                List<Double> starttime = new ArrayList<>();
                List<Double> endtime = new ArrayList<>();
                for (Pair<Double, Double> val_pair : ent.getValue()) {
                    starttime.add(val_pair.getFirst());
                    endtime.add(val_pair.getSecond());
                }
                Double earliest = 9.0;
                for (int i = 0; i < starttime.size(); i++) {
                    double time_range = starttime.get(i)-earliest;
                    System.out.print(time_range+"\n");
                    System.out.print(duration+"\n");
                    if (time_range >= duration) {
                        double start = starttime.get(i) - duration;
                        double end = endtime.get(i);
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(start, end);
                        ent.getValue().remove(to_rem);
                        ent.getValue().add(to_add);
                        avaliable.put(ent_date_string, ent.getValue());
                        curr.delTask(to_add_name,d);
                        System.out.print(start);
                        System.out.print(end);
                        return new Pair<>(curr, new Availability(avaliable));
                    } else {
                        double start = earliest;
                        double end = endtime.get(i);
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i),endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(start, end);
                        ent.getValue().remove(to_rem);
                        ent.getValue().add(to_add);
                        avaliable.put(ent_date_string,ent.getValue());
                        duration = duration - time_range;
                        earliest = endtime.get(i);
                        /*to_add_date = ent_date;*/

                    }
                }

            }

        }
        return new Pair<>(curr, new Availability(avaliable));
    }
    public Pair<TaskList, Availability> scheduleOneTest(TaskList curr, Date to_add_date,Date to_add_duedate,
                                                        String to_add_name,Double duration, Availability user) throws SQLException, ParseException {
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        int subtask = 1;
        Iterator<Map.Entry<String, List<Pair<Double, Double>>>> iterator = avaliable.entrySet().iterator();
        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {

            String ent_date_string = ent.getKey();
            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
                List<Double> starttime = new ArrayList<>();
                List<Double> endtime = new ArrayList<>();
                for (Pair<Double, Double> val_pair : ent.getValue()) {
                    starttime.add(val_pair.getFirst());
                    endtime.add(val_pair.getSecond());
                }
                for (int i = 0; i < starttime.size(); i++) {
                    double time_range = endtime.get(i) - starttime.get(i);
                    if (time_range > duration) {
                        double start = starttime.get(i) + duration;
                        double end = endtime.get(i);
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(start, end);
                        ent.getValue().remove(to_rem);
                        ent.getValue().add(to_add);
                        avaliable.put(ent_date_string, ent.getValue());
                        curr.addTask(to_add_name + " part " + subtask, to_add_duedate, ent_date, duration);
                        return new Pair<>(curr, new Availability(avaliable));
                    } else {
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        ent.getValue().remove(to_rem);
                        curr.addTask(to_add_name + " part " + subtask,
                                to_add_duedate, ent_date, time_range);
                        subtask++;
                        duration = duration - time_range;
                        /*to_add_date = ent_date;*/

                    }
                }

            }

        }
        return new Pair<>(curr, new Availability(avaliable));
    }
}

