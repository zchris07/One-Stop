package Controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import kotlin.Pair;
import model.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

import Functionality.*;


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
    public static void signupPost(Dao userDao){
        Spark.post("/signup", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            List<User> aUser = userDao.queryForEq("email", email);
            System.out.println(aUser);
            if (!aUser.isEmpty()) {
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
    public static void resetPost(Dao userDao){
        Spark.post("/resetPassword", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            List<User> aUser = userDao.queryForEq("email", email);
            if (aUser != null) {
                UpdateController.updatePassword(email, password, userDao);
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
    public static void loginPost(Dao userDao){
        Spark.post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            // password authentication
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hashedPassword = new String(messageDigest.digest());

            List<User> lstur = userDao.queryForEq("email", email);
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
    public static void userprofileGet(Dao userDao){
        Spark.get("/userprofile", (req, res) -> {
            //TODO: LY - now the email is used as userid, fix that in the future.

            String userid;
            // if (req.cookie("userid") != null) {}
            userid = req.cookie("userid");
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
    public static void userprofilePut(Dao userDao){
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

            UpdateController.updateUser(useremail, firstname, lastname, organization, status, summary, image, userDao);
            res.status(201);
            res.type("application/json");
            List<User> aUser = userDao.queryForEq("email", useremail);
            return aUser.get(0).toJsonString();
        });

    }
    public static void showlistGet(Dao tasklistDao){
        Spark.get("/showList", (req, res) -> {
            String userid;
            if (req.cookie("userid") != null) {
                userid = req.cookie("userid");
            } else {
                userid = "";
            }
            String listId = req.queryParams("listId");
            Integer listIdInt = Integer.parseInt(listId);
            QueryBuilder<TaskList, Integer> builder = tasklistDao.queryBuilder();

            List<TaskList> ems = builder.where().eq("listId", listIdInt).query();
            res.type("application/json");
            if (ems.size() == 0) {
                return "";
            }
            return ems.get(0).toJsonString();
        });
    }
    public static void addlistPost(Dao worksonDao, Dao tasklistDao){
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
            for (int i = 0; i < colabidStringArr.length; i++) {
                String colabid = colabidStringArr[i].trim();
                WorksOn wks = new WorksOn(colabid, listName);
                worksonDao.create(wks);
            }

            TaskList tasklist = new TaskList(listName);
            tasklist.setUserid(userid);
            tasklistDao.create(tasklist);
            List<TaskList> ems = tasklistDao.queryForEq("listName", listName);
            res.status(201);
            res.type("application/json");
            return ems.get(0).toJsonString();
        });
    }
    public static void deletelist(Dao tasklistDao){
        Spark.delete("/deleteList", (req, res) -> {
            String listId = req.queryParams("listId");
            List<TaskList> ems = tasklistDao.queryForEq("listId", listId);
            tasklistDao.delete(ems.get(0));
            res.status(204);
            return "";
        });
    }
    public static void addtaskPost(Dao tasklistDao, Dao userDao){
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
            List<TaskList> ems = tasklistDao.queryForEq("listId", listId);
    /*ems.get(0).addTask(taskName, dueDay_date, date, duration,
            taskDao);*/
    /*scheduleFunctions temp = new scheduleFunctions();
    if (null.equals(this_available)) {
        this_available = new Availability();
    }*/
            scheduleFunctions temp = new scheduleFunctions();
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            Pair<TaskList, Availability> new_avail = temp.scheduleOne(ems.get(0),date,dueDay_date,
                    taskName,duration,importance, aUser.get(0), tasklistDao);
            res.status(201);
            res.type("application/json");
            User.setThisMap(new_avail.component2().getThisMap(),userDao);


            List<TaskList> ems2 = tasklistDao.queryForEq("listId", listId);


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
    public static void deleteTask(Dao tasklistDao, Dao userDao){
        Spark.delete("/deleteTask", (req, res) -> {
            String listId = req.queryParams("listId");
            String taskName = req.queryParams("taskName");
            List<TaskList> ems = tasklistDao.queryForEq("listId", listId);
            /*ems.get(0).delTask(taskName, taskDao);*/
            scheduleFunctions temp = new scheduleFunctions();
            TaskList.Task this_task = ems.get(0).getTask(taskName,tasklistDao);
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            Pair<TaskList, Availability> new_avail = temp.addBackTask(ems.get(0),this_task.getDate()
                    ,this_task.getDueDay(), taskName,this_task.getDuration(),aUser.get(0),
                    this_task.getExactStart(),this_task.getExactEnd(),
                    tasklistDao);
            res.status(201);
            res.type("application/json");
            User.setThisMap(new_avail.component2().getThisMap(),userDao);
            List<TaskList> ems2 = tasklistDao.queryForEq("listId", listId);
            return ems2.get(0).toJsonString();
        });
    }
    public static void scheduleGet(Dao userDao){
        Spark.get("/schedule", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            model.put("imageUrl", aUser.get(0).getProfileImage());
            return new ModelAndView(model, "public/schedule.vm");
        }, new VelocityTemplateEngine());
    }
    public static void schedulePut(Dao tasklistDao){
        Spark.put("/schedule", (req, res) -> {
            Schedule schedule = new Schedule();
            String userid;
            if (req.cookie("userid") != null) {
                userid = req.cookie("userid");
            } else {
                userid = "";
            }
            QueryBuilder<TaskList, Integer> builder = tasklistDao.queryBuilder();
            List<TaskList> ems = builder.where().eq("userId", userid).query();
//            res.type("application/json");
            return schedule.getAllTaskDate(userid, tasklistDao);
        });
    }
    public static void showDetailGet(Dao tasknoteDao){
        Spark.get("/showDetail", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String taskName = req.queryParams("taskName");
            List<TaskNote> ems = tasknoteDao.queryForEq("taskName", taskName);
            String notes = "";
            if (ems.size() != 0) {
                notes = ems.get(0).toString();
            }
            model.put("taskName", taskName);
            model.put("notes", notes);
            return new ModelAndView(model, "public/detail.vm");
        }, new VelocityTemplateEngine());
    }
    public static void showDetailPut(Dao tasknoteDao){
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

            UpdateController.updateNote(taskName, taskNote, isCheckedGrammar, isCheckedSpelling, isCheckedCapital, isCheckedLongRunning, tasknoteDao);
            res.status(201);
            res.type("application/json");
            List<TaskNote> ems2 = tasknoteDao.queryForEq("taskName", taskName);
            return ems2.get(0).toJsonString();
        });
    }
    public static void main(Dao tasklistDao, Dao worksonDao, Dao userDao){
        Spark.get("/main", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("userid") != null) {

                // if log in info is available, make lists visible to the owner and collabrator
                model.put("userid", req.cookie("userid"));
                List<TaskList> tasklists = tasklistDao.queryForEq("userid", req.cookie("userid"));
                List<WorksOn> worksons = worksonDao.queryForEq("collabratorid", req.cookie("userid"));
                for (int i = 0; i < worksons.size(); i++) {
                    String listid = worksons.get(i).getListid();
                    List<TaskList> cur = tasklistDao.queryForEq("listName", listid);
                    if (cur.size() != 0)
                        tasklists.addAll(cur);
                }
                model.put("lists", tasklists);
            } else {
                List<TaskList> tasklists = tasklistDao.queryForEq("userid", "");
                model.put("lists", tasklists);
            }

//            for profile image
            List<User> aUser = userDao.queryForEq("email", req.cookie("userid"));
            model.put("imageUrl", aUser.get(0).getProfileImage());

            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());
    }

    public static void updateDate(Dao tasklistDao){
        Spark.put("/editDate", (req, res) -> {
            String listId = req.queryParams("listId");
            String taskName = req.queryParams("taskName");
            String editDate = req.queryParams("editeddueDay");
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            System.out.println(editDate);
            Date editDateFormatted = formatter.parse(editDate);
            List<TaskList> ems = tasklistDao.queryForEq("listId", listId);
            ems.get(0).updateTaskDate(taskName,tasklistDao,editDateFormatted);
            res.status(201);
            res.type("application/json");
            return "";
        });
    }
}
