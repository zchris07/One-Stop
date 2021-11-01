package model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(generatedId = true)
    private Integer userId;
    @DatabaseField(canBeNull = false)
    private String hashedPassword;
    @DatabaseField(canBeNull = false, unique = true)
    private String email;

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

    public String getHashedPassword() {return this.hashedPassword; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}