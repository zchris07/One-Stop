package model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(generatedId = true)
    private Integer userId;
    @DatabaseField(canBeNull = false)
    private String firstName;
    @DatabaseField(canBeNull = false)
    private String lastName;
    @DatabaseField(canBeNull = true)
    private String organization;
    @DatabaseField(canBeNull = true)
    private String email;

    public User(Integer userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(Integer userId, String firstName, String lastName, String userName, String organization, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.email = email;
    }

    public User(Integer userId, String firstName, String lastName, String userName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", organization='" + organization + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
