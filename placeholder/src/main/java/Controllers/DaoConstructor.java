
package Controllers;

        import com.j256.ormlite.dao.Dao;
        import com.j256.ormlite.dao.DaoManager;
        import com.j256.ormlite.jdbc.JdbcConnectionSource;
        import com.j256.ormlite.support.ConnectionSource;
        import com.j256.ormlite.table.TableUtils;
        import model.TaskList;
        import model.TaskNote;
        import model.User;
        import model.WorksOn;
        import java.sql.SQLException;


public class DaoConstructor {

        public static Dao<TaskList, Integer> getTaskListRMLiteDao() throws SQLException {
                final String URI = "jdbc:sqlite:./JBApp.db";
                ConnectionSource connectionSource = new JdbcConnectionSource(URI);
                TableUtils.createTableIfNotExists(connectionSource, TaskList.class);
//        TableUtils.dropTable(connectionSource, TaskList.class,false);
                return DaoManager.createDao(connectionSource, TaskList.class);
        }

        public static Dao getUserORMLiteDao() throws SQLException {
                final String URI = "jdbc:sqlite:./JBApp.db";
                ConnectionSource connectionSource = new JdbcConnectionSource(URI);
                TableUtils.createTableIfNotExists(connectionSource, User.class);
                return DaoManager.createDao(connectionSource, User.class);
        }

        public static Dao getWorksOnORMLiteDao() throws SQLException {
                final String URI = "jdbc:sqlite:./JBApp.db";
                ConnectionSource connectionSource = new JdbcConnectionSource(URI);
                TableUtils.createTableIfNotExists(connectionSource, WorksOn.class);
                return DaoManager.createDao(connectionSource, WorksOn.class);
        }

        public static Dao<TaskNote, Integer> getTaskNoteRMLiteDao() throws SQLException {
                final String URI = "jdbc:sqlite:./JBApp.db";
                ConnectionSource connectionSource = new JdbcConnectionSource(URI);
                TableUtils.createTableIfNotExists(connectionSource, TaskNote.class);
//        TableUtils.dropTable(connectionSource, TaskList.class,false);
                return DaoManager.createDao(connectionSource, TaskNote.class);
        }
}
