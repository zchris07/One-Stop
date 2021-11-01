import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
//import model.PItem;
import model.TaskList;
import model.User;
import model.WorksOn;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.security.MessageDigest;
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

    private static Dao getUserORMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        return DaoManager.createDao(connectionSource, User.class);
    }

    private static Dao getWorksOnORMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, WorksOn.class);
        return DaoManager.createDao(connectionSource, WorksOn.class);
    }

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);
        Spark.staticFiles.location("/public");

        Spark.get("/signup", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/signup.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/login.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/signup", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            // password secure hash
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hashedPassword = new String(messageDigest.digest());

            User ur = new User(email, hashedPassword);
            getUserORMLiteDao().create(ur);
            res.cookie("userid", email);
            res.status(201);
            res.redirect("/main");
            return null;
        });

        Spark.get("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/login.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            // password authentication
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hashedPassword = new String(messageDigest.digest());

            List<User> lstur = getUserORMLiteDao().queryForEq("email", email);
            if(lstur != null && !lstur.isEmpty()){
                if (lstur.get(0).getHashedPassword().equals(hashedPassword)) {
                    // do something login related
                    res.cookie("userid", email);
                    res.redirect("/main");
                }
                else {
                    // warn
                    res.redirect("/warn");
                }
            }
            else {
                // warn
                res.redirect("/warn");
            }

            return null;
        });

        Spark.get("/userprofile", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("login") == null && req.cookie("sign-up") == null)
                res.redirect("/");
            else {
                if(req.cookie("login") == null)
                    model.put("username", req.cookie("sign-up"));
                else
                    model.put("username", req.cookie("login"));
            }
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
//            String userid = req.queryParams("userid");
            String userid;
            if (req.cookie("userid") != null) {
                userid = req.cookie("userid");
            }
            else {
                userid = "";
            }
            String colabidString = req.queryParams("colabidstring");

            // handle Collaborator
            String[] colabidStringArr = colabidString.split(";");
            Dao worksOnDao = getWorksOnORMLiteDao();
            for(int i=0; i<colabidStringArr.length;i++){
                String colabid = colabidStringArr[i].trim();
                WorksOn wks = new WorksOn(colabid, listName);
                worksOnDao.create(wks);
            }

            TaskList tasklist = new TaskList (listName);
            tasklist.setUserid(userid);
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
            if (req.cookie("userid") != null) {

                // if log in info is available, make lists visible to the owner and collabrator
                model.put("userid", req.cookie("userid"));
                List<TaskList> tasklists = getTaskListRMLiteDao().queryForEq("userid", req.cookie("userid"));
                List<WorksOn> worksons = getWorksOnORMLiteDao().queryForEq("collabratorid", req.cookie("userid"));
                for(int i=0;i< worksons.size();i++){
                    String listid = worksons.get(i).getListid();
                    List<TaskList> cur = getTaskListRMLiteDao().queryForEq("listName", listid);
                    if(cur.size()!=0)
                        tasklists.addAll(cur);
                }
                model.put("lists", tasklists);
            }
            else {
                List<TaskList> tasklists = getTaskListRMLiteDao().queryForEq("userid", "");
                model.put("lists", tasklists);
            }

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
