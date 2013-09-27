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
    String UserId,fileName = "", hdnselectvalue = "";
    private static final int BYTES_DOWNLOAD = 1024;
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // intializing session
        Integer ID = Integer.parseInt(session.getAttribute("userId").toString()); // storing sessIon value Into the ID VarIable 
        UserId = String.valueOf(ID);// convertIng ID Into StrIng

        String hdnData = request.getParameter("hdnData"); // gettIng value of Hidden hdnData frm Jsp page Into servlet
        String btnRawsearch = request.getParameter("btnRawsearch");// gettIng value of button btnRawsearch frm Jsp page Into servlet
        String btnValidsearch = request.getParameter("btnValidsearch");// gettIng value of button btnValidsearch frm Jsp page Into servlet
        String btnFixsearch = request.getParameter("btnFixsearch");// gettIng value of button btnFixsearch frm Jsp page Into servlet
        String btnEFsearch = request.getParameter("btnEFsearch");// gettIng value of button btnEFsearch frm Jsp page Into servlet

        Alrd_column.clear(); // clearing the arraylIst
        Alrd_value.clear(); // clearing the arraylIst
        arrTime.clear();  // clearing the arraylIst
        arrColumn_sac.clear(); // clearing the arraylIst
        arrValue_sac.clear(); // clearing the arraylIst
        if (hdnData != null && !hdnData.equals("") && !hdnData.equals("DF") && !hdnData.equals("DS")) { //checkIng the hdndata value that If Its null or not
            if (arrls.size() != 0) {
                arrls.clear(); // clearing the arraylIst
            }
        }
        if ("RD".equalsIgnoreCase(hdnData)) { // If rawData page Is clIcked
            dc.getfileNames("RawData", "MF", UserId, arrls); // get all the fIle names of the raw Data as per user Id
            request.setAttribute("arrls", arrls); // set array lIst to pass to the next page
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            RequestDispatcher rd = request.getRequestDispatcher("/RawDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("VD".equalsIgnoreCase(hdnData)) { // If ValId Data page Is clIcked
            dc.getfileNames("ValidData", "MD", UserId, arrls); // get all the fIle names of the ValId Data as per user Id
            request.setAttribute("arrls", arrls); // set array lIst to pass to the next page
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            request.setAttribute("arrTime", arrTime);
            RequestDispatcher rd = request.getRequestDispatcher("/ValidDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("FD".equalsIgnoreCase(hdnData)) { // If Fixation Data page Is clIcked
            dc.getfileNames("FixData", "MD", UserId, arrls); // get all the fIle names of the Fixation Data as per user Id
            request.setAttribute("arrls", arrls); // set array lIst to pass to the next page
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            request.setAttribute("Alrd_lbl_column", arrColumn_sac); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_lbl_value", arrValue_sac);
            RequestDispatcher rd = request.getRequestDispatcher("/FixDataList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("EF".equalsIgnoreCase(hdnData)) { // // If Eye Feature page Is clIcked
            dc.getfileNames("EyeFeature", "MD", UserId, arrls); // get all the fIle names of the Eye Feature as per user Id
            request.setAttribute("arrls", arrls); // set array lIst to pass to the next page
            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", Alrd_value);
            RequestDispatcher rd = request.getRequestDispatcher("/EyeFeatureList.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("kill".equalsIgnoreCase(hdnData)) {
            request.getSession().invalidate(); // killiNg session here
            response.sendRedirect(request.getContextPath() + "/loginuser.jsp");
        } else if ("DF".equalsIgnoreCase(hdnData) && !"Fix".equalsIgnoreCase(btnFixsearch)) { // If download fIxatIon FIle is clicked
            DownloadFile(response, hdnData); // download FixatIon FILe
            request.setAttribute("arrls", arrls);
        } else if ("DS".equalsIgnoreCase(hdnData)  && !"Fix".equalsIgnoreCase(btnFixsearch)) { // If download saccade FIle is clicked
            DownloadFile(response, hdnData); // download saccade FILe
            request.setAttribute("arrls", arrls);
        }

        if ("Raw".equalsIgnoreCase(btnRawsearch)) {
            RawData(request, response); // reading and dIsplayIng raw data well btnRawsearch Is clIcked
        } else if ("Valid".equalsIgnoreCase(btnValidsearch)) {
            ValidData(request, response); // reading and dIsplayIng Valid data well btnValidsearch Is clIcked
        } else if ("Fix".equalsIgnoreCase(btnFixsearch)) {
            FixSaccData(request, response); // reading and dIsplayIng Fix data well btnFixsearch Is clIcked
        } else if ("EyeFeature".equalsIgnoreCase(btnEFsearch)) {
            EyeFeature(request, response); // reading and dIsplayIng EyeFeature data well btnEFsearch Is clIcked
        }


    }

    private void DownloadFile(HttpServletResponse response, String filetype) throws FileNotFoundException, IOException {
        response.setContentType("text/plain");
        int len=getServletContext().getRealPath("/").indexOf("PipeLine/");
        
        String filepath = getServletContext().getRealPath("/").substring(0, len);
        
        File file;
        if (filetype.equals("DF")) { // If download FIxatIon fIle Is clIcked
            response.setHeader("Content-Disposition",
                    "attachment;filename=Fixation.txt");
           file = new File(filepath + "PipeLine/" + "fix.txt");  // gettIng path to download fIxatIon FIle
        } else { // else download saccade fIle
            response.setHeader("Content-Disposition",
                    "attachment;filename=Saccade.txt");
            file = new File(filepath + "PipeLine/" + "sac.txt"); // gettIng path to download saccade FIle
        }

        FileInputStream fileIn = new FileInputStream(file); // assIgIng the FIle to fIleInput sream
        //ServletOutputStream out = response.getOutputStream();
        OutputStream out = response.getOutputStream();

        byte[] outputByte = new byte[4096]; // assIngIng bytes 
        int byteRead;
        while ((byteRead = fileIn.read(outputByte, 0, 4096)) != -1) { // readIng FIle bytes UntIl bytes exIsts
            out.write(outputByte, 0, byteRead); // wrItIng bytes Into the textFIle
        }
        fileIn.close();     // closIng fIle
        out.flush(); // flushIng fIle
        out.close(); // closIng output stream
    }

    private void RawData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue"); // gettIng total numbers of rawData rows from the textFIle 
        fileName = request.getParameter("hdnselectText"); // settIng textFIle of RawData Into the FIle name
        dc.get_DataHbase_common(0, 1000, "ok", UserId, "RawData", fileName, "MF", Alrd_column, Alrd_value); // getting raw data from Hbase
        request.setAttribute("fileName", hdnselectvalue); // setting hdnselectvalue for forwarding data to next page
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        RequestDispatcher rd = request.getRequestDispatcher("/RawDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void ValidData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue");// gettIng total numbers of ValId Data rows from the textFIle 
        fileName = request.getParameter("hdnselectText"); // settIng textFIle of ValId Data Into the FIle name
        dc.get_DataHbase(0, 1000, UserId, "ValidData", fileName, Alrd_column, Alrd_value, arrTime); // getting valId data from Hbase
        request.setAttribute("fileName", hdnselectvalue); // setting hdnselectvalue for forwarding data to next page
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        request.setAttribute("arrTime", arrTime);
        RequestDispatcher rd = request.getRequestDispatcher("/ValidDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void FixSaccData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DataClass dc = new DataClass(getServletContext().getRealPath("/"));
        System.out.println(getServletContext().getRealPath("/")+" ===psppspsts");
        
        hdnselectvalue = request.getParameter("hdnselectvalue"); // gettIng total numbers of fIxatIon & Saccade data rows from the textFIle 
        fileName = request.getParameter("hdnselectText"); // settIng textFIle of fIxatIon & Saccade Data Into the FIle name

        dc.get_DataHbase_WriteTextFile(0, 0, "fix", UserId, "FixData", fileName, "MD", Alrd_column, Alrd_value);//reading fixation
        dc.get_DataHbase_WriteTextFile(0, 0, "sac", UserId, "FixData", fileName + "-S-", "MD", arrColumn_sac, arrValue_sac);// Adding -S- in the filename to make it varry from fix and reading Saccade
        request.setAttribute("fileName", hdnselectvalue); // setting hdnselectvalue for forwarding data to next page
        
        request.setAttribute("arrls", arrls);
        request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_value", Alrd_value);
        request.setAttribute("Alrd_lbl_column", arrColumn_sac); // setting array List for forwarding data to next page
        request.setAttribute("Alrd_lbl_value", arrValue_sac);
        RequestDispatcher rd = request.getRequestDispatcher("/FixDataList.jsp"); // redirecting to the next page
        rd.forward(request, response);
    }

    private void EyeFeature(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        hdnselectvalue = request.getParameter("hdnselectvalue"); // gettIng total numbers of eyeFeature rows from the textFIle 
        fileName = request.getParameter("hdnselectText"); // settIng textFIle of eyeFeature Data Into the FIle name
        dc.get_DataHbase_common(0, 1000, "", UserId, "EyeFeature", fileName, "MD", Alrd_column, Alrd_value); // getting eyefeature from Hbase
        request.setAttribute("fileName", hdnselectvalue); // setting hdnselectvalue for forwarding data to next page
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
