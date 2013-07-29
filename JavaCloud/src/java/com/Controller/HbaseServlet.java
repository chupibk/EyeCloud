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
    
   private static  Configuration conf = null;
   

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
        try{
            //creatTable(tablename, familys);
            addRecord(tablename, row, famIly, qualifier, value);
        }
        catch(Exception e){
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
           conf=HBaseConfiguration.create();
           
           HTable table = new HTable(conf, tablename);
          
           Put put = new Put(Bytes.toBytes(rowkey));
           put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);

            System.out.println("insert record " + rowkey + " to table " + tablename + " ok.");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public static void creatTable(String tableName, String[] familys)
            throws Exception {
        
        conf=HBaseConfiguration.create();
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
    

    public void main(String[] args) {
        try {
            String tablename = "data";
            String[] familys = {"grade", "course"};
            creatTable(tablename, familys);

            addRecord(tablename, "zkb", "grade", "", "5");
            addRecord(tablename, "zkb", "course", "", "90");
            addRecord(tablename, "zkb", "course", "math", "97");
            addRecord(tablename, "zkb", "course", "art", "87");
            addRecord(tablename, "baoniu", "grade", "", "4");
            addRecord(tablename, "baoniu", "course", "math", "89");

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
