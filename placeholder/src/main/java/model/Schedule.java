package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import static Controllers.DaoConstructor.*;

public class Schedule {

     public String getAllTaskDate(String userid, Dao tasklistDao) throws SQLException {
        List<TaskList> taskLists = tasklistDao.queryForEq("userId", userid);
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i=0;i<taskLists.size();i++) {
            result.append(taskDateListToJsonString(taskLists.get(i)));
            if (i!= taskLists.size()-1){
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }
    public String taskDateListToJsonString(TaskList taskList_whole) {
        StringBuilder sb = new StringBuilder();
        if (taskList_whole.taskList!= null){
            for (int i=0;i<taskList_whole.taskList.size();i++) {
                sb.append(taskList_whole.taskList.get(i).taskToJsonString());
                if (i!= taskList_whole.taskList.size()-1){
                    sb.append(",");
                }
            }}
        return sb.toString();
    }

}