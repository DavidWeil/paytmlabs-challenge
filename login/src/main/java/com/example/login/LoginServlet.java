package com.example.login;

import com.example.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The servlet that handles login (authentication) and user sign-up.
 */
public final class LoginServlet extends HttpServlet {

    private final Gson gson;

    public LoginServlet() {
        gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showLoginPage(resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        String user = req.getParameter("username");
        String pass = req.getParameter("password");
        if (user == null || pass == null || isUnclean(user) || user.length() > 14) {
            String message = null;
            if (user == null) {
                message = "Username must be specified";
            }
            else if (pass == null) {
                message = "Password must be specified";
            }
            else if (isUnclean(user)) {
                message = "Invalid username (please user only word characters)";
            }
            else if (user.length() > 14) {
                message = "Username too long";
            }
            showLoginPage(resp, message);
            return;
        }

        // At this point the user name is safe for inclusion in a URL.

        // Get the user (test for existing if sign-up)
        HttpURLConnection connection = (HttpURLConnection) new URL("/user/" + user).openConnection();
        connection.connect();
        int status = connection.getResponseCode();
        User u;
        if ("login".equals(action)) {
            if (status == 200) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    u = gson.fromJson(reader, User.class);
                    if (!u.getPasswordHash().equals(getSHAHash(pass))) {
                        // NOTE:  I know that this is not considered good practice (for security reasons)
                        // but it makes debugging easier.
                        showLoginPage(resp, "Incorrect password.");
                        return;
                    }
                }
                catch (IOException e) {
                    showLoginPage(resp, "Error retrieving user data: " + e.getMessage());
                    return;
                }
            }
            else {
                // NOTE:  I know that this is not considered good practice (for security reasons)
                // but it makes debugging easier.
                showLoginPage(resp, "User not found: " + connection.getResponseMessage());
                return;
            }
        }
        else {  // "signup"
            if (status == 200) {
                showLoginPage(resp, "That user name is already in use.");
                return;
            }

            connection = (HttpURLConnection) new URL("/user/" + user).openConnection();
            connection.setRequestMethod("PUT");
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            u = new User(user);
            u.setPasswordHash(getSHAHash(pass));
            gson.toJson(u, writer);
            connection.connect();
            status = connection.getResponseCode();
            if (status != 200 && status != 201) {
                showLoginPage(resp, "Error creating user: " + connection.getResponseMessage());
                return;
            }
        }

        // We are authenticated; set the auth cookie and redirect to the originating page.
        resp.addCookie(getHttpCookie("LCBOAuth", "authorized"));
        resp.addCookie(getHttpCookie("LCBOUser", user));
        resp.addCookie(getHttpCookie("LCBOStore", u.getSelectedStore()));
        String redirectURL = getCookieValue(req, "originalURL");
        if (redirectURL == null) {
            redirectURL = "/product";
        }
        resp.sendRedirect(redirectURL);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private Cookie getHttpCookie(String name, String value) {
        Cookie c = new Cookie(name, value);
        c.setHttpOnly(true);
        return c;
    }

    /**
     * Note that as a simplification of a real authentication system, this does not bother salting the hashes.
     */
    private String getSHAHash(String password) {

        java.security.MessageDigest d;
        try {
            d = java.security.MessageDigest.getInstance("SHA-1");
            d.reset();
            d.update(password.getBytes());
            return DatatypeConverter.printHexBinary(d.digest());
        }
        catch (NoSuchAlgorithmException e) {
            // The standard defines that this algorithm must be provided.
        }
        return null;
    }

    private void showLoginPage(HttpServletResponse response, String message) throws IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Login or Sign Up</title>");
        writer.println("</head>");
        writer.println("<body style=\"margin: 20px;\">");

        if (message != null) {
            writer.println("<div style=\"color: red; font-size: larger; bottom-margin: 20px;\">");
            writer.println(message);
            writer.println("</div>");
        }

        writer.println("<div style=\"display: flex;\">");
        writer.println("<div style=\"flex: 1 1 0\">");
        writer.println("<form action=\"login\" method=\"post\">");
        writer.println("<h4>Login</h4>");
        writer.println("<input type=\"hidden\" name=\"action\" value=\"login\">");
        writer.println("Username: <input type=\"text\" name=\"username\">");
        writer.println("Password: <input type=\"text\" name=\"password\">");
        writer.println("<input type=\"submit\" value=\"Login\">");
        writer.println("</form>");
        writer.println("</div>");
        writer.println("<div style=\"flex: 1 1 0\">");
        writer.println("<form action=\"login\" method=\"post\">");
        writer.println("<h4>Sign Up</h4>");
        writer.println("<input type=\"hidden\" name=\"action\" value=\"signup\">");
        writer.println("Username: <input type=\"text\" name=\"username\">");
        writer.println("Password: <input type=\"text\" name=\"password\">");
        writer.println("<input type=\"submit\" value=\"Sign Up\">");
        writer.println("</form>");
        writer.println("</div>");
        writer.println("</div>");

        writer.println("<div>");
        writer.println("<h3>Notes</h3>");
        writer.println("<p>I explicitly did not use so-called \"password\" fields because I have read convincing arguments ");
        writer.println("that except in unusual cases they are mostly an unnecessary source of errors and frustration ");
        writer.println("without actually providing any real security.</p>");
        writer.println("<p>Similarly, with the password being visible, it is not as important to force the user to enter ");
        writer.println("their password twice.</p>");
        writer.println("</div>");

        writer.println("</body>");
        writer.println("</html>");
    }

    private boolean isUnclean(String test) {
        // Examine the leading string of word characters.
        Pattern p = Pattern.compile("^(\\w*)");
        Matcher m = p.matcher(test);
        // If we don't find any match, or the match doesn't include the full string, it's an error.
        return !m.find() || !m.group(1).equals(test);
    }
}
