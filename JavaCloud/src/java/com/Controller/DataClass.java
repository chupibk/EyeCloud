/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
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
    // hbase(main):006:0> create 'ValidData','VD','LD','CD'
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
        HTable table = null;
        try {
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
                    } else if (hold_b == 0) {
                        ArrayRD_Value.add("/");
                        hold_b = 1;
                    } else {
                        if (hold_a == hold_b) {
                            ArrayRD_Value.add("/");
                            hold_b = 0;
                        }
                        hold_b++;
                    }
                    ArrayRD_Value.add(new String(kv.getValue()));
                }
            }
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    Connection connection = null;
    PreparedStatement preStat = null;
    Statement stat = null;
    ResultSet rs = null;

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

//            preStat = connection.prepareStatement("select * from tblRegister");
//            rs = preStat.executeQuery();
//            System.out.println(rs);

        } catch (Exception e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return 0;
        }

    }

    public int loginUser(String email, String password) {
        try{
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
            ID= Integer.valueOf(rs.getString("Id"));
        }
        return ID;
        }
        catch(Exception e){
        return 0;    
        }
    }
}
