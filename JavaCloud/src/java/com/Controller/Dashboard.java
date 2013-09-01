/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
@WebServlet(name = "Dashboard", urlPatterns = {"/Dashboard"})
public class Dashboard extends HttpServlet {

    DataClass dc = new DataClass();
    LinkedHashMap<String, String> arrls = new LinkedHashMap<String, String>();
    ArrayList<String> Alrd_value = new ArrayList<String>();
    ArrayList<String> Alrd_column = new ArrayList<String>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String hdnData = request.getParameter("hdnData");
        if (arrls.size() != 0) {
            arrls.clear();
        }
        if ("RD".equalsIgnoreCase(hdnData)) {
            dc.getfileNames("RawData", "MF", "1", arrls);
            Alrd_column.clear();
            Alrd_value.clear();

        } else if ("VD".equalsIgnoreCase(hdnData)) {
        } else if ("FD".equalsIgnoreCase(hdnData)) {
        }
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        RequestDispatcher rd = request.getRequestDispatcher("/RawDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);


    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
