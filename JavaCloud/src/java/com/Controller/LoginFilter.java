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
        AddIntolist();
        
    }
    
    private void AddIntolist(){
        ar.add("/JavaCloud/loginuser.jsp");
        ar.add("/JavaCloud/registeruser");
        ar.add("/JavaCloud/registeruser.jsp");
        ar.add("/JavaCloud/css/admin-login.css");
        ar.add("/JavaCloud/css/layout/login/login_wrapper_bg.gif");
        ar.add("/JavaCloud/css/layout/login/form_bg.gif");
        ar.add("/JavaCloud/css/layout/login/submit_left.gif");
        ar.add("/JavaCloud/css/layout/login/logo.png");
        ar.add("/JavaCloud/css/layout/login/submit_middle.gif");
        ar.add("/JavaCloud/img/logo1.png");
        ar.add("/JavaCloud/css/layout/login/submit_right.gif");
        ar.add("/JavaCloud/css/admin.css");
        ar.add("/JavaCloud/css/tables.css");
        ar.add("/JavaCloud/css/forms.css");
        ar.add("/JavaCloud/css/dashboard.css");
        ar.add("/JavaCloud/css/layout/site/tables/approve_middle.gif");
        ar.add("/JavaCloud/css/general.css");
        ar.add("/JavaCloud/css/layout/site/tables/approve_left.gif");
        ar.add("/JavaCloud/css/layout/site/content_bottom.gif");
        ar.add("/JavaCloud/css/layout/site/title_wrapper_right.png");
        ar.add("/JavaCloud/css/layout/site/sct_right.gif");
        ar.add("/JavaCloud/css/layout/site/scb.png");
        ar.add("/JavaCloud/css/layout/site/title_wrapper_right2.png");
        ar.add("/JavaCloud/css/layout/site/sidebar_menu_bg.gif");
        ar.add("/JavaCloud/css/layout/site/scb2.png");
        ar.add("/JavaCloud/css/pagination.css");
        ar.add("/JavaCloud/css/layout/site/content_bottom.gif");
        ar.add("/JavaCloud/css/layout/site/title_wrapper_left.png");
        ar.add("/JavaCloud/css/layout/site/tables/approve_middle.gif");
        ar.add("/JavaCloud/css/layout/site/tables/approve_left.gif");
        ar.add("/JavaCloud/css/layout/site/title_wrapper_left2.png");
        ar.add("/JavaCloud/css/layout/site/sct_left_right2.gif");
        ar.add("/JavaCloud/css/layout/site/scb_left2.png");
        ar.add("/JavaCloud/css/layout/site/content_top.png");
        ar.add("/JavaCloud/css/layout/site/sct_left.gif");
        ar.add("/JavaCloud/css/layout/site/title_wrapper_left2.png");
        ar.add("/JavaCloud/css/layout/site/sidebar_lk_bg.gif");
        ar.add("/JavaCloud/css/layout/site/sct_left_right.gif");
        ar.add("/JavaCloud/css/layout/site/title_wrapper_middle.png");
        ar.add("/JavaCloud/css/layout/site/tables/approve_right.gif");
        ar.add("/JavaCloud/css/layout/site/scb_left.png");
        ar.add("/JavaCloud/css/layout/site/scb_right.png");
        ar.add("/JavaCloud/css/layout/site/scb_right2.png");
        ar.add("/JavaCloud/css/layout/site/sct_left2.gif");
        ar.add("/JavaCloud/css/layout/site/sct_right2.gif");

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        boolean allowedRequest = false;

        if (ar.contains(request.getRequestURI())) {
            allowedRequest = true;
        } else {
            System.out.println(request.getRequestURI());
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
