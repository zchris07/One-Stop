package services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.PItem;
import model.PList;

import java.sql.SQLException;

public class SrvcPList {

    private boolean islocal;

    public SrvcPList(boolean islocal) {
        this.islocal = islocal;
    }

    public Dao getPListORMLiteDao() throws SQLException {
        final String URI;
        if(this.islocal)
            URI = "jdbc:sqlite:./db_local.db";
        else
            URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, PList.class);
        return DaoManager.createDao(connectionSource, PList.class);
    }

    public Dao getPItemORMLiteDao() throws SQLException {
        final String URI;
        if(this.islocal)
            URI = "jdbc:sqlite:./db_local.db";
        else
            URI = "jdbc:sqlite:./db_cloud.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
        TableUtils.createTableIfNotExists(connectionSource, PItem.class);
        return DaoManager.createDao(connectionSource, PItem.class);
    }

}
