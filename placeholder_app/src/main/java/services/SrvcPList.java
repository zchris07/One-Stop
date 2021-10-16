package services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Functionality;
import model.PItem;
import model.PList;

import java.sql.SQLException;

public class SrvcPList {

    public SrvcPList() {

    }

    public Dao getPListORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, PList.class);
        return DaoManager.createDao(connectionSource, PList.class);
    }

    public Dao getFunctionalityORMLiteDao() throws SQLException {
        final String URI;
        URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, Functionality.class);
        return DaoManager.createDao(connectionSource, Functionality.class);
    }

}
