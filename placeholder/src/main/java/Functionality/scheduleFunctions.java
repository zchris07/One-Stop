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
import org.jetbrains.annotations.NotNull;

public class scheduleFunctions {

    /**
     * This function handles the case where a collaborator's availability is changed. No task is
     * added, but a new availability is generated.
     *
     * @param to_add_date start date of task
     * @param to_add_duedate end date of task
     * @param to_add_name name of task
     * @param duration how long does the task take
     * @param importance higher importance task will replace lower importance task
     * @param flexible indicate if the task can be moved or not
     * @param user the User that is currently adding the task
     * @param d Dao
     * @return a pair of availability and task list
     * @throws SQLException when using dao fails
     * @throws ParseException when parse fails
     */

    public Availability scheduleOne_noadd(Date to_add_date, Date to_add_duedate,
                                          String to_add_name, Double duration, Double importance, Boolean flexible, User user, Dao<TaskList, Integer> d) throws SQLException, ParseException {
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        int task_part = 1;
        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {
            //format string into date
            String ent_date_string = ent.getKey();
            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
                //create two list containing the start and end of an available time slot
                List<Double> starttime = new ArrayList<>();
                List<Double> endtime = new ArrayList<>();
                for (Pair<Double, Double> val_pair : ent.getValue()) {
                    starttime.add(val_pair.getFirst());
                    endtime.add(val_pair.getSecond());
                }
                for (int i = 0; i < starttime.size(); i++) {
                    double time_range = endtime.get(i) - starttime.get(i);
                    //when duration becomes 0, we have finished scheduling.
                    if (duration.equals(0.0)) {
                        break;
                    }

                    if (time_range == 0) {
                        continue;
                    }
                    if (time_range >= duration) {
                        //when there is more time than we need, we take the time away from the start of this time slot
                        double start = starttime.get(i) + duration;
                        double end = endtime.get(i);
                        //avaliable.remove(ent_date);
                        return getTaskListAvailabilityPairNoAdd( to_add_duedate, to_add_name, duration, importance, flexible, d, avaliable, task_part, ent, ent_date_string, ent_date, starttime, endtime, i, start, end);
                    } else {
                        //when the time slot doesn't have enough time to fit the entire duration, we split task into parts.
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i), endtime.get(i));
                        editAvailable(ent, to_rem, to_add);
                        task_part++;
                        duration = duration - time_range;
                        avaliable.put(ent_date_string, ent.getValue());
                    }
                }

            }

        }



        return new Availability(avaliable);
    }

    //this generates a map that is sorted based on the importance. Lower importance will be at front of map.
    @NotNull
    private TreeMap<Double, List<TaskList.Task>> getDoubleListTreeMap(TaskList curr) {
        TreeMap<Double, List<TaskList.Task>> imp_map = new TreeMap<>();
        for (TaskList.Task t : curr.getTaskList()) {
            Double imp = t.getImportance();
            if (imp_map.containsKey(imp)) {
                imp_map.get(imp).add(t);
            } else {
                List<TaskList.Task> task = new ArrayList<>();
                task.add(t);
                imp_map.put(imp, task);
            }
        }
        return imp_map;
    }


    //add a task into the task list, change the availability accordingly. The task is completely inserted
    //and there will be no more parts left
    @NotNull
    private Availability getTaskListAvailabilityPairNoAdd(Date to_add_duedate, String to_add_name, Double duration, Double importance, Boolean flexible, Dao<TaskList, Integer> d, Map<String, List<Pair<Double, Double>>> avaliable, int subtask, Map.Entry<String, List<Pair<Double, Double>>> ent, String ent_date_string, Date ent_date, List<Double> starttime, List<Double> endtime, int i, double start, double end) throws SQLException {
        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
        Pair<Double, Double> to_add = new Pair<>(start, end);
        editAvailable(ent, to_rem, to_add);
        avaliable.put(ent_date_string, ent.getValue());
        return new Availability(avaliable);
    }

    //adds and removes some time slot and sort the list of availabilities
    private void editAvailable(Map.Entry<String, List<Pair<Double, Double>>> ent, Pair<Double, Double> to_rem, Pair<Double, Double> to_add) {
        ent.getValue().remove(to_rem);
        ent.getValue().add(to_add);
        ent.getValue().sort((o1, o2) -> {
            if (o1.getFirst() < o2.getFirst()) {
                return -1;
            } else if (o1.getFirst().equals(o2.getFirst())) {
                return 0;
            } else {
                return 1;
            }
        });
    }




    /**
     * this function puts a task in the tasklist, using the inputs as attributes. While inputting this task,
     * availability will change according to how long the task takes. Tasks might be split into multiple parts
     * depending on the duration of it. Importance and flexible helps decide when
     * there is not enough time to put every task in, which task will be replaced.
     *
     * @param curr the task list to edit
     * @param to_add_date start date of task
     * @param to_add_duedate end date of task
     * @param to_add_name name of task
     * @param duration how long does the task take
     * @param importance higher importance task will replace lower importance task
     * @param flexible indicate if the task can be moved or not
     * @param user the User that is currently adding the task
     * @param d Dao
     * @return a pair of availability and task list
     * @throws SQLException when using dao fails
     * @throws ParseException when parse fails
     */

    public Pair<TaskList, Availability> scheduleOne(TaskList curr, Date to_add_date, Date to_add_duedate,
                                                    String to_add_name, Double duration, Double importance, Boolean flexible, User user, Dao<TaskList, Integer> d) throws SQLException, ParseException {
        TreeMap<Double, List<TaskList.Task>> imp_map = getDoubleListTreeMap(curr);
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        int task_part = 1;
        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {
            //format string into date
            String ent_date_string = ent.getKey();
            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
                //create two list containing the start and end of an available time slot
                List<Double> starttime = new ArrayList<>();
                List<Double> endtime = new ArrayList<>();
                for (Pair<Double, Double> val_pair : ent.getValue()) {
                    starttime.add(val_pair.getFirst());
                    endtime.add(val_pair.getSecond());
                }
                for (int i = 0; i < starttime.size(); i++) {
                    double time_range = endtime.get(i) - starttime.get(i);
                    //when duration becomes 0, we have finished scheduling.
                    if (duration.equals(0.0)) {
                        break;
                    }

                    if (time_range == 0) {
                        continue;
                    }
                    if (time_range >= duration) {
                        //when there is more time than we need, we take the time away from the start of this time slot
                        double start = starttime.get(i) + duration;
                        double end = endtime.get(i);
                        //avaliable.remove(ent_date);
                        return getTaskListAvailabilityPair(curr, to_add_duedate, to_add_name, duration, importance, flexible, d, avaliable, task_part, ent, ent_date_string, ent_date, starttime, endtime, i, start, end);
                    } else {
                        //when the time slot doesn't have enough time to fit the entire duration, we split task into parts.
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i), endtime.get(i));
                        editAvailable(ent, to_rem, to_add);
                        curr.addTask(to_add_name + " part " + task_part,
                                to_add_duedate, ent_date, time_range, importance, starttime.get(i), endtime.get(i), flexible, d);
                        task_part++;
                        duration = duration - time_range;
                        avaliable.put(ent_date_string, ent.getValue());
                    }
                }

            }

        }
        //if the task cannot be fit into the available time slots, it check if any lower importance lvl
        //tasks can be replaced.
        while (duration > 0) {
            Map.Entry<Double, List<TaskList.Task>> first_ent = imp_map.firstEntry();

            for (TaskList.Task allT : first_ent.getValue()) {
                if (duration.equals(0.0)) {
                    break;
                }

                if (allT.getImportance() >= importance) {
                    return new Pair<>(curr, new Availability(avaliable));
                }
                if (allT.getFlexible().equals(false)) {
                    continue;
                }
                Double smallDur = allT.getDuration();
                if (smallDur > duration) {
                    return partiallyReplaceAndPushBack(curr, to_add_duedate, to_add_name, duration, importance, flexible, user, d, task_part, allT);
                    //Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                } else {
                    //when the current task cannot fit in the remainder
                    Pair<TaskList, Availability> to_ret = completelyReplaceAndPushBack(curr, to_add_duedate, to_add_name, importance, flexible, user, d, task_part, allT, smallDur);
                    curr = to_ret.getFirst();
                    avaliable = to_ret.getSecond().getThisMap();
                    duration = duration - smallDur;
                    task_part = task_part + 1;


                }
            }
            imp_map.remove(first_ent.getKey());
        }


        return new Pair<>(curr, new Availability(avaliable));
    }


    //when the task we are on have more time then needed to fit in the more important task, part of the task
    //will be taken away and the more important task will be added
    private Pair<TaskList, Availability> partiallyReplaceAndPushBack(TaskList curr, Date to_add_duedate, String to_add_name, Double duration, Double importance, Boolean flexible, User user, Dao<TaskList, Integer> d, int subtask, TaskList.Task allT) throws SQLException, ParseException {
        Double add_start = allT.getExactStart();
        Double add_end = allT.getExactStart() + duration;

        Double start = allT.getExactStart() + duration;
        Double end = allT.getExactEnd();
        curr.delTask(allT.getTaskName(), d);

        curr.addTask(allT.getTaskName(), allT.getDueDay(), allT.getDate(), end - start
                , allT.getImportance(), start, end, flexible, d);
        //check if the replaced task can be pushed back a few days
        String new_name = allT.getTaskName().split("part")[0] + " pushed";

        curr.addTask(to_add_name + " part " + subtask,
                to_add_duedate, allT.getDate(), duration, importance, add_start, add_end, flexible, d);

        return scheduleOne(curr, allT.getDate(), allT.getDueDay(), new_name, duration, allT.getImportance(), allT.getFlexible(), user, d);
    }

    //when the task we are on have more time then needed to fit in the more important task, part of the task
    //will be taken away and the more important task will be added
    private Pair<TaskList, Availability> completelyReplaceAndPushBack(TaskList curr, Date to_add_duedate, String to_add_name, Double importance, Boolean flexible, User user, Dao<TaskList, Integer> d, int subtask, TaskList.Task allT, Double smallDur) throws SQLException, ParseException {
        Double add_start = allT.getExactStart();
        Double add_end = allT.getExactEnd();

        curr.delTask(allT.getTaskName(), d);

        curr.addTask(to_add_name + " part " + subtask,
                to_add_duedate, allT.getDate(), smallDur, importance, add_start, add_end, flexible, d);

        String new_name = allT.getTaskName().split("part")[0] + " pushed";


        Pair<TaskList, Availability> to_ret = scheduleOne(curr, allT.getDate(), allT.getDueDay(), new_name, smallDur, allT.getImportance(), allT.getFlexible(), user, d);
        return to_ret;
    }

    //add a task into the task list, change the availability accordingly. The task is completely inserted
    //and there will be no more parts left
    @NotNull
    private Pair<TaskList, Availability> getTaskListAvailabilityPair(TaskList curr, Date to_add_duedate, String to_add_name, Double duration, Double importance, Boolean flexible, Dao<TaskList, Integer> d, Map<String, List<Pair<Double, Double>>> avaliable, int subtask, Map.Entry<String, List<Pair<Double, Double>>> ent, String ent_date_string, Date ent_date, List<Double> starttime, List<Double> endtime, int i, double start, double end) throws SQLException {
        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
        Pair<Double, Double> to_add = new Pair<>(start, end);
        editAvailable(ent, to_rem, to_add);
        avaliable.put(ent_date_string, ent.getValue());
        curr.addTask(to_add_name + " part " + subtask, to_add_duedate, ent_date, duration, importance,
                starttime.get(i), start, flexible, d);
        return new Pair<>(curr, new Availability(avaliable));
    }


    public Pair<TaskList, Availability> scheduleOneTest(TaskList curr, Date to_add_date, Date to_add_duedate,
                                                        String to_add_name, Double duration, Double importance, Boolean flexible, User user) throws SQLException, ParseException {
        TreeMap<Double, List<TaskList.Task>> imp_map = getDoubleListTreeMap(curr);
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        int task_part = 1;
        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {
            //format string into date
            String ent_date_string = ent.getKey();
            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
                //create two list containing the start and end of an available time slot
                List<Double> starttime = new ArrayList<>();
                List<Double> endtime = new ArrayList<>();
                for (Pair<Double, Double> val_pair : ent.getValue()) {
                    starttime.add(val_pair.getFirst());
                    endtime.add(val_pair.getSecond());
                }
                for (int i = 0; i < starttime.size(); i++) {
                    double time_range = endtime.get(i) - starttime.get(i);
                    //when duration becomes 0, we have finished scheduling.
                    if (duration.equals(0.0)) {
                        break;
                    }

                    if (time_range == 0) {
                        continue;
                    }
                    if (time_range >= duration) {
                        //when there is more time than we need, we take the time away from the start of this time slot
                        double start = starttime.get(i) + duration;
                        double end = endtime.get(i);
                        //avaliable.remove(ent_date);
                        return getTaskListAvailabilityPairTest(curr, to_add_duedate, to_add_name, duration, importance, flexible, avaliable, task_part, ent, ent_date_string, ent_date, starttime, endtime, i, start, end);
                    } else {
                        //when the time slot doesn't have enough time to fit the entire duration, we split task into parts.
                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i), endtime.get(i));
                        editAvailable(ent, to_rem, to_add);
                        curr.addTask(to_add_name + " part " + task_part,
                                to_add_duedate, ent_date, time_range, importance, starttime.get(i), endtime.get(i), flexible);
                        task_part++;
                        duration = duration - time_range;
                        avaliable.put(ent_date_string, ent.getValue());
                    }
                }

            }

        }
        //if the task cannot be fit into the available time slots, it check if any lower importance lvl
        //tasks can be replaced.
        while (duration > 0) {
            Map.Entry<Double, List<TaskList.Task>> first_ent = imp_map.firstEntry();

            for (TaskList.Task allT : first_ent.getValue()) {
                if (duration.equals(0.0)) {
                    break;
                }

                if (allT.getImportance() >= importance) {
                    return new Pair<>(curr, new Availability(avaliable));
                }
                if (allT.getFlexible().equals(false)) {
                    continue;
                }
                Double smallDur = allT.getDuration();
                if (smallDur > duration) {
                    return partiallyReplaceAndPushBackTest(curr, to_add_duedate, to_add_name, duration, importance, flexible, user, task_part, allT);
                    //Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
                } else {
                    //when the current task cannot fit in the remainder
                    Pair<TaskList, Availability> to_ret = completelyReplaceAndPushBackTest(curr, to_add_duedate, to_add_name, importance, flexible, user, task_part, allT, smallDur);
                    curr = to_ret.getFirst();
                    avaliable = to_ret.getSecond().getThisMap();
                    duration = duration - smallDur;
                    task_part = task_part + 1;


                }
            }
            imp_map.remove(first_ent.getKey());
        }


        return new Pair<>(curr, new Availability(avaliable));
    }

    //when the task we are on have more time then needed to fit in the more important task, part of the task
    //will be taken away and the more important task will be added
    private Pair<TaskList, Availability> partiallyReplaceAndPushBackTest(TaskList curr, Date to_add_duedate, String to_add_name, Double duration, Double importance, Boolean flexible, User user, int subtask, TaskList.Task allT) throws SQLException, ParseException {
        Double add_start = allT.getExactStart();
        Double add_end = allT.getExactStart() + duration;

        Double start = allT.getExactStart() + duration;
        Double end = allT.getExactEnd();
        curr.delTask(allT.getTaskName());

        curr.addTask(allT.getTaskName(), allT.getDueDay(), allT.getDate(), end - start
                , allT.getImportance(), start, end, flexible);
        //check if the replaced task can be pushed back a few days
        String new_name = allT.getTaskName().split("part")[0] + " pushed";

        curr.addTask(to_add_name + " part " + subtask,
                to_add_duedate, allT.getDate(), duration, importance, add_start, add_end, flexible);

        return scheduleOneTest(curr, allT.getDate(), allT.getDueDay(), new_name, duration, allT.getImportance(), allT.getFlexible(), user);
    }

    //when the task we are on have more time then needed to fit in the more important task, part of the task
    //will be taken away and the more important task will be added
    private Pair<TaskList, Availability> completelyReplaceAndPushBackTest(TaskList curr, Date to_add_duedate, String to_add_name, Double importance, Boolean flexible, User user, int subtask, TaskList.Task allT, Double smallDur) throws SQLException, ParseException {
        Double add_start = allT.getExactStart();
        Double add_end = allT.getExactEnd();

        curr.delTask(allT.getTaskName());

        curr.addTask(to_add_name + " part " + subtask,
                to_add_duedate, allT.getDate(), smallDur, importance, add_start, add_end, flexible);

        String new_name = allT.getTaskName().split("part")[0] + " pushed";


        Pair<TaskList, Availability> to_ret = scheduleOneTest(curr, allT.getDate(), allT.getDueDay(), new_name, smallDur, allT.getImportance(), allT.getFlexible(), user);
        return to_ret;
    }

    //add a task into the task list, change the availability accordingly. The task is completely inserted
    //and there will be no more parts left
    @NotNull
    private Pair<TaskList, Availability> getTaskListAvailabilityPairTest(TaskList curr, Date to_add_duedate, String to_add_name, Double duration, Double importance, Boolean flexible, Map<String, List<Pair<Double, Double>>> avaliable, int subtask, Map.Entry<String, List<Pair<Double, Double>>> ent, String ent_date_string, Date ent_date, List<Double> starttime, List<Double> endtime, int i, double start, double end) throws SQLException {
        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
        Pair<Double, Double> to_add = new Pair<>(start, end);
        editAvailable(ent, to_rem, to_add);
        avaliable.put(ent_date_string, ent.getValue());
        curr.addTask(to_add_name + " part " + subtask, to_add_duedate, ent_date, duration, importance,
                starttime.get(i), start, flexible);
        return new Pair<>(curr, new Availability(avaliable));
    }



    /**
     * this function deletes a task in the tasklist, using the inputs as attributes. While deleting this task,
     * availability will be added back to the list. The adding back of availability might need several iterations
     * to complete, when there is not enough time in between pairs of current availability.
     * @param curr the current tasklist
     * @param to_add_date the date that the task occurs on
     * @param to_add_name the name of task to delete
     * @param user the current User
     * @param exact_start exact start time
     * @param exact_end exact end time
     * @param d dao
     * @return a pair of availability and tasklist
     * @throws SQLException when using dao wrongly
     * @throws ParseException when parse input not valid
     */

    public Pair<TaskList, Availability> addBackTask(TaskList curr, Date to_add_date,
                                                    String to_add_name,  User user,
                                                    Double exact_start, Double exact_end, Dao<TaskList, Integer> d) throws SQLException, ParseException {
        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
        //format date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(to_add_date);
        List<Pair<Double, Double>> map_ent = avaliable.get(strDate);
        custom_sort(map_ent);
        for (int i = 0; i <= map_ent.size() - 1; i++) {
            //when the start time of task to delete is before the task we are looking at
            if (exact_start < map_ent.get(i).getFirst()) {
                if (exact_end.equals(map_ent.get(i).getFirst())) {
                    double start = exact_start;
                    double end = map_ent.get(i).getSecond();
                    return add_to_Avail(curr, to_add_name, d, avaliable, strDate, map_ent, i, start, end);
                } else {
                    //in the case we can't combine two time slots, add a new time slot to the date
                    //we are doing the task and delete the task
                    double start = exact_start;
                    double end = exact_end;
                    Pair<Double, Double> to_add = new Pair<>(start, end);
                    map_ent.add(to_add);
                    combine_avail(map_ent, start, end);
                    custom_sort(map_ent);
                    avaliable.put(strDate, map_ent);
                    curr.delTask(to_add_name, d);
                    return new Pair<>(curr, new Availability(avaliable));
                }
                //in the case we reaches end of the available time slots and still can't find a task that
                //starts later than the task to delete's start time, we add this time slot.
            } else if (i == (map_ent.size() - 1)) {
                double start = map_ent.get(i).getFirst();
                double end = exact_end;
                return add_to_Avail(curr, to_add_name, d, avaliable, strDate, map_ent, i, start, end);
            }
        }
        return new Pair<>(curr, new Availability(avaliable));
    }

    //replace the ith available time slot with a new time slot that absorbs the duration of the deleted task
    //and delete the task
    @NotNull
    private Pair<TaskList, Availability> add_to_Avail(TaskList curr, String to_add_name, Dao<TaskList, Integer> d, Map<String, List<Pair<Double, Double>>> avaliable, String strDate, List<Pair<Double, Double>> map_ent, int i, double start, double end) throws SQLException {
        Pair<Double, Double> to_add = new Pair<>(start, end);

        map_ent.remove(i);
        map_ent.add(to_add);
        combine_avail(map_ent, start, end);
        custom_sort(map_ent);
        avaliable.put(strDate, map_ent);
        curr.delTask(to_add_name, d);
        return new Pair<>(curr, new Availability(avaliable));
    }
    // sort using custom comparator
    private void custom_sort(List<Pair<Double, Double>> map_ent) {
        map_ent.sort((o1, o2) -> {
            if (o1.getFirst() < o2.getFirst()) {
                return -1;
            } else if (o1.getFirst().equals(o2.getFirst())) {
                return 0;
            } else {
                return 1;
            }
        });
    }
    //combines two tasks when a task have the end time being another task's start time
    private void combine_avail(List<Pair<Double, Double>> map_ent, double start, double end) {
        Double new_start = start;
        //set and find the time slots to remove and to add
        Pair<Double, Double> to_rem_one = new Pair<>(-1.0, -1.0);
        Pair<Double, Double> to_add_one = new Pair<>(-1.0, -1.0);
        for (Pair<Double, Double> might_dup : map_ent) {
            if (might_dup.getSecond().equals(start)) {
                to_rem_one = might_dup;
                to_add_one = new Pair<>(might_dup.getFirst(), end);
                new_start = might_dup.getFirst();
            }

        }
        removeOldAvail(map_ent, start, end, to_rem_one, to_add_one);
        Pair<Double, Double> to_rem_two = new Pair<>(-1.0, -1.0);
        Pair<Double, Double> to_add_two = new Pair<>(-1.0, -1.0);

        for (Pair<Double, Double> might_dup : map_ent) {
            if (might_dup.getFirst().equals(end)) {
                to_rem_two = might_dup;
                to_add_two = new Pair<>(new_start, might_dup.getSecond());
            }


        }
        removeOldAvail(map_ent, new_start, end, to_rem_two, to_add_two);
        custom_sort(map_ent);
    }
    //remove and add a time slot
    private void removeOldAvail(List<Pair<Double, Double>> map_ent, double start, double end, Pair<Double, Double> to_rem_one, Pair<Double, Double> to_add_one) {
        if (!to_rem_one.equals(new Pair<>(-1.0, -1.0))) {
            map_ent.remove(new Pair<>(start, end));
            map_ent.remove(to_rem_one);
        }
        if (!to_add_one.equals(new Pair<>(-1.0, -1.0))) {
            map_ent.add(to_add_one);
        }
    }
}
//package Functionality;
//import kotlin.Pair;
//import model.Availability;
//import model.TaskList;
//
//import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.time.*;
//import com.j256.ormlite.dao.Dao;
//import model.User;
//import org.jetbrains.annotations.NotNull;
//
//public class scheduleFunctions {
//
//        /**
//     * this function puts a task in the tasklist, using the inputs as attributes. While inputting this task,
//     * availability will change according to how long the task takes. Tasks might be split into multiple parts
//     * depending on the duration of it. Importance and flexible helps decide when
//     * there is not enough time to put every task in, which task will be replaced.
//     *
//     * @param to_add_date start date of task
//     * @param to_add_duedate end date of task
//     * @param to_add_name name of task
//     * @param duration how long does the task take
//     * @param importance higher importance task will replace lower importance task
//     * @param flexible indicate if the task can be moved or not
//     * @param user the User that is currently adding the task
//     * @param d Dao
//     * @return a pair of availability and task list
//     * @throws SQLException when using dao fails
//     * @throws ParseException when parse fails
//     */
//
//    public Availability scheduleOne_noadd(Date to_add_date, Date to_add_duedate,
//                                                          String to_add_name, Double duration, Double importance, Boolean flexible, User user, Dao<TaskList, Integer> d) throws SQLException, ParseException {
//        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
//        int task_part = 1;
//        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {
//            //format string into date
//            String ent_date_string = ent.getKey();
//            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
//            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
//                //create two list containing the start and end of an available time slot
//                List<Double> starttime = new ArrayList<>();
//                List<Double> endtime = new ArrayList<>();
//                for (Pair<Double, Double> val_pair : ent.getValue()) {
//                    starttime.add(val_pair.getFirst());
//                    endtime.add(val_pair.getSecond());
//                }
//                for (int i = 0; i < starttime.size(); i++) {
//                    double time_range = endtime.get(i) - starttime.get(i);
//                    //when duration becomes 0, we have finished scheduling.
//                    if (duration.equals(0.0)) {
//                        break;
//                    }
//
//                    if (time_range == 0) {
//                        continue;
//                    }
//                    if (time_range >= duration) {
//                        //when there is more time than we need, we take the time away from the start of this time slot
//                        double start = starttime.get(i) + duration;
//                        double end = endtime.get(i);
//                        //avaliable.remove(ent_date);
//                        return getTaskListAvailabilityPairNoAdd( to_add_duedate, to_add_name, duration, importance, flexible, d, avaliable, task_part, ent, ent_date_string, ent_date, starttime, endtime, i, start, end);
//                    } else {
//                        //when the time slot doesn't have enough time to fit the entire duration, we split task into parts.
//                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i), endtime.get(i));
//                        editAvailable(ent, to_rem, to_add);
//                        task_part++;
//                        duration = duration - time_range;
//                        avaliable.put(ent_date_string, ent.getValue());
//                    }
//                }
//
//            }
//
//        }
//
//
//
//        return new Availability(avaliable);
//    }
//
//    //this generates a map that is sorted based on the importance. Lower importance will be at front of map.
//    @NotNull
//    private TreeMap<Double, List<TaskList.Task>> getDoubleListTreeMap(TaskList curr) {
//        TreeMap<Double, List<TaskList.Task>> imp_map = new TreeMap<>();
//        for (TaskList.Task t : curr.getTaskList()) {
//            Double imp = t.getImportance();
//            if (imp_map.containsKey(imp)) {
//                imp_map.get(imp).add(t);
//            } else {
//                List<TaskList.Task> task = new ArrayList<>();
//                task.add(t);
//                imp_map.put(imp, task);
//            }
//        }
//        return imp_map;
//    }
//
//
//    //add a task into the task list, change the availability accordingly. The task is completely inserted
//    //and there will be no more parts left
//    @NotNull
//    private Availability getTaskListAvailabilityPairNoAdd(Date to_add_duedate, String to_add_name, Double duration, Double importance, Boolean flexible, Dao<TaskList, Integer> d, Map<String, List<Pair<Double, Double>>> avaliable, int subtask, Map.Entry<String, List<Pair<Double, Double>>> ent, String ent_date_string, Date ent_date, List<Double> starttime, List<Double> endtime, int i, double start, double end) throws SQLException {
//        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//        Pair<Double, Double> to_add = new Pair<>(start, end);
//        editAvailable(ent, to_rem, to_add);
//        avaliable.put(ent_date_string, ent.getValue());
//        return new Availability(avaliable);
//    }
//
//    //adds and removes some time slot and sort the list of availabilities
//    private void editAvailable(Map.Entry<String, List<Pair<Double, Double>>> ent, Pair<Double, Double> to_rem, Pair<Double, Double> to_add) {
//        ent.getValue().remove(to_rem);
//        ent.getValue().add(to_add);
//        ent.getValue().sort((o1, o2) -> {
//            if (o1.getFirst() < o2.getFirst()) {
//                return -1;
//            } else if (o1.getFirst().equals(o2.getFirst())) {
//                return 0;
//            } else {
//                return 1;
//            }
//        });
//    }
//
//
//    public Pair<TaskList, Availability> scheduleOne(TaskList curr, Date to_add_date, Date to_add_duedate,
//                                                    String to_add_name, Double duration, Double importance, Boolean flexible,User user, Dao<TaskList, Integer> d) throws SQLException, ParseException {
//
//        TreeMap<Double, List<TaskList.Task>> imp_map = new TreeMap<>();
//        for (TaskList.Task t :curr.getTaskList()) {
//            Double imp = t.getImportance();
//            if (imp_map.containsKey(imp)){
//                imp_map.get(imp).add(t);
//            } else {
//                List<TaskList.Task> task = new ArrayList<>();
//                task.add(t);
//                imp_map.put(imp,task);
//            }
//        }
//        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
//        int subtask = 1;
//        Iterator<Map.Entry<String, List<Pair<Double, Double>>>> iterator = avaliable.entrySet().iterator();
//        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {
//
//            String ent_date_string = ent.getKey();
//            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
//            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
//                List<Double> starttime = new ArrayList<>();
//                List<Double> endtime = new ArrayList<>();
//                for (Pair<Double, Double> val_pair : ent.getValue()) {
//                    starttime.add(val_pair.getFirst());
//                    endtime.add(val_pair.getSecond());
//                }
//                for (int i = 0; i < starttime.size(); i++) {
//                    double time_range = endtime.get(i) - starttime.get(i);
//                    if (duration.equals(0.0)) {
//                        break;
//                    }
//
//                    if (time_range == 0 ) {
//                        continue;
//                    }
//                    if (time_range >= duration) {
//
//                        double start = starttime.get(i) + duration;
//                        double end = endtime.get(i);
////                        avaliable.remove(ent_date);
//                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//                        Pair<Double, Double> to_add = new Pair<>(start, end);
//                        ent.getValue().remove(to_rem);
//                        ent.getValue().add(to_add);
//                        System.out.println(ent.getValue());
//                        ent.getValue().sort((o1, o2) -> {
//                            if (o1.getFirst() < o2.getFirst()) {
//                                return -1;
//                            } else if (o1.getFirst().equals(o2.getFirst())) {
//                                return 0;
//                            } else {
//                                return 1;
//                            }
//                        });
//                        System.out.println(ent.getValue());
//                        avaliable.put(ent_date_string, ent.getValue());
//                        curr.addTask(to_add_name+" part "+subtask,to_add_duedate,ent_date,duration,importance,
//                                starttime.get(i),start, flexible,d);
//                        duration = 0.0;
//                        return new Pair<>(curr, new Availability(avaliable));
//                    } else {
//                        avaliable.remove(ent_date);
//                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//                        Pair<Double, Double> to_add = new Pair<>(endtime.get(i),endtime.get(i));
//                        ent.getValue().remove(to_rem);
//                        ent.getValue().add(to_add);
//                        System.out.println(ent.getValue());
//                        ent.getValue().sort((o1, o2) -> {
//                            if (o1.getFirst() < o2.getFirst()) {
//                                return -1;
//                            } else if (o1.getFirst().equals(o2.getFirst())) {
//                                return 0;
//                            } else {
//                                return 1;
//                            }
//                        });
//                        System.out.println(ent.getValue());
//                        curr.addTask(to_add_name+" part "+subtask,
//                                to_add_duedate,ent_date,time_range,importance,starttime.get(i),endtime.get(i), flexible, d);
//                        subtask++;
//                        duration = duration - time_range;
//                        /*to_add_date = ent_date;*/
//                        avaliable.put(ent_date_string, ent.getValue());
//                    }
//                }
//
//            }
//
//        }
//
//        while (duration>0) {
//            Map.Entry<Double, List<TaskList.Task>> first_ent = imp_map.firstEntry();
//
//            for (TaskList.Task allT : first_ent.getValue()) {
//                if (duration.equals(0.0)) {
//                    break;
//                }
//
//                if (allT.getImportance() >= importance ) {
//                    return new Pair<>(curr, new Availability(avaliable));
//                }
//                if (allT.getFlexible().equals(false)) {
//                    continue;
//                }
//                Double smallDur = allT.getDuration();
//                if (smallDur > duration) {
//                    Double add_start = allT.getExactStart();
//                    Double add_end = allT.getExactStart() + duration ;
//
//                    Double start = allT.getExactStart()+ duration;
//                    Double end = allT.getExactEnd() ;
//                    curr.delTask(allT.getTaskName(),d);
//
//                    curr.addTask(allT.getTaskName(), allT.getDueDay(),allT.getDate(),end-start
//                            ,allT.getImportance(),start,end, flexible, d);
//
//                    String new_name = allT.getTaskName().split("part")[0]+ " pushed";
//
//                    curr.addTask(to_add_name+" part "+subtask,
//                            to_add_duedate,allT.getDate(),duration,importance,add_start,add_end, flexible, d);
//
//                    Pair<TaskList, Availability> to_ret = scheduleOne(curr, allT.getDate(),allT.getDueDay(),new_name,duration,allT.getImportance(),allT.getFlexible(), user,d);
//
//                    return to_ret;
//                    //Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//                } else {
//                    Double add_start = allT.getExactStart();
//                    Double add_end = allT.getExactEnd();
//
//                    curr.delTask(allT.getTaskName(),d);
//
//                    curr.addTask(to_add_name+" part "+subtask,
//                            to_add_duedate,allT.getDate(),smallDur,importance,add_start,add_end, flexible,d);
//
//                    String new_name = allT.getTaskName().split("part")[0]+ " pushed";
//
//
//                    Pair<TaskList, Availability> to_ret = scheduleOne(curr, allT.getDate(),allT.getDueDay(),new_name,smallDur,allT.getImportance(),allT.getFlexible(), user,d);
//                    curr = to_ret.getFirst();
//                    avaliable = to_ret.getSecond().getThisMap();
//                    duration = duration - smallDur;
//                    subtask = subtask + 1;
//
//
//                }
//            }
//            imp_map.remove(first_ent.getKey());
//        }
//
//
//        return new Pair<>(curr, new Availability(avaliable));
//    }
//
//    public Pair<TaskList, Availability> addBackTask(TaskList curr, Date to_add_date,Date to_add_duedate,
//                                                    String to_add_name,Double duration, User user,
//                                                    Double exact_start, Double exact_end, Dao<TaskList, Integer> d) throws SQLException, ParseException {
//        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String strDate = dateFormat.format(to_add_date);
//        List<Pair<Double, Double>> map_ent = avaliable.get(strDate);
//        map_ent.sort((o1, o2) -> {
//            if (o1.getFirst() < o2.getFirst()) {
//                return -1;
//            } else if (o1.getFirst().equals(o2.getFirst())) {
//                return 0;
//            } else {
//                return 1;
//
//            }
//        });
//        for (int i = 0; i <= map_ent.size() - 1; i++) {
//            if (exact_start < map_ent.get(i).getFirst()) {
//                if (exact_end.equals(map_ent.get(i).getFirst())) {
//                    double start = exact_start;
//                    double end = map_ent.get(i).getSecond();
//                    Pair<Double, Double> to_add = new Pair<>(start, end);
////                    System.out.println(start);
////                    System.out.println(end);
////                    avaliable.remove(strDate);
//                    map_ent.remove(i);
//                    map_ent.add(to_add);
//                    combine_avail(map_ent, start, end);
//
//                    System.out.println(map_ent);
//                    map_ent.sort((o1, o2) -> {
//                        if (o1.getFirst() < o2.getFirst()) {
//                            return -1;
//                        } else if (o1.getFirst().equals(o2.getFirst())) {
//                            return 0;
//                        } else {
//                            return 1;
//                        }
//                    });
//                    System.out.println(map_ent);
//
//                    avaliable.put(strDate, map_ent);
//
//                    curr.delTask(to_add_name, d);
//                    return new Pair<>(curr, new Availability(avaliable));
//                } else{
//                    double start = exact_start;
//                    double end = exact_end;
//                    Pair<Double, Double> to_add = new Pair<>(start, end);
//                    //System.out.println(start);
//                    //System.out.println(end);
//                    //avaliable.remove(strDate);
//                    map_ent.add(to_add);
//
//                    combine_avail(map_ent, start, end);
//
//                    System.out.println(map_ent);
//                    map_ent.sort((o1, o2) -> {
//                        if (o1.getFirst() < o2.getFirst()) {
//                            return -1;
//                        } else if (o1.getFirst().equals(o2.getFirst())) {
//                            return 0;
//                        } else {
//                            return 1;
//                        }
//                    });
//                    System.out.println(map_ent);
//
//                    avaliable.put(strDate, map_ent);
//                    curr.delTask(to_add_name, d);
//                    return new Pair<>(curr, new Availability(avaliable));
//                }
//
//
//            } else if (i == (map_ent.size() - 1)) {
//                double start = map_ent.get(i).getFirst();
//                double end = exact_end;
//                Pair<Double, Double> to_add = new Pair<>(start, end);
////                avaliable.remove(strDate);
//                map_ent.remove(i);
//                map_ent.add(to_add);
//                combine_avail(map_ent, start, end);
//                System.out.println(map_ent);
//                map_ent.sort((o1, o2) -> {
//                    if (o1.getFirst() < o2.getFirst()) {
//                        return -1;
//                    } else if (o1.getFirst().equals(o2.getFirst())) {
//                        return 0;
//                    } else {
//                        return 1;
//                    }
//                });
//                System.out.println(map_ent);
//                avaliable.put(strDate, map_ent);
//                curr.delTask(to_add_name, d);
//                return new Pair<>(curr, new Availability(avaliable));
//            }
//        }
//        return new Pair<>(curr, new Availability(avaliable));
//    }
//
//    private void combine_avail(List<Pair<Double, Double>> map_ent, double start, double end) {
//        Double new_start = start;
//        Pair<Double, Double> to_rem_one = new Pair<>(-1.0, -1.0);
//        Pair<Double, Double> to_add_one = new Pair<>(-1.0, -1.0);
//        for (Pair<Double, Double> might_dup : map_ent) {
//            if (might_dup.getSecond().equals(start)) {
//                System.out.println("entered first");
//                to_rem_one = might_dup;
//                to_add_one = new Pair<>(might_dup.getFirst(), end);
//                new_start = might_dup.getFirst();
//            }
//        }
//        if (!to_rem_one.equals(new Pair<>(-1.0, -1.0))) {
//            map_ent.remove(new Pair<>(start, end));
//            map_ent.remove(to_rem_one);
//            System.out.println(new Pair<>(start, end));
//            System.out.println(to_rem_one);
//        }
//        if (!to_add_one.equals(new Pair<>(-1.0, -1.0))) {
//            map_ent.add(to_add_one);
//        }
//        Pair<Double, Double> to_rem_two = new Pair<>(-1.0, -1.0);
//        Pair<Double, Double> to_add_two = new Pair<>(-1.0, -1.0);
//
//        for (Pair<Double, Double> might_dup : map_ent) {
//            if (might_dup.getFirst().equals(end)) {
//                System.out.println("entered second");
//
//                to_rem_two = might_dup;
//                to_add_two = new Pair<>(new_start, might_dup.getSecond());
//                start = might_dup.getFirst();
//            }
//
//
//        }
//        if (!to_rem_two.equals(new Pair<>(-1.0, -1.0))) {
//            map_ent.remove(new Pair<>(new_start, end));
//            map_ent.remove(to_rem_two);
//        }
//        if (!to_add_two.equals(new Pair<>(-1.0, -1.0))) {
//            map_ent.add(to_add_two);
//        }
//        System.out.println(map_ent);
//        map_ent.sort((o1, o2) -> {
//            if (o1.getFirst() < o2.getFirst()) {
//                return -1;
//            } else if (o1.getFirst().equals(o2.getFirst())) {
//                return 0;
//            } else {
//                return 1;
//            }
//        });
//        System.out.println(map_ent);
//    }


//    public Pair<TaskList, Availability> scheduleOneTest(TaskList curr, Date to_add_date,Date to_add_duedate,
//                                                        String to_add_name,Double duration, Double importance, Availability user) throws SQLException, ParseException {
//        Map<String, List<Pair<Double, Double>>> avaliable = user.getThisMap();
//        int subtask = 1;
//        Iterator<Map.Entry<String, List<Pair<Double, Double>>>> iterator = avaliable.entrySet().iterator();
//        for (Map.Entry<String, List<Pair<Double, Double>>> ent : avaliable.entrySet()) {
//
//            String ent_date_string = ent.getKey();
//            Date ent_date = new SimpleDateFormat("yyyy-MM-dd").parse(ent_date_string);
//            if ((ent_date.compareTo(to_add_date) >= 0) && (ent_date.compareTo(to_add_duedate) <= 0)) {
//                List<Double> starttime = new ArrayList<>();
//                List<Double> endtime = new ArrayList<>();
//                for (Pair<Double, Double> val_pair : ent.getValue()) {
//                    starttime.add(val_pair.getFirst());
//                    endtime.add(val_pair.getSecond());
//                }
//                for (int i = 0; i < starttime.size(); i++) {
//                    double time_range = endtime.get(i) - starttime.get(i);
//                    if (time_range > duration) {
//                        double start = starttime.get(i) + duration;
//                        double end = endtime.get(i);
//                        avaliable.remove(ent_date);
//                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//                        Pair<Double, Double> to_add = new Pair<>(start, end);
//                        ent.getValue().remove(to_rem);
//                        ent.getValue().add(to_add);
//                        avaliable.put(ent_date_string, ent.getValue());
//                        curr.addTask(to_add_name + " part " + subtask, to_add_duedate, ent_date, duration, importance
//                        ,starttime.get(i),start);
//                        return new Pair<>(curr, new Availability(avaliable));
//                    } else {
//                        avaliable.remove(ent_date);
//                        Pair<Double, Double> to_rem = new Pair<>(starttime.get(i), endtime.get(i));
//                        ent.getValue().remove(to_rem);
//                        curr.addTask(to_add_name + " part " + subtask,
//                                to_add_duedate, ent_date, time_range, importance, starttime.get(i), endtime.get(i));
//                        subtask++;
//                        duration = duration - time_range;
//                        /*to_add_date = ent_date;*/
//
//                    }
//                }
//
//            }
//
//        }
//        return new Pair<>(curr, new Availability(avaliable));
//    }
