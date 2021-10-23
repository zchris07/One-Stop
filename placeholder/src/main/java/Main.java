import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
//import model.PItem;
import model.TaskList;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static Dao <TaskList,Integer> getTaskListRMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, TaskList.class);
//        TableUtils.dropTable(connectionSource, TaskList.class,false);
        return DaoManager.createDao(connectionSource, TaskList.class);
    }

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);
        Spark.staticFiles.location("/public");

        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("login") != null)
                model.put("login", req.cookie("login"));
            return new ModelAndView(model, "public/login.vm");
        }, new VelocityTemplateEngine());

        // used set a username cookie
        Spark.post("/", (req, res) -> {
            String emailAddress = req.queryParams("emailAddress");
            String password = req.queryParams("password");
            res.cookie("sign-up", emailAddress);
            res.redirect("/main");
            return null;
        });

        Spark.get("/sign-up", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("sign-up") != null)
                model.put("sign-up", req.cookie("sign-up"));
            return new ModelAndView(model, "public/sign_up.vm");
        }, new VelocityTemplateEngine());

        // used set a username cookie
        Spark.post("/sign-up", (req, res) -> {
            String emailAddress = req.queryParams("emailAddress");
            String password = req.queryParams("password");
            res.cookie("login", emailAddress);
            res.redirect("/");
            return null;
        });

        Spark.get("/userprofile", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/profile.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/showList", (req, res) -> {
            String listId = req.queryParams("listId");
            Integer listIdInt = Integer.parseInt(listId);
            Dao<TaskList, Integer> emDao = getTaskListRMLiteDao();
            List<TaskList> ems = emDao.queryForEq("listId", listIdInt);
            res.type("application/json");
            return  ems.get(0).toJsonString();
        });
        Spark.post("/addList", (req, res) -> {
            String listName = req.queryParams("listName");
            TaskList tasklist = new TaskList (listName);
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            taskDao.create(tasklist);
            List<TaskList> ems = taskDao.queryForEq("listName", listName);
            res.status(201);
            res.type("application/json");
            return  ems.get(0).toJsonString();
        });
        Spark.delete("/deleteList", (req, res) -> {
            String listId = req.queryParams("listId");
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            List<TaskList> ems = taskDao.queryForEq("listId", listId);
            taskDao.delete(ems.get(0));
            res.status(204);
            return "";
        });
        Spark.post("/addTask", (req, res) -> {
            String listId = req.queryParams("listId");
            String taskName = req.queryParams("taskName");
            String dueDay = req.queryParams("dueDay");
            String date_string = req.queryParams("date");

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);

            Date date = formatter.parse(date_string);
            Date dueDay_date = formatter.parse(dueDay);
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            List<TaskList> ems = taskDao.queryForEq("listId", listId);
            ems.get(0).addTask(taskName, dueDay_date, date, taskDao);
            res.status(201);
            res.type("application/json");
            List<TaskList> ems2 = taskDao.queryForEq("listId", listId);

            return ems2.get(0).toJsonString();
        });
        Spark.delete("/deleteTask", (req, res) -> {
            String listId = req.queryParams("listId");
            String taskName = req.queryParams("taskName");
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            List<TaskList> ems = taskDao.queryForEq("listId", listId);
            ems.get(0).delTask(taskName, taskDao);
            res.status(201);
            res.type("application/json");
            List<TaskList> ems2 = taskDao.queryForEq("listId", listId);

            return ems2.get(0).toJsonString();
        });
        Spark.get("/main", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TaskList> tasklists = getTaskListRMLiteDao().queryForAll();
            model.put("lists", tasklists);
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());
//        // create a new task
//        Spark.post("/main", (req, res) -> {
////            String id = req.queryParams("id");
//            String taskName = req.queryParams("taskName");
//            String dueDay = req.queryParams("dueDay");
//
//            String date_string = req.queryParams("date");
//
//            String pattern = "yyyy-MM-dd";
//            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//
//            Date date = formatter.parse(date_string);
//            Date dueDay_date = formatter.parse(dueDay);
//            PItem pitem = new PItem (taskName,dueDay_date, date);
//            getPItemRMLiteDao().create(pitem);
//            res.status(201);
//            res.type("application/json");
//            return new Gson().toJson(pitem.toJsonString());
//        });
////
//        // delete tasks
//        Spark.delete("/Delete-task", (req, res) -> {
//
//            String taskName = req.queryParams("taskName");
//            Dao<TaskList, Integer> emDao = getPItemRMLiteDao();
//
//            List<PItem> ems = emDao.queryForEq("taskName", taskName);
//            int del = 0;
////            System.out.println(getPItemRMLiteDao().queryForAll());
//            if (ems != null  && !ems.isEmpty()) {
//                del = emDao.deleteById(ems.get(0).getId());
//            }
//            res.status(200);
//            res.type("application/json");
//            if (del > 0) {
//                return new Gson().toJson(ems.get(0).toString());
//            }
//            return new Gson().toJson("{}");
//        });



    }
}
