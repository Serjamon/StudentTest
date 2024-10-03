package ru.courses.education;

import lombok.SneakyThrows;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBEmployee {

    public static Connection con;

    @SneakyThrows
    public static void main(String[] args) {

        int annId = 0;
        setConnection();

        annId = findEmployeeByName("Ann");

        if (annId != -1) {
            System.out.println("Анна перемещена в отдел HR");
            setDeptHR(annId);
        }

        System.out.println("Исправлено имен струдников: " + correctEmployeeNames());

        System.out.println("Кол-во сотрудников в отделе ИТ: " + emplInIT());

    }

    @SneakyThrows
    private static int findEmployeeByName(String name) {
        int count = 0;
        int emplId = -1;

        String q = "SELECT ID FROM Employee WHERE Name = ?";
        PreparedStatement preparedStatement = con.prepareStatement(q);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            count++;
            emplId = resultSet.getInt("ID");
        }

        if (count == 1) {
            return emplId;
        }

        return -1; // Если не найден
    }

    @SneakyThrows
    private static void setDeptHR(int emplId) {
        String updateQuery = "UPDATE Employee SET DepartmentID = (SELECT ID FROM Department WHERE Name = 'HR') WHERE ID = ?";
        PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
        preparedStatement.setInt(1, emplId);
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    private static int correctEmployeeNames() {
        String q = "SELECT ID, Name FROM Employee";
        int renamedCount = 0;

        PreparedStatement selectStatement = con.prepareStatement(q);
        ResultSet resultSet = selectStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("Name");
            if (name != null && !name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
                String newName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                renameEmployee(id, newName);
                renamedCount++;
            }
        }

        return renamedCount;
    }

    @SneakyThrows
    private static void renameEmployee(int emplId, String newName) {
        String updateQuery = "UPDATE Employee SET Name = ? WHERE ID = ?";
        PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
        preparedStatement.setString(1, newName);
        preparedStatement.setInt(2, emplId);
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    private static int emplInIT() {
        String q = "SELECT COUNT(*) FROM Employee WHERE DepartmentID = (SELECT ID FROM Department WHERE Name = 'IT')";
        PreparedStatement preparedStatement = con.prepareStatement(q);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }


    @SneakyThrows
    public static void setConnection(){
        con = DriverManager.getConnection("jdbc:h2:C:\\Work\\Office\\Office");
        if (con!=null) {
            System.out.println("setConnection = OK");
        } else {
            System.out.println("setConnection = NO OK");
        }
    }



}
