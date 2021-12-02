package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = "tasknote")
public class TaskNote {
    @DatabaseField(columnName="taskname",canBeNull = false,unique = true)
    private String taskname;

    @DatabaseField(columnName="tasknote",canBeNull = false,unique = true)
    private String tasknote = "hello world.";

    public TaskNote(String _taskName, String _taskNote) {
        this.taskname = _taskName;
        this.tasknote = _taskNote;
    }
    public TaskNote() {

    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"taskName\":\"").append(taskname).append("\",\"taskNote\":\"").append(tasknote).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public String toString() {
        return tasknote;
    }

    public void updateNote(String taskName, String taskNote, Dao<TaskNote,Integer> dao) throws SQLException {
        UpdateBuilder<TaskNote, Integer> builder = dao.updateBuilder();
        builder.updateColumnValue("tasknote", taskNote);
        builder.where().eq("taskname", taskName);
        dao.update(builder.prepare());
    }
}
