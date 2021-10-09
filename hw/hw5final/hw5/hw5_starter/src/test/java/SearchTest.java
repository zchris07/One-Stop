import okhttp3.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {
    private final String URI = "jdbc:sqlite:./JBApp.db";

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SearchAPITest {

        final String BASE_URL = "http://localhost:7000";
        private OkHttpClient client;

        @BeforeAll
        public void setUpAll() {
            client = new OkHttpClient();
        }

        // check for respond code
        @Test
        public void testHTTPPostJobSearchEndpoint1() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "real")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            assertEquals(200, response.code());
        }

        // check for respond format
        @Test
        public void testHTTPPostJobSearchEndpoint2() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "real")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            String contentType = response.header("Content-Type");
            assertEquals(contentType, "application/json");
        }

        // check the length of json for empty keyword
        @Test
        public void testHTTPPostJobSearchEndpoint3() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            //System.out.println(response.body().string());
            int resLength = response.body().string().length();
            assertEquals(resLength, 2030); // LY: hard coded length for MY database
        }

        // check the length of json for keyword " " (one space)
        @Test
        public void testHTTPPostJobSearchEndpoint4() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", " ")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute(); // NOTE: the body can only be consumed once!!!
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            int resLength = response.body().string().length();
            assertEquals(resLength, 2030); // LY: hard coded length for MY database
        }

        // check the content of json for a non-matching keyword
        @Test
        public void testHTTPPostJobSearchEndpoint5() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "zigzag")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            String resString = response.body().string();
            assertEquals(resString, "[]");
        }

        // check the content of json for a non-matching keyword with space connection
        @Test
        public void testHTTPPostJobSearchEndpoint6() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "zigzag zigzag")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            String resString = response.body().string();
            assertEquals(resString, "[]");
        }

        // check the content of json for a phrase that partially match
        @Test
        public void testHTTPPostJobSearchEndpoint7() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "zigzag software")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            String resString = response.body().string();
            assertEquals(resString, "[]");
        }

        // check the length of json for keyword "software"
        @Test
        public void testHTTPPostJobSearchEndpoint8() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "software")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            int resLength = response.body().string().length();
            assertEquals(resLength, 1485); // 358 for salesforce job with database untouched from git clone
        }

        // check the content of json for keyword "S0ftware"
        @Test
        public void testHTTPPostJobSearchEndpoint9() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "S0ftware")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            String resString = response.body().string();
            assertEquals(resString, "[]");
        }

        // check the length of json for keyword "SoFTWare engineer"
        @Test
        public void testHTTPPostJobSearchEndpoint10() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "SoFTWare engineer")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            int resLength = response.body().string().length();
            assertEquals(resLength, 728); // 358 for salesforce job with database untouched from git clone
        }

        // check the length of json for keyword "R&d" (letter+symbols)
        @Test
        public void testHTTPPostJobSearchEndpoint11() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "R&d")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            int resLength = response.body().string().length();
            assertEquals(resLength, 220); // 220 for the nestle job with database untouched from git clone
        }

        // check the content of json for keyword "software           Engineer"
        @Test
        public void testHTTPPostJobSearchEndpoint12() throws IOException {
            String endpoint = BASE_URL + "/search";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("key", "software           Engineer")
                    .build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("debug print");
            //System.out.println(response.body().string());
            //System.out.println(response.body().string().length());
            System.out.println("debug print end");
            String resString = response.body().string();
            assertEquals(resString, "[]");
        }
    }


}
