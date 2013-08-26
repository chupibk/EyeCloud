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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String txtfname = request.getParameter("txtfname");
        String txtemail = request.getParameter("txtemail");
        String txtpass = request.getParameter("txtpass");
        String txtCpass = request.getParameter("txtCpass");
        String txtcountry = request.getParameter("txtcountry");
        String txtcity = request.getParameter("txtcity");
        String txtadd = request.getParameter("txtadd");
        String txtphone = request.getParameter("txtphone");
        String txtmob = request.getParameter("txtmob");
        String txtpostal = request.getParameter("txtpostal");
        String txtstate = request.getParameter("txtstate");
        String btnlogin=request.getParameter("btnlogin");
        
        int result = 0;
        if (txtpass.equals(txtCpass)) {
            if (!txtfname.isEmpty() && !txtemail.isEmpty()
                     && !txtcountry.isEmpty() && !txtstate.isEmpty()
                    && !txtcity.isEmpty() && !txtadd.isEmpty()
                    && !txtphone.isEmpty()) {
                result = dc.RegIsteruser_mysql(txtfname, txtemail,
                        txtpass, txtcountry, txtstate,
                        txtcity, txtadd, txtmob,
                        txtphone, txtpostal);

                if (result == 1) {
                    
                } else {
                    request.setAttribute("name", txtfname);
                    request.setAttribute("email", txtemail);
                    request.setAttribute("country", txtcountry);
                    request.setAttribute("state", txtstate);
                    request.setAttribute("city", txtcity);
                    request.setAttribute("address", txtadd);
                    request.setAttribute("mobile", txtmob);
                    request.setAttribute("phone", txtphone);
                    request.setAttribute("postal", txtpostal);
                    RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                    rd.forward(request, response);
                }
            } else {
                request.setAttribute("error", "1");
                request.setAttribute("name", txtfname);
                request.setAttribute("email", txtemail);
                request.setAttribute("country", txtcountry);
                request.setAttribute("state", txtstate);
                request.setAttribute("city", txtcity);
                request.setAttribute("address", txtadd);
                request.setAttribute("mobile", txtmob);
                request.setAttribute("phone", txtphone);
                request.setAttribute("postal", txtpostal);
                RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                rd.forward(request, response);
            }
        } else if("login".equalsIgnoreCase(btnlogin)) {
           int hold=dc.loginUser(txtemail, txtpass);
           System.out.println(hold);
            
        }
        else {
            request.setAttribute("error", "2");
            request.setAttribute("name", txtfname);
            request.setAttribute("email", txtemail);
            request.setAttribute("country", txtcountry);
            request.setAttribute("state", txtstate);
            request.setAttribute("city", txtcity);
            request.setAttribute("address", txtadd);
            request.setAttribute("mobile", txtmob);
            request.setAttribute("phone", txtphone);
            request.setAttribute("postal", txtpostal);
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        }


    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
