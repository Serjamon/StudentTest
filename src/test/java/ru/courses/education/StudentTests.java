package ru.courses.education;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.BasicHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentTests {

    private Student student;
    private CloseableHttpClient httpClientMock;

    @BeforeEach
    public void createStudent() {

        student = new Student("Vasya");
        httpClientMock = Mockito.mock(CloseableHttpClient.class);
        student.httpClient = httpClientMock;

    }


    @SneakyThrows
    @Test
    public void testSetGradesTrue() {

        //инициализируем все заглушки
        HttpGet requestMock = Mockito.mock(HttpGet.class);
        CloseableHttpResponse httpResponseMock = Mockito.mock(CloseableHttpResponse.class);
        HttpEntity entityMock = Mockito.mock(HttpEntity.class);

        //вместо requestMock нужен был, оказывается, Mockito.any()
        Mockito.when(httpClientMock.execute(Mockito.any())).thenReturn(httpResponseMock);
        Mockito.when(httpResponseMock.getEntity()).thenReturn(entityMock);
        //тут не работало как в методе, выбрасывало исключение, что может быть только InputStream, а не строка
        //ужс, я, наверно, стал почетным читателем стэковерфлоу, пока это делал
        InputStream istr = new ByteArrayInputStream("true".getBytes());
        Mockito.when(entityMock.getContent()).thenReturn(istr);

        //валидная оценка
        student.addGrade(4);
        assertEquals(student.getGrades().contains(4), true);
    }

    @SneakyThrows
    @Test
    public void testSetGradesFalse() {

        //инициализируем все заглушки
        HttpGet requestMock = Mockito.mock(HttpGet.class);
        CloseableHttpResponse httpResponseMock = Mockito.mock(CloseableHttpResponse.class);
        HttpEntity entityMock = Mockito.mock(HttpEntity.class);

        Mockito.when(httpClientMock.execute(Mockito.any())).thenReturn(httpResponseMock);
        Mockito.when(httpResponseMock.getEntity()).thenReturn(entityMock);
        InputStream istr = new ByteArrayInputStream("false".getBytes());
        Mockito.when(entityMock.getContent()).thenReturn(istr);

        //НЕвалидная оценка
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.addGrade(0);
        });
        assertEquals("0 is wrong grade", exception.getMessage());
    }

}
