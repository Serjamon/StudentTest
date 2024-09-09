package ru.courses.main;

import ru.courses.education.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Student stud = new Student("Dimon");

        List<Integer> grades = Arrays.asList(new Integer[]{2, 3, 5, 4, 3});
        stud.addGrade(4);
        stud.addGrade(3);
        stud.addGrade(3);
        stud.addGrade(5);
        List<Integer> grd = stud.getGrades();
        System.out.println(grd);
        grd.add(44);
        System.out.println(stud.getGrades());
        System.out.println(grd.toString());

    }
}
