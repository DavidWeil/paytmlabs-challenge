package com.example.hello;

import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * Test for the hello servlet.
 */
public class HelloTest {

    private static final String HTML_FORMAT =
            "<html>\n<head>\n<title>Hello Servlet</title>\n</head>\n" +
            "<body>\n<h1>Hello %1$s!</h1>\n</body>\n</html>\n";
    private static final String JSON_FORMAT = "{\"source\": \"hello\", \"message\": \"Hello %1$s!\"}\n";

    @Test
    public void testHelloHttp() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Hello hello = new Hello();
        hello.doGet(request, response);

        verify(response).setContentType("text/html");
        assertEquals(getHtml("world"), baos.toString());
    }

    @Test
    public void testHelloJson() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Accept")).thenReturn("application/json");
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Hello hello = new Hello();
        hello.doGet(request, response);

        verify(response).setContentType("application/json");
        assertEquals(getJson("world"), baos.toString());
    }

    @Test
    public void testHelloJimHttp() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/Jim/ExtraGarbage");
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Hello hello = new Hello();
        hello.doGet(request, response);

        verify(response).setContentType("text/html");
        assertEquals(getHtml("Jim"), baos.toString());
    }

    @Test
    public void testHelloJimJson() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/Jim%2fattempttoaccessotherresource");
        when(request.getHeader("Accept")).thenReturn("application/json");
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Hello hello = new Hello();
        hello.doGet(request, response);

        verify(response).setContentType("application/json");
        assertEquals(getJson("Jim"), baos.toString());
    }

    private String getHtml(String name) {
        return String.format(HTML_FORMAT, name);
    }

    private String getJson(String name) {
        return String.format(JSON_FORMAT, name);
    }
}
