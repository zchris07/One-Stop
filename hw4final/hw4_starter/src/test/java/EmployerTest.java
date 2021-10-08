import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Employer;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

// This is a JUnit test class with tw inner classes named EmployerORMLiteDaoTest and
// EmployerAPITest. The test cases that test the JBApp database (using ORMLite and SQLite)
// goe into EmployerORMLiteDaoTest and the tests that test the "employers" api endpoint go
// into EmployerAPITest
public class EmployerTest {
    private final String URI = "jdbc:sqlite:./JBApp.db";

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class EmployerORMLiteDaoTest {

        private ConnectionSource connectionSource;
        private Dao<Employer, Integer> dao;

        // create a new connection to JBApp database, create "employers" table, and create a
        // new dao to be used by test cases
        @BeforeAll
        public void setUpAll() throws SQLException {
            connectionSource = new JdbcConnectionSource(URI);
            TableUtils.createTableIfNotExists(connectionSource, Employer.class);
            dao = DaoManager.createDao(connectionSource, Employer.class);
        }

        // delete all rows in the employers table before each test case
        @BeforeEach
        public void setUpEach() throws SQLException {

            TableUtils.clearTable(connectionSource, Employer.class);
        }

        // inserting a new record where name is null must fail, the reason being
        // there is a non-null constraint on the "name" column in "employers" table!
        @Test
        public void testCreateNameNull() {
            //create a new employer instance
            Employer e = new Employer(null, "Tech", "Summary");
            // try to insert into employers table. This must fail!
            Assertions.assertThrows(SQLException.class, () -> dao.create(e));
        }

        // inserting a new record where sector is an empty string must succeed!
        @Test
        public void testCreateSectorEmpty() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Company1", "", "Summary");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            List<Employer> ls = dao.queryForEq("name", e.getName());
            assertEquals(ls.size(), 1);
            assertEquals("", ls.get(0).getSector());
        }

        // insert multiple employer records, and assert they were indeed added!
        @Test
        public void testReadMutipleEmployers() throws SQLException {
            // create multiple new employer instance
            List<Employer> lsCreate = new ArrayList<>();
            lsCreate.add(new Employer("Salesforce", "Tech", "An American cloud-based software company focused on customer relationship management services!"));
            lsCreate.add(new Employer("Sonos", "Tech", "Sonos is a developer and manufacturer of audio products best known for its multi-room audio products!"));
            lsCreate.add(new Employer("Fedex", "Transportation/E-Commerce", "An American multinational conglomerate holding company which focuses on transportation, e-commerce and business services!"));
            lsCreate.add(new Employer("First Solar", "Energy", "A leading global provider of comprehensive PV solar solutions!"));
            // try to insert them into employers table. This must succeed!
            dao.create(lsCreate);
            // read all employers
            List<Employer> lsRead = dao.queryForAll();
            // assert all employers in lsCreate were inserted and can be read
            assertEquals(lsCreate, lsRead);
        }

        // insert a new record, then delete it, and assert it was indeed removed!
        @Test
        public void testDeleteAllFieldsMatch() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            List<Employer> ls1 = dao.queryForEq("name", e.getName());
            assertEquals(1, ls1.size());
            assertEquals("Kraft Heinz", ls1.get(0).getName());
            dao.delete(e);
            // Assert "Karft Heinz" was removed from employers
            List<Employer> ls2 = dao.queryForEq("name", e.getName());
            assertEquals(0, ls2.size());
        }

        // insert a new employer, update its sector, then assert it was indeed updated!
        @Test
        public void testUpdateSector() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            e.setId(22);
            // try to insert into employers table. This must succeed!
            dao.create(e);
            e.setSector("Food/Beverage");
            dao.createOrUpdate(e);
            // assert the sector is updated successfully!
            assertEquals("Food/Beverage", dao.queryForEq("name", "Kraft Heinz").get(0).getSector());
        }

        // TODO 1: Think of more test cases for all CRUD operations and add them below!
        //  Write a test case for each of the following scenarios and assert the expected outcome:
        //  C(reate):
        //      1) inserting two employers with same exact name fails
        //      2) inserting two employers with same exact sector succeeds
        //      3) inserting an employer with an empty name succeeds
        //      4) inserting an employer where employer's id is set manually on the
        //         employer object (using setId(int) method) is not (necessarily) the id value
        //         that gets inserted in the table!

        @Test
        public void testCreateEmpty_test_exact_two_employers() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Company1", "", "Summary");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            Employer e2 = new Employer("Company1", "", "Summary");
            Assertions.assertThrows(SQLException.class, () -> dao.create(e2));
        }

        @Test
        public void testCreateEmpty_test_exact_two_sector() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Company1", "Food", "Summary");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            Employer e2 = new Employer("Company2", "Food", "Summary2");
            dao.create(e2);
            List<Employer> ls2 = dao.queryForAll();
            assertEquals(2, ls2.size());
        }

        @Test
        public void testCreateEmpty_Name() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("", "FOOD", "Summary");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            List<Employer> ls = dao.queryForAll();
            assertEquals(ls.size(), 1);
            assertEquals("", ls.get(0).getName());
        }

        @Test
        public void testCreateSet_Not_equal() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            e.setId(22);
            // try to insert into employers table. This must succeed!
            dao.create(e);
            List<Employer> ls = dao.queryForEq("name", e.getName());
            assertNotEquals(22, ls.get(0).getId());
        }

        //  U(pdate)
        //      1) updating an employer's id to an id value that does NOT exist in the table succeeds
        //      2) updating an employer's id to an id value that exists in the table fails
        //      3) updating an employer's name that is already exists in the table succeeds
        //      4) updating an employer's summary that already exists in the table succeeds
        @Test
        public void testUpdate_Not_Exist() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            dao.updateId(e,22);
            List<Employer> ls2 = dao.queryForAll();
            assertEquals(22, ls2.get(0).getId());
        }

        @Test
        public void testUpdate_Exist() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            dao.updateId(e,22);
            Employer e2 = new Employer("Kraft Heinz2", "Food2", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e2);
            Assertions.assertThrows(SQLException.class, () -> dao.updateId(e2,22));
        }
        @Test
        public void testUpdate_Exist_Employer_name() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            e.setName("Heinz");
            dao.createOrUpdate(e);
            List<Employer> ls2 = dao.queryForAll();
            assertEquals("Heinz", ls2.get(0).getName());
        }
        @Test
        public void testUpdate_Exist_Employer_summary() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            e.setSummary("Updated summary");
            dao.createOrUpdate(e);
            List<Employer> ls2 = dao.queryForAll();
            assertEquals("Updated summary", ls2.get(0).getSummary());
        }
        //  D(elete)
        //      1) Deleting an employer record (using the "delete" function of ORMLite) based on an id that does not exist does not delete any rows even if arow with the same exact name exists
        //      2) Deleting an employer record (using the "delete" function of ORMLite)
        //      based on an id that exists succeeds even if the names are different
        //      3) Deleting a collection of employers at once (using the "delete"
        //      function of ORMLite) removes all those employers that exist from the table
        //      4) Deleting a collection of employers at once (using the "delete" function of ORMLite)
        //      where none of them exists does not remove any rows from the table
        @Test
        public void testDelete_NOT_EXIST_EMPLOYEE() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.delete(e);
            dao.delete(e);
        }
        @Test
        public void testDelete_EXIST_EMPLOYEE() throws SQLException {
            // create a new employer instance
            Employer e = new Employer("Kraft Heinz", "Food", "A global food and beverage company!");
            // try to insert into employers table. This must succeed!
            dao.create(e);
            List<Employer> ls = dao.queryForAll();
            List<Employer> ls2 = dao.queryForEq("id",ls.get(0).getId());
            dao.delete(ls2.get(0));
        }
        @Test
        public void testDelete_Collection_EXIST() throws SQLException {
            // create a new employer instance
            List<Employer> lsCreate = new ArrayList<>();
            lsCreate.add(new Employer("Salesforce", "Tech", "An American cloud-based software company focused on customer relationship management services!"));
            lsCreate.add(new Employer("Sonos", "Tech", "Sonos is a developer and manufacturer of audio products best known for its multi-room audio products!"));
            lsCreate.add(new Employer("Fedex", "Transportation/E-Commerce", "An American multinational conglomerate holding company which focuses on transportation, e-commerce and business services!"));
            lsCreate.add(new Employer("First Solar", "Energy", "A leading global provider of comprehensive PV solar solutions!"));
            // try to insert them into employers table. This must succeed!
            dao.create(lsCreate);
            dao.delete(lsCreate);
            List<Employer> ls = dao.queryForAll();
            assertEquals(0, ls.size());
        }
        @Test
        public void testDelete_Collection_NOT_EXIST() throws SQLException {
            // create a new employer instance
            List<Employer> lsCreate = new ArrayList<>();
            lsCreate.add(new Employer("Salesforce", "Tech", "An American cloud-based software company focused on customer relationship management services!"));
            lsCreate.add(new Employer("Sonos", "Tech", "Sonos is a developer and manufacturer of audio products best known for its multi-room audio products!"));
            lsCreate.add(new Employer("Fedex", "Transportation/E-Commerce", "An American multinational conglomerate holding company which focuses on transportation, e-commerce and business services!"));
            lsCreate.add(new Employer("First Solar", "Energy", "A leading global provider of comprehensive PV solar solutions!"));
            // try to insert them into employers table. This must succeed!
            dao.delete(lsCreate);
            List<Employer> ls = dao.queryForAll();
            assertEquals(0, ls.size());
        }
    }


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class EmployerAPITest {

        final String BASE_URL = "http://localhost:7000";
        private OkHttpClient client;

        @BeforeAll
        public void setUpAll() {
            client = new OkHttpClient();
        }

        @Test
        public void testHTTPGetEmployersEndpoint() throws IOException {
            // TODO 2: Write code to send a http get request using OkHttp to thr
            //  "employers" endpoint and assert that the received status code is OK (200)!
            //  Note: In order for this to work, you need to make sure your local sparkjava
            //  server is running, before you run the JUnit test!
            Request request = new Request.Builder()
                    .url(BASE_URL + "/employers")
                    .build();
            Response response = client.newCall(request).execute();
            assertEquals(200, response.code());
            String contentType = response.header("Content-Type");
            assertEquals(contentType, "application/json");
        }
    }

}