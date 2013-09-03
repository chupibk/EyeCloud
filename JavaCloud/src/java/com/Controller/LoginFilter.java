/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samsalman
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    ArrayList<String> ar = new ArrayList<String>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
        ar.add("/JavaCloud/loginuser.jsp");
        ar.add("/JavaCloud/registeruser");
        ar.add("/JavaCloud/registeruser.jsp");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        boolean allowedRequest = false;

        if (ar.contains(request.getRequestURI())) {
            allowedRequest = true;
        }

        if (!allowedRequest) {
            if (session == null || session.getAttribute("username") == null) {
                response.sendRedirect(request.getContextPath() + "/loginuser.jsp");
                return;
            } else {
                chain.doFilter(req, res);
                return;
            }
        } else {

            chain.doFilter(req, res);
            return;

        }
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
}
