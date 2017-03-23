package com.example.product;

import com.example.data.*;
import com.example.data.deserializers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Displays a page containing information on products from the LCBO.
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String productData = request.getPathInfo();
        String pageURL = getBaseUrl(request);
        if (productData == null || productData.length() < 2) {
            // Do a general query for a page of data
            String pageNum = request.getParameter("page");
            int page = 1;
            try {
                page = Integer.parseInt(pageNum);
            } catch (NumberFormatException e) {
                // We don't care; just use the first page.
            }
            String targetURL = BASE_URL + "?page=" + page;
            showListPage(response, pageURL, targetURL);
        } else {
            // Remove the leading '/'
            String productID = productData.substring(1);
            String targetURL = BASE_URL + productID;
            showProductPage(response, pageURL, targetURL);
        }
    }

    private static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath;
    }

    private void showListPage(HttpServletResponse response, String pageURL, String targetURL)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>LCBO Product Display</title>");
        writer.println("</head>");
        writer.println("<body>");

        HttpURLConnection connection = (HttpURLConnection) new URL(targetURL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + API_KEY);
        connection.connect();
        int status = connection.getResponseCode();
        if (status == 200) {
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                Type productListType = new TypeToken<ListResponse<Product>>(){}.getType();
                ListResponse<Product> results = gson.fromJson(reader, productListType);

                if (results.getStatus() != 200) {
                    writer.println("Request error:");
                    writer.println(results.getMessage());
                }
                else {
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
                }
            }
            catch (IOException e) {
                writer.println("Error retrieving data:");
                writer.println(e.getLocalizedMessage());
            }
        }
        else {
            writer.println("Error connecting to LCBO server:");
            writer.println(connection.getResponseMessage());
        }

        writer.println("</body>");
        writer.println("</html>");
    }

    private void showProductPage(HttpServletResponse response, String pageURL, String targetURL) throws IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>LCBO Product Display</title>");
        writer.println("</head>");
        writer.println("<body>");

        HttpURLConnection connection = (HttpURLConnection) new URL(targetURL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + API_KEY);
        connection.connect();
        int status = connection.getResponseCode();
        if (status == 200) {
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                Type productType = new TypeToken<SimpleResponse<Product>>(){}.getType();
                SimpleResponse<Product> results = gson.fromJson(reader, productType);

                if (results.getStatus() != 200) {
                    writer.println("Request error:");
                    writer.println(results.getMessage());
                }
                else {

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
                }
            }
            catch (IOException e) {
                writer.println("Error retrieving data:");
                writer.println(e.getLocalizedMessage());
            }
        }
        else {
            writer.println("Error connecting to LCBO server:");
            writer.println(connection.getResponseMessage());
        }

        writer.println("</body>");
        writer.println("</html>");
    }
}
