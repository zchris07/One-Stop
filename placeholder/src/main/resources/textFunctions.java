import com.j256.ormlite.stmt.query.In;
import org.json.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.HashSet;
import java.util.HashMap;

public class textFunctions {
    private String[] punctuations = new String[]{",",":",";","-","[","]","{","}","(",")","'","\"","."};
    //for a input string, we want to find all the missing punctuations,or fix the
    // incorrect punctuations, based on the length of the sentence/ capital letters?
    //context of the sentence.
    private String myKey = "b9f66eec-4cb6-4f05-92b6-e074b0c30ec5";

    private ArrayList<Integer> getPositionOfCapital(String prob) {
        String [] sList = prob.split(" ");
        ArrayList <Integer> posList = new ArrayList<>();
        for (int j = 1; j < sList.length;j++) {
            if (Character.isUpperCase(sList[j].charAt(0))){
                if (sList[j-1].charAt(sList.length-1)=='.'){
                    posList.add(j);
                }
            }
        }
        return posList;
    }


    /*private ArrayList<String> removeNearByCapital(String prob){
        String [] sList = prob.split(" ");
        ArrayList<String> listCopy = new ArrayList<String>(Arrays.asList(sList));
        Iterator<String> it_list = listCopy.iterator();
        while(it_list.hasNext()){
            for (String punc: punctuations){
                if (it_list.toString().contains(punc)) {
                    listCopy.remove(it_list.toString());
                    if (it_list.hasNext()) {
                        listCopy.remove(it_list.next());
                    }
                    break;
                }
            }
        }
        return listCopy;
    }*/
    private String fixSpellingIssues(String prob){
        String[] list_split = prob.split(" ");
        ArrayList<String> reformed_string = new ArrayList<>();
        for (int j=0; j< list_split.length;j++) {
            String urlStr = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
                    +list_split[j]+"?key="+myKey;

            try {
                URL obj = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int response = con.getResponseCode();
                if (response== 200) {
                    Scanner responseReader = new Scanner(con.getInputStream());
                    StringBuffer buffer = new StringBuffer();
                    while (responseReader.hasNextLine()) {
                        buffer.append(responseReader.nextLine()+"\n");
                    }
                    responseReader.close();
                    String jsonString = buffer.toString() ; //assign your JSON String here
                    JSONArray jsonArr = new JSONArray(jsonString);
                    HashSet<String> pronoun = new HashSet<>();

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        String fl = jsonArr.getJSONObject(i).getString("fl");
                        pronoun.add(fl);
                    }

                    if (!(pronoun.isEmpty())) {
                        reformed_string.add(list_split[j]);
                    }
                    else{
                        String[] possible_matches = jsonString.split(",");
                        if (!(possible_matches.length==0)){
                            reformed_string.add(possible_matches[0]);
                        } else{
                            break;
                        }

                    }
                }

            } catch (MalformedURLException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reformed_string.toString();
    }

    private String fixMissingPunctuations(String prob){
        String[] list_split = prob.split(" ");
        ArrayList<Integer> Cap_position = getPositionOfCapital(prob);
        ArrayList<String> reformed_string = new ArrayList<>();
        for (int j=0; j<list_split.length;j++){
            //when we find a upper case letter, that is NOT contained
            //after a fullstop. we then check if that word is a noun
            // with some error handling
            if (Character.isUpperCase(list_split[j].charAt(0))) {
                String urlStr = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
                        +list_split[j]+"?key="+myKey;

                try {
                    URL obj = new URL(urlStr);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int response = con.getResponseCode();
                    if (response== 200) {
                        Scanner responseReader = new Scanner(con.getInputStream());
                        StringBuffer buffer = new StringBuffer();
                        while (responseReader.hasNextLine()) {
                            buffer.append(responseReader.nextLine()+"\n");
                        }
                        responseReader.close();
                        String jsonString = buffer.toString() ; //assign your JSON String here
                        JSONArray jsonArr = new JSONArray(jsonString);
                        HashSet<String> pronoun = new HashSet<>();

                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            String fl = jsonArr.getJSONObject(i).getString("fl");
                            pronoun.add(fl);
                        }

                        if (pronoun.contains("noun")) {
                            reformed_string.add(list_split[j]);
                        }
                        //when the def doesn't contain a noun, we want to change the
                        //uppercase to lowercase.
                        else{
                            if (!Cap_position.contains(j)){
                                list_split[j]= list_split[j].replace(list_split[j].charAt(0),
                                        Character.toLowerCase(list_split[j].charAt(0)));
                            }
                            else{
                                reformed_string.add(list_split[j]);
                            }
                        }
                    }

                } catch (MalformedURLException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            else {
                reformed_string.add(list_split[j]);
            }
        }
    return reformed_string.toString();
    }

    private void detectTasks(String prob) {
        if (prob.contains("Task:")){
            String[] list_split = prob.split("Task:");
            for (int i = 1; i < list_split.length; i++) {
                if (!((list_split[i].contains("."))||(list_split[i].contains(",")))){
                    //send list_split[i] to a task. we scan for the fields of the tasks
                    //like duedates, etc. The following pattern can be applied to other fields
                    String due_dates;
                    if (list_split[i].contains("due dates")){
                        String[] find_Date = list_split[i].split("due dates:");
                        if (find_Date[1].matches("(.*)[./,](.*)")){
                            due_dates = find_Date[1].split("[./,]")[1];
                        } else{
                            due_dates = find_Date[1];
                        }
                    }
                    else {
                        due_dates = null;
                    }
                }
            }
        }
    }



    //for a input string, we want to make all passive sentence into active sentence
    //this can be done by finding the verb, noun and locating the "was (verb)" or "were (verb)"
    //, saving this task for future
    /*public String fixPassive(String prob) {
        return prob;

    }*/

}
