/*package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@DatabaseTable(tableName = "TaskList")
public class TaskList {
    @DatabaseField(generatedId = true, columnName="listId")
    private Integer listId;
    @DatabaseField(columnName="listName",canBeNull = false,unique = true)
    private String listName;
    @DatabaseField(columnName = "userid", canBeNull = true)
    private String userid;
    @DatabaseField(columnName="taskList",canBeNull = false,dataType = DataType.SERIALIZABLE)
    public SerializedList<Task> taskList = new SerializedList<>();
    public static class SerializedList<Task> extends ArrayList<Task> implements Serializable {
    }
    public static class Task implements Serializable {

        @DatabaseField(canBeNull = false,columnName="taskName", unique = true)
        private String taskName;
        @DatabaseField(dataType = DataType.DATE_STRING,columnName="dueDay",
                format = "yyyy-MM-dd")
        private Date dueDay;
        @DatabaseField(dataType = DataType.DATE_STRING,columnName="date",
                format = "yyyy-MM-dd")
        private Date date;
        @DatabaseField(canBeNull = false,columnName = "duration")
        private Float duration;

        public Task(String taskName, Date dueDay, Date date, Float duration) {
            this.taskName = taskName;
            this.dueDay = dueDay;
            this.date = date;
            this.duration = duration;
        }
/*
        public String getTaskName() {
            return taskName;
        }

        public Date getDueDay() {
            return dueDay;
        }

        public Date getDate() {
            return date;
        }

        public Float getDuration() {
            return duration;
        }

        public void setDuration(Float duration) {
            this.duration = duration;
        }

        public String taskToJsonString() {
            SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd");

            return "{\"taskName\":\"" + taskName + "\",\"duration_day\":\"" + date_formater.format(dueDay) + "\",\"date\":\"" + date_formater.format(date) + "\" + \"taskName\":\"\" + duration}";
        }
    }

    public TaskList(String listName) {
        this.listName= listName;
    }
    public TaskList() {
    }

    public Integer getId() {
        return listId;
    }




    public void addTask(String taskName, Date dueDay, Date date,Float duration, Dao<TaskList,Integer> dao) throws SQLException {
        Task task = new Task(taskName, dueDay, date,duration);
        taskList.add(task);
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("taskList", taskList);
        builder.where().eq("listId",listId);
        dao.update(builder.prepare());
    }
    public void delTask(String taskName, Dao<TaskList,Integer> dao) throws SQLException {
        for (int i = 0; i < taskList.size(); i++) {
            if (Objects.equals(taskList.get(i).taskName, taskName)) {
                taskList.remove(i);
                break;
            }
        }
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("taskList", taskList);
        builder.where().eq("listId",listId);
        dao.update(builder.prepare());
    }
    public String getListName() {
        return listName;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"listId\":").append(listId).append(",\"listName\":\"").append(listName).append("\",\"taskList\":[");
        if (taskList!= null){
        for (int i=0;i<taskList.size();i++) {
            sb.append(taskList.get(i).taskToJsonString());
            if (i!= taskList.size()-1){
                sb.append(",");
            }
        }}
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}*/
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

@DatabaseTable(tableName = "TaskList")
public class TaskList {
    @DatabaseField(generatedId = true, columnName = "listId")
    private Integer listId;
    @DatabaseField(columnName = "listName", canBeNull = false, unique = true)
    private String listName;
    @DatabaseField(columnName = "userid", canBeNull = true)
    private String userid;
    @DatabaseField(columnName = "taskList", canBeNull = false, dataType = DataType.SERIALIZABLE)
    public SerializedList<Task> taskList = new SerializedList<>();


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

        public Task(String taskName, Date dueDay,  Date date, Double duration, Double importance, Double exactStart, Double exactEnd) {
            this.taskName = taskName;
            this.dueDay = dueDay;
            this.duration = duration;
            this.date = date;
            this.importance = importance;
            this.exactStart = exactStart;
            this.exactEnd = exactEnd;
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

        public String taskToJsonString() {
            SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd");

            return "{\"taskName\":\"" + taskName + "\",\"duration_day\":\"" + date_formater.format(dueDay) + "\",\"date\":\""
                    + date_formater.format(date) + "\",\"duration\":\"" + duration +"\",\"importance\":\"" + importance +
                    "\",\"exactStart\":\"" + exactStart+"\",\"exactEnd\":\"" + exactEnd+"\"}";
        }
    }



    public TaskList(String listName) {
        this.listName = listName;
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
        return listId;
    }
    public void addTask(String taskName, Date dueDay, Date date, Double duration,Double importance, Double exactStart, Double exactEnd){
        Task task = new Task(taskName, dueDay,date,duration, importance, exactStart,exactEnd);
        taskList.add(task);
    }


    public void addTask(String taskName, Date dueDay, Date date, Double duration, Double importance,
                        Double exactStart, Double exactEnd, Dao<TaskList, Integer> dao) throws SQLException {
        Task task = new Task(taskName, dueDay, date, duration,importance, exactStart, exactEnd);
        taskList.add(task);
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("taskList", taskList);
        builder.where().eq("listId", listId);
        dao.update(builder.prepare());
    }

    public void delTask(String taskName, Dao<TaskList, Integer> dao) throws SQLException {
        for (int i = 0; i < taskList.size(); i++) {
            if (Objects.equals(taskList.get(i).taskName, taskName)) {
                taskList.remove(i);
                break;
            }
        }
        UpdateBuilder<TaskList, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("taskList", taskList);
        builder.where().eq("listId", listId);
        dao.update(builder.prepare());
    }

    public String getListName() {
        return listName;
    }

    public TaskList.Task getTask(String taskName, Dao<TaskList,Integer> dao) throws SQLException{
        for (int i = 0; i < taskList.size(); i++) {
            if (Objects.equals(taskList.get(i).taskName, taskName)) {
                return taskList.get(i);
            }
        }
        return null;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"listId\":").append(listId).append(",\"listName\":\"").append(listName).append("\",\"taskList\":[");
        if (taskList != null) {
            for (int i = 0; i < taskList.size(); i++) {
                sb.append(taskList.get(i).taskToJsonString());
                if (i != taskList.size() - 1) {
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