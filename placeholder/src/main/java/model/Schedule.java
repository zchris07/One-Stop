package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Schedule {

    private static Dao<TaskList,Integer> getTaskListRMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, TaskList.class);
//        TableUtils.dropTable(connectionSource, TaskList.class,false);
        return DaoManager.createDao(connectionSource, TaskList.class);
    }
    public String getAllTaskDate(String userid) throws SQLException {
        List<TaskList> taskLists = getTaskListRMLiteDao().queryForEq("userId", userid);
        return taskDateListToJsonString(taskLists.get(0));
    }
    public String taskDateListToJsonString(TaskList taskList_whole) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (taskList_whole.taskList!= null){
            for (int i=0;i<taskList_whole.taskList.size();i++) {
                sb.append(taskList_whole.taskList.get(i).taskToJsonString());
                if (i!= taskList_whole.taskList.size()-1){
                    sb.append(",");
                }
            }}
        sb.append("]");
        return sb.toString();
    }

}
