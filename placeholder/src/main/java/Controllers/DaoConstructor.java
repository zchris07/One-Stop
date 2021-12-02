package Controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoConstructor {

        public static String tasklistCreateTableSql = "CREATE TABLE IF NOT EXISTS tasklist (id SERIAL PRIMARY KEY, " +
                "listname VARCHAR(100) NOT NULL UNIQUE," + " userid VARCHAR(100), " +
                "tasklist BYTEA NOT NULL);";
        public static String userCreateTableSql = "CREATE TABLE IF NOT EXISTS usertable (id SERIAL PRIMARY KEY," +
                "hashedpassword VARCHAR(1000) NOT NULL," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "firstname VARCHAR(100)," +
                "lastname VARCHAR(100)," +
                "summary VARCHAR(10000)," +
                "organization VARCHAR(100)," +
                "status VARCHAR(100)," +
                "profileimage VARCHAR(100) NOT NULL," +
                "availability BYTEA NOT NULL);";
        public static String worksonCreateTableSql = "CREATE TABLE IF NOT EXISTS workson (id serial PRIMARY KEY, " +
                "collabratorid VARCHAR(100) NOT NULL," +
                " listid VARCHAR(100) NOT NULL);";
        public static String tasknoteCreateTableSql = "CREATE TABLE IF NOT EXISTS tasknote (taskname VARCHAR(100) NOT NULL UNIQUE," +
                " tasknote VARCHAR(10000));";

        private static Connection getConnection() throws URISyntaxException, SQLException {
                String databaseUrl = System.getenv("DATABASE_URL");
                URI dbUri = new URI(databaseUrl);
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                        + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
                return DriverManager.getConnection(dbUrl, username, password);
        }

        private static void CreateTableIfNotExists(String sql){
                try (Connection conn = getConnection()) {
                        Statement st = conn.createStatement();
                        st.execute(sql);
                } catch (URISyntaxException | SQLException e) {
                        e.printStackTrace();
                }
        }

        public static Dao getTheDao(Class c, String s) throws SQLException, URISyntaxException {
                String databaseUrl = System.getenv("DATABASE_URL");
                if (databaseUrl == null) {
                        // Not on Heroku, so use SQLite
                        final String URI = "jdbc:sqlite:./JBApp.db";
                        ConnectionSource connectionSource = new JdbcConnectionSource(URI);
                        TableUtils.createTableIfNotExists(connectionSource, c);
                        return DaoManager.createDao(connectionSource, c);
                }
                URI dbUri = new URI(databaseUrl);
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                        + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
                ConnectionSource connectionSource = new JdbcConnectionSource(dbUrl, username, password);
                CreateTableIfNotExists(s);
                return DaoManager.createDao(connectionSource, c);
        }
}