package ru.courses.education;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class StudentRestTests {

    private boolean getTests, postTests, delTests;

    private final int idStud = 9999991;
    private final String nameStud = "Vasya Kamushkin";
    private final int[] marksStud = {3, 4, 5, 4, 3};

    @BeforeEach
    @SneakyThrows
    public void createStudents() {

        if (getTests || delTests) {
            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = new Student(idStud, nameStud, marksStud);
            String json11 = objectMapper.writeValueAsString(stud);

            URL obj = new URL("http://localhost:8080/student/");
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = json11.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            urlConnection.getResponseCode();
        }
    }

    @AfterEach
    @SneakyThrows
    public void delStudents() {

        if (getTests || postTests) {
            URL obj = new URL("http://localhost:8080/student/" + idStud);
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.getResponseCode();
        }
    }

    @Nested
    class GetTests {
        public GetTests() {
            getTests = true;
            postTests = false;
            delTests = false;
        }

        @SneakyThrows
        @Test
        //Студент есть в БД
        public void getStudent1() {

            RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .get()
                    .then()
                    .statusCode(200);

        }

        @Test
        //Студента нет в БД
        public void getStudent2() {

            delStudents();

            RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .get()
                    .then()
                    .statusCode(404);

        }

        @Test
        //id, name равен
        public void getStudent3() {

            RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .get()
                    .then()
                    .contentType(ContentType.JSON)
                    .body("id", Matchers.equalTo(idStud))
                    .body("name", Matchers.equalTo(nameStud));

        }

        @SneakyThrows
        @Test
        //marks равен
        public void getStudent4() {

            String res = RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .get()
                    .then()
                    .contentType(ContentType.JSON)
                    .extract().asString();

            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = objectMapper.readValue(res, Student.class);
            Assertions.assertArrayEquals(marksStud, stud.getMarks());

        }
    }

    @Nested
    class PostTests {

        public PostTests() {
            getTests = false;
            postTests = true;
            delTests = false;
        }

        @SneakyThrows
        @Test
        //400 empty name
        public void postStudent1() {

            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = new Student(idStud, null, marksStud);
            String json11 = objectMapper.writeValueAsString(stud);

            RestAssured.given().baseUri("http://localhost:8080/student")
                    .body(json11).contentType("application/json; utf-8")
                    .when()
                    .post()
                    .then()
                    .statusCode(400);

        }

        @SneakyThrows
        @Test
        //code 201, id, name, marks
        public void postStudent2() {

            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = new Student(idStud, nameStud, marksStud);
            String json11 = objectMapper.writeValueAsString(stud);

            RestAssured.given().baseUri("http://localhost:8080/student")
                    .body(json11).contentType("application/json; utf-8")
                    .when()
                    .post()
                    .then()
                    .statusCode(201);

            String res = RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .get()
                    .then()
                    .contentType(ContentType.JSON)
                    .extract().asString();

            Student stud2 = objectMapper.readValue(res, Student.class);
            Assertions.assertEquals(idStud, stud2.getId());
            Assertions.assertEquals(nameStud, stud2.getName());
            Assertions.assertArrayEquals(marksStud, stud2.getMarks());

        }

        @SneakyThrows
        @Test
        //обновление по ИД
        public void postStudent3() {

            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = new Student(idStud, nameStud, marksStud);
            String json11 = objectMapper.writeValueAsString(stud);

            RestAssured.given().baseUri("http://localhost:8080/student")
                    .body(json11).contentType("application/json; utf-8")
                    .when()
                    .post()
                    .then()
                    .statusCode(201);

            //
            stud.setName("New Name");
            stud.setMarks(new int[]{3, 2, 1});
            json11 = objectMapper.writeValueAsString(stud);

            RestAssured.given().baseUri("http://localhost:8080/student")
                    .body(json11).contentType("application/json; utf-8")
                    .when()
                    .post()
                    .then()
                    .statusCode(201);

            String res = RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .get()
                    .then()
                    .contentType(ContentType.JSON)
                    .extract().asString();

            Student stud2 = objectMapper.readValue(res, Student.class);
            Assertions.assertEquals(idStud, stud2.getId());
            Assertions.assertEquals("New Name", stud2.getName());
            Assertions.assertArrayEquals(new int[]{3, 2, 1}, stud2.getMarks());

        }

        @SneakyThrows
        @Test
        //201 empty id
        public void postStudent4() {

            Integer newId;

            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = new Student(0, "anyname", new int[]{});
            String json11 = objectMapper.writeValueAsString(stud);
            json11 = json11.replace("0", "null");

            newId = RestAssured.given().baseUri("http://localhost:8080/student")
                    .body(json11).contentType("application/json; utf-8")
                    .when()
                    .post()
                    .then()
                    .statusCode(201)
                    .extract().as(Integer.class);

            Assertions.assertNotNull(newId);

            URL obj = new URL("http://localhost:8080/student/" + newId);
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.getResponseCode();

        }

        @SneakyThrows
        @Test
        //400 wrong id
        public void postStudent5() {

            ObjectMapper objectMapper = new ObjectMapper();
            Student stud = new Student(55, "validName", marksStud);
            String json11 = objectMapper.writeValueAsString(stud);
            json11 = json11.replace("55", "string");

            RestAssured.given().baseUri("http://localhost:8080/student")
                    .body(json11).contentType("application/json; utf-8")
                    .when()
                    .post()
                    .then()
                    .statusCode(400);

        }

    }

    @Nested
    class DelTests {

        public DelTests() {
            getTests = false;
            postTests = false;
            delTests = true;
        }

        @SneakyThrows
        @Test
        //code 200
        public void delStudent1() {

            RestAssured.given().baseUri("http://localhost:8080/student/" + idStud)
                    .when()
                    .delete()
                    .then()
                    .statusCode(200);
        }

        @SneakyThrows
        @Test
        // code 404
        public void postStudent2() {

            RestAssured.given().baseUri("http://localhost:8080/student/" + idStud +"3")
                    .when()
                    .delete()
                    .then()
                    .statusCode(404);

        }

    }

    @Nested
    class TopStudTests {

        public TopStudTests() {
            getTests = false;
            postTests = false;
            delTests = false;
        }
        //TODO
        // get /topStudent код 200 и пустое тело, если студентов в базе нет.
        // как это проверить? заглушками?

        @SneakyThrows
        @Test
        //
        public void topStudent1() {

        }

        @SneakyThrows
        @Test
        //
        public void topStudent2() {

            RestAssured.given().baseUri("http://localhost:8080/student/" + idStud +"3")
                    .when()
                    .delete()
                    .then()
                    .statusCode(404);

        }

    }

}



