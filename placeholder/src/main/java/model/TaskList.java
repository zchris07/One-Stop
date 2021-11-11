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
    static class Task implements Serializable {

        @DatabaseField(canBeNull = false,columnName="taskName", unique = true)
        private String taskName;
        @DatabaseField(dataType = DataType.DATE_STRING,columnName="dueDay",
                format = "yyyy-MM-dd")
        private Date dueDay;
        @DatabaseField(dataType = DataType.DATE_STRING,columnName="date",
                format = "yyyy-MM-dd")
        private Date date;

        public Task(String taskName, Date dueDay, Date date) {
            this.taskName = taskName;
            this.dueDay = dueDay;
            this.date = date;
        }

        public String taskToJsonString() {
            SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd");

            return "{\"taskName\":\"" + taskName + "\",\"duration_day\":\"" + date_formater.format(dueDay) + "\",\"date\":\"" + date_formater.format(date) + "\"}";
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

    public void addTask(String taskName, Date dueDay, Date date, Dao<TaskList,Integer> dao) throws SQLException {
        Task task = new Task(taskName, dueDay, date);
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
}