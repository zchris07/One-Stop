import com.google.gson.Gson;
import model.*;
import services.SrvcPItem;
import services.SrvcPList;
import services.SrvcUser;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);

        SrvcPList srvList = new SrvcPList();
        SrvcPItem srvItem = new SrvcPItem();
        SrvcUser srvUser = new SrvcUser();

        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));
                // TODO: password authentication
            }
            model.put("theme", req.cookie("theme"));
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

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
            model.put("plists", ls);
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/plists", (req, res) -> {
            PList lst = new PList();
            srvList.getPListORMLiteDao().create(lst);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(lst);
        });

        Spark.get("/pitems", (req, res) -> {
            List<PItem> lsItem = srvItem.getPItemORMLiteDao().queryForAll();

            // TODO add list functions

//            if()
//                List<Duedate> lsDuedate = srvItem.getDuedateORMLiteDao().queryForAll();
//            if()
//                List<Calendar> lsCalendar = srvItem.getCalendarORMLiteDao().queryForAll();
//            if()
//                List<Notes> lsNotes = srvItem.getNotesORMLiteDao().queryForAll();
//            if()
//                List<TaskManager> lsTaskManager = srvItem.getTaskManagerORMLiteDao().queryForAll();

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("pitems", lsItem);
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/pitems", (req, res) -> {

            // TODO add function contents into items

//            if()
//                List<Duedate> lsDuedate = srvItem.getDuedateORMLiteDao().queryForAll();
//            if()
//                List<Calendar> lsCalendar = srvItem.getCalendarORMLiteDao().queryForAll();
//            if()
//                List<Notes> lsNotes = srvItem.getNotesORMLiteDao().queryForAll();
//            if()
//                List<TaskManager> lsTaskManager = srvItem.getTaskManagerORMLiteDao().queryForAll();

            String listid = req.queryParams("listid");
            String contentshown = req.queryParams("contentshown");
            PItem itm = new PItem(Integer.parseInt(listid));
            srvItem.getPItemORMLiteDao().create(itm);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(itm);
        });

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

        Spark.get("/userprofile", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null)
                model.put("username", req.cookie("username"));
            model.put("theme", req.cookie("theme"));
            return new ModelAndView(model, "public/userprofile.vm");
        }, new VelocityTemplateEngine());

    }
}
