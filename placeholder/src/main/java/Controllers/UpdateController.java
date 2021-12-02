package Controllers;

import Functionality.textFunctions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import model.TaskNote;
import model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class UpdateController {

    public static void updateNote(String taskName, String taskNote, String isCheckedGrammar, String isCheckedSpelling, String isCheckedCapital, String isCheckedLongRunning, Dao<TaskNote, Integer> dao) throws SQLException {

        List<TaskNote> check = dao.queryForEq("taskname", taskName);

        if (check.size() == 0) {
            textFunctions text_functions = new textFunctions();

            if (isCheckedGrammar.equals("Yes")) {
                taskNote = text_functions.fixMissingFullStop(taskNote);
            }
            if (isCheckedSpelling.equals("Yes")) {
                taskNote = text_functions.fixSpellingIssues(taskNote);
            }
            if (isCheckedCapital.equals("Yes")) {
                taskNote = text_functions.fixCapitalLettersInString(taskNote);
            }
            if (isCheckedLongRunning.equals("Yes")) {
                taskNote = text_functions.fixLongRunningSentence(taskNote);
            }
            TaskNote newTaskNote = new TaskNote(taskName, taskNote);

            dao.create(newTaskNote);
        } else {
            textFunctions text_functions = new textFunctions();

            if (isCheckedGrammar.equals("Yes")) {
                taskNote = text_functions.fixMissingFullStop(taskNote);
                System.out.print(taskNote);
            }
            if (isCheckedSpelling.equals("Yes")) {
                taskNote = text_functions.fixSpellingIssues(taskNote);
                System.out.print(taskNote);
            }
            if (isCheckedCapital.equals("Yes")) {
                taskNote = text_functions.fixCapitalLettersInString(taskNote);
                System.out.print(taskNote);
            }

            UpdateBuilder<TaskNote, Integer> builder = dao.updateBuilder();

            builder.updateColumnValue("tasknote", taskNote);
            builder.where().eq("taskname", taskName);
            dao.update(builder.prepare());
        }
    }

    public static void updateUser(String useremail, String firstName, String lastName, String organization, String status,
                                   String summary, String image, Dao<User, Integer> dao) throws SQLException {
        List<User> check = dao.queryForEq("email", useremail);
        UpdateBuilder<User, Integer> builder = dao.updateBuilder();
        if (firstName != "" & firstName != null) {
            builder.updateColumnValue("firstname", firstName);
        }
        if (lastName != "" & lastName != null) {
            builder.updateColumnValue("lastname", lastName);
        }
        if (organization != "" & organization != null) {
            builder.updateColumnValue("organization", organization);
        }
        if (status != "" & status != null) {
            builder.updateColumnValue("status", status);
        }
        if (summary != "" & summary != null) {
            builder.updateColumnValue("summary", summary);
        }
        if (image != "" & image != null) {
            builder.updateColumnValue("profileimage", image);
        }
        builder.where().eq("email", useremail);
        dao.update(builder.prepare());
    }

    public static void updatePassword(String email, String password, Dao<User, Integer> dao) throws SQLException, NoSuchAlgorithmException {
        UpdateBuilder<User, Integer> builder = dao.updateBuilder();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes());
        String hashedPassword = new String(messageDigest.digest());
        builder.updateColumnValue("hashedpassword", hashedPassword);
        builder.where().eq("email", email);
        dao.update(builder.prepare());
    }
}
