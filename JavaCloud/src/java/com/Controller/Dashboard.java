/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    private static final int BYTES_DOWNLOAD = 1024;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    String UserId;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer ID = Integer.parseInt(session.getAttribute("userId").toString());
        UserId = String.valueOf(ID);

        String hdnData = request.getParameter("hdnData");
        String btnRawsearch = request.getParameter("btnRawsearch");
        String btnValidsearch = request.getParameter("btnValidsearch");
        String btnFixsearch = request.getParameter("btnFixsearch");
        String btnEFsearch = request.getParameter("btnEFsearch");

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
            dc.getfileNames("RawData", "MF", UserId, arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            RequestDispatcher rd = request.getRequestDispatcher("/RawDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("VD".equalsIgnoreCase(hdnData)) {
            dc.getfileNames("ValidData", "MD", UserId, arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            request.setAttribute("arrTime", arrTime);
            RequestDispatcher rd = request.getRequestDispatcher("/ValidDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("FD".equalsIgnoreCase(hdnData)) {
            dc.getfileNames("FixData", "MD", UserId, arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            request.setAttribute("Alrd_lbl_column", arrColumn_sac); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_lbl_value", arrValue_sac);
            RequestDispatcher rd = request.getRequestDispatcher("/FixDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("EF".equalsIgnoreCase(hdnData)) { // Eye Feature
            dc.getfileNames("EyeFeature", "MD", UserId, arrls);
            request.setAttribute("arrls", arrls);
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            RequestDispatcher rd = request.getRequestDispatcher("/EyeFeatureList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("kill".equalsIgnoreCase(hdnData)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/loginuser.jsp");
        } else if ("DF".equalsIgnoreCase(hdnData)) { // If It Is fIxatIon FIle
            DownloadFile(response, hdnData); // download FixatIon FILe
        } else if ("DS".equalsIgnoreCase(hdnData)) { // If It Is saccade FIle
            DownloadFile(response, hdnData); // download saccade FILe
        }

        if ("Raw".equalsIgnoreCase(btnRawsearch)) {
            RawData(request, response);
        } else if ("Valid".equalsIgnoreCase(btnValidsearch)) {
            ValidData(request, response);
        } else if ("Fix".equalsIgnoreCase(btnFixsearch)) {
            FixSaccData(request, response);
        } else if ("EyeFeature".equalsIgnoreCase(btnEFsearch)) {
            EyeFeature(request, response);
        }


    }

    private void DownloadFile(HttpServletResponse response, String filetype) throws FileNotFoundException, IOException {
        response.setContentType("text/plain");
        File file;
        if (filetype.equals("DF")) {
            response.setHeader("Content-Disposition",
                    "attachment;filename=Fixation.txt");
            file = new File(getServletContext().getRealPath("/") + "download/" + "fix.txt");
        } else {
            response.setHeader("Content-Disposition",
                    "attachment;filename=Saccade.txt");
            file = new File(getServletContext().getRealPath("/") + "download/" + "sac.txt");
        }

        FileInputStream fileIn = new FileInputStream(file);
        //ServletOutputStream out = response.getOutputStream();
        OutputStream out = response.getOutputStream();

        byte[] outputByte = new byte[4096];
        int byteRead;
        while ((byteRead = fileIn.read(outputByte, 0, 4096)) != -1) {
            out.write(outputByte, 0, byteRead);
        }
        fileIn.close();
        out.flush();
        out.close();
    }

    private void RawData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue");
        fileName = request.getParameter("hdnselectText");
        dc.get_DataHbase_common(0, 1000, "ok", UserId, "RawData", fileName, "MF", Alrd_column, Alrd_value); // getting raw data from Hbase
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
        dc.get_DataHbase(0, 1000, UserId, "ValidData", fileName, Alrd_column, Alrd_value, arrTime);
        request.setAttribute("fileName", hdnselectvalue);
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        request.setAttribute("arrTime", arrTime);
        RequestDispatcher rd = request.getRequestDispatcher("/ValidDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void FixSaccData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DataClass dc = new DataClass(getServletContext().getRealPath("/"));
        hdnselectvalue = request.getParameter("hdnselectvalue");
        fileName = request.getParameter("hdnselectText");

        dc.get_DataHbase_WriteTextFile(0, 0, "fix", UserId, "FixData", fileName, "MD", Alrd_column, Alrd_value);//reading fixation
        dc.get_DataHbase_WriteTextFile(0, 0, "sac", UserId, "FixData", fileName + "-S-", "MD", arrColumn_sac, arrValue_sac);// Adding -S- in the filename to make it varry from fix and reading Saccade
        request.setAttribute("fileName", hdnselectvalue);
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        request.setAttribute("Alrd_lbl_column", arrColumn_sac); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_lbl_value", arrValue_sac);
        RequestDispatcher rd = request.getRequestDispatcher("/FixDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void EyeFeature(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue");
        fileName = request.getParameter("hdnselectText");
        dc.get_DataHbase_common(0, 1000, "", UserId, "EyeFeature", fileName, "MD", Alrd_column, Alrd_value); // getting raw data from Hbase
        request.setAttribute("fileName", hdnselectvalue);
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        RequestDispatcher rd = request.getRequestDispatcher("/EyeFeatureList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
