package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

public class TaskNote {
    @DatabaseField(columnName="taskName",canBeNull = false,unique = true)
    private String taskName;

    @DatabaseField(columnName="taskNote",canBeNull = false,unique = true)
    private String taskNote = "hello world.";

    public TaskNote(String _taskName, String _taskNote) {
        this.taskName = _taskName;
        this.taskNote = _taskNote;
    }
    public TaskNote() {

    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"taskName\":\"").append(taskName).append("\",\"taskNote\":\"").append(taskNote).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public String toString() {
        return taskNote;
    }

    public void updateNote(String taskName, String taskNote, Dao<TaskNote,Integer> dao) throws SQLException {
        UpdateBuilder<TaskNote, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("taskNote", taskNote);
        builder.where().eq("taskName", taskName);
        dao.update(builder.prepare());
    }
}
