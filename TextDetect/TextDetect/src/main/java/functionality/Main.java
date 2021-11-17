package functionality;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class Main {
    static int PORT = 7000;

    private static int getPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            PORT = Integer.parseInt(herokuPort);
        }
        return PORT;
    }

    public static void main(String[] args) {

//        Spark.port(getPort());
        Spark.port(PORT);

        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/main.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/detect", (req, res) -> {
            String url;
            if (req.cookie("url") != null) {
                url = req.cookie("url");
            }
            else
            {
                url = req.queryParams("url");
            }
            return DetectTextGcs.detectTextGcs(url);
//            return null;
        });


        Spark.post("/saveurl", (req, res) -> {
            String url = req.queryParams("url");
            res.cookie("url", url);
            return "";
        });

    }

}
