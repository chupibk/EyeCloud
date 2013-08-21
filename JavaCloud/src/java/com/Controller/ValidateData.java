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

    private int sumX, sumY;
    private int countxy = 0, duration = 0, countxySac = 0, durationSac = 0;
    private Integer RESOLUTION_WIDTH, RESOLUTION_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT,
            FIXATION_DURATION_THRESHOLD, VELOCITY_THRESHOLD, Missing_Time_THRESHOLD;
    ArrayList<String> arrDist = new ArrayList<String>();
    ArrayList<String> arrX = new ArrayList<String>();
    ArrayList<String> arrY = new ArrayList<String>();
    ArrayList<String> arrT = new ArrayList<String>();
    ArrayList<String> arrXsac = new ArrayList<String>();
    ArrayList<String> arrYsac = new ArrayList<String>();
    ArrayList<String> arrColumn = new ArrayList<String>();
    ArrayList<String> arrValue = new ArrayList<String>();
    ArrayList<String> arrTime = new ArrayList<String>();
    ArrayList<String> arrColumn_lbl = new ArrayList<String>();
    ArrayList<String> arrValue_lbl = new ArrayList<String>();
    DataClass dc = new DataClass();
    long loopStarter, looprunner = 1000;
    private static Configuration conf = null;

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
                    addData_inHbase("ValidData", filename, "LD", arrColumn, arrValue);
                }
            }
            table.close();
            dc.InsertMapRecord("ValidData", Efilename, "LD", "1", filename); //Insert validData file name as row and Label data Filename as value 
            dc.InsertMapRecord("ValidData", filename, "MD", "1", String.valueOf(counter)); // here I am storig nos of rows of ValIDData Into LD(Label Data) column FamIly
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float pixalToCenti(int x1, int y1, int x2, int y2) {
        float xf1, xf2, yf1, yf2;
        xf1 = (float) x1 * SCREEN_WIDTH / RESOLUTION_WIDTH;
        xf2 = (float) x2 * SCREEN_WIDTH / RESOLUTION_WIDTH;
        yf1 = (float) y1 * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
        yf2 = (float) y2 * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public float VT_Degree(int x1, int y1, int x2, int y2, float d1, float d2, int dur) {
        float cent = pixalToCenti(x1, y1, x2, y2);
        float degree;
        degree = (float) (2 * (Math.atan((cent / 2) / (d1))));
        return (float) ((degree / (2 * Math.PI))) * 360; // it should be in mili second
    }

    public void putXY(int x, int y, int time) {
        countxy++;
        sumX += x;
        sumY += y;
        duration += time;

    }
    Boolean FlagSccade;

    public void FixAlgorithm(String filename) throws IOException {
        FlagSccade = false;
        //   boolean sacFlag = false;
        counter = 0;
        counterSaccade = 0;
        long NosRow = Integer.valueOf(dc.get_MapFile("ValidData", filename, "MD"));
        HTable table = new HTable(conf, "ValidData");
        int count = 0;
        for (long a = 0; a <= NosRow - 1; a++) {
            Get get = new Get(Bytes.toBytes("1" + ":" + filename + ":" + a));
            Result result = table.get(get);
            for (KeyValue kv : result.raw()) {
                if (Bytes.toString(kv.getQualifier()).equals("AvgDist")) {
                    arrDist.add(new String(kv.getValue()));
                } else if (Bytes.toString(kv.getQualifier()).equals("AvgGxleft")) {
                    arrX.add(new String(kv.getValue()));
                } else if (Bytes.toString(kv.getQualifier()).equals("AvgGyleft")) {
                    arrY.add(new String(kv.getValue()));
                } else if (Bytes.toString(kv.getQualifier()).equals("Timestamp")) {
                    String time = new String(kv.getValue());
                    time = time.replace("\n", "");
                    arrT.add(time);
                    count++;
                    if (count == 2) {
                        int durtmp = Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0));
                        if (durtmp <= Missing_Time_THRESHOLD) {
                            float tmp = VT_Degree(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)),
                                    Integer.parseInt(arrX.get(1)), Integer.parseInt(arrY.get(1)),
                                    Integer.parseInt(arrDist.get(0)), Integer.parseInt(arrDist.get(1)),
                                    Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0)));

                            if (tmp <= VELOCITY_THRESHOLD) {
                                if (FlagSccade == true) {
                                    //  System.out.println("Saccade X " + arrXsac.get(0) + "\t" + arrXsac.get(countxySac - 1) + "\t" + durationSac);
                                    //  System.out.println("Saccade Y " + arrYsac.get(0) + "\t" + arrYsac.get(countxySac - 1) + "\t" + durationSac);
                                    addfix_Sac_IntoHbase(filename);
                                    arrXsac.clear();
                                    arrYsac.clear();
                                    //  sacFlag = false;
                                    countxySac = 0;
                                    durationSac = 0;
                                }
                                putXY(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)),
                                        Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0)));
                                FlagSccade = false;
                            } else {
                                if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {

                                    addfix_Sac_IntoHbase(filename);
                                }
                                FlagSccade = true;
                                // sacFlag = true;
                                arrXsac.add(arrX.get(0));
                                arrYsac.add(arrY.get(0));
                                durationSac += Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0));
                                countxySac++;
                            }
                            count = 1;
                            arrDist.remove(0);
                            arrX.remove(0);
                            arrY.remove(0);
                            arrT.remove(0);

                        }
                    }
                }

            }

        }
        if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {
//            System.out.println((float) sumX / countxy
//                    + "\t" + (float) sumY / countxy + "\t" + duration);
            addfix_Sac_IntoHbase(filename);
        }
    }

    public void addfix_Sac_IntoHbase(String filename) {
        if (FlagSccade == true) {
            arrColumn.add("XsacStart");
            arrValue.add(arrXsac.get(0));
            arrColumn.add("XsacEnd");
            arrValue.add(arrXsac.get(countxySac - 1));
            arrColumn.add("XsacDur");
            arrValue.add(String.valueOf(durationSac));
            arrColumn.add("YsacStart");
            arrValue.add(arrYsac.get(0));
            arrColumn.add("YsacEnd");
            arrValue.add(arrYsac.get(countxySac - 1));
            arrColumn.add("YsacDur");
            arrValue.add(String.valueOf(durationSac));
            addScade_inHbase("FixData", filename + "-S-", "SC", arrColumn, arrValue);// Inserting Saccade with adding "S" in file name
            arrXsac.clear();
            arrYsac.clear();
            countxySac = 0;
            durationSac = 0;
        } else {
            arrColumn.clear();
            arrValue.clear();
            arrColumn.add("sumX");
            arrValue.add(String.valueOf(sumX / countxy));
            arrColumn.add("sumY");
            arrValue.add(String.valueOf(sumY / countxy));
            arrColumn.add("sumDur");
            arrValue.add(String.valueOf(duration));
            addData_inHbase("FixData", filename, "FX", arrColumn, arrValue); /// Inserting Fixtation

            arrColumn.clear();
            arrValue.clear();

        }
        countxy = 0;
        sumX = 0;
        sumY = 0;
        duration = 0;

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
                } else if (Double.parseDouble(arrValue.get(gpXLindex).equals("") ? "0" : arrValue.get(gpXLindex)) < 1) // For Validity if XLeft is less than 0
                {
                    if (gxleft != null && !gxleft.equals("")) {
                        breakflag = true;
                    }
                } else if (Double.parseDouble(arrValue.get(gpXRindex).equals("") ? "0" : arrValue.get(gpXRindex)) < 1) // For Validity L=0 or 3 & then Update R
                {
                    if (gxright != null && !gxright.equals("")) {
                        breakflag = true;
                    }
                } else if (Double.parseDouble(arrValue.get(gpYLindex).equals("") ? "0" : arrValue.get(gpYLindex)) < 1) // For Validity L=0 or 3 & then Update R
                {
                    if (gyleft != null && !gyleft.equals("")) {
                        breakflag = true;
                    }

                } else if (Double.parseDouble(arrValue.get(gpYRindex).equals("") ? "0" : arrValue.get(gpYRindex)) < 1) // For Validity L=0 or 3 & then Update R
                {
                    if (gyright != null && !gyright.equals("")) {
                        breakflag = true;
                    }
                } else if ((arrValue.get(vLindex).equals("0") || arrValue.get(vLindex).equals("3")) && arrValue.get(vRindex).equals("4")) // For Validity L=0 or 3 & then Update R
                {
                    arrValue.set(gpXRindex, arrValue.get(gpXLindex));
                    arrValue.set(gpYRindex, arrValue.get(gpYLindex));
                    arrValue.set(dRindex, arrValue.get(dLindex));

                } else if (arrValue.get(vLindex).equals("4") && (arrValue.get(vRindex).equals("0") || arrValue.get(vRindex).equals("3"))) // For Validity R=0 or 3 & then Update L
                {
                    arrValue.set(gpXLindex, arrValue.get(gpXRindex));
                    arrValue.set(gpYLindex, arrValue.get(gpYRindex));
                    arrValue.set(dLindex, arrValue.get(dRindex));

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

                    arrValue.set(dLindex, String.valueOf(Math.round(DLeft)));
                    arrValue.set(dRindex, String.valueOf(Math.round(DRight)));
                    //arrAvgColumn.add("Timestamp");
                    // arrAvgValue.add(arrValue.get(arrColumn.indexOf("Timestamp")));

                    arrColumn.add("AvgDist");
                    if ((dright != null && !dright.equals("")) && (dleft != null && !dleft.equals(""))) {
                        DLeft = (DLeft + DRight) / 2;
                        arrValue.add(String.valueOf(Math.round(DLeft)));
                    } else if (dright != null && !dright.equals("")) {
                        arrValue.add(String.valueOf(Math.round(DRight)));
                    } else if (dleft != null && !dleft.equals("")) {
                        arrValue.add(String.valueOf(Math.round(DLeft)));
                    }

                    arrColumn.add("AvgGxleft");
                    if ((gxleft != null && !gxleft.equals("")) && (gxright != null && !gxright.equals(""))) {
                        gpXleft = (Double.valueOf(arrValue.get(gpXLindex)) + Double.valueOf(arrValue.get(gpXRindex))) / 2;
                        arrValue.add(String.valueOf(Math.round(gpXleft)));
                    } else if (gxleft != null && !gxleft.equals("")) {
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpXLindex)))));
                    } else if (gxright != null && !gxright.equals("")) {
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpXRindex)))));
                    }

                    arrColumn.add("AvgGyleft");
                    if ((gyleft != null && !gyleft.equals("")) && (gyright != null && !gyright.equals(""))) {
                        gpYleft = (Double.valueOf(arrValue.get(gpYLindex)) + Double.valueOf(arrValue.get(gpYRindex))) / 2;
                        arrValue.add(String.valueOf(Math.round(gpYleft)));
                    } else if (gyleft != null && !gyleft.equals("")) {
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpYLindex)))));
                    } else if (gyright != null && !gyright.equals("")) {
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpYRindex)))));
                    }

                    addData_inHbase("ValidData", filename, "VD", arrColumn, arrValue);

                }
            }
            table.close();
            dc.InsertMapRecord("ValidData", filename, "MD", "1", String.valueOf(counter)); // here I am storig nos of rows of ValIDData Into MD(Map Data) column FamIly
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();

        } catch (IOException e1) {
        }

    }
    long counter = 0, counterSaccade = 0;

    public void addData_inHbase(String tablename, String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, tablename);
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes("1" + ":" + rowKey + ":" + counter)); //userId + Filename+rownumber
            for (int a = 0; a <= arrColumn.size() - 1; a++) {
                put.add(Bytes.toBytes(CQ), Bytes.toBytes(arrColumn.get(a)), Bytes.toBytes(arrValue.get(a)));
            }
            table.put(put);
            counter++;
            table.flushCommits();
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScade_inHbase(String tablename, String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, tablename);
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes("1" + ":" + rowKey + ":" + counterSaccade)); //userId + Filename+rownumber
            for (int a = 0; a <= arrColumn.size() - 1; a++) {
                put.add(Bytes.toBytes(CQ), Bytes.toBytes(arrColumn.get(a)), Bytes.toBytes(arrValue.get(a)));
            }
            table.put(put);
            counterSaccade++;
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
        SCREEN_WIDTH = Integer.parseInt(session.getAttribute("xscreen").toString());
        SCREEN_HEIGHT = Integer.parseInt(session.getAttribute("yscreen").toString());
        RESOLUTION_WIDTH = Integer.parseInt(session.getAttribute("xresol").toString());
        RESOLUTION_HEIGHT = Integer.parseInt(session.getAttribute("yresol").toString());
        FIXATION_DURATION_THRESHOLD = Integer.parseInt(session.getAttribute("fxdr").toString());
        VELOCITY_THRESHOLD =Integer.parseInt(session.getAttribute("velth").toString());
        Missing_Time_THRESHOLD = Integer.parseInt(session.getAttribute("mstm").toString());

        arrColumn.clear();
        arrValue.clear();
        arrTime.clear();

        String holdNext = request.getParameter("btnNext");
        String holdRun = request.getParameter("btnRun");
        if ("Next".equalsIgnoreCase(holdNext)) {
            loopStarter = looprunner;
            looprunner = looprunner + 1000;
            dc.get_DataHbase(loopStarter, looprunner, "1", "ValidData", Efilename, arrColumn, arrValue, arrTime); //UserID TO BE ADDED IN IT
        } else if ("Run Fixation".equalsIgnoreCase(holdRun)) {
            FixAlgorithm(Efilename);
            dc.InsertMapRecord("FixData", Efilename, "MD", "1", String.valueOf(counter));// inserting Nos of Rows of fixation
            dc.InsertMapRecord("FixData", Efilename + "-S-", "MD", "1", String.valueOf(counterSaccade)); // inserting Nos of Rows of Sccade

            arrColumn.clear();
            arrValue.clear();
            arrColumn_lbl.clear();
            arrValue_lbl.clear();
            dc.get_DataHbase_common(0, 0, "", "1", "FixData", Efilename, "MD", arrColumn, arrValue);//reading fixation
            dc.get_DataHbase_common(0, 0, "", "1", "FixData", Efilename + "-S-", "MD", arrColumn_lbl, arrValue_lbl);// reading Saccade
            request.setAttribute("arrColumn", arrColumn);
            request.setAttribute("arrValue", arrValue);
            request.setAttribute("arrColumn_lbl", arrColumn_lbl);
            request.setAttribute("arrValue_lbl", arrValue_lbl);

            RequestDispatcher rd = request.getRequestDispatcher("/ShowFixData.jsp");
            rd.forward(request, response);
        } else {
            Read_RawData_forValidation(Efilename, gxleft, gxright, gyleft, gyright, dleft, dright);
            Read_LabelData_forValdiation(Efilename, Lfilename);
            arrColumn.clear();
            arrValue.clear();
            arrTime.clear();
            arrColumn_lbl.clear();
            arrValue_lbl.clear();
            dc.get_DataHbase(0, 1000, "1", "ValidData", Efilename, arrColumn, arrValue, arrTime); //UserID TO BE ADDED IN IT
            dc.get_DataHbase_common(0, 0, "", "1", "ValidData", Lfilename, "MD", arrColumn_lbl, arrValue_lbl);//UserID TO BE ADDED IN IT
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
