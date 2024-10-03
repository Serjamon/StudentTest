package ru.courses.education;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTests {

    private Connection con;

    @Test
    @SneakyThrows
    public void getConnection(){
        con = DriverManager.getConnection("jdbc:h2:~\\Office");
        if (con!=null) {
            System.out.println("OK");
        } else {
            System.out.println("NO OK");
        }
    }




}
