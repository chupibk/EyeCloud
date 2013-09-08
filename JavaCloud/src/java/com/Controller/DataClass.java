/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

/**
 * ++++
 *
 * @author samsalman
 */
public class DataClass {

    private static Configuration conf = null;

    static {
        conf = HBaseConfiguration.create();
    }
    // hbase(main):005:0> create 'RawData','EF','LF','MF' 
    // RawData column family structure
    // hbase(main):006:0> create 'ValidData','VD','LD','MD'
    // ValidData column family structure
    // create 'FixData','FX','SC','MD'
    // FixData column family structure

    public boolean isBlankOrNull(String str) {
        return (str == null || "".equals(str.trim()));
    }

    public void get_DataHbase(long loopStarter, long loopruner, String userId, String tablename, String rowkey, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value, ArrayList<String> ArrayRD_Time) throws IOException {

        try {

            HTable table = new HTable(conf, tablename);
            List<Get> Rowlist = new ArrayList<Get>();
            for (long a = loopStarter; a <= loopruner - 1; a++) {
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a))); //Here I am adding all the numbers of rows that I want to call
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist);
            for (Result r : result) {
                for (KeyValue kv : r.raw()) {
                    if (Bytes.toString(kv.getQualifier()).equals("GazePointXLeft") // here I am only showing these specific columns and values
                            || Bytes.toString(kv.getQualifier()).equals("GazePointYLeft")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointXRight")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointYRight")
                            || Bytes.toString(kv.getQualifier()).equals("StimuliName")
                            || Bytes.toString(kv.getQualifier()).equals("ValidityRight")
                            || Bytes.toString(kv.getQualifier()).equals("ValidityLeft")
                            || Bytes.toString(kv.getQualifier()).equals("DistanceLeft")) {
                        if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) {
                            ArrayRD_Column.add(new String(kv.getQualifier())); //Here I am adding all the columns
                            hold_a++;
                        } else if (hold_b == 0) {
                            ArrayRD_Value.add("/"); // here I add "\" to show that from here New Recrds its started
                            hold_b = 1;
                        } else {
                            if (hold_a == hold_b) {
                                ArrayRD_Value.add("/"); // here I add "\" to show that from here New Recrds its started
                                hold_b = 0;
                            }
                            hold_b++;
                        }
                        ArrayRD_Value.add(new String(kv.getValue())); // here I am adding values 
                    } else if (Bytes.toString(kv.getQualifier()).equals("Timestamp")) { // I am adding time stamp
                        ArrayRD_Time.add(new String(kv.getValue()));
                    }
                }
            }
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String get_MapFile(String tablename, String rowkey, String CF) { // This function will gives single record frm database
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

    public void InsertMapRecord(String tablename, String rowkey, String CF, String qualifier, String value) { // TO Insert one record

        try {
            HTable table = new HTable(conf, tablename);
            Put put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(CF), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
            table.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //This funcation is just like get_DataHbase but just contain some other setup
    public void get_DataHbase_common(long loopStarter, long loopruner, String flag, String userId, String tablename, String rowkey, String Columnfly, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value) throws IOException {
        System.out.println(System.getProperty("user.dir")); 
        HTable table = null;
        boolean flagcolumn = false, flagline = false;
        String holdvalue = "";
        
        File file;
        if (flag.equals("fix")) { // for writing into textfile
            URL url = getClass().getResource("/download/fix.txt");
            file = new File(url.getPath());
             
        } else {
            URL url = getClass().getResource("/download/sac.txt");
            file = new File(url.getPath());
        }
        try {
            FileWriter fileWriter = new FileWriter(file); // puting file into filewriter

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (flag.equals("ok")) // if its not a lable data
            {
            } else {
                loopStarter = 0;
                String NosRow = get_MapFile(tablename, rowkey, Columnfly);// LD for label data
                loopruner = Integer.valueOf(NosRow);
            }
            table = new HTable(conf, tablename);

            List<Get> Rowlist = new ArrayList<Get>();
            for (long a = loopStarter; a <= loopruner - 1; a++) {
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a)));
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist);
            for (Result r : result) {
                for (KeyValue kv : r.raw()) {
                    if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) {
                        ArrayRD_Column.add(new String(kv.getQualifier()));
                        hold_a++;
                        if (flag.equals("fix") || flag.equals("sac")) { // checking here if it is fix or sac then write into the text file
                            bufferedWriter.write(new String(kv.getQualifier()) + "\t");
                        }
                    } else if (hold_b == 0) {
                        ArrayRD_Value.add("/");
                        flagcolumn = true; // if all the columns are written into the file THEN MAKE VAriable trUE
                        flagline = true; // SETting varaible to true for new line
                        hold_b = 1;
                        if (flag.equals("fix") || flag.equals("sac")) {
                            bufferedWriter.newLine();
                        }
                    } else {

                        if (hold_a == hold_b) {
                            ArrayRD_Value.add("/");
                            hold_b = 0;
                            if (flag.equals("fix") || flag.equals("sac")) { 
                                bufferedWriter.newLine(); // adding new line into the textfile
                            }
                        }
                        hold_b++;
                        flagcolumn = true;
                    }
                    ArrayRD_Value.add(new String(kv.getValue()));
                    if (flag.equals("fix") || flag.equals("sac")) { 
                        if (flagcolumn) { // if all the columns are written on the file
                            bufferedWriter.write(holdvalue); // write string variable into the file
                            if (flagline) { // adding new line ONCE
                                bufferedWriter.newLine();
                                flagline = false;
                            }
                            bufferedWriter.write(new String(kv.getValue()) + "\t"); // now simply writing value into the file
                            holdvalue = "";
                        } else { // adding values into the string varaible untill all the columNs are written in the file
                            holdvalue += new String(kv.getValue()) + "\t";
                        }
                    }
                }
            }
            table.close();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getfileNames(String tablename, String CF, String column, LinkedHashMap<String, String> arrls) throws IOException {
        HTable table = new HTable(conf, tablename);
        Scan s = new Scan();
        s.addColumn(Bytes.toBytes(CF), Bytes.toBytes(column));
        ResultScanner scanner = table.getScanner(s);
        arrls.put("Select", "Select");
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                if (tablename.equals("FixData")) {
                    if (!new String(rr.getRow()).contains("-S-")) {
                        arrls.put(new String(rr.getValue(Bytes.toBytes(CF), Bytes.toBytes(column))), new String(rr.getRow()));
                    }
                } else {
                    arrls.put(new String(rr.getValue(Bytes.toBytes(CF), Bytes.toBytes(column))), new String(rr.getRow()));
                }

            }
        } finally {
            scanner.close();
        }
    }
    //////////////////////////////
    //          MY SQL         /// 
    ///////////////////////////////
    Connection connection = null;
    PreparedStatement preStat = null;
    Statement stat = null;
    ResultSet rs = null;
    public String email, username, country, state, city, address, mobileNO, phoneNo, postalcode;

    public int CheckEmaIl(String email) {
        try {
            int ID = 0;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");
            preStat = connection
                    .prepareStatement("SELECT * from CloudLogin.tblRegister where Email= ?");
            preStat.setString(1, email);
            rs = preStat.executeQuery();
            while (rs.next()) {
                ID = Integer.valueOf(rs.getString("Id"));
            }
            return ID;
        } catch (Exception e) {
            return 0;
        }
    }

    public String getUserDetails(int Id) {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");
            preStat = connection
                    .prepareStatement("SELECT * from CloudLogin.tblRegister where ID= ?");
            preStat.setInt(1, Id);
            rs = preStat.executeQuery();
            while (rs.next()) {
                email = rs.getString("Email");
                username = rs.getString("Fullname");
                country = rs.getString("Country");
                state = rs.getString("State");
                city = rs.getString("City");
                address = rs.getString("Address");
                mobileNO = rs.getString("MobileNo");
                phoneNo = rs.getString("PhoneNo");
                postalcode = rs.getString("PostalCode");

            }
            return email;
        } catch (Exception e) {
            return "";
        }
    }

    public int RegIsteruser_mysql(String name, String email, String pass, String country, String state,
            String city, String address, String mobNum, String phoneNum, String postalcode) {
        try {
            int result = 0;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");
            preStat = connection.prepareStatement("insert into tblRegister values(default,?,?,?,?,?,?,?,?,?,?)");
            preStat.setString(1, name);
            preStat.setString(2, email);
            preStat.setString(3, pass);
            preStat.setString(4, country);
            preStat.setString(5, state);
            preStat.setString(6, city);
            preStat.setString(7, address);
            preStat.setString(8, mobNum);
            preStat.setString(9, phoneNum);
            preStat.setString(10, postalcode);
            preStat.executeUpdate();
            if (preStat.getUpdateCount() == 1) {
                result = 1;
            } else {
                result = 0;
            }
            return result;

        } catch (Exception e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return 0;
        }

    }

    public int UpdateUser_detail(String name, String Id, String pass, String country, String state,
            String city, String address, String mobNum, String phoneNum, String postalcode) {
        try {
            int result = 0;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");
            preStat = connection.prepareStatement("update tblRegister set Fullname=?,Country=?,State=?,City=?,"
                    + "Address=?,MobileNo=?,PhoneNo=?,PostalCode=?,Password=?  where Id=?");
            preStat.setString(1, name);
            preStat.setString(2, country);
            preStat.setString(3, state);
            preStat.setString(4, city);
            preStat.setString(5, address);
            preStat.setString(6, mobNum);
            preStat.setString(7, phoneNum);
            preStat.setString(8, postalcode);
            preStat.setString(9, pass);
            preStat.setString(10, Id);
            preStat.executeUpdate();
            if (preStat.getUpdateCount() == 1) {
                result = 1;
            } else {
                result = 0;
            }
            return result;

        } catch (Exception e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return 0;
        }

    }

    public int loginUser(String email, String password) {
        try {
            int ID = 0;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");
            preStat = connection
                    .prepareStatement("SELECT * from CloudLogin.tblRegister where Email= ? and Password= ?");
            preStat.setString(1, email);
            preStat.setString(2, password);
            rs = preStat.executeQuery();
            while (rs.next()) {
                ID = Integer.valueOf(rs.getString("Id"));
                username = rs.getString("Fullname");

            }
            return ID;
        } catch (Exception e) {
            return 0;
        }
    }
}
