package Functionality;

import org.json.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class textFunctions {
    private String[] punctuations = new String[]{",",":",";","-","[","]","{","}","(",")","'","\"","."};
    //for a input string, we want to find all the missing punctuations,or fix the
    // incorrect punctuations, based on the length of the sentence/ capital letters?
    //context of the sentence.
    private String myKey = "b9f66eec-4cb6-4f05-92b6-e074b0c30ec5";
    private String spanish_Key = "9882eac3-e45a-4872-be13-83f31e129ccf";



    private ArrayList<Integer> getPositionOfCapital(String prob) {
        String [] sList = prob.split(" ");
        ArrayList <Integer> posList = new ArrayList<>();
        for (int j = 1; j < sList.length;j++) {
            if (Character.isUpperCase(sList[j].charAt(0))){
                posList.add(j);
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

    public String fixLongRunningSentence(String prob) {
        String[] list_split = prob.split("[.;:,]");

        ArrayList<String> reformed_string = new ArrayList<>();
        ArrayList<HashSet<String>> pronoun = new ArrayList<>();
        ArrayList<String> reformed_string_sentence = new ArrayList<>();
        for (int j = 0; j < list_split.length; j++) {
            if (list_split[j].split(" ").length < 20) {
                reformed_string_sentence.add(list_split[j]);
                continue;
            }
            //when we find a upper case letter, that is NOT contained
            //after a fullstop. we then check if that word is a noun
            // with some error handling
            String[] list_split_space = list_split[j].split(" ");
            int len_counter = 0;
            for (int k = 0; k < list_split_space.length; k++) {
                len_counter++;
                HashSet<String> pronoun_ele = new HashSet<>();

                String urlStr = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
                        + list_split_space[k] + "?key=" + myKey;

                try {
                    URL obj = new URL(urlStr);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int response = con.getResponseCode();
                    if (response == 200) {
                        Scanner responseReader = new Scanner(con.getInputStream());
                        StringBuffer buffer = new StringBuffer();
                        while (responseReader.hasNextLine()) {
                            buffer.append(responseReader.nextLine() + "\n");
                        }
                        responseReader.close();
                        String jsonString = buffer.toString(); //assign your JSON String here
                        JSONArray jsonArr = new JSONArray(jsonString);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            try {
                                //String fl = jsonArr.getJSONObject(i).getString("fl");
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                String id_string = jsonObj.getJSONObject("meta").getString("id").replaceAll("[^a-zA-Z]", "");
                                if (id_string.equalsIgnoreCase(list_split_space[k])) {
                                    String fl = jsonArr.getJSONObject(i).getString("fl");
                                    if (fl.equals("conjunction")) {
                                        pronoun_ele.add(fl);
                                    }
                                }


                            } catch (JSONException e) {
                                continue;
                            }
                        }
                        if (!pronoun_ele.isEmpty()) {
                            if (len_counter > 10) {
                                reformed_string.add("," + list_split_space[k]);
                                len_counter = 0;
                            } else {
                                reformed_string.add(list_split_space[k]);
                            }
                        } else{
                            reformed_string.add(list_split_space[k]);
                        }
                    }

                } catch (MalformedURLException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }


                pronoun.add(pronoun_ele);
            }
            String concat = "";
            for (int i = 0; i < reformed_string.size(); i++) {
                if (i != (reformed_string.size() - 1)) {
                    concat = concat + reformed_string.get(i) + " ";
                } else {
                    concat = concat + reformed_string.get(i);
                }
            }
            reformed_string_sentence.add(concat);
        }
        String concat_sen = "";
        for (int i = 0; i < reformed_string_sentence.size(); i++) {
            if (i != (reformed_string_sentence.size()-1)) {
                concat_sen = concat_sen + reformed_string_sentence.get(i) + ".";
            } else {
                concat_sen = concat_sen + reformed_string_sentence.get(i) + ".";
            }
        }
        return concat_sen;
    }

    public String fixSpellingIssues(String prob){
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
                System.out.print(response);
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
                        try{
                            String fl = jsonArr.getJSONObject(i).getString("fl");
                            pronoun.add(fl);
                        } catch (JSONException e) {
                            break;
                        }

                    }

                    if (!(pronoun.isEmpty())) {
                        reformed_string.add(list_split[j]);
                    }
                    else{
                        String[] possible_matches = jsonString.split(",");
                        if (!(possible_matches.length==0)){
                            int match_length;
                            if (possible_matches.length>5) {
                                match_length = 5;
                            } else {
                                match_length = possible_matches.length -1;
                            }
                            // to increase accuracy of our phrase's occurance, we find if there is a word in front or following
                            //word we are interested and concatenate them.
                            int max_occur = 0;
                            float max_occur_val = 0;
                            float fl = 0;
                            for (int i = 0; i<match_length; i++) {
                                String ngram;
                                if (j>0) { ngram = reformed_string.get(reformed_string.size()-1).replaceAll("[^a-zA-Z0-9]", "") + "%20" +possible_matches[i].replaceAll("[^a-zA-Z0-9]", ""); }
                                else {ngram = possible_matches[i];}
                                String ngramString = "https://books.google.com/ngrams/json?content="+ngram+"&year_start=2000&year_end=2020&corpus=26&smoothing=3";
                                URL ngramObj = new URL(ngramString);
                                HttpURLConnection ngramcon = (HttpURLConnection) ngramObj.openConnection();
                                ngramcon.setRequestMethod("GET");
                                int ngramresponse = ngramcon.getResponseCode();
                                System.out.print(ngramresponse);
                                if (ngramresponse== 200) {
                                    Scanner ngramresponseReader = new Scanner(ngramcon.getInputStream());
                                    StringBuffer ngrambuffer = new StringBuffer();
                                    while (ngramresponseReader.hasNextLine()) {
                                        ngrambuffer.append(ngramresponseReader.nextLine() + "\n");
                                    }
                                    ngramresponseReader.close();
                                    String ngramjsonString = ngrambuffer.toString(); //assign your JSON String here
                                    JSONArray ngramjsonArr = new JSONArray(ngramjsonString);

                                    try {
                                        JSONArray timeseries = ngramjsonArr.getJSONObject(0).getJSONArray("timeseries");
                                        fl = Float.parseFloat(timeseries.optString(0));
                                        if (i == 0) {
                                            max_occur = 0;
                                            max_occur_val = fl;
                                        } else {
                                            if (fl > max_occur_val) {
                                                max_occur = i;
                                                max_occur_val = fl;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        continue;
                                    }

                                }
                            }
                            if (Character.isUpperCase(list_split[j].charAt(0))) {
                                System.out.print(max_occur);
                                String match = possible_matches[max_occur].replaceAll("[^a-zA-Z0-9]","");
                                reformed_string.add(Character.toUpperCase(match.charAt(0)) + match.substring(1));
                            } else {
                                reformed_string.add(possible_matches[max_occur].replaceAll("[^a-zA-Z0-9]", ""));
                            }
                        } else{
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
        String concat="";
        for (int i = 0;i<reformed_string.size(); i++) {
            if (i != (reformed_string.size() - 1)) {
                concat = concat + reformed_string.get(i) + " ";
            } else {
                concat = concat + reformed_string.get(i);
            }
        }
        return concat;
    }

    public String fixCapitalLettersInString(String prob) {
        String[] list_split = prob.split(" ");
        for (int j=0; j< list_split.length;j++) {
            String focus = list_split[j];
            if (focus.length() >1) {
                if (Character.isUpperCase(focus.charAt(0))) {
                    if (j>=1) {
                        if (!list_split[j-1].contains(".")) {
                            focus = Character.toLowerCase(focus.charAt(0)) + focus.substring(1);
                        }
                    }
                }

                for (int k = 1; k < focus.length(); k++) {
                    if (Character.isUpperCase(focus.charAt(k))) {
                        focus = focus.substring(0, k) + Character.toLowerCase(focus.charAt(k)) + focus.substring(k + 1);
                    }
                }
            }
            list_split[j] = focus;
        }
        String concat="";
        for (int i = 0;i< list_split.length; i++){
            if (i != (list_split.length-1)) {
                concat = concat + list_split[i] + " ";
            }
            else {
                concat = concat + list_split[i];
            }
        }
        return concat;
    }

    /*public String fixTenses(String prob){
        String[] list_split = prob.split(" ");
        ArrayList<Integer> Cap_position = getPositionOfCapital(prob);
        ArrayList<String> reformed_string = new ArrayList<>();
        ArrayList<HashSet<String>> pronoun = new ArrayList<>();
        for (int j=0; j<list_split.length;j++) {
            //when we find a upper case letter, that is NOT contained
            //after a fullstop. we then check if that word is a noun
            // with some error handling
            HashSet<String> pronoun_ele = new HashSet<>();

            String urlStr = "https://www.dictionaryapi.com/api/v3/references/spanish/json/"
                    + list_split[j] + "?key=" + spanish_Key;

            try {
                URL obj = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int response = con.getResponseCode();
                if (response == 200) {
                    Scanner responseReader = new Scanner(con.getInputStream());
                    StringBuffer buffer = new StringBuffer();
                    while (responseReader.hasNextLine()) {
                        buffer.append(responseReader.nextLine() + "\n");
                    }
                    responseReader.close();
                    String jsonString = buffer.toString(); //assign your JSON String here
                    JSONArray jsonArr = new JSONArray(jsonString);


                    for (int i = 0; i < jsonArr.length(); i++) {
                        try {
                            String fl = jsonArr.getJSONObject(i).getString("fl");
                            pronoun_ele.add(fl);
                        } catch (JSONException e) {
                            break;
                        }
                    }
                    if ((j != 0) && (list_split[j - 1].contains("."))) {
                        if (Character.isLowerCase(list_split[j].charAt(0))) {
                            list_split[j] = list_split[j].replace(list_split[j].charAt(0),
                                    Character.toUpperCase(list_split[j].charAt(0)));
                            reformed_string.add(list_split[j]);
                        } else {
                            reformed_string.add(list_split[j]);

                        }
                    } else {

                        if (Cap_position.contains(j)) {
                            list_split[j] = list_split[j].replace(list_split[j].charAt(0),
                                    Character.toLowerCase(list_split[j].charAt(0)));
                            reformed_string.add(list_split[j]);
                        } else {
                            reformed_string.add(list_split[j]);
                        }
                    }
                }

            } catch (MalformedURLException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }


            pronoun.add(pronoun_ele);
        }
        String concat="";
        for (int i = 0;i<reformed_string.size(); i++){
            if (i != (reformed_string.size()-1)) {
                concat = concat + reformed_string.get(i) + " ";
            }
            else {
                concat = concat + reformed_string.get(i);
            }
        }
        return concat;
    }*/

    public String fixMissingFullStop(String prob){
        String[] list_split = prob.split(" ");
        ArrayList<Integer> Cap_position = getPositionOfCapital(prob);
        ArrayList<String> reformed_string = new ArrayList<>();
        ArrayList<HashSet<String>> pronoun = new ArrayList<>();
        for (int j=0; j<list_split.length;j++) {
            //when we find a upper case letter, that is NOT contained
            //after a fullstop. we then check if that word is a noun
            // with some error handling
            HashSet<String> pronoun_ele = new HashSet<>();

            String urlStr = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
                    + list_split[j] + "?key=" + myKey;

            try {
                URL obj = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int response = con.getResponseCode();
                if (response == 200) {
                    Scanner responseReader = new Scanner(con.getInputStream());
                    StringBuffer buffer = new StringBuffer();
                    while (responseReader.hasNextLine()) {
                        buffer.append(responseReader.nextLine() + "\n");
                    }
                    responseReader.close();
                    String jsonString = buffer.toString(); //assign your JSON String here
                    JSONArray jsonArr = new JSONArray(jsonString);


                    for (int i = 0; i < jsonArr.length(); i++) {
                        try {
                            String fl = jsonArr.getJSONObject(i).getString("fl");
                            pronoun_ele.add(fl);
                        } catch (JSONException e) {
                            break;
                        }
                    }
                    if ((j != 0) && (list_split[j - 1].contains("."))) {
                        if (Character.isLowerCase(list_split[j].charAt(0))) {
                            list_split[j] = list_split[j].replace(list_split[j].charAt(0),
                                    Character.toUpperCase(list_split[j].charAt(0)));
                            reformed_string.add(list_split[j]);
                        } else {
                            reformed_string.add(list_split[j]);

                        }
                    } else {

                        if (Cap_position.contains(j)) {
                            list_split[j] = list_split[j].replace(list_split[j].charAt(0),
                                    Character.toLowerCase(list_split[j].charAt(0)));
                            reformed_string.add(list_split[j]);
                        } else {
                            reformed_string.add(list_split[j]);
                        }
                    }
                }

            } catch (MalformedURLException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }


            pronoun.add(pronoun_ele);
        }
        String concat="";
        for (int i = 0;i<reformed_string.size(); i++){
            if (i != (reformed_string.size()-1)) {
                concat = concat + reformed_string.get(i) + " ";
            }
            else {
                concat = concat + reformed_string.get(i);
            }
        }
        return concat;
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

