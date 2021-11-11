package model;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

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

    public User(){
    }

    public User(String email, String hashedPassword) {
        this.email=email;
        this.hashedPassword=hashedPassword;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHashedPassword() {return this.hashedPassword; }

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