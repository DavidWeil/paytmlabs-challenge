package com.example.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link UserServlet}.
 */
public class UserServletTest {

    private static final String USER_JSON =
            "{\"name\":\"steve\",\"passwordHash\":\"0fd32ee492c23c48\",\"selectedStore\":\"511\"}";
//    "{\"name\":\"steve\",\"passwordHash\":\"0fd32ee492c23c48\",\"selectedStore\":\"511\",\"recent\":[]}";
    private static final String RECENT_JSON =
            "{\"id\":\"53059\",\"name\":\"Goldfish Merlot\",\"thumbnail\":\"fish.jpg\"}";

    @Test
    public void testGetUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve");
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(USER_JSON, baos.toString());
    }

    @Test
    public void testGetErrorMissingUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);

        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/");
        response = mock(HttpServletResponse.class);
        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);
    }

    @Test
    public void testGetErrorNoSuchUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/george");
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, UserServlet.USER_ID_DOES_NOT_EXIST);
    }

    @Test
    public void testGetErrorExtraneousPath() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/recent/23049");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);
    }

    @Test
    public void testGetErrorUnknownCommand() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/cart");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);
    }

    @Test
    public void testGetRecent() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/recent");
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("[]\n", baos.toString());
    }

    @Test
    public void testGetRandom() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/random");
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true);
        when(response.getWriter()).thenReturn(writer);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doGet(request, response);

        verify(response).setContentType("text/plain");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("77033\n", baos.toString());
    }

    @Test
    public void testPostUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/steve");
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        assertEquals(servlet.userMap.size(), 1);
        assertTrue(servlet.userMap.containsKey("steve"));
    }

    @Test
    public void testPostErrorMissingUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);

        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/");
        response = mock(HttpServletResponse.class);
        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);
    }

    @Test
    public void testPostErrorDuplicateUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/steve");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_CONFLICT, UserServlet.USER_ID_ALREADY_EXISTS);
    }

    @Test
    public void testPostErrorInvalidPath() throws IOException, ServletException {

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/recent");
        HttpServletResponse response = mock(HttpServletResponse.class);

        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);

        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/location/toronto");
        response = mock(HttpServletResponse.class);

        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);

        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/recent/193237/750ml");
        response = mock(HttpServletResponse.class);

        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);
    }

    @Test
    public void testPostRecent() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(RECENT_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/steve/recent/53059");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
        // TODO: When recent products are tracked, check this got stored.
    }

    @Test
    public void testPutUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/steve");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    @Test
    public void testPutErrorMissingUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doPut(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);

        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/");
        response = mock(HttpServletResponse.class);
        servlet.doPut(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);
    }

    @Test
    public void testPutErrorNoSuchUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/george");
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doPut(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, UserServlet.USER_ID_DOES_NOT_EXIST);
    }

    @Test
    public void testPutErrorExtraneousPath() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(USER_JSON.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/steve/recent/23049");
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doPut(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);
    }

    @Test
    public void testDeleteUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDeleteErrorMissingUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet();
        servlet.doDelete(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);

        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/");
        response = mock(HttpServletResponse.class);
        servlet.doDelete(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.MISSING_USER_ID);
    }

    @Test
    public void testDeleteErrorNoSuchUser() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/george");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doDelete(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, UserServlet.USER_ID_DOES_NOT_EXIST);
    }

    @Test
    public void testDeleteErrorExtraneousPath() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/steve/recent/23049");
        HttpServletResponse response = mock(HttpServletResponse.class);

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(USER_JSON, User.class);
        UserServlet servlet = new UserServlet();
        servlet.userMap.put(user.getName(), user);
        servlet.doDelete(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, UserServlet.BAD_COMMAND);
    }
}