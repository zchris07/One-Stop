//package functionality;
//
//import spark.ModelAndView;
//import spark.Spark;
//import spark.template.velocity.VelocityTemplateEngine;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Main {
////    static int PORT = 7000;
//    static int  PORT = 8081;
//
//    private static int getPort() {
//        String herokuPort = System.getenv("PORT");
//        if (herokuPort != null) {
//            PORT = Integer.parseInt(herokuPort);
//        }
//        return PORT;
//    }
//
//    public static void main(String[] args) {
//
////        Spark.port(getPort());
//        Spark.port(PORT);
//
//        Spark.get("/", (req, res) -> {
//            Map<String, Object> model = new HashMap<>();
//            return new ModelAndView(model, "public/main.vm");
//        }, new VelocityTemplateEngine());
//
//        Spark.post("/detect", (req, res) -> {
//            String url;
//            if (req.cookie("url") != null) {
//                url = req.cookie("url");
//            }
//            else
//            {
//                url = req.queryParams("url");
//            }
//            return DetectTextTess.detect();
////            return null;
//        });
//
//
//        Spark.post("/saveurl", (req, res) -> {
//            String url = req.queryParams("url");
//            res.cookie("url", url);
//            return "";
//        });
//
//    }
//
//}


package functionality;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Scanner;

public class Main {
//    static int PORT = 7000;
    static int PORT = 8081;

    private static int getPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            PORT = Integer.parseInt(herokuPort);
        }
        return PORT;
    }

    private static void creatCredentialFile() {
        try {
            File myObj = new File("/tmp/googlecredential.json");
//            File myObj = new File("./googlecredential.json");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void writeToCredential() {
        creatCredentialFile();
        try {
            FileWriter myWriter = new FileWriter("/tmp/googlecredential.json");
//            FileWriter myWriter = new FileWriter("./googlecredential.json");

            String jsonString = System.getenv("GOOGLE_CREDENTIALS") + "}";
            JsonParser parser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonElement el = JsonParser.parseString(jsonString);
            jsonString = gson.toJson(el); // done
            myWriter.write(jsonString );
            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
//        try {
//            File myObj = new File("/tmp/googlecredential.json");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine();
//                System.out.println(data);
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {

//        Spark.port(getPort());
        Spark.port(PORT);

        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/main.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/detect", (req, res) -> {
            writeToCredential();


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
