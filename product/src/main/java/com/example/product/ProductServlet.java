package com.example.product;

import com.example.data.*;
import com.example.data.deserializers.*;
import com.example.user.Recent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Displays a page containing information on products from the LCBO.
 *
 * This servlet responds to 2 different requests: "/product" which returns a paged list of all LCBO products and
 * "/product/{id}" which returns a page specific to that one product.
 */
public final class ProductServlet extends HttpServlet {

    private static final String API_KEY = "MDpkYzc4ODEzOC0wZTYzLTExZTctODAyOS0zNzAwZjMyZWE0YjE6akhFSjZJbTNzS1lwcVpEMVVwZUVSRUtReVBiWFJ5N2JmRE9M";
    private static final String BASE_URL = "https://lcboapi.com/products/";

    private final Gson gson;

    public ProductServlet() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Pager.class, new PagerDeserializer())
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .registerTypeAdapter(Store.class, new StoreDeserializer())
                .registerTypeAdapter(ListResponse.class, new ListResponseDeserializer())
                .registerTypeAdapter(SimpleResponse.class, new SimpleResponseDeserializer())
                .create();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // For simplicity we only send JSON if it's specifically requested.
        String accept = request.getHeader("Accept");
        boolean sendJSON = "application/json".equals(accept);

        String productData = request.getPathInfo();
        if (productData == null || productData.length() < 2) {
            // Do a general query for a page of data
            String pageNum = request.getParameter("page");
            int page = 1;
            try {
                page = Integer.parseInt(pageNum);
            }
            catch (NumberFormatException e) {
                // We don't care; just use the first page.
            }

            ListResponse<Product> results;
            try {
                results = getProductList(page);
            }
            catch (IOException e) {
                reportError(response, sendJSON, e.getMessage());
                return;
            }

            if (results.getStatus() != 200) {
                reportError(response, sendJSON, results.getMessage());
                return;
            }

            if (sendJSON) {
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                gson.toJson(results.getResults(), writer);
                writer.flush();
            }
            else {
                String pageURL = getBaseUrl(request);
                showListPage(request, response, pageURL, results);
            }
        }

        // Send the data for a single product.
        else {
            // Remove the leading '/'
            String productID = productData.substring(1);

            SimpleResponse<Product> results;
            try {
                results = getProductData(productID);
            }
            catch (IOException e) {
                reportError(response, sendJSON, e.getMessage());
                return;
            }

            if (results.getStatus() != 200) {
                reportError(response, sendJSON, results.getMessage());
                return;
            }

            // Note that the user viewed this product (add it to the recent list)
            recordRecent(request, productID, results.getResult());

            if (sendJSON) {
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                gson.toJson(results.getResult(), writer);
                writer.flush();
            }
            else {
                showProductPage(request, response, results);
            }
        }
    }

    private static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath;
    }

    // Note we've already validated the product ID.
    private void recordRecent(HttpServletRequest request, String productID, Product product) throws IOException {

        String user = getCookieValue(request, "LCBOUser");
        if (user == null) {
            // This could be cleaner...
            throw new IllegalStateException("Viewing page without a logged-in user");
        }

        HttpURLConnection connection = (HttpURLConnection)
                new URL(getExternalUrl(request) + "/user/" + user + "/recent/" + productID).openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        Writer writer = new OutputStreamWriter(connection.getOutputStream());
        Recent recent = new Recent(productID, product.getName(), product.getThumbURL());
        gson.toJson(recent, writer);
        writer.flush();
        connection.connect();

        int status = connection.getResponseCode();
        if (status != HttpServletResponse.SC_OK && status != HttpServletResponse.SC_ACCEPTED) {
            throw new IOException("Error persisting recent value: " + connection.getResponseMessage());
        }
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

    private static String getExternalUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        return scheme + serverName + serverPort;
    }

    private ListResponse<Product> getProductList(int page) throws IOException {

        String targetURL = BASE_URL + "?page=" + page;
        HttpURLConnection connection = (HttpURLConnection) new URL(targetURL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + API_KEY);
        connection.connect();
        int status = connection.getResponseCode();
        if (status == 200) {
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                Type productListType = new TypeToken<ListResponse<Product>>(){}.getType();
                return gson.fromJson(reader, productListType);
            }
        }
        throw new IOException("Error response: " + status);
    }

    private void reportError(HttpServletResponse response, boolean sendJSON, String message) throws IOException {
        if (sendJSON) {
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.print("{\"type\":\"error\",\"message\":\"");
            writer.print(gson.toJson(message));
            writer.println("\"}");
        }
        else {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>Error</title>");
            writer.println("</head>");
            writer.println("<body>");

            writer.println("Error retrieving data:");
            writer.println(message);

            writer.println("</body>");
            writer.println("</html>");
        }
    }

    private void showListPage(HttpServletRequest request, HttpServletResponse response, String pageURL,
            ListResponse<Product> results) throws IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>LCBO Product Display</title>");
        writer.println("</head>");
        writer.println("<body>");

        writer.println("<table border=\"0\">");
        writer.println("<tr>");
        writer.println("<th>Product ID</th>");
        writer.println("<th>Name</th>");
        writer.println("</tr>");

        List<Product> products = results.getResults();
        for (Product p : products) {
            writer.println("<tr>");
            writer.println("<th>" + p.getId() + "</th>");
            writer.println("<th><a href=\"" + pageURL + '/' + p.getId() + "\">" + p.getName() + "</a></th>");
            writer.println("</tr>");
        }
        writer.println("</table>");

        Pager pager = results.getPager();
        int current = pager.getCurrentPage();
        if (current > 1) {
            writer.println("<a href=\"" + pageURL + "?page=" + (current - 1) + "\">Previous Page</a>");
        }
        else {
            writer.println("Previous Page");
        }
        writer.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

        if (current < pager.getLastPage()) {
            writer.println("<a href=\"" + pageURL + "?page=" + (current + 1) + "\">Next Page</a>");
        }
        else {
            writer.println("Next Page");
        }

        String recentContent = fetchRecent(request);
        if (recentContent != null && !recentContent.isEmpty()) {
            writer.println("<h2>Recently Viewed</ht>");
            writer.append(recentContent);
        }

        writer.println("</body>");
        writer.println("</html>");
    }

    private SimpleResponse<Product> getProductData(String productID) throws IOException {

        String targetURL = BASE_URL + productID;
        HttpURLConnection connection = (HttpURLConnection) new URL(targetURL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + API_KEY);
        connection.connect();
        int status = connection.getResponseCode();
        if (status == 200) {
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                Type productType = new TypeToken<SimpleResponse<Product>>(){}.getType();
                return gson.fromJson(reader, productType);
            }
        }
        throw new IOException("Error response: " + status);
    }

    private void showProductPage(HttpServletRequest request, HttpServletResponse response,
            SimpleResponse<Product> results) throws IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>LCBO Product Display</title>");
        writer.println("</head>");
        writer.println("<body>");

        Product product = results.getResult();
        writer.println("<table border=\"0\">");

        writer.println("<tr>");
        writer.println("<td>ID</td>");
        writer.println("<td>" + product.getId() + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>Name</td>");
        writer.println("<td>" + product.getName() + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>Origin</td>");
        writer.println("<td>" + product.getOrigin() + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>Category</td>");
        writer.println("<td>" + product.getCategory() + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>Image</td>");
        writer.println("<td><img src=\"" + product.getThumbURL() + "\"></td>");
        writer.println("</tr>");
        writer.println("</table>");

        String recentContent = fetchRecent(request);
        if (recentContent != null && !recentContent.isEmpty()) {
            writer.println("<h2>Recently Viewed</ht>");
            writer.append(recentContent);
        }

        writer.println("</body>");
        writer.println("</html>");
    }

    // Retrieve the set of recently-viewed products for inclusion in the product page.
    private String fetchRecent(HttpServletRequest request) throws IOException {

        String user = getCookieValue(request, "LCBOUser");
        if (user == null) {
            // This could be cleaner...
            throw new IllegalStateException("Viewing page without a logged-in user");
        }

        HttpURLConnection connection = (HttpURLConnection)
                new URL(getExternalUrl(request) + "/user/" + user + "/recent").openConnection();
        connection.setRequestMethod("GET");
        // Get HTML for direct inclusion in this page.
        connection.setRequestProperty("Accept", "text/html");
        connection.connect();

        int status = connection.getResponseCode();
        if (status == 200) {
            // Read the response into a String and return it.
            try (Scanner s = new java.util.Scanner(connection.getInputStream())) {
                return s.useDelimiter("\\A").hasNext() ? s.next() : "";
            }
        }
        throw new IOException("Error reading recent products: " + connection.getResponseMessage());
    }
}
