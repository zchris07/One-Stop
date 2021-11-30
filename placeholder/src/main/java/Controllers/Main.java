package Controllers;

import com.j256.ormlite.dao.Dao;
import java.net.URISyntaxException;
import java.sql.SQLException;
import spark.Spark;
import model.*;

public class Main {

    static int PORT_NUM = 7000;
    private static int getPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            PORT_NUM = Integer.parseInt(herokuPort);
        }
        return PORT_NUM;
    }

    public static void main(String[] args) throws SQLException, URISyntaxException {

        Dao userDao     = DaoConstructor.getTheDao(User.class, DaoConstructor.userCreateTableSql);
        Dao tasklistDao = DaoConstructor.getTheDao(TaskList.class, DaoConstructor.tasklistCreateTableSql);
        Dao worksonDao  = DaoConstructor.getTheDao(WorksOn.class, DaoConstructor.worksonCreateTableSql);
        Dao tasknoteDao = DaoConstructor.getTheDao(TaskNote.class, DaoConstructor.tasknoteCreateTableSql);

        Spark.port(PORT_NUM);
        Spark.staticFiles.location("/public");

        APIEndpoint.rootGet();
        APIEndpoint.signupGet();
        APIEndpoint.signupPost(userDao);
        APIEndpoint.accExist();
        APIEndpoint.loginGet();
        APIEndpoint.loginPost(userDao);
        APIEndpoint.loginGet();
        APIEndpoint.resetGet();
        APIEndpoint.resetPost(userDao);
        APIEndpoint.nonexistGet();
        APIEndpoint.nonexistPswGet();
        APIEndpoint.userprofileGet(userDao);
        APIEndpoint.userprofilePut(userDao);
        APIEndpoint.showlistGet(tasklistDao);
        APIEndpoint.showDetailPut(tasknoteDao);
        APIEndpoint.addlistPost(worksonDao, tasklistDao);
        APIEndpoint.deletelist(tasklistDao);
        APIEndpoint.addtaskPost(tasklistDao, userDao);
        APIEndpoint.deleteTask(tasklistDao, userDao);
        APIEndpoint.scheduleGet(userDao);
        APIEndpoint.schedulePut(tasklistDao);
        APIEndpoint.showDetailGet(tasknoteDao);
        APIEndpoint.main(tasklistDao, worksonDao, userDao);
        APIEndpoint.updateDate(tasklistDao);
    }

}
