package Controllers;
import spark.Spark;


public class Main {
    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);
        Spark.staticFiles.location("/public");

        // Init pages
        APIEndpoint.rootGet();
        APIEndpoint.signupGet();
        APIEndpoint.signupPost();
        APIEndpoint.accExist();

        // User pages
        APIEndpoint.loginGet();
        APIEndpoint.loginPost();
        APIEndpoint.loginPost();
        APIEndpoint.loginGet();
        APIEndpoint.resetGet();
        APIEndpoint.resetPost();
        APIEndpoint.nonexistGet();
        APIEndpoint.nonexistPswGet();
        APIEndpoint.userprofileGet();
        APIEndpoint.userprofilePut();

        // List task pages
        APIEndpoint.showlistGet();
        APIEndpoint.showDetailPut();
        APIEndpoint.addlistPost();
        APIEndpoint.deletelist();
        APIEndpoint.addtaskPost();
        APIEndpoint.deleteTask();

        // Scheduling pages
        APIEndpoint.scheduleGet();
        APIEndpoint.schedulePut();
        APIEndpoint.showDetailGet();

        // Image detection pages
        APIEndpoint.imgDetectUpload();
        APIEndpoint.imgDetectSaveUrl();
        APIEndpoint.imgDetect();

        // Main page
        APIEndpoint.main();
    }

}
