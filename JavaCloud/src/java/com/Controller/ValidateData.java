/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.RegionException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samsalman
 */
@WebServlet(name = "ValidateData", urlPatterns = {"/ValidateData"})
public class ValidateData extends HttpServlet {

    private static Configuration conf = null;
    ArrayList<String> arrColumn = new ArrayList<String>();
    ArrayList<String> arrValue = new ArrayList<String>();
    ArrayList<String> arrTime = new ArrayList<String>();
    ArrayList<String> arrAvgColumn = new ArrayList<String>();
    ArrayList<String> arrAvgValue = new ArrayList<String>();
    ArrayList<String> arrColumn_lbl = new ArrayList<String>();
    ArrayList<String> arrValue_lbl = new ArrayList<String>();
    DataClass dc = new DataClass();
    long loopStarter, looprunner = 1000;

    static {
        conf = HBaseConfiguration.create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public void Read_LabelData_forValdiation(String Efilename, String filename) {
        HTable table = null;
        try {

            counter = 0;
            int begin, end;
            long NosRow = Integer.valueOf(dc.get_MapFile("RawData", filename, "MF"));
            boolean breakLoop;
            table = new HTable(conf, "RawData");
            for (int a = 0; a <= NosRow - 1; a++) {
                breakLoop = false;
                Get get = new Get(Bytes.toBytes("1" + ":" + filename + ":" + a));
                Result result = table.get(get);
                arrColumn.clear();
                arrValue.clear();

                for (KeyValue kv : result.raw()) {
                    arrColumn.add(new String(kv.getQualifier()));
                    arrValue.add(new String(kv.getValue()));
                }
                begin = arrColumn.indexOf("Begin Time - msec");
                end = arrColumn.indexOf("End Time - msec");

                if ((arrValue.get(begin).equals("") && arrValue.get(end).equals(""))) // If row is blank or contains garbage
                {
                    breakLoop = true;
                }
                if (!breakLoop) {
                    addvalidData(filename, "LD", arrColumn, arrValue);
                }
            }
            table.close();
            dc.InsertMapRecord("ValidData", Efilename, "LD", "1", filename); //Insert validData file name as row and Label data Filename as value 
            dc.InsertMapRecord("ValidData", filename, "LD", "1", String.valueOf(counter)); // here I am storig nos of rows of ValIDData Into LD(Label Data) column FamIly
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Read_RawData_forValidation(String filename, String gxleft, String gxright,
            String gyleft, String gyright, String dleft, String dright) {

        HTable table = null;
        try {

            counter = 0;
            long NosRow = Integer.valueOf(dc.get_MapFile("RawData", filename, "MF"));
            int vLindex, vRindex, dLindex, dRindex, gpXLindex,
                    gpXRindex, gpYLindex, gpYRindex;
            Double DLeft, DRight, gpXleft, gpYleft;
            int DLlength, DRlenght;
            boolean breakflag;
            table = new HTable(conf, "RawData");
            for (long a = 0; a <= NosRow - 1; a++) {
                breakflag = false;
                Get get = new Get(Bytes.toBytes("1" + ":" + filename + ":" + a));
                Result result = table.get(get);
                arrColumn.clear();
                arrValue.clear();
                for (KeyValue kv : result.raw()) {
                    arrColumn.add(new String(kv.getQualifier()));
                    arrValue.add(new String(kv.getValue()));
                }
                vLindex = arrColumn.indexOf("ValidityLeft");
                vRindex = arrColumn.indexOf("ValidityRight");
                dLindex = arrColumn.indexOf("DistanceLeft");
                dRindex = arrColumn.indexOf("DistanceRight");
                gpXLindex = arrColumn.indexOf("GazePointXLeft");
                gpXRindex = arrColumn.indexOf("GazePointXRight");
                gpYLindex = arrColumn.indexOf("GazePointYLeft");
                gpYRindex = arrColumn.indexOf("GazePointYRight");

                if (arrValue.get(vLindex).equals("4") && arrValue.get(vRindex).equals("4")) // For Validity L & R
                {
                    breakflag = true;
                } else if ((arrValue.get(vLindex).equals("0") || arrValue.get(vLindex).equals("3")) && arrValue.get(vRindex).equals("4")) // For Validity L=0 or 3 & then Update R
                {
                    arrValue.set(gpXRindex, arrValue.get(gpXLindex));
                    arrValue.set(gpYRindex, arrValue.get(gpYLindex));

                } else if (arrValue.get(vLindex).equals("4") && (arrValue.get(vRindex).equals("0") || arrValue.get(vRindex).equals("3"))) // For Validity R=0 or 3 & then Update L
                {
                    arrValue.set(gpXLindex, arrValue.get(gpXRindex));
                    arrValue.set(gpYLindex, arrValue.get(gpYRindex));

                } else if ((arrValue.get(vLindex).equals("") && arrValue.get(vRindex).equals(""))) // If row is blank or contains garbage
                {
                    breakflag = true;
                }
                if (!breakflag) {
                    DLeft = new Double(arrValue.get(dLindex));
                    DLlength = (int) (Math.log10(DLeft.intValue()) + 1);
                    if (DLlength != 2) {
                        DLeft = DLeft / 10;
                    }
                    DRight = new Double(arrValue.get(dRindex));
                    DRlenght = (int) (Math.log10(DRight.intValue()) + 1);
                    if (DRlenght != 2) {
                        DRight = DRight / 10;
                    }
                    addvalidData(filename, "VD", arrColumn, arrValue);

//                    arrAvgColumn.add("Timestamp");
//                    arrAvgValue.add(arrValue.get(arrColumn.indexOf("Timestamp")));
//
//                    arrAvgColumn.add("AvgDist");
//                    if (dright != null && dleft != null) {
//                        DLeft = (DLeft + DRight) / 2;
//                        arrAvgValue.add(String.valueOf(Math.round(DLeft)));
//                    } else if (dright != null) {
//                        arrAvgValue.add(String.valueOf(Math.round(DRight)));
//                    } else if (dleft != null) {
//                        arrAvgValue.add(String.valueOf(Math.round(DLeft)));
//                    }
//
//                    arrAvgColumn.add("AvgGxleft");
//                    if (gxleft != null && gxright != null) {
//                        gpXleft = (Double.valueOf(arrValue.get(gpXLindex)) + Double.valueOf(arrValue.get(gpXRindex))) / 2;
//                        arrAvgValue.add(String.valueOf(Math.round(gpXleft)));
//                    } else if (gxleft != null) {
//                        arrAvgValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpXLindex)))));
//                    } else if (gxright != null) {
//                        arrAvgValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpXRindex)))));
//                    }
//
//                    arrAvgColumn.add("AvgGyleft");
//                    if (gyleft != null && gyright != null) {
//                        gpYleft = (Double.valueOf(arrValue.get(gpYLindex)) + Double.valueOf(arrValue.get(gpYRindex))) / 2;
//                        arrAvgValue.add(String.valueOf(Math.round(gpYleft)));
//                    } else if (gyleft != null) {
//                        arrAvgValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpYLindex)))));
//                    } else if (gyright != null) {
//                        arrAvgValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpYRindex)))));
//                    }
//                    addvalidData(filename, "CD", arrAvgColumn, arrAvgValue);

                }
            }
            table.close();
            dc.InsertMapRecord("ValidData", filename, "LD", "1", String.valueOf(counter)); // here I am storig nos of rows of ValIDData Into LD(Label Data) column FamIly
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();

        } catch (IOException e1) {
        }

    }
    long counter = 0;

    public void addvalidData(String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, "ValidData");
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes("1" + ":" + rowKey + ":" + counter)); //userId + Filename+rownumber
            for (int a = 0; a <= arrColumn.size() - 1; a++) {
                put.add(Bytes.toBytes(CQ), Bytes.toBytes(arrColumn.get(a)), Bytes.toBytes(arrValue.get(a)));
            }
            table.put(put);
            // if ("CD".equals(CQ))
            {
                counter++;
            }
            table.flushCommits();
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //out.println("System ");
        HttpSession session = request.getSession(false);
        String Efilename = (String) session.getAttribute("hdnfilename");
        String Lfilename = (String) session.getAttribute("hdnlblfilename");
        String gxleft = (String) session.getAttribute("hdnxleft");
        String gxright = (String) session.getAttribute("hdnxright");
        String gyleft = (String) session.getAttribute("hdnyleft");
        String gyright = (String) session.getAttribute("hdnyright");
        String dleft = (String) session.getAttribute("hdndleft");
        String dright = (String) session.getAttribute("hdndright");

        arrColumn.clear();
        arrValue.clear();
        arrTime.clear();

        String holdNext = request.getParameter("btnNext");
        if ("Next".equalsIgnoreCase(holdNext)) {
            loopStarter = looprunner;
            looprunner = looprunner + 1000;
            dc.get_DataHbase(loopStarter, looprunner, "1", "ValidData", Efilename, arrColumn, arrValue, arrTime); //UserID TO BE ADDED IN IT
        } else {
            Read_RawData_forValidation(Efilename, gxleft, gxright, gyleft, gyright, dleft, dright);
            Read_LabelData_forValdiation(Efilename, Lfilename);
            arrColumn.clear();
            arrValue.clear();
            arrTime.clear();
            arrColumn_lbl.clear();
            arrValue_lbl.clear();
            dc.get_DataHbase(0, 1000, "1", "ValidData", Efilename, arrColumn, arrValue, arrTime); //UserID TO BE ADDED IN IT
            dc.get_DataHbase_common(0, 0, "", "1", "ValidData", Lfilename, "LD", arrColumn_lbl, arrValue_lbl);//UserID TO BE ADDED IN IT
        }

        request.setAttribute("arrColumn", arrColumn);
        request.setAttribute("arrValue", arrValue);
        request.setAttribute("arrTime", arrTime);
        request.setAttribute("arrColumn_lbl", arrColumn_lbl);
        request.setAttribute("arrValue_lbl", arrValue_lbl);

        RequestDispatcher rd = request.getRequestDispatcher("/ShowValidData.jsp");
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
