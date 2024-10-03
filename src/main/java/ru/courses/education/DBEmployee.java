package ru.courses.education;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DBEmployee {

    @SneakyThrows
    public static void main(String[] args) {
        int countBeforeDel, countAfterDel;
        boolean isCorrect;


        isCorrect = checkDeptDelete();
        System.out.println("Требование " + (isCorrect ? "выполнено" : "не выполнено"));

    }

    @SneakyThrows
    private static boolean checkDeptDelete() {

        Connection con2 = DriverManager.getConnection("jdbc:h2:C:\\Work\\Office\\Office");
        String q = "SELECT empl.ID, DP.ID FROM\n" +
                "PUBLIC.EMPLOYEE empl LEFT JOIN PUBLIC.DEPARTMENT dp\n" +
                "ON EMPL.DEPARTMENTID = dp.ID\n" +
                "GROUP BY EMPL.ID\n" +
                "HAVING DP.ID IS NULL;";
        PreparedStatement preparedStatement = con2.prepareStatement(q);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return false;
        }

        return true;
    }


}
