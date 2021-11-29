package Functionality;
import kotlin.Pair;
import model.Availability;
import model.TaskList;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;
import com.j256.ormlite.dao.Dao;
import model.User;

public class scheduleFunctions {

    public Pair<TaskList, Availability> scheduleOne(TaskList curr, Date to_add_date, Date to_add_duedate,
                                                    String to_add_name, Double duration, Double importance, User user, Dao<TaskList, Integer> d) throws SQLException, ParseException {

        TreeMap<Double, List<TaskList.Task>> imp_map = new TreeMap<>();
        for (TaskList.Task t :curr.getTaskList()) {
            Double imp = t.getImportance();
            if (imp_map.containsKey(imp)){
                imp_map.get(imp).add(t);
            } else {
                List<TaskList.Task> task = new ArrayList<>();
                task.add(t);
                imp_map.put(imp,task);
            }
        }
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
                        curr.addTask(to_add_name+" part "+subtask,to_add_duedate,ent_date,duration,importance,
                                starttime.get(i),start, d);
                        duration = 0.0;
                        return new Pair<>(curr, new Availability(avaliable));
                    } else {
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i),endtime.get(i));
                        ent.getValue().remove(to_rem);
                        ent.getValue().add(to_add);
                        curr.addTask(to_add_name+" part "+subtask,
                                to_add_duedate,ent_date,time_range,importance,starttime.get(i),endtime.get(i), d);
                        subtask++;
                        duration = duration - time_range;
                        /*to_add_date = ent_date;*/
                        avaliable.put(ent_date_string, ent.getValue());
                    }
                }

            }

        }

        while (duration>0) {
            Map.Entry<Double, List<TaskList.Task>> first_ent = imp_map.firstEntry();

            for (TaskList.Task allT : first_ent.getValue()) {
                if (allT.getImportance() >= importance ) {
                    return new Pair<>(curr, new Availability(avaliable));
                }
                Double smallDur = allT.getDuration();
                if (smallDur > duration) {
                    Double add_start = allT.getExactStart();
                    Double add_end = allT.getExactStart() + duration ;

                    Double start = allT.getExactStart()+ duration;
                    Double end = allT.getExactEnd() ;
                    curr.delTask(allT.getTaskName(),d);

                    curr.addTask(allT.getTaskName(), allT.getDueDay(),allT.getDate(),end-start
                    ,allT.getImportance(),start,end);

                    curr.addTask(to_add_name+" part "+subtask,
                            to_add_duedate,allT.getDate(),duration,importance,add_start,add_end, d);

                    return new Pair<>(curr, new Availability(avaliable));
                    //Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                } else {
                    Double add_start = allT.getExactStart();
                    Double add_end = allT.getExactEnd();

                    curr.delTask(allT.getTaskName(),d);

                    curr.addTask(to_add_name+" part "+subtask,
                            to_add_duedate,allT.getDate(),smallDur,importance,add_start,add_end, d);
                    duration = duration - smallDur;
                    subtask = subtask + 1;


                }
            }
            imp_map.remove(first_ent.getKey());
        }


        return new Pair<>(curr, new Availability(avaliable));
    }

    public Pair<TaskList, Availability> addBackTask(TaskList curr, Date to_add_date,Date to_add_duedate,
                                                    String to_add_name,Double duration, User user,
                                                    Double exact_start, Double exact_end, Dao<TaskList, Integer> d) throws SQLException, ParseException {
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(to_add_date);
        List<Pair<Double, Double>> map_ent = avaliable.get(strDate);
        map_ent.sort(new Comparator<Pair<Double, Double>>() {
            @Override
            public int compare(Pair<Double, Double> o1, Pair<Double, Double> o2) {
                if (o1.getFirst() < o2.getFirst()) {
                    return -1;
                } else if (o1.getFirst().equals(o2.getFirst())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        for (int i = 0; i <= map_ent.size() - 1; i++) {
            if (exact_start < map_ent.get(i).getFirst()) {
                double start = exact_start;
                double end = map_ent.get(i).getSecond();
                Pair<Double, Double> to_add = new Pair<>(start, end);
                avaliable.remove(strDate);
                map_ent.remove(i);
                map_ent.add(to_add);
                avaliable.put(strDate, map_ent);
                curr.delTask(to_add_name, d);
                return new Pair<>(curr, new Availability(avaliable));
            } else if (i == (map_ent.size() - 1)) {
                double start = map_ent.get(i).getFirst();
                double end = exact_end;
                Pair<Double, Double> to_add = new Pair<>(start, end);
                avaliable.remove(strDate);
                map_ent.remove(i);
                map_ent.add(to_add);
                avaliable.put(strDate, map_ent);
                curr.delTask(to_add_name, d);
                return new Pair<>(curr, new Availability(avaliable));
            }
        }
        return new Pair<>(curr, new Availability(avaliable));
    }


        /*
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
                        System.out.println("start "+start+"end "+end+"\n");
                        System.out.println("start "+starttime.get(i)+"end "+endtime.get(i)+"\n");
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
                        to_add_date = ent_date;

                    }
                }

            }

        }
        return new Pair<>(curr, new Availability(avaliable));*/
    public Pair<TaskList, Availability> scheduleOneTest(TaskList curr, Date to_add_date,Date to_add_duedate,
                                                        String to_add_name,Double duration, Double importance, Availability user) throws SQLException, ParseException {
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
                        curr.addTask(to_add_name + " part " + subtask, to_add_duedate, ent_date, duration, importance
                        ,starttime.get(i),start);
                        return new Pair<>(curr, new Availability(avaliable));
                    } else {
                        avaliable.remove(ent_date);
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        ent.getValue().remove(to_rem);
                        curr.addTask(to_add_name + " part " + subtask,
                                to_add_duedate, ent_date, time_range, importance, starttime.get(i), endtime.get(i));
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

