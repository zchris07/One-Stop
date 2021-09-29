import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Job;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JobTest {

    private final String URI = "jdbc:sqlite:./JBApp.db";

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class JobORMLiteDaoTest {
        // TODO 5: Similar to what was done in EmployerTest.EmployerORMLiteDaoTest class, write JUnit tests
        //  to test basic CRUD operations on the jobs table! Think of interesting test cases and
        //  write at least four different test cases for each of the C(reate)/U(pdate)/D(elete)
        //  operations!
        //  Note: You need to (write code to) create the "jobs" table before writing your test cases!

        private ConnectionSource connectionSource;
        private Dao<Job, Integer> dao;

        // create a new connection to JBApp database, create "jobs" table, and create a
        // new dao to be used by test cases
        @BeforeAll
        public void setUpAll() throws SQLException {
            connectionSource = new JdbcConnectionSource(URI);
            TableUtils.createTableIfNotExists(connectionSource, Job.class);
            dao = DaoManager.createDao(connectionSource, Job.class);
        }

        // delete all rows in the job table before each test case
        @BeforeEach
        public void setUpEach() throws SQLException {
            TableUtils.clearTable(connectionSource, Job.class);
        }

        // inserting a new record where title is null must fail, the reason being
        // there is a non-null constraint on the "title" column in "jobs" table!
        @Test
        public void testCreateTitleNull() {
            Date datePosted = new Date(1630468800000L); // 2021-09-01
            Date deadline = new Date(1630555200000L);   // 2021-09-02
            //create a new job instance
            Job j = new Job(null, datePosted, deadline, "domain1", "loc1", true, true, "requirements", 1, null);
            // try to insert into jobs table. This must fail!
            Assertions.assertThrows(SQLException.class, () -> dao.create(j));
        }

        // inserting a new record where domain is an empty string must succeed!
        @Test
        public void testCreateDomainEmpty() throws SQLException {
            Date datePosted = new Date(1630468800000L); // 2021-09-01
            Date deadline = new Date(1630555200000L);   // 2021-09-02
            //create a new job instance
            Job j = new Job("title1", datePosted, deadline, "", "loc1", true, true, "requirements", 1, null);
            // try to insert into jobs table. This must succeed!
            dao.create(j);
            List<Job> ls = dao.queryForEq("domain", j.getDomain());
            assertEquals(ls.size(), 1);
            assertEquals("", ls.get(0).getDomain());
        }

        // insert a new record, then delete it, and assert it was indeed removed!
        @Test
        public void testDeleteAllFieldsMatch() throws SQLException {
            Date datePosted = new Date(1630468800000L); // 2021-09-01
            Date deadline = new Date(1630555200000L);   // 2021-09-02
            //create a new job instance
            Job j = new Job("title1", datePosted, deadline, "", "loc1", true, true, "requirements", 1, null);
            // try to insert into jobs table. This must succeed!
            dao.create(j);
            List<Job> ls1 = dao.queryForEq("title", j.getTitle());
            assertEquals(1, ls1.size());
            assertEquals("title1", ls1.get(0).getTitle());
            dao.delete(j);
            // Assert "title1 was removed from jobs
            List<Job> ls2 = dao.queryForEq("title", j.getTitle());
            assertEquals(0, ls2.size());
        }

        // insert a new employer, update its sector, then assert it was indeed updated!
        @Test
        public void testUpdateDomain() throws SQLException {
            Date datePosted = new Date(1630468800000L); // 2021-09-01
            Date deadline = new Date(1630555200000L);   // 2021-09-02
            //create a new job instance
            Job j = new Job("title1", datePosted, deadline, "", "loc1", true, true, "requirements", 1, null);
            // try to insert into jobs table. This must succeed!
            dao.create(j);
            j.setDomain("domain1");
            dao.createOrUpdate(j);
            // assert the domain is updated successfully!
            assertEquals("domain1", dao.queryForEq("title", "title1").get(0).getDomain());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class JobAPITest {

        final String BASE_URL = "http://localhost:7000";
        private OkHttpClient client;

        @BeforeAll
        public void setUpAll() {
            client = new OkHttpClient();
        }

        @Test
        public void testHTTPGetJobsEndPoint() throws IOException {
            // TODO 6: Write code to send a http get request using OkHttp to the
            //  "jobs" endpoint and assert that the received status code is OK (200)!
            //  Note: In order for this to work, you need to make sure your local sparkjava
            //  server is running, before you run the JUnit test!
        }
    }

}
