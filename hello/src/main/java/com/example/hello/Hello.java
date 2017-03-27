package com.example.hello;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello World example servlet.
 */
public class Hello extends HttpServlet {

    private static final String WORLD = "world";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Who are we saying "hi" to?
        String toGreet = request.getPathInfo();
        if (toGreet == null) {
            toGreet = WORLD;
        }
        else {
            // Let's be safe.
            Pattern p = Pattern.compile("^/(\\w*)");
            Matcher m = p.matcher(toGreet);
            if (m.find()) {
                toGreet = m.group(1);
            }
            else {
                toGreet = WORLD;
            }
        }

        // For simplicity we only send JSON if it's specifically requested.
        String accept = request.getHeader("Accept");
        boolean sendJSON = "application/json".equals(accept);

        if (sendJSON) {
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.println("{\"source\": \"hello\", \"message\": \"Hello " + toGreet + "!\"}");
        }
        else {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>Hello Servlet</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h1>Hello " + toGreet + "!</h1>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}
