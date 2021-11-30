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
import model.Availability;

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(generatedId = true, columnName = "id")
    private static Integer id;
    @DatabaseField(canBeNull = false)
    private String hashedpassword;
    @DatabaseField(canBeNull = false, unique = true)
    private String email;
    @DatabaseField(canBeNull = true)
    private String firstname;
    @DatabaseField(canBeNull = true)
    private String lastname;
    @DatabaseField(canBeNull = true)
    private String summary;
    @DatabaseField(canBeNull = true)
    private String organization;
    @DatabaseField(canBeNull = true)
    private String status;
    @DatabaseField(canBeNull = false)
    private String profileimage = "https://i.imgur.com/hepj9ZS.png";
    @DatabaseField(columnName = "availability", canBeNull = false, dataType = DataType.SERIALIZABLE)
    private static Availability availability = new Availability();

    public User(){
    }

    public User(String email, String hashedPassword) {
        this.email=email;
        this.hashedpassword=hashedPassword;
        this.profileimage = "https://i.imgur.com/hepj9ZS.png";
    }

    public Integer getUserId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
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
    public String getProfileImage() {return profileimage;}
    public void setProfileImage(String imageUrl) {this.profileimage = imageUrl;}

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHashedPassword() {return this.hashedpassword; }

    public Availability getAvailability() {
        return availability;
    }

    public Map<String, List<Pair<Double, Double>>> getThisMap() {
        return this.getAvailability().getThisMap();
    }

    public static void setThisMap(Map<String, List<Pair<Double, Double>>> thisMap,Dao userDao) throws SQLException {
        availability.setThisMap(thisMap);
        UpdateBuilder<Availability, Integer> builder = userDao.updateBuilder();
        builder.updateColumnValue("availability", availability);
        builder.where().eq("id", id);
        userDao.update(builder.prepare());
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"userId\":\"").append(id).
                append("\",\"email\":\"").append(email).
                append("\",\"firstName\":\"").append(firstname).
                append("\",\"lastName\":\"").append(lastname).
                append("\",\"summary\":\"").append(summary).
                append("\",\"status\":\"").append(status).
                append("\",\"organization\":\"").append(organization).append("\"");
        sb.append("}");
        return sb.toString();
    }
}