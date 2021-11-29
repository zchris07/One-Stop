package Controllers;

import Functionality.textFunctions;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import kotlin.Pair;
import model.*;
import org.apache.commons.lang.ObjectUtils;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import Functionality.scheduleFunctions;


import static Controllers.DaoConstructor.*;
import static Controllers.UpdateController.*;


public class APIEndpoint {
    static Availability this_available = new Availability();
    public static void rootGet(){

        Spark.get("/", (req, res) -> {
            res.redirect("/login");
            return null;
    });}

    public static void signupGet(){
        Spark.get("/signup", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/signup.vm");
        }, new VelocityTemplateEngine());
    }
    public static void signupPost(){
        Spark.post("/signup", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");


            Dao userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", email);
            System.out.println(aUser);
            if (!aUser.isEmpty()) {
                System.out.println("here");
                res.redirect("/accountexist");
            } else {
                // password secure hash
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(password.getBytes());
                String hashedPassword = new String(messageDigest.digest());
                User ur = new User(email, hashedPassword);
                userDao.create(ur);
                res.cookie("userid", email);
                res.status(201);
                res.redirect("/main");
            }
            return null;
        });
    }
    public static void accExist() {
        Spark.get("/accountexist", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/existErr.vm");
        }, new VelocityTemplateEngine());
    }
    public static void resetGet() {
        Spark.get("/resetPassword", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/resetPsw.vm");
        }, new VelocityTemplateEngine());
    }
    public static void resetPost(){
        Spark.post("/resetPassword", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            Dao userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", email);
            if (aUser != null) {
                updatePassword(email, password, userDao);
                res.cookie("userid", email);
                res.status(201);
                res.redirect("/main");
                return null;
            } else {
                res.status(201);
                res.redirect("/warns");
                return null;
            }
        });
    }
    public static void loginGet(){
        Spark.get("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/login.vm");
        }, new VelocityTemplateEngine());
    }
    public static void loginPost(){
        Spark.post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            // password authentication
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hashedPassword = new String(messageDigest.digest());

            List<User> lstur = getUserORMLiteDao().queryForEq("email", email);
            if (lstur != null && !lstur.isEmpty()) {
                if (lstur.get(0).getHashedPassword().equals(hashedPassword)) {
                    // do something login related
                    res.cookie("userid", email);
                    res.redirect("/main");
                } else {
                    // warn
                    res.redirect("/nonexistpsw");
                }
            } else {
                // warn
                res.redirect("/nonexistacc");
            }

            return null;
        });

    }
    public static void nonexistGet() {
        Spark.get("/nonexistacc", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/nonexist.vm");
        }, new VelocityTemplateEngine());
    }
    public static void nonexistPswGet() {
        Spark.get("/nonexistpsw", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/nonexistpsw.vm");
        }, new VelocityTemplateEngine());
    }
    public static void userprofileGet(){
        Spark.get("/userprofile", (req, res) -> {
            //TODO: LY - now the email is used as userid, fix that in the future.

            String userid;
            // if (req.cookie("userid") != null) {}
            userid = req.cookie("userid");
            Dao<User, Integer> userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", userid);
            Map<String, Object> model = new HashMap<>();
            model.put("aUser", aUser);
            System.out.println(aUser.get(0).getProfileImage());
            if (aUser.get(0).getProfileImage() == null) {
                model.put("imageUrl", "https://i.imgur.com/hepj9ZS.png");
            } else {
                model.put("imageUrl", aUser.get(0).getProfileImage());
            }
            return new ModelAndView(model, "public/profile.vm");
        }, new VelocityTemplateEngine());
    }
    public static void userprofilePut(){
        Spark.put("/userprofile", (req, res) -> {
            //TODO: LY - now the email is used as userid, fix that in the future.
            String useremail;
            useremail = req.cookie("userid");

            String firstname = req.queryParams("firstName");
            String lastname = req.queryParams("lastName");
            String organization = req.queryParams("organization");
            String summary = req.queryParams("summary");
            String status = req.queryParams("status");
            String image = req.queryParams("profileImage");

            Dao<User, Integer> userDao = getUserORMLiteDao();
            updateUser(useremail, firstname, lastname, organization, status, summary, image, userDao);
            res.status(201);
            res.type("application/json");
            List<User> aUser = userDao.queryForEq("email", useremail);
            return aUser.get(0).toJsonString();
        });

    }
    public static void showlistGet(){
        Spark.get("/showList", (req, res) -> {
            String userid;
            if (req.cookie("userid") != null) {
                userid = req.cookie("userid");
            } else {
                userid = "";
            }
            Dao<TaskList, Integer> emDao = getTaskListRMLiteDao();
            String listId = req.queryParams("listId");
            Integer listIdInt = Integer.parseInt(listId);
            QueryBuilder<TaskList, Integer> builder = emDao.queryBuilder();

            List<TaskList> ems = builder.where().eq("userId", userid).and().eq("listId", listIdInt).query();
            res.type("application/json");
            if (ems.size() == 0) {
                return "";
            }
            return ems.get(0).toJsonString();
        });
    }
    public static void addlistPost(){
        Spark.post("/addList", (req, res) -> {
            String listName = req.queryParams("listName");
//            String userid = req.queryParams("userid");
            String userid;
            if (req.cookie("userid") != null) {
                userid = req.cookie("userid");
            } else {
                userid = "";
            }
            String colabidString = req.queryParams("colabidstring");

            // handle Collaborator
            String[] colabidStringArr = colabidString.split(";");
            Dao worksOnDao = getWorksOnORMLiteDao();
            for (int i = 0; i < colabidStringArr.length; i++) {
                String colabid = colabidStringArr[i].trim();
                WorksOn wks = new WorksOn(colabid, listName);
                worksOnDao.create(wks);
            }

            TaskList tasklist = new TaskList(listName);
            tasklist.setUserid(userid);
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            taskDao.create(tasklist);
            List<TaskList> ems = taskDao.queryForEq("listName", listName);
            res.status(201);
            res.type("application/json");
            return ems.get(0).toJsonString();
        });
    }
    public static void deletelist(){
        Spark.delete("/deleteList", (req, res) -> {
            String listId = req.queryParams("listId");
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            List<TaskList> ems = taskDao.queryForEq("listId", listId);
            taskDao.delete(ems.get(0));
            res.status(204);
            return "";
        });
    }
    public static void addtaskPost(){
        Spark.post("/addTask", (req, res) -> {
            String listId = req.queryParams("listId");
            String taskName = req.queryParams("taskName");
            String dueDay = req.queryParams("dueDay");
            String date_string = req.queryParams("date");
            Double duration = Double.parseDouble(req.queryParams("duration"));
            Double importance = Double.parseDouble(req.queryParams("importance"));
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);

            Date date = formatter.parse(date_string);
            Date dueDay_date = formatter.parse(dueDay);
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            List<TaskList> ems = taskDao.queryForEq("listId", listId);
    /*ems.get(0).addTask(taskName, dueDay_date, date, duration,
            taskDao);*/
    /*scheduleFunctions temp = new scheduleFunctions();
    if (null.equals(this_available)) {
        this_available = new Availability();
    }*/
            scheduleFunctions temp = new scheduleFunctions();
            Dao userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            Pair<TaskList, Availability> new_avail = temp.scheduleOne(ems.get(0),date,dueDay_date,
                    taskName,duration,importance, aUser.get(0),taskDao);
            res.status(201);
            res.type("application/json");
            this_available.setThisMap(new_avail.component2().getThisMap());


            List<TaskList> ems2 = taskDao.queryForEq("listId", listId);


            /*return new_avail.component1().toJsonString();*/
            return ems2.get(0).toJsonString();
        });/*
            ems.get(0).addTask(taskName, dueDay_date, date, taskDao);
            res.status(201);
            res.type("application/json");
            List<TaskList> ems2 = taskDao.queryForEq("listId", listId);

            return ems2.get(0).toJsonString();
        });*/
    }
    public static void deleteTask(){
        Spark.delete("/deleteTask", (req, res) -> {
            String listId = req.queryParams("listId");
            String taskName = req.queryParams("taskName");
            Dao<TaskList, Integer> taskDao = getTaskListRMLiteDao();
            List<TaskList> ems = taskDao.queryForEq("listId", listId);
            /*ems.get(0).delTask(taskName, taskDao);*/
            scheduleFunctions temp = new scheduleFunctions();
            TaskList.Task this_task = ems.get(0).getTask(taskName,taskDao);
            System.out.print(this_task.getTaskName());
            Pair<TaskList, Availability> new_avail = temp.addBackTask(ems.get(0),this_task.getDate()
                    ,this_task.getDueDay(), taskName,this_task.getDuration(),this_available,taskDao);
            res.status(201);
            res.type("application/json");
            this_available.setThisMap(new_avail.component2().getThisMap());
            List<TaskList> ems2 = taskDao.queryForEq("listId", listId);
            return ems2.get(0).toJsonString();
        });
    }
    public static void scheduleGet(){
        Spark.get("/schedule", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Dao userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            model.put("imageUrl", aUser.get(0).getProfileImage());
            return new ModelAndView(model, "public/schedule.vm");
        }, new VelocityTemplateEngine());
    }
    public static void schedulePut(){
        Spark.put("/schedule", (req, res) -> {
            Schedule schedule = new Schedule();
            String userid;
            if (req.cookie("userid") != null) {
                userid = req.cookie("userid");
            } else {
                userid = "";
            }
            Dao<TaskList, Integer> emDao = getTaskListRMLiteDao();
            QueryBuilder<TaskList, Integer> builder = emDao.queryBuilder();

            List<TaskList> ems = builder.where().eq("userId", userid).query();
//            res.type("application/json");
            return schedule.getAllTaskDate(userid);
        });
    }
    public static void showDetailGet(){
        Spark.get("/showDetail", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String taskName = req.queryParams("taskName");
            Dao<TaskNote, Integer> noteDao = getTaskNoteRMLiteDao();
            List<TaskNote> ems = noteDao.queryForEq("taskName", taskName);
            String notes = "";
            if (ems.size() != 0) {
                notes = ems.get(0).toString();
            }
            model.put("taskName", taskName);
            model.put("notes", notes);
            return new ModelAndView(model, "public/detail.vm");
        }, new VelocityTemplateEngine());
    }
    public static void showDetailPut(){
        Spark.put("/addNotes", (req, res) -> {
            String taskName = req.queryParams("taskName");
            String taskNote = req.queryParams("taskNote");
            String isCheckedGrammar = req.queryParams("isCheckedGrammar");
            //System.out.println(isCheckedGrammar);
            String isCheckedSpelling = req.queryParams("isCheckedSpelling");
            //System.out.println(isCheckedSpelling);
            String isCheckedCapital = req.queryParams("isCheckedCapital");
            //System.out.println(isCheckedCapital);
            String isCheckedLongRunning = req.queryParams("isCheckedLongRunning");


            Dao<TaskNote, Integer> noteDao = getTaskNoteRMLiteDao();
            updateNote(taskName, taskNote, isCheckedGrammar, isCheckedSpelling, isCheckedCapital, isCheckedLongRunning, noteDao);
            res.status(201);
            res.type("application/json");
            List<TaskNote> ems2 = noteDao.queryForEq("taskName", taskName);
            return ems2.get(0).toJsonString();
        });
    }
    public static void main(){
        Spark.get("/main", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("userid") != null) {

                // if log in info is available, make lists visible to the owner and collabrator
                model.put("userid", req.cookie("userid"));
                List<TaskList> tasklists = getTaskListRMLiteDao().queryForEq("userid", req.cookie("userid"));
                List<WorksOn> worksons = getWorksOnORMLiteDao().queryForEq("collabratorid", req.cookie("userid"));
                for (int i = 0; i < worksons.size(); i++) {
                    String listid = worksons.get(i).getListid();
                    List<TaskList> cur = getTaskListRMLiteDao().queryForEq("listName", listid);
                    if (cur.size() != 0)
                        tasklists.addAll(cur);
                }
                model.put("lists", tasklists);
            } else {
                List<TaskList> tasklists = getTaskListRMLiteDao().queryForEq("userid", "");
                model.put("lists", tasklists);
            }

//            for profile image
            Dao userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            model.put("imageUrl", aUser.get(0).getProfileImage());

            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());
    }
}
