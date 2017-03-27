package com.example.filter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link AccessFilter}
 */
public class AccessFilterTest {

    @Test
    public void testNoAuth() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        Filter accessFilter = constructFilter();
        accessFilter.doFilter(request, response, filterChain);

        verify(request).getCookies();
        verify(response).sendRedirect("/login");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testValidAuth() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("LCBOAuth", "Authorized");
        when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        Filter accessFilter = constructFilter();
        accessFilter.doFilter(request, response, filterChain);

        verify(request).getCookies();
        verify(response, never()).sendRedirect("/login");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testInvalidAuth() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("LCBOAuth", "Expired");
        when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        Filter accessFilter = constructFilter();
        accessFilter.doFilter(request, response, filterChain);

        verify(request, times(1)).getCookies();
        verify(response, times(1)).sendRedirect("/login");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testReturnCookie() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("/product");
        when(request.getPathInfo()).thenReturn("/104329");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        Filter accessFilter = constructFilter();
        accessFilter.doFilter(request, response, filterChain);

        verify(request).getCookies();
        verify(response).sendRedirect("/login");
        verify(filterChain, never()).doFilter(request, response);

        ArgumentCaptor<Cookie> cookieArg = ArgumentCaptor.forClass(Cookie.class);
        verify(response, times(1)).addCookie(cookieArg.capture());
        assertEquals("originalURL", cookieArg.getValue().getName());
        assertEquals("/product/104329", cookieArg.getValue().getValue());
    }

    private Filter constructFilter() throws ServletException {
        Filter accessFilter = new AccessFilter();
        FilterConfig fc = mock(FilterConfig.class);
        when(fc.getInitParameter("login_path")).thenReturn("login");
        accessFilter.init(fc);
        return accessFilter;
    }
}