package Functionality;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Detectron {

    // Check if the credential file is in the file system, if not, create it
    public static void creatCredentialFile() {
        try {
            // TODO: change path to deployed host
            File myObj = new File("/tmp/googlecredential.json");
            //File myObj = new File("./googlecredential.json");

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

    // Write the credential file from the environment variable
    // Make sure the content is the json file, excluding the right bracket.
    public static void writeToCredential() {
        creatCredentialFile();
        try {
            // TODO: change path to deployed host
            FileWriter myWriter = new FileWriter("/tmp/googlecredential.json");
//            FileWriter myWriter = new FileWriter("./googlecredential.json");
            String jsonString = System.getenv("GOOGLE_CREDENTIALS") + "}";
            JsonParser parser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonElement el = JsonParser.parseString(jsonString);
            jsonString = gson.toJson(el);
            myWriter.write(jsonString );
            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Test logged API json content
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


}
