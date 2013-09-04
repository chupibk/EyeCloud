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
import javax.servlet.http.HttpSession;

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
    boolean flagupdate = false;
    int result = 0;

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
        if ("kill".equalsIgnoreCase(request.getParameter("hdnData"))) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/loginuser.jsp");
        } else if ("edit".equalsIgnoreCase(request.getParameter("hdnData"))) { // users wants to see hIs profIle
            HttpSession session = request.getSession(false);
            session.setAttribute("hdnData", "edit");
            getUserDetails(request, response);
            flagupdate = true;
            

        } else if (txtpass.equals(txtCpass)) { // if password is same
            if (!txtemail.isEmpty() && !txtpass.isEmpty() && !txtfname.isEmpty()) { // if required field Is not empty
                if (flagupdate) { // users wants to update his profle
                    UpdateUser(request, response);
                } else { // users wants to register
                    registerUser(request, response);
                }

            } else { // requIre fIeld Is empty
                request.setAttribute("error", "1");
                setAttribute(request);
                RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                rd.forward(request, response);
            }
        } else if ("login".equalsIgnoreCase(btnlogin)) { //If login button Is pressed

            int loginUser=dc.loginUser(txtemail, txtpass);
            if (loginUser > 0) { // successfull logiN
                HttpSession session = request.getSession(false);
                session.setAttribute("username", dc.username);
                session.setAttribute("userId", loginUser);
                RequestDispatcher rd = request.getRequestDispatcher("/Dashboard.jsp");
                rd.forward(request, response);
            } else { // there Is some error In logIn
                request.setAttribute("error", "1");
                RequestDispatcher rd = request.getRequestDispatcher("/loginuser.jsp");
                rd.forward(request, response);
            }
        } else if ("register".equalsIgnoreCase(btnregister)) { //If regIster button Is pressed
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);

        } else { // if password doesnt match wIth confIrm password
            request.setAttribute("error", "2");
            setAttribute(request);
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            if (result == 1) { //iF user iS regIster successfully
                RequestDispatcher rd = request.getRequestDispatcher("/loginuser.jsp");
                rd.forward(request, response);
            } else { //if user can not regIster
                setAttribute(request);
                request.setAttribute("error", "5");
                RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                rd.forward(request, response);
            }
        }

    }

    private void UpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String Id = String.valueOf(session.getAttribute("userId"));
        result = dc.UpdateUser_detail(txtfname, Id, txtpass, txtcountry, txtstate, txtcity, txtadd, txtmob, txtphone, txtpostal);
        if (result == 1) {
            session.setAttribute("username", txtfname);
            request.setAttribute("success", "1");
            request.setAttribute("error", "0");
            setAttribute(request);
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("error", "6");
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
        if(flagupdate){
            request.setAttribute("btnlabel", "1");
            request.setAttribute("hdnData", "edit");
            
        }
    }

    private void getUserDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userid = Integer.parseInt(session.getAttribute("userId").toString());
        dc.getUserDetails(userid);
        
        request.setAttribute("name", dc.username);
        request.setAttribute("email", dc.email);
        request.setAttribute("country", dc.country);
        request.setAttribute("state", dc.state);
        request.setAttribute("city", dc.city);
        request.setAttribute("address", dc.address);
        request.setAttribute("mobile", dc.mobileNO);
        request.setAttribute("phone", dc.phoneNo);
        request.setAttribute("postal", dc.postalcode);
        request.setAttribute("btnlabel", "1");
        RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
        rd.forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
