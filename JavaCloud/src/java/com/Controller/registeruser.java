/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author samsalman
 */
@WebServlet(name = "registeruser", urlPatterns = {"/registeruser"})
public class registeruser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    DataClass dc = new DataClass();
    String txtfname, txtemail, txtpass, txtCpass, txtcountry, txtcity, txtadd,
            txtphone, txtmob, txtpostal, txtstate, btnlogin, btnregister;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        txtfname = request.getParameter("txtfname");
        txtemail = request.getParameter("txtemail");
        txtpass = request.getParameter("txtpass");
        txtCpass = request.getParameter("txtCpass");
        txtcountry = request.getParameter("txtcountry");
        txtcity = request.getParameter("txtcity");
        txtadd = request.getParameter("txtadd");
        txtphone = request.getParameter("txtphone");
        txtmob = request.getParameter("txtmob");
        txtpostal = request.getParameter("txtpostal");
        txtstate = request.getParameter("txtstate");
        btnlogin = request.getParameter("btnlogin");
        btnregister = request.getParameter("btnregister");

        int result = 0;
        if (txtpass.equals(txtCpass)) { // if password is same
            if (!txtemail.isEmpty() && !txtpass.isEmpty()) { // if required field Is not empty
                if (dc.CheckEmaIl(txtemail) != 0) { //If emaIl already exIsts
                    request.setAttribute("error", "3");
                    setAttribute(request);
                    RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                    rd.forward(request, response);
                } else {
                    result = dc.RegIsteruser_mysql(txtfname, txtemail,
                            txtpass, txtcountry, txtstate,
                            txtcity, txtadd, txtmob,
                            txtphone, txtpostal);

                    if (result == 1) {
                        RequestDispatcher rd = request.getRequestDispatcher("/loginuser.jsp");
                        rd.forward(request, response);
                    } else { //if user can not regIster
                        setAttribute(request);
                        RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                        rd.forward(request, response);
                    }
                }
            } else {
                request.setAttribute("error", "1");
                setAttribute(request);
                RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                rd.forward(request, response);
            }
        } else if ("login".equalsIgnoreCase(btnlogin)) { //If login button Is pressed

            if (dc.loginUser(txtemail, txtpass) > 0) {
                RequestDispatcher rd = request.getRequestDispatcher("/Dashboard.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("error", "1");
                RequestDispatcher rd = request.getRequestDispatcher("/loginuser.jsp");
                rd.forward(request, response);
            }
        } else if ("register".equalsIgnoreCase(btnregister)) { //If regIster button Is pressed
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);

        } else {
            request.setAttribute("error", "2");
            setAttribute(request);
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        }
    }

    private void setAttribute(HttpServletRequest request) {
        request.setAttribute("name", txtfname);
        request.setAttribute("email", txtemail);
        request.setAttribute("country", txtcountry);
        request.setAttribute("state", txtstate);
        request.setAttribute("city", txtcity);
        request.setAttribute("address", txtadd);
        request.setAttribute("mobile", txtmob);
        request.setAttribute("phone", txtphone);
        request.setAttribute("postal", txtpostal);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
