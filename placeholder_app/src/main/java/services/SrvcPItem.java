package services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Duedate;
import model.Notes;
import model.PItem;
import model.TaskManager;

import java.sql.SQLException;
import java.util.Calendar;

public class SrvcPItem {

    public Dao getPItemORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, PItem.class);
        return DaoManager.createDao(connectionSource, PItem.class);
    }

    public Dao getDuedateORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, Duedate.class);
        return DaoManager.createDao(connectionSource, Duedate.class);
    }

    public Dao getCalendarORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, Calendar.class);
        return DaoManager.createDao(connectionSource, Calendar.class);
    }

    public Dao getNotesORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, Notes.class);
        return DaoManager.createDao(connectionSource, Notes.class);
    }

    public Dao getTaskManagerORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, TaskManager.class);
        return DaoManager.createDao(connectionSource, TaskManager.class);
    }

}
