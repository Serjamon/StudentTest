package ru.courses.education;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudenTests {

    private Student student;

    @BeforeEach
    public void createStudent(){
        student = new Student("Vasya");
    }

    @Test
    public void testGetName() {
        assertEquals("Vasya", student.getName());
    }

    @Test
    public void testSetName() {
        student.setName("testSetName");
        assertEquals("testSetName", student.getName());
    }

    @Test
    public void testSetGrades() {
        List<Integer> grades = Arrays.asList(new Integer[]{4, 3, 5, 2});
        student.addGrade(4);
        student.addGrade(3);
        student.addGrade(5);
        student.addGrade(2);
        assertEquals(grades, student.getGrades());
    }

    @Test
    public void testIllegalGrades() {
        assertThrows(IllegalArgumentException.class, () ->
                student.addGrade(1));
        assertThrows(IllegalArgumentException.class, () ->
                student.addGrade(6));
    }

    @Test
    public void testGradesIncapsulation() {
        List<Integer> grades = Arrays.asList(new Integer[]{4, 3});
        student.addGrade(4);
        student.addGrade(3);

        student.getGrades().add(55);

        assertEquals(grades, student.getGrades());
    }

    @Test
    void testEquals(){
        student.addGrade(4);
        student.addGrade(3);

        Student student1 = new Student(student.getName());
        student1.addGrade(4);
        student1.addGrade(3);

        assertEquals(student, student1);
    }

    @Test
    void testHash(){
        student.addGrade(4);
        student.addGrade(3);

        Student student1 = new Student(student.getName());
        student1.addGrade(4);
        student1.addGrade(3);

        assertEquals(student.hashCode(), student1.hashCode());
    }

    @Test
    void testToString(){
        student.addGrade(4);
        student.addGrade(3);

        String str = "Student{name=Vasya, marks=[4, 3]}";

        assertEquals(student.toString(), str);
    }

}
