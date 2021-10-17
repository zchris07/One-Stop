import com.google.gson.Gson;
import model.*;
import services.SrvcPItem;
import services.SrvcPList;
import services.SrvcUser;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.*;

public class Server {

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);

        SrvcPList srvList = new SrvcPList();
        SrvcPItem srvItem = new SrvcPItem();
        SrvcUser srvUser = new SrvcUser();

        // TODO: make sure compatibility
        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));
                // TODO: password authentication
            }
            model.put("theme", req.cookie("theme"));
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        // TODO: make sure compatibility
        Spark.post("/", (req, res) -> {
            String username = req.queryParams("username");
            String color = req.queryParams("theme");
            res.cookie("username", username);
            res.cookie("theme", color);
            res.redirect("/");
            return null;
        });

        Spark.get("/plists", (req, res) -> {
            List<PList> ls = srvList.getPListORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            // TODO: make sure front-end content, modify model put
            model.put("plists", ls);
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/plists", (req, res) -> {

            PList lst = new PList();
            srvList.getPListORMLiteDao().create(lst);

            Functionality fnc = new Functionality(lst.getId());

            // TODO: Get user input from front-end
            if(Boolean.parseBoolean(req.queryParams("hasdate"))) {
                fnc.setHasdate(true);
            }
            if(Boolean.parseBoolean(req.queryParams("hasnotes"))) {
                fnc.setHasnotes(true);
            }
//            if(Boolean.parseBoolean(req.queryParams("hascalendar"))) {
//                fnc.setHascalendar(true);
//            }

            res.status(201);
            res.type("application/json");

            return new Gson().toJson(lst);
        });

        Spark.get("/pitems", (req, res) -> {

            // TODO: Get user input from front-end
            Integer listid = Integer.parseInt(req.queryParams("listid"));

            List<PItem> lsItem = srvItem.getPItemORMLiteDao().queryForAll();
            List<Functionality> lsFunctionality = srvList.getFunctionalityORMLiteDao().queryForEq("listid", listid);

            // TODO add list functions
            if(lsFunctionality.get(0).getHasdate()) {
                List<Duedate> lsDuedate = srvItem.getDuedateORMLiteDao().queryForEq("listid", listid);
            }
            if(lsFunctionality.get(0).getHasnotes()) {
                List<Notes> lsNotes = srvItem.getNotesORMLiteDao().queryForEq("listid", listid);
            }
//            if(lsFunctionality.get(0).getHascalendar()) {
//                List<Calendar> lsCalendar = srvItem.getCalendarORMLiteDao().queryForAll();
//            }
//            if()
//                List<TaskManager> lsTaskManager = srvItem.getTaskManagerORMLiteDao().queryForAll();

            Map<String, Object> model = new HashMap<String, Object>();
            // TODO: make sure front-end content, modify model put
            model.put("pitems", lsItem);
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/pitems", (req, res) -> {

            // TODO: Get user input from front-end
            String listid = req.queryParams("listid");
            String contentshown = req.queryParams("contentshown");

            // Get functionalities of the list
            List<Functionality> lsFunctionality = srvList.getFunctionalityORMLiteDao().queryForEq("listid", listid);
            List<Notes> lsNotes = srvItem.getNotesORMLiteDao().queryForEq("listid", listid);
//            List<Calendar> lsCalendar = srvItem.getCalendarORMLiteDao().queryForEq("listid", listid);

            // create the item entry in the database
            PItem itm = new PItem(Integer.parseInt(listid));
            srvItem.getPItemORMLiteDao().create(itm);

            // TODO add function contents into items
            if(lsFunctionality.get(0).getHasdate()) {

                Duedate ddt = new Duedate(itm.getId());

                SimpleDateFormat SDFormat = new SimpleDateFormat("yyyy-MM-dd");

                String duedate = req.queryParams("duedate");
                Date duedate_ = SDFormat.parse(duedate);
                ddt.setDuedate(duedate_);

                if(req.queryParams("startdate")!=null){
                    String startdate = req.queryParams("startdate");
                    Date startdate_ = SDFormat.parse(startdate);
                    ddt.setStartdate(startdate_);
                }
                // TODO: ask front end if needed
//                if(req.queryParams("duetime")!=null){
//                    String duetime = req.queryParams("duetime");
//                }
//                if(req.queryParams("starttime")!=null){
//                    String starttime = req.queryParams("starttime");
//                }

            }
            if(lsFunctionality.get(0).getHasnotes()){
                String notecontent = req.queryParams("notecontent");
                Notes nts = new Notes(itm.getId(), notecontent);
                srvItem.getNotesORMLiteDao().create(nts);
            }
//            if(lsCalendar.get(0).get) {
//
//            }

            res.status(201);
            res.type("application/json");

            return new Gson().toJson(itm);
        });

        // TODO: ask for front end specifics
        Spark.get("/userprofile", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null)
                model.put("username", req.cookie("username"));
            model.put("theme", req.cookie("theme"));
            return new ModelAndView(model, "public/userprofile.vm");
        }, new VelocityTemplateEngine());

//        Spark.post("/addlist", (req, res) -> {
//            String username = req.queryParams("username");
//            String color = req.queryParams("theme");
//            res.cookie("username", username);
//            res.cookie("color", color);
//            res.redirect("/");
//            return null;
//        });
//
//        Spark.post("/additem", (req, res) -> {
//            String username = req.queryParams("listid");
//            String color = req.queryParams("theme");
//            res.cookie("username", username);
//            res.cookie("theme", color);
//            res.redirect("/");
//            return null;
//        });
    }
}
