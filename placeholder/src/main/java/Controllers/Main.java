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

        Spark.port(getPort());
        Spark.staticFiles.location("/public");

        // Init pages
        APIEndpoint.rootGet();
        APIEndpoint.signupGet();
        APIEndpoint.signupPost(userDao);
        APIEndpoint.accExist();

        // User pages
        APIEndpoint.loginGet();
        APIEndpoint.loginPost(userDao);
        APIEndpoint.resetGet();
        APIEndpoint.resetPost(userDao);
        APIEndpoint.nonexistGet();
        APIEndpoint.nonexistPswGet();
        APIEndpoint.userprofileGet(userDao);
        APIEndpoint.userprofilePut(userDao);

        // List task pages
        APIEndpoint.showlistGet(tasklistDao);
        APIEndpoint.showDetailPut(tasknoteDao);
        APIEndpoint.addlistPost(worksonDao, tasklistDao);
        APIEndpoint.deletelist(tasklistDao);
        APIEndpoint.addavailPost(userDao);
        APIEndpoint.addtaskPost(tasklistDao, userDao);
        APIEndpoint.deleteTask(tasklistDao, userDao);

        // Scheduling pages
        APIEndpoint.scheduleGet(userDao);
        APIEndpoint.schedulePut(tasklistDao);
        APIEndpoint.showDetailGet(tasknoteDao);

        // Image detection pages
        APIEndpoint.imgDetectUpload();
        APIEndpoint.imgDetectSaveUrl();
        APIEndpoint.imgDetect();

        // Main page
        APIEndpoint.main(tasklistDao, worksonDao, userDao);

        // Others
        APIEndpoint.updateDate(tasklistDao);
    }

}
