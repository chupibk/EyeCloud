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
    ArrayList<String> arrTime = new ArrayList<String>();
    ArrayList<String> arrColumn_sac = new ArrayList<String>();
    ArrayList<String> arrValue_sac = new ArrayList<String>();
    String fileName = "", hdnselectvalue = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String hdnData = request.getParameter("hdnData");
        String btnRawsearch = request.getParameter("btnRawsearch");
        String btnValidsearch = request.getParameter("btnValidsearch");
        String btnFixsearch = request.getParameter("btnFixsearch");

        Alrd_column.clear();
        Alrd_value.clear();
        arrTime.clear();
        arrColumn_sac.clear();
        arrValue_sac.clear();
        if (hdnData != null && !hdnData.equals("")) {
            if (arrls.size() != 0) {
                arrls.clear();
            }
        }
        if ("RD".equalsIgnoreCase(hdnData)) {
            dc.getfileNames("RawData", "MF", "1", arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            RequestDispatcher rd = request.getRequestDispatcher("/RawDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("VD".equalsIgnoreCase(hdnData)) {
            dc.getfileNames("ValidData", "MD", "1", arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            request.setAttribute("arrTime", arrTime);
            RequestDispatcher rd = request.getRequestDispatcher("/ValidDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("FD".equalsIgnoreCase(hdnData)) {
            dc.getfileNames("FixData", "MD", "1", arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            request.setAttribute("Alrd_lbl_column", arrColumn_sac); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_lbl_value", arrValue_sac);
            RequestDispatcher rd = request.getRequestDispatcher("/FixDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("kill".equalsIgnoreCase(hdnData)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/loginuser.jsp");
        }

        if ("Raw".equalsIgnoreCase(btnRawsearch)) {
            RawData(request, response);
        } else if ("Valid".equalsIgnoreCase(btnValidsearch)) {
            ValidData(request, response);
        } else if ("Fix".equalsIgnoreCase(btnFixsearch)) {
            FixSaccData(request, response);
        }


    }

    private void RawData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue");
        fileName = request.getParameter("hdnselectText");
        dc.get_DataHbase_common(0, 1000, "ok", "1", "RawData", fileName, "MF", Alrd_column, Alrd_value); // getting raw data from Hbase
        request.setAttribute("fileName", hdnselectvalue);
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        RequestDispatcher rd = request.getRequestDispatcher("/RawDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void ValidData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue");
        fileName = request.getParameter("hdnselectText");
        dc.get_DataHbase(0, 1000, "1", "ValidData", fileName, Alrd_column, Alrd_value, arrTime);
        request.setAttribute("fileName", hdnselectvalue);
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        request.setAttribute("arrTime", arrTime);
        RequestDispatcher rd = request.getRequestDispatcher("/ValidDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void FixSaccData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue");
        fileName = request.getParameter("hdnselectText");
        dc.get_DataHbase_common(0, 0, "", "1", "FixData", fileName, "MD", Alrd_column, Alrd_value);//reading fixation
        dc.get_DataHbase_common(0, 0, "", "1", "FixData", fileName + "-S-", "MD", arrColumn_sac, arrValue_sac);// Adding -S- in the filename to make it varry from fix and reading Saccade
        request.setAttribute("fileName", hdnselectvalue);
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        request.setAttribute("Alrd_lbl_column", arrColumn_sac); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_lbl_value", arrValue_sac);
        RequestDispatcher rd = request.getRequestDispatcher("/FixDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
