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
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author samsalman
 */
@WebServlet(name = "HbaseServlet", urlPatterns = {"/HbaseServlet"})
public class HbaseServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static Configuration conf = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String tablename = "Dummy";
        String[] familys = {"grade", "course"};
        String actIon = request.getParameter("action");
        String row = request.getParameter("row");
        String famIly = request.getParameter("famIly");
        String qualifier = request.getParameter("qualifier");
        String value = request.getParameter("value");

        if ("Add".equalsIgnoreCase(actIon)) {
            try {
                //creatTable(tablename, familys);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            addRecord(tablename, row, famIly, qualifier, value);
//            out.println(row + " " + famIly + " " + qualifier + " " + value);
//        return;    
            return;
        } else {
            out.println(actIon);
        }
        out.close();

        request.getRequestDispatcher("newjsp_1.jsp").forward(request, response);


    }

    static {
        conf = HBaseConfiguration.create();
    }

    public static String get_MapFile(String tablename, String rowkey, String CF) {
        String holdvalue = null;
        try {
            HTable table = new HTable(conf, tablename);
            Get get = new Get(rowkey.getBytes());
            get.addFamily(CF.getBytes());
            Result rs = table.get(get);
            for (KeyValue kv : rs.raw()) {
                holdvalue = new String(kv.getValue());
            }
            table.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return holdvalue;

    }
    public static int RESOLUTION_WIDTH = 1024;
    public static int RESOLUTION_HEIGHT = 768;
    public static int SCREEN_WIDTH = 21;
    public static int SCREEN_HEIGHT = 16;
    public static int THOUSAND = 1000;
    public static int TEN = 10;
    public static int SAMPLE_RATE = 300;
    public static int FIXATION_DURATION_THRESHOLD = 100; // Millisecond
    public static float VELOCITY_THRESHOLD = 75;
    public static float Missing_Time_THRESHOLD = 100;// Millisecond

//    public static float pixalToCenti(int x1, int y1, int x2, int y2) {
//        float x;
//        float y;
//        x = Math.abs((float) (x1 - x2)) * SCREEN_WIDTH / RESOLUTION_WIDTH;
//        y = Math.abs((float) (y1 - y2)) * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
//        return (float) Math.sqrt(x * x + y * y);
//    }
    public static float pixalToCenti(int x1, int y1, int x2, int y2) {
        float xf1, xf2, yf1, yf2;
        xf1 = (float) x1 * SCREEN_WIDTH / RESOLUTION_WIDTH;
        xf2 = (float) x2 * SCREEN_WIDTH / RESOLUTION_WIDTH;
        yf1 = (float) y1 * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
        yf2 = (float) y2 * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static float VT_Degree(int x1, int y1, int x2, int y2, float d1, float d2, int dur) {
        float cent = pixalToCenti(x1, y1, x2, y2);
        float degree;
        degree = (float) (2 * (Math.atan((cent / 2) / (d1))));
        return (float) ((degree / (2 * Math.PI))) * 360; // it should be in mili second
    }
    static ArrayList<String> arrDist = new ArrayList<String>();
    static ArrayList<String> arrX = new ArrayList<String>();
    static ArrayList<String> arrY = new ArrayList<String>();
    static ArrayList<String> arrT = new ArrayList<String>();
    static ArrayList<String> arrXsac = new ArrayList<String>();
    static ArrayList<String> arrYsac = new ArrayList<String>();

    public static void FixAlgorithm() throws IOException {
        boolean sacFlag = false;
        long NosRow = Integer.valueOf(get_MapFile("ValidData", "Rec 01-All-Data", "MD"));
        System.out.println(NosRow);
        HTable table = new HTable(conf, "ValidData");
        int count = 0;
        for (long a = 0; a <= NosRow - 1; a++) {
            Get get = new Get(Bytes.toBytes("1" + ":" + "Rec 01-All-Data" + ":" + a));
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
                                if (sacFlag == true) {
                                    System.out.println("Saccade X " + arrXsac.get(0) + "\t" + arrXsac.get(countxySac - 1) + "\t" + durationSac);
                                    System.out.println("Saccade Y " + arrYsac.get(0) + "\t" + arrYsac.get(countxySac - 1) + "\t" + durationSac);
                                    arrXsac.clear();
                                    arrYsac.clear();
                                    sacFlag = false;
                                    countxySac = 0;
                                    durationSac = 0;
                                }
                                putXY(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)),
                                        Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0)));

                            } else {
                                if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {
                                    System.out.println((float) sumX / countxy
                                            + "\t" + (float) sumY / countxy + "\t" + duration);
                                    countxy = 0;
                                    sumX = 0;
                                    sumY = 0;
                                    duration = 0;

                                }
                                sacFlag = true;
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

                        } else {
                            System.out.println("test");
                        }
                    }
                }

            }

        }
        if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {
            System.out.println((float) sumX / countxy
                    + "\t" + (float) sumY / countxy + "\t" + duration);
            countxy = 0;
            sumX = 0;
            sumY = 0;
            duration = 0;
        }
    }

    public static void putXY(int x, int y, int time) {
        countxy++;
        sumX += x;
        sumY += y;
        duration += time;

    }
    private static int sumX;
    private static int sumY;
    private static int countxy = 0;
    private static int countxySac = 0;
    private static int duration = 0;
    private static int durationSac = 0;
    static Connection connection = null;
    static PreparedStatement preStat = null;
    static Statement stat = null;
    static ResultSet rs = null;

    public static void Insertmysql() {

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");

            preStat = connection.prepareStatement("insert into tblRegister values(default,?,?,?,?,?,?,?,?,?,?)");

            preStat.setString(1, "Test");
            preStat.setString(2, "TestEmail");
            preStat.setString(3, "TestWebpage");
            preStat.setString(4, "ss");
            preStat.setString(5, "TestSummary");
            preStat.setString(6, "TestComment");
            preStat.setString(7, "TestComment7");
            preStat.setString(8, "TestComment8");
            preStat.setString(9, "TestComment9");
            preStat.setString(10, "TestComment10");
            preStat.executeUpdate();

            preStat = connection.prepareStatement("select * from tblRegister");
            rs = preStat.executeQuery();
            System.out.println(rs);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

    }

    public static void main(String[] args) {
        try {
            // System.out.println(get_MapFile("01-01-All-Data.txt"));
            // FixAlgorithm();
            Insertmysql();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO code application logic here
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
