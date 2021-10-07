import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Employer;
import model.Job;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.sql.SQLException;
import java.util.*;

public class Server {

    private static Dao getEmployerORMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, Employer.class);
        return DaoManager.createDao(connectionSource, Employer.class);
    }

    private static Dao getJobORMLiteDao() throws SQLException {
        final String URI = "jdbc:sqlite:./JBApp.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, Job.class);
        return DaoManager.createDao(connectionSource, Job.class);
    }

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);



        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null)
                model.put("username", req.cookie("username"));
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/", (req, res) -> {
            String username = req.queryParams("username");
            String color = req.queryParams("color");
            res.cookie("username", username);
            res.redirect("/");
            return null;
        });

        Spark.get("/employers", (req, res) -> {
            List<Employer> ls = getEmployerORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("employers", ls);
            return new ModelAndView(model, "public/employers.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/employers", (req, res) -> {
            String name = req.queryParams("name");
            String sector = req.queryParams("sector");
            String summary = req.queryParams("summary");
            Employer em = new Employer(name, sector, summary);
            getEmployerORMLiteDao().create(em);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(em.toString());
        });

        Spark.get("/addemployers", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "public/addemployers.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/addjobs", (req, res) -> {
            List<Employer> ls = getEmployerORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("employers", ls);
            return new ModelAndView(model, "public/addjobs.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/jobs", (req, res) -> {
            List<Job> ls = getJobORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("jobs", ls);
            return new ModelAndView(model, "public/jobs.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/search", (req, res) -> {
            List<Job> ls = getJobORMLiteDao().queryForAll();
            ArrayList<Job> searched = new ArrayList<Job>();
            String thekey = req.queryParams("key");
            for (int i = 0; i < ls.size(); i++) {
                Job eachJob = ls.get(i);
                String title = eachJob.getTitle();
                String name = eachJob.getEmployer().getName();
                String sector = eachJob.getEmployer().getSector();
                if (title.toLowerCase().contains(thekey.toLowerCase()) ||
                        name.toLowerCase().contains(thekey.toLowerCase()) ||
                        sector.toLowerCase().contains(thekey.toLowerCase())) {
                    System.out.println(name);
                    searched.add(eachJob);
                }
            }
            res.type("application/json");
            return new Gson().toJson(searched);
        });

        Spark.get("/search", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "public/search.vm");
        }, new VelocityTemplateEngine());

    }
}
