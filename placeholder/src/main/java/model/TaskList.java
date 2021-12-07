package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@DatabaseTable(tableName = "tasklist")
public class TaskList {
    @DatabaseField(generatedId = true, columnName = "id")
    private Integer id;
    @DatabaseField(columnName = "listname", canBeNull = false, unique = true)
    private String listname;
    @DatabaseField(columnName = "userid", canBeNull = true)
    private String userid;
    @DatabaseField(columnName = "tasklist", canBeNull = false, dataType = DataType.SERIALIZABLE)
    public SerializedList<Task> tasklist = new SerializedList<>();


    public static class SerializedList<Task> extends ArrayList<Task> implements Serializable {
    }

    public static class Task implements Serializable {

        @DatabaseField(canBeNull = false, columnName = "taskName", unique = true)
        private String taskName;
        @DatabaseField(dataType = DataType.DATE_STRING, columnName = "dueDay",
                format = "yyyy-MM-dd")
        private Date dueDay;
        @DatabaseField(dataType = DataType.DOUBLE, columnName = "duration")
        private Double duration;
        @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date",
                format = "yyyy-MM-dd")
        private Date date;
        @DatabaseField(dataType = DataType.DOUBLE,columnName = "importance")
        private Double importance;
        @DatabaseField(dataType = DataType.DOUBLE, columnName = "exactStart")
        private Double exactStart;
        @DatabaseField(dataType = DataType.DOUBLE, columnName = "exactEnd")
        private Double exactEnd;
        @DatabaseField(dataType = DataType.BOOLEAN, columnName = "flexible")
        private Boolean flexible;

        public Task(String taskName, Date dueDay,  Date date, Double duration, Double importance, Double exactStart, Double exactEnd
                ,Boolean flexible) {
            this.taskName = taskName;
            this.dueDay = dueDay;
            this.duration = duration;
            this.date = date;
            this.importance = importance;
            this.exactStart = exactStart;
            this.exactEnd = exactEnd;
            this.flexible = flexible;
        }

        /*public Task(String taskName, Date dueDay, Date date, Double duration, Double importance) {
            this.taskName = taskName;
            this.dueDay = dueDay;
            this.date = date;
            this.duration = duration;
            this.importance = importance;
        }*/

        public String getTaskName() {
            return taskName;
        }

        public Date getDueDay() {
            return dueDay;
        }

        public Double getDuration() {
            return duration;
        }

        public Date getDate() {
            return date;
        }

        public Double getImportance() { return importance;}

        public Double getExactStart() {
            return exactStart;
        }

        public Double getExactEnd() {
            return exactEnd;
        }

        public Boolean getFlexible() {
            return flexible;
        }

        public String taskToJsonString() {
            SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd");

            return "{\"taskName\":\"" + taskName + "\",\"duration_day\":\"" + date_formater.format(dueDay) + "\",\"date\":\""
                    + date_formater.format(date) + "\",\"duration\":\"" + duration +"\",\"importance\":\"" + importance +
                    "\",\"exactStart\":\"" + exactStart+"\",\"exactEnd\":\"" + exactEnd +"\",\"flexible\":\"" + flexible+"\"}";
        }
    }



    public TaskList(String listName) {
        this.listname = listName;
    }

    public TaskList() {
    }
    /*public List<Date> getTaskListdueDate(){
        List<Date> l_list = new ArrayList<>() ;
        for (Task task : taskList) {
            l_list.add(task.dueDay);
        }
        return l_list;
    }
    public List<String> getTaskListName() {
        List<String> l_list = new ArrayList<>() ;
        for (Task task : taskList) {
            l_list.add(task.taskName);
        }
        return l_list;
    }
    public List<Date> getTaskListDate() {
        List<Date> l_list = new ArrayList<>() ;
        for (Task task : taskList) {
            l_list.add(task.date);
        }
        return l_list;
    }
    public List<Double> getTaskListDuration() {
        List<Double> l_list = new ArrayList<>() ;
        for (Task task : taskList) {
            l_list.add(task.duration);
        }
        return l_list;
    }*/

    public Integer getId() {
        return id;
    }
    public void addTask(String taskName, Date dueDay, Date date, Double duration,Double importance, Double exactStart,
                        Double exactEnd, Boolean flexible){
        Task task = new Task(taskName, dueDay,date,duration, importance, exactStart,exactEnd,flexible);
        tasklist.add(task);

    }


    public void addTask(String taskName, Date dueDay, Date date, Double duration, Double importance,
                        Double exactStart, Double exactEnd, Boolean flexible, Dao<TaskList, Integer> dao) throws SQLException {
        Task task = new Task(taskName, dueDay, date, duration,importance, exactStart, exactEnd,flexible);
        tasklist.add(task);
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("tasklist", tasklist);
        builder.where().eq("id", id);
        dao.update(builder.prepare());
    }


    public void delTask(String taskName, Dao<TaskList, Integer> dao) throws SQLException {
        for (int i = 0; i < tasklist.size(); i++) {
            if (Objects.equals(tasklist.get(i).taskName, taskName)) {
                tasklist.remove(i);
                break;
            }
        }
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("tasklist", tasklist);
        builder.where().eq("id", id);
        dao.update(builder.prepare());
    }

    public void delTask(String taskName) throws SQLException {
        for (int i = 0; i < tasklist.size(); i++) {
            if (Objects.equals(tasklist.get(i).taskName, taskName)) {
                tasklist.remove(i);
                break;
            }
        }
    }

    public void updateTaskDate(String taskName, Dao<TaskList,Integer> dao,Date editedDate ) throws SQLException {
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        for (int i = 0; i < tasklist.size(); i++) {
            if (Objects.equals(tasklist.get(i).taskName, taskName)) {
                Task task =tasklist.get(i) ;
                task.date =  editedDate;
                tasklist.set(i,task);
                break;
            }
        }

        builder.updateColumnValue("tasklist", tasklist);
        builder.where().eq("id", id);
        dao.update(builder.prepare());
    }

    public String getListName() {
        return listname;
    }

    public TaskList.Task getTask(String taskName, Dao<TaskList,Integer> dao) throws SQLException{
        for (int i = 0; i < tasklist.size(); i++) {
            if (Objects.equals(tasklist.get(i).taskName, taskName)) {
                return tasklist.get(i);
            }
        }
        return null;
    }

    public List<Task> getTaskList() {
        return tasklist;
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"listId\":").append(id).append(",\"listName\":\"").append(listname).append("\",\"taskList\":[");
        if (tasklist != null) {
            for (int i = 0; i < tasklist.size(); i++) {
                sb.append(tasklist.get(i).taskToJsonString());
                if (i != tasklist.size() - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}