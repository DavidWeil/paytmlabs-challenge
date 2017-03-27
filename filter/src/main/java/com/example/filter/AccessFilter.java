package com.example.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet filter to control access to webapps in a simple way that we can easily adjust.
 */
public class AccessFilter implements Filter {

    private String loginPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig != null) {
            loginPath = filterConfig.getInitParameter("login_path");
        }

        // Fallback in case of configuration problem.
        if (loginPath == null) {
            // TODO:  Log this.
            loginPath = "login";
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        String authToken = null;
        if (cookies != null) {
            int i = 0;
            while (authToken == null && i < cookies.length) {
                Cookie c = cookies[i++];
                if (c.getName().equalsIgnoreCase("lcboauth")) {
                    authToken = c.getValue();
                }
            }
        }

        if (!"authorized".equalsIgnoreCase(authToken)) {
            // Store this path so we can return after logging in.
            String path = req.getContextPath();
            String info = req.getPathInfo();
            if (info != null) {
                path = path + info;
            }
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.addCookie(new Cookie("originalURL", path));
            resp.sendRedirect("/" + loginPath);
        }
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
