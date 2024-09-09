package ru.courses.education;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
