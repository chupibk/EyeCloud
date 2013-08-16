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
                addRecord(tablename, row, famIly, qualifier, value);
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

    public static void addRecord(String tablename, String rowkey, String family,
            String qualifier, String value) throws ZooKeeperConnectionException, MasterNotRunningException, IOException {
        try {

            // HBaseAdmin admin = new HBaseAdmin(conf);
            conf = HBaseConfiguration.create();

            HTable table = new HTable(conf, tablename);

            Put put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));

            table.put(put);

            System.out.println("insert record " + rowkey + " to table " + tablename + " ok.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        conf = HBaseConfiguration.create();
    }

    public static void creatTable(String tableName, String[] familys)
            throws Exception {

        conf = HBaseConfiguration.create();
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (int i = 0; i < familys.length; i++) {
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
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
   static ArrayList<String> arrDist = new ArrayList<String>();
   static ArrayList<String> arrX = new ArrayList<String>();
   static ArrayList<String> arrY = new ArrayList<String>();
   static ArrayList<String> arrT = new ArrayList<String>();    

    public static void FixAlgorithm() throws IOException {
        long NosRow = Integer.valueOf(get_MapFile("ValidData", "01-01-All-Data.txt", "MD"));
        System.out.println(NosRow);
        HTable table = new HTable(conf, "ValidData");
        int count = 0;
        for (long a = 0; a <= 4 - 1; a++) {
            Get get = new Get(Bytes.toBytes("1" + ":" + "01-01-All-Data.txt" + ":" + a));
            Result result = table.get(get);
            for (KeyValue kv : result.raw()) {
                if (Bytes.toString(kv.getQualifier()).equals("AvgDist")) {
                    arrDist.add(new String(kv.getValue()));
                    
                } else if (Bytes.toString(kv.getQualifier()).equals("AvgGxleft")) {
                    arrX.add(new String(kv.getValue()));
                } else if (Bytes.toString(kv.getQualifier()).equals("AvgGyleft")) {
                    arrY.add(new String(kv.getValue()));
                } else if (Bytes.toString(kv.getQualifier()).equals("Timestamp")) {
                    arrT.add(new String(kv.getValue()));
                    count++;
                    if (count == 2) {
                        System.out.println(arrDist);
                        System.out.println(arrX);
                        System.out.println(arrY);
                        System.out.println(arrT);
                        count=1;
                        arrDist.remove(0);arrX.remove(0);arrY.remove(0);arrT.remove(0);
                        System.out.println("REMOVED");
                    }
                }

            }

        }


    }

    public static void main(String[] args) {
        try {
           // System.out.println(get_MapFile("01-01-All-Data.txt"));
            FixAlgorithm();

//            System.out.println("=========get one record========");
//            HbaseTest.getonerecord(tablename, "baoniu");
//
//            System.out.println("=========show all record========");
//            HbaseTest.getAllRecord(tablename);
//
//            System.out.println("=========del one record========");
//            HbaseTest.delRecord(tablename, "zkb");
//            HbaseTest.getAllRecord(tablename);
//
//            System.out.println("=========show all record========");
//            HbaseTest.getAllRecord(tablename);
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
