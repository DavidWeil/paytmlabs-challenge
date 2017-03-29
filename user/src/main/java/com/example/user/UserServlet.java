package com.example.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for handling user actions.
 */
public class UserServlet extends HttpServlet {

    public static final String MISSING_USER_ID = "Missing User ID";
    public static final String USER_ID_ALREADY_EXISTS = "User ID Already Exists";
    public static final String USER_ID_DOES_NOT_EXIST = "User ID Does Not Exist";
    public static final String BAD_COMMAND = "Bad Command";

    final Map<String, UserData> userMap = new HashMap<>();
    private final Gson gson;

    public UserServlet() {
        gson = new GsonBuilder().create();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER_ID);
            return;
        }

        String[] elements = path.split("/");
        if (elements.length < 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER_ID);
            return;
        }
        String userID = elements[1];

        UserData data = userMap.get(userID);
        if (data == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, USER_ID_DOES_NOT_EXIST);
            return;
        }
        User user = data.getUser();

        if (elements.length == 2) {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            gson.toJson(user, writer);
            writer.flush();
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        else if (elements.length > 3) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_COMMAND);
            return;
        }

        // Retrieving data related to the user.
        String action = elements[2];
        switch (action) {
            case "recent":
                // Check if we need to sent HTML or JSON.
                String accept = req.getHeader("Accept");
                boolean sendJSON = "application/json".equals(accept);
                sendRecent(resp, data, sendJSON);
                break;
            case "random":
                // TODO:  This is currently just stubbed.
                resp.setContentType("text/plain");
                resp.getWriter().println("77033");
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_COMMAND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER_ID);
            return;
        }

        String[] elements = path.split("/");
        if (elements.length < 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER_ID);
            return;
        }
        String userID = elements[1];
        UserData data = userMap.get(userID);

        if (elements.length == 2) {
            if (data != null) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, USER_ID_ALREADY_EXISTS);
                return;
            }

            User user = gson.fromJson(req.getReader(), User.class);
            userMap.put(userID, new UserData(user));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
        else if (elements.length == 4 && "recent".equals(elements[2])){

            // Setting a recent product.
            // String id = elements[3];

            // Read a recent product from the message body.
            Recent recent = gson.fromJson(req.getReader(), Recent.class);
            data.addRecent(recent);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_COMMAND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER_ID);
            return;
        }

        String[] elements = path.split("/");
        if (elements.length != 2) {
            String message = elements.length < 2 ? MISSING_USER_ID : BAD_COMMAND;
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            return;
        }

        String userID = elements[1];
        UserData data = userMap.get(userID);
        if (data == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, USER_ID_DOES_NOT_EXIST);
            return;
        }
        User user = data.getUser();

        // Read a user from the message body.
        User newUser = gson.fromJson(req.getReader(), User.class);
        // Update the fields of the existing user.
        user.setPasswordHash(newUser.getPasswordHash());
        user.setSelectedStore(newUser.getSelectedStore());

        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER_ID);
            return;
        }

        String[] elements = path.split("/");
        if (elements.length != 2) {
            String message = elements.length < 2 ? MISSING_USER_ID : BAD_COMMAND;
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            return;
        }

        String userID = elements[1];

        UserData data = userMap.remove(userID);
        if (data == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, USER_ID_DOES_NOT_EXIST);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // Send recent product data.
    private void sendRecent(HttpServletResponse response, UserData data, boolean sendJSON)
            throws IOException {

        List<Recent> recentList = data.getRecent();
        PrintWriter writer = response.getWriter();

        // Handle JSON (the simple case) first.
        if (sendJSON) {
            response.setContentType("application/json");
            gson.toJson(recentList, writer);
            writer.flush();
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        response.setContentType("text/html");
        if (recentList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        writer.println("<div style=\"display: flex;\">");

        for (Recent r : recentList) {
            writer.println("<div style=\"width: 100px;\">");

            writer.println("<img src=\"" + r.getThumbnail() + "\" height=\"100\" width=\"100\"><br>");
            writer.println(r.getName());
            writer.println("</div>");
        }
        writer.println("</div>");
    }
}
