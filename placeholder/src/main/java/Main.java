import Functionality.textFunctions;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
//import model.PItem;
import model.*;
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

    private static Dao <TaskNote,Integer> getTaskNoteRMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, TaskNote.class);
//        TableUtils.dropTable(connectionSource, TaskList.class,false);
        return DaoManager.createDao(connectionSource, TaskNote.class);
    }

    private static void updateNote(String taskName, String taskNote, String isCheckedGrammar, String isCheckedSpelling, String isCheckedCapital, String isCheckedLongRunning, Dao<TaskNote, Integer> dao) throws SQLException {

        List<TaskNote> check = dao.queryForEq("taskName", taskName);

        if (check.size() == 0) {
            textFunctions text_functions = new textFunctions();

            if (isCheckedGrammar.equals("Yes")) {
                taskNote = text_functions.fixMissingFullStop(taskNote);
            }
            if (isCheckedSpelling.equals("Yes")) {
                taskNote = text_functions.fixSpellingIssues(taskNote);
            }
            if (isCheckedCapital.equals("Yes")) {
                taskNote = text_functions.fixCapitalLettersInString(taskNote);
            }
            if (isCheckedLongRunning.equals("Yes")) {
                taskNote = text_functions.fixLongRunningSentence(taskNote);
            }
            TaskNote newTaskNote = new TaskNote(taskName, taskNote);

            dao.create(newTaskNote);
        } else {
            textFunctions text_functions = new textFunctions();

            if (isCheckedGrammar.equals("Yes")) {
                taskNote = text_functions.fixMissingFullStop(taskNote);
                System.out.print(taskNote);
            }
            if (isCheckedSpelling.equals("Yes")) {
                taskNote = text_functions.fixSpellingIssues(taskNote);
                System.out.print(taskNote);
            }
            if (isCheckedCapital.equals("Yes")) {
                taskNote = text_functions.fixCapitalLettersInString(taskNote);
                System.out.print(taskNote);
            }

            UpdateBuilder<TaskNote, Integer> builder = dao.updateBuilder();

            builder.updateColumnValue("taskNote", taskNote);
            builder.where().eq("taskName", taskName);
            dao.update(builder.prepare());
        }
    }

    private static void updateUser(String useremail, String firstName, String lastName, String organization, String status,
                                   String summary, Dao<User, Integer> dao) throws SQLException {
        List<User> check = dao.queryForEq("email", useremail);
        UpdateBuilder<User, Integer> builder = dao.updateBuilder();
        if (firstName != "" & firstName != null) {builder.updateColumnValue("firstName", firstName);}
        if (lastName != "" & lastName!= null) {builder.updateColumnValue("lastName", lastName);}
        if (organization != "" & organization != null) {builder.updateColumnValue("organization", organization);}
        if (status != "" & status != null) {builder.updateColumnValue("status", status);}
        if (summary != "" & summary != null) {builder.updateColumnValue("summary", summary);}
        builder.where().eq("email", useremail);
        dao.update(builder.prepare());
    }

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);
        Spark.staticFiles.location("/public");

        Spark.get("/", (req, res) -> {
            res.redirect("/login");
            return null;
        });

        Spark.get("/signup", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/signup.vm");
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
            //TODO: LY - now the email is used as userid, fix that in the future.

            String userid;
            // if (req.cookie("userid") != null) {}
            userid = req.cookie("userid");
            Dao<User, Integer> userDao = getUserORMLiteDao();
            List<User> aUser = userDao.queryForEq("email", userid);
            Map<String, Object> model = new HashMap<>();
            model.put("aUser", aUser);
            return new ModelAndView(model, "public/profile.vm");
        }, new VelocityTemplateEngine());

        Spark.put("/userprofile", (req, res) -> {
            //TODO: LY - now the email is used as userid, fix that in the future.
            String useremail;
            useremail = req.cookie("userid");

            String firstname = req.queryParams("firstName");
            String lastname = req.queryParams("lastName");
            String organization = req.queryParams("organization");
            String summary = req.queryParams("summary");
            String status = req.queryParams("status");

            Dao<User, Integer> userDao = getUserORMLiteDao();
            updateUser(useremail, firstname, lastname, organization, status, summary, userDao);
            res.status(201);
            res.type("application/json");
            List<User> aUser = userDao.queryForEq("email", useremail);
            return aUser.get(0).toJsonString();
        });


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

        Spark.get("/schedule", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/schedule.vm");
        }, new VelocityTemplateEngine());

        Spark.put("/schedule", (req,res) -> {
            Schedule schedule= new Schedule();
            return schedule.getAllTaskDate();
        });

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
            updateNote(taskName, taskNote, isCheckedGrammar,isCheckedSpelling,isCheckedCapital, isCheckedLongRunning,noteDao);
            res.status(201);
            res.type("application/json");
            List<TaskNote> ems2 = noteDao.queryForEq("taskName", taskName);
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



    }
}
