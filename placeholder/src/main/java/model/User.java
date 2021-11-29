package model;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;
import kotlin.Pair;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(generatedId = true)
    private Integer userId;
    @DatabaseField(canBeNull = false)
    private String hashedPassword;
    @DatabaseField(canBeNull = false, unique = true)
    private String email;
    @DatabaseField(canBeNull = true)
    private String firstName;
    @DatabaseField(canBeNull = true)
    private String lastName;
    @DatabaseField(canBeNull = true)
    private String summary;
    @DatabaseField(canBeNull = true)
    private String organization;
    @DatabaseField(canBeNull = true)
    private String status;
    @DatabaseField(canBeNull = false)
    private String profileImage = "https://i.imgur.com/hepj9ZS.png";
    @DatabaseField(columnName = "availability", canBeNull = false, dataType = DataType.SERIALIZABLE)
    private Availability thisMap;

    private static class Availability implements Serializable{
        private Map<String, List<Pair<Double,Double>>> thisMap;
        public Availability() {

            String dt = "2021-10-01";  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map<String, List<Pair<Double, Double>>> new_map = new LinkedHashMap<>();
            for (int i = 0;i<= 100; i++) {
                Calendar cal = Calendar.getInstance();
                c.add(Calendar.DATE, 1);  // number of days to add
                dt = sdf.format(c.getTime());  // dt is now the new date
                Pair<Double, Double> newPair = new Pair<>(9.0, 21.0);
                List<Pair<Double, Double>> newList = new ArrayList();
                newList.add(newPair);
                new_map.put(dt, newList);

            }
            this.thisMap = new_map;
            //c.add(Calendar.DATE, 1);  // number of days to add
            //dt = sdf.format(c.getTime());  // dt is now the new date
        }
        public Availability(Map<String, List<Pair<Double, Double>>> thisMap) {
            this.thisMap = thisMap;
        }
        public Map<String, List<Pair<Double, Double>>> getThisMap() {
            return thisMap;
        }

        public void setThisMap(Map<String, List<Pair<Double, Double>>> thisMap) {
            this.thisMap = thisMap;
        }
    }



    public User(){
    }

    public User(String email, String hashedPassword) {
        this.email=email;
        this.hashedPassword=hashedPassword;
        this.profileImage = "https://i.imgur.com/hepj9ZS.png";
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }
    public String getProfileImage() {return profileImage;}
    public void setProfileImage(String imageUrl) {this.profileImage = imageUrl;}

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHashedPassword() {return this.hashedPassword; }

    public Map<String, List<Pair<Double, Double>>> getThisMap() {
        return thisMap.getThisMap();
    }

    public void setThisMap(Map<String, List<Pair<Double, Double>>> thisMap) {
        this.thisMap.setThisMap(thisMap);
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"userId\":\"").append(userId).
                append("\",\"email\":\"").append(email).
                append("\",\"firstName\":\"").append(firstName).
                append("\",\"lastName\":\"").append(lastName).
                append("\",\"summary\":\"").append(summary).
                append("\",\"status\":\"").append(status).
                append("\",\"organization\":\"").append(organization).append("\"");
        sb.append("}");
        return sb.toString();
    }
}