package ru.courses.education;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class StudentRestTests {

    private boolean GetTests;

    private final int idStud = 9999991;
    private final String nameStud = "Vasya Kamushkin";
    private final int[] marksStud = {3, 4, 5, 4, 3};

    @BeforeEach  @SneakyThrows
    public void init() {
        if (GetTests) {
            // Код, который нужно выполнить только перед определенными тестами
            System.out.println("Выполняется подготовка для специфической группы тестов");
        }
    }
    @BeforeEach @SneakyThrows
    public void createStudents(){

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

    @AfterEach @SneakyThrows
    public void delStudents(){

        URL obj = new URL("http://localhost:8080/student/" + idStud);
        HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.getResponseCode();

    }

    @Nested
    class GetTests {
        public GetTests() {
            GetTests = true;
        }

        @Test
        public void testA() {
            // Тест A
            System.out.println("Тест A выполняется");
        }

        @Test
        public void testB() {
            // Тест B
            System.out.println("Тест B выполняется");
        }
    }

    @SneakyThrows
    @Test
    //Студент есть в БД
    public void getStudent1() {

        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + idStud)
                .when()
                .get()
                .then()
                .statusCode(200);

    }

    @Test
    //Студента нет в БД
    public void getStudent2() {

        delStudents();

        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + idStud)
                .when()
                .get()
                .then()
                .statusCode(404);

    }

    @Test
    //id равен
    public void getStudent3() {

        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + idStud)
                .when()
                .get()
                .then()
                .body("id", Matchers.equalTo(idStud));

    }

    @Test
    //name равен
    public void getStudent4() {

        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + idStud)
                .when()
                .get()
                .then()
                .body("name", Matchers.equalTo(nameStud));

    }

    @SneakyThrows
    @Test
    //marks равен
    public void getStudent5() {

        String res = RestAssured.given()
                .baseUri("http://localhost:8080/student/" + idStud)
                .when()
                .get()
                .then()
                .extract().asString();

        ObjectMapper objectMapper = new ObjectMapper();
        Student stud = objectMapper.readValue(res, Student.class);
        Assertions.assertArrayEquals(marksStud, stud.getMarks());

    }



    @Test
    public void postStudent1() {

        delStudents();

        RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", Matchers.equalTo("13"));

    }



}
