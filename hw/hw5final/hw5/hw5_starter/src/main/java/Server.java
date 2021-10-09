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
import java.text.SimpleDateFormat;
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

    private static Employer getEmployer(String emp,List<Employer> emlst ) {
        for (int i =0; i<emlst.size(); i++) {
            Employer emptemp = emlst.get(i);
            String nmtemp = emptemp.getName();
            if (nmtemp.equals(emp)) {
                return emptemp;
            }
        }
        return null;
    }

    public static void main(String[] args) {

        final int PORT_NUM = 7000;
        Spark.port(PORT_NUM);



        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null)
                model.put("username", req.cookie("username"));
                model.put("color", req.cookie("color"));
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/", (req, res) -> {
            String username = req.queryParams("username");
            String color = req.queryParams("color");
            res.cookie("username", username);
            res.cookie("color", color);
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
            return new Gson().toJson(em);
        });

        Spark.get("/addemployers", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "public/addemployers.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/jobs", (req, res) -> {
            List<Job> ls = getJobORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("jobs", ls);
            return new ModelAndView(model, "public/jobs.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/jobs", (req, res) -> {
            String title = req.queryParams("title");
            System.out.println("debug");
            SimpleDateFormat SDFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datePosted = SDFormat.parse(req.queryParams("dateposted"));
            Date deadline = SDFormat.parse(req.queryParams("deadline"));
//            Date datePosted = SimpleDateFormat.getDateInstance().parse(req.queryParams("dateposted"));
//            Date deadline = SimpleDateFormat.getDateInstance().parse(req.queryParams("deadline"));
            String domain = req.queryParams("domain");
            String location = req.queryParams("location");

            boolean fullTime = Boolean.parseBoolean(req.queryParams("fulltime"));
            boolean salaryBased = Boolean.parseBoolean(req.queryParams("salary"));

            String requirements = req.queryParams("requirements");
            int payAmount = Integer.parseInt(req.queryParams("payments"));

            List<Employer> emlst = getEmployerORMLiteDao().queryForAll();

            Job job = new Job(title, datePosted, deadline, domain, location, fullTime, salaryBased, requirements, payAmount, getEmployer(req.queryParams("employer"), emlst));
            getJobORMLiteDao().create(job);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(job);
        });

        Spark.get("/addjobs", (req, res) -> {
            List<Employer> ls = getEmployerORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("employers", ls);
            return new ModelAndView(model, "public/addjobs.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/search", (req, res) -> {
            List<Job> ls = getJobORMLiteDao().queryForAll();
            ArrayList<Job> searched = new ArrayList<Job>();
            String thekey = req.queryParams("key");
            for (int i = 0; i < ls.size(); i++) {
                Job eachJob = ls.get(i);
                String title = eachJob.getTitle();
                String name = eachJob.getEmployer().getName();
                String domain = eachJob.getDomain();
                if (title.toLowerCase().contains(thekey.toLowerCase()) ||
                        name.toLowerCase().contains(thekey.toLowerCase()) ||
                        domain.toLowerCase().contains(thekey.toLowerCase())) {
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
