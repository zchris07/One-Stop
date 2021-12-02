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
        List<TaskList> taskLists = tasklistDao.queryForEq("userid", userid);
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < taskLists.size(); i++) {
             result.append(taskDateListToJsonString(taskLists.get(i)));
        }
        if(result.length() > 0){
            result.deleteCharAt(result.length() - 1);
        }
        result.append("]");
        return result.toString();
    }
    public String taskDateListToJsonString(TaskList taskList_whole) {
        StringBuilder sb = new StringBuilder();
        if (taskList_whole.getTaskList() != null) {
            for (int i = 0; i < taskList_whole.getTaskList().size(); i++) {
                sb.append(taskList_whole.getTaskList().get(i).taskToJsonString());
//                if (i!= taskList_whole.getTaskList().size()-1){
                    sb.append(",");
//                }
            }

        }
        return sb.toString();
    }

}