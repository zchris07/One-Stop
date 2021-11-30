package Controllers;
import spark.Spark;


public class Main {
    public static void main(String[] args) {
        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);
        Spark.staticFiles.location("/public");
        APIEndpoint.rootGet();
        APIEndpoint.signupGet();
        APIEndpoint.signupPost();
        APIEndpoint.accExist();
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
        APIEndpoint.showlistGet();
        APIEndpoint.showDetailPut();
        APIEndpoint.addlistPost();
        APIEndpoint.deletelist();
        APIEndpoint.addtaskPost();
        APIEndpoint.deleteTask();
        APIEndpoint.scheduleGet();
        APIEndpoint.schedulePut();
        APIEndpoint.showDetailGet();
        APIEndpoint.main();
        APIEndpoint.updateDate();
    }

}
