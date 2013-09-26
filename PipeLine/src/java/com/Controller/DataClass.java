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

    public DataClass() {
    }

    static {
        conf = HBaseConfiguration.create();
    }
    String holdpath;

    public DataClass(String holdpath) {
        this.holdpath = holdpath;


    }
    // create 'RawData','EF','LF','MF' --------RawData table & column family structure
    // create 'ValidData','VD','LD','MD' ------ValidData table & column family structure
    // create 'FixData','FX','SC','MD' --------FixData table & column family structure
    // create 'EyeFeature', 'FS','MD' ---------- Eyefeature table & column family structure

    // thiis function is used for getting recrods of specific columns from valid data
    public void get_DataHbase(long loopStarter, long loopruner, String userId, String tablename, String rowkey, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value, ArrayList<String> ArrayRD_Time) throws IOException {

        try {

            HTable table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon 
            List<Get> Rowlist = new ArrayList<Get>(); // makIng arraylIst of Get element
            for (long a = loopStarter; a <= loopruner - 1; a++) { // runnIng loop as per gIven loopruner by the user
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a))); //Here I am readIng all the numbers of rows and adding them Into the arrayLIst
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist); // gettIng data from Hbase by GIVEn the Rowlist and result array
            for (Result r : result) { // runnINg loop untIll result has value
                for (KeyValue kv : r.raw()) {
                    if (Bytes.toString(kv.getQualifier()).equals("GazePointXLeft") // here I am only showing these specific columns and values
                            || Bytes.toString(kv.getQualifier()).equals("GazePointYLeft")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointXRight")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointYRight")
                            || Bytes.toString(kv.getQualifier()).equals("StimuliName")
                            || Bytes.toString(kv.getQualifier()).equals("ValidityRight")
                            || Bytes.toString(kv.getQualifier()).equals("ValidityLeft")
                            || Bytes.toString(kv.getQualifier()).equals("DistanceLeft")) {
                        if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) { //checkIng If the column are already added
                            ArrayRD_Column.add(new String(kv.getQualifier())); //Here I am adding all the columns
                            hold_a++; // IncrementIng hold_a by 1
                        } else if (hold_b == 0) {
                            ArrayRD_Value.add("/"); // here I add "/" to show that from here New Recrds its started
                            hold_b = 1; //setting hold_b to 1 so that IF condItIon should nt get true
                        } else {
                            if (hold_a == hold_b) {
                                ArrayRD_Value.add("/"); // here I add "/" to show that from here New Recrds its started
                                hold_b = 0;
                            }
                            hold_b++; // IncrementIng hold_b by 1
                        }
                        ArrayRD_Value.add(new String(kv.getValue())); // here I am adding values to ArrayRD_Value array LIST
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

    public String get_MapFile(String tablename, String rowkey, String CF) { // This function will gives record frm database
        String holdvalue = null;
        try {
            HTable table = new HTable(conf, tablename);// assIgIng tablename and confIguratIon 
            Get get = new Get(rowkey.getBytes()); // assIgIng rowkey to fIltr frm the table

            get.addFamily(CF.getBytes()); // addIng column famIly to the fIlter
            Result rs = table.get(get); // gettIng result frm the database and assIgIng that to the result varIable
            for (KeyValue kv : rs.raw()) {// runnIng loop as per the value we fInd & storIng It to the varIable
                holdvalue = new String(kv.getValue());
            }
            table.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return holdvalue;

    }

    public void InsertMapRecord(String tablename, String rowkey, String CF, String qualifier, String value) { // TO Insert one record In Hbase

        try {
            HTable table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon 
            Put put = new Put(Bytes.toBytes(rowkey)); // makIng Instance of the Put WIth the rowkey
            put.add(Bytes.toBytes(CF), Bytes.toBytes(qualifier), Bytes.toBytes(value)); // addIng columnFamIly, Column name and Values Into the Put
            table.put(put); // InsertIng record Into the table now wIth the Put
            table.close(); // fInally closIng the table 

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // This is the common method of retrieving of data from Hbase

    public void get_DataHbase_common(long loopStarter, long loopruner, String flag, String userId, String tablename, String rowkey, String Columnfly, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value) throws IOException {
        HTable table = null;
        try {
            if (flag.equals("ok")) // if specified looprunner value Is assIgned
            {
            } else {
                loopStarter = 0;
                String NosRow = get_MapFile(tablename, rowkey, Columnfly);// ThIs funcatIon wILL gIves us Nos of Records present In the table In a gIven table,rowkey and Column FamIly 
                loopruner = Integer.valueOf(NosRow); // assIgIng the result to the varIable
            }
            table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon 

            List<Get> Rowlist = new ArrayList<Get>(); // makIng arraylIst of Get element
            for (long a = loopStarter; a <= loopruner - 1; a++) { // runnIng loop as per nos of result we have In loopruner 
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a))); //Here I am readIng all the numbers of rows and adding them Into the arrayLIst
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist); // gettIng data from Hbase by GIVEn the Rowlist and result array
            for (Result r : result) { // runnINg loop untIll result has value
                for (KeyValue kv : r.raw()) {
                    if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) { //checkIng If the column are already added Into the arrayLIst
                        ArrayRD_Column.add(new String(kv.getQualifier())); //Here I am adding all the columns
                        hold_a++; // IncrementIng hold_a by 1
                    } else if (hold_b == 0) {
                        ArrayRD_Value.add("/"); // here I add "/" to show that from here New Recrds its started
                        hold_b = 1; //setting hold_b to 1 so that IF condItIon should nt get true
                    } else {
                        if (hold_a == hold_b) {
                            ArrayRD_Value.add("/"); // here I add "/" to show that from here New Recrds its started
                            hold_b = 0;
                        }
                        hold_b++; // IncrementIng hold_b by 1
                    }
                    ArrayRD_Value.add(new String(kv.getValue())); // here I am adding values to ArrayRD_Value array LIST
                }
            }
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //This funcation is just like get_DataHbase but It wIll also WrIte Result Into text FIle so that User can download
    public void get_DataHbase_WriteTextFile(long loopStarter, long loopruner, String flag, String userId, String tablename, String rowkey, String Columnfly, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value) throws IOException {
        HTable table = null;
        boolean flagcolumn = false, flagline = false;
        String holdvalue = "";
        String filepath = "";
        File file;
        filepath = holdpath.substring(0, holdpath.length() - 11);
        if (flag.equals("fix")) { // for writing holdpathinto textfile
            file = new File(filepath + "/download/" + "fix.txt"); //wrItIng fIxaTIon fIle // add web also if you runs on development machine like this /web/download/
        } else {
            file = new File(filepath + "/download/" + "sac.txt"); //wrItIng Saccade fIle // add web also if you runs on development machine like this /web/download/
        }
        try {
            FileWriter fileWriter = new FileWriter(file); // puting file into filewriter
            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (flag.equals("ok")) // if its not a lable data
            {
            } else {
                loopStarter = 0;
                String NosRow = get_MapFile(tablename, rowkey, Columnfly);// ThIs funcatIon wILL gIves us Nos of Records present In the table In a gIven table,rowkey and Column FamIly 
                loopruner = Integer.valueOf(NosRow); // assIgIng the result to the varIable
            }
            table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon
            List<Get> Rowlist = new ArrayList<Get>(); // makIng arraylIst of Get element
            for (long a = loopStarter; a <= loopruner - 1; a++) { // runnIng loop as per gIven loopruner by the user
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a))); //Here I am readIng all the numbers of rows and adding them Into the arrayLIst
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist); // gettIng data from Hbase by GIVEn the Rowlist and result array
            for (Result r : result) { // runnINg loop untIll result has value
                for (KeyValue kv : r.raw()) {
                    if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) { //checkIng If the column are already added
                        ArrayRD_Column.add(new String(kv.getQualifier())); //Here I am adding all the columns
                        hold_a++; //  // IncrementIng hold_a by 1
                        if (flag.equals("fix") || flag.equals("sac")) { // checking here if it is fix or sac then write into the text file
                            bufferedWriter.write(new String(kv.getQualifier()) + "\t"); // wrItIng values Into TextFIle and also addIng tab space dIstance
                        }
                    } else if (hold_b == 0) {
                        ArrayRD_Value.add("/"); // here I add "/" to show that from here New Recrds its started
                        flagcolumn = true; // if all the columns are written into the file THEN MAKE VAriable trUE
                        flagline = true; // SETting varaible to true for new line
                        hold_b = 1;
                        if (flag.equals("fix") || flag.equals("sac")) {
                            bufferedWriter.newLine(); //addIng new LIne
                        }
                    } else {
                        if (hold_a == hold_b) {
                            ArrayRD_Value.add("/");// here I add "/" to show that from here New Recrds its started
                            hold_b = 0;
                            if (flag.equals("fix") || flag.equals("sac")) {
                                bufferedWriter.newLine(); // adding new line into the textfile
                            }
                        }
                        hold_b++; // IncrementIng hold_b by 1
                        flagcolumn = true;
                    }
                    ArrayRD_Value.add(new String(kv.getValue()));
                    if (flag.equals("fix") || flag.equals("sac")) {
                        if (flagcolumn) { // if all the columns are written on the file
                            bufferedWriter.write(holdvalue); // write string variable into the file
                            if (flagline) { // adding new line ONCE
                                bufferedWriter.newLine(); // adding new line into the textfile
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
            table.close(); //closIng the table
            bufferedWriter.close();//closIng the BufferWrIter

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getfileNames(String tablename, String CF, String column, LinkedHashMap<String, String> arrls) throws IOException {
        HTable table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon 
        Scan s = new Scan(); // makIng Instance of Scan
        s.addColumn(Bytes.toBytes(CF), Bytes.toBytes(column)); // addIng columnFamIly and Column Into the Scan
        ResultScanner scanner = table.getScanner(s); // assIgIng the scanner to scan 
        arrls.put("Select", "Select"); // addIng select value to the arrayLIst
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) { // runnInt the loop untIl the RR Is not null
                if (tablename.equals("FixData")) { // If there Is a request for FIXDATA table 
                    if (!new String(rr.getRow()).contains("-S-")) { // In FIXDATA table I STore two records for fIleName one Is for saccade and another Is for FIxatIon
                        // In Saccade I add -S- after the FIle name, that's why here I check & only show 
                        arrls.put(new String(rr.getValue(Bytes.toBytes(CF), Bytes.toBytes(column))), new String(rr.getRow())); // addIng the values Into the ArrayLIst
                    }
                } else {
                    arrls.put(new String(rr.getValue(Bytes.toBytes(CF), Bytes.toBytes(column))), new String(rr.getRow())); // addIng the values Into the ArrayLIst
                }

            }
        } finally {
            scanner.close();
        }
    }
    //////////////////////////////
    //          MY SQL         /// 
    ///////////////////////////////
    // DeclarIng all the publIc varIable here
    Connection connection = null;
    PreparedStatement preStat = null;
    Statement stat = null;
    ResultSet rs = null;
    public String email, username, country, state, city, address, mobileNO, phoneNo, postalcode;
    String MysqlCon = "jdbc:mysql://localhost:3306/CloudLogin";
    //String MysqlCon="jdbc:mysql://localhost:3306/Cloud";
    String Mysqluser = "root";
    //String Mysqluser="cloudadmin";
    //String MysqlPass="Vg78gRt";
    String MysqlPass = "Sa1234";
    //"jdbc:mysql://localhost:3306/CloudLogin"

    public int CheckEmaIl(String email) {
        try {
            int ID = 0;
            Class.forName("com.mysql.jdbc.Driver"); //settIng drIver for Mysql
            connection = DriverManager
                    .getConnection(MysqlCon, Mysqluser, MysqlPass); // maKIng connectIon wIth mysql
            preStat = connection
                    .prepareStatement("SELECT * from tblRegister where Email= ?"); // selectIng records from the table
            preStat.setString(1, email); // assIgIng the parameter to the where column
            rs = preStat.executeQuery(); // executIng query
            while (rs.next()) { // runnIng loop untIl value
                ID = Integer.valueOf(rs.getString("Id")); // assIgIng value to the varIable
            }
            return ID;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    public String getUserDetails(int Id) {
        try {

            Class.forName("com.mysql.jdbc.Driver"); //settIng drIver for Mysql
            connection = DriverManager
                    .getConnection(MysqlCon, Mysqluser, MysqlPass); // maKIng connectIon wIth mysql
            preStat = connection
                    .prepareStatement("SELECT * from tblRegister where ID= ?");  // selectIng records from the table
            preStat.setInt(1, Id); // assIgIng the parameter to the where column
            rs = preStat.executeQuery(); // executIng query
            while (rs.next()) { // runnIng loop untIl results exIsts
                email = rs.getString("Email"); // assIgIng value to the varIable
                username = rs.getString("Fullname");
                country = rs.getString("Country");
                state = rs.getString("State");
                city = rs.getString("City");
                address = rs.getString("Address");
                mobileNO = rs.getString("MobileNo");
                phoneNo = rs.getString("PhoneNo");
                postalcode = rs.getString("PostalCode");

            }
            return email; //returIng result
        } catch (Exception e) {
            return "";
        }
    }

    public int RegIsteruser_mysql(String name, String email, String pass, String country, String state,
            String city, String address, String mobNum, String phoneNum, String postalcode) {
        try {
            int result = 0;
            Class.forName("com.mysql.jdbc.Driver"); //settIng drIver for Mysql
            connection = DriverManager
                    .getConnection(MysqlCon, Mysqluser, MysqlPass); // maKIng connectIon wIth mysql
            preStat = connection.prepareStatement("insert into tblRegister values(default,?,?,?,?,?,?,?,?,?,?)"); // selectIng records from the table
            preStat.setString(1, name); // assIgIng the parameter to the where column
            preStat.setString(2, email);
            preStat.setString(3, pass);
            preStat.setString(4, country);
            preStat.setString(5, state);
            preStat.setString(6, city);
            preStat.setString(7, address);
            preStat.setString(8, mobNum);
            preStat.setString(9, phoneNum);
            preStat.setString(10, postalcode);
            preStat.executeUpdate(); // executIng query
            if (preStat.getUpdateCount() == 1) { //checkIng If the result Is Inserted
                result = 1; // return 1 where record Is Inserted
            } else {
                result = 0; // else 0
            }
            return result; // returnIng result

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
            Class.forName("com.mysql.jdbc.Driver"); //settIng drIver for Mysql
            connection = DriverManager
                    .getConnection(MysqlCon, Mysqluser, MysqlPass); // maKIng connectIon wIth mysql
            preStat = connection.prepareStatement("update tblRegister set Fullname=?,Country=?,State=?,City=?,"
                    + "Address=?,MobileNo=?,PhoneNo=?,PostalCode=?,Password=?  where Id=?"); // selectIng records from the table
            preStat.setString(1, name); // assIgIng the parameter to the where column
            preStat.setString(2, country);
            preStat.setString(3, state);
            preStat.setString(4, city);
            preStat.setString(5, address);
            preStat.setString(6, mobNum);
            preStat.setString(7, phoneNum);
            preStat.setString(8, postalcode);
            preStat.setString(9, pass);
            preStat.setString(10, Id);
            preStat.executeUpdate(); // executIng query
            if (preStat.getUpdateCount() == 1) {  //checkIng If the result Is Inserted
                result = 1; // return 1 where record Is Inserted
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
            Class.forName("com.mysql.jdbc.Driver"); //settIng drIver for Mysql
            connection = DriverManager
                    .getConnection(MysqlCon, Mysqluser, MysqlPass); // maKIng connectIon wIth mysql
            preStat = connection
                    .prepareStatement("SELECT * from tblRegister where Email= ? and Password= ?"); // selectIng records from the table
            preStat.setString(1, email); // assIgIng the parameter to the where column
            preStat.setString(2, password);
            rs = preStat.executeQuery(); // executIng query
            while (rs.next()) { // runnIng loop untIl results exIsts
                ID = Integer.valueOf(rs.getString("Id")); // assIgIng value to the varIable
                username = rs.getString("Fullname");

            }
            return ID; //returnIng the ID
        } catch (Exception e) {
            System.out.print(e);
            return 0;
        }
    }

    public String recoverPassword(String email) {
        try {
            String pass = "";
            Class.forName("com.mysql.jdbc.Driver"); //settIng drIver for Mysql
            connection = DriverManager
                    .getConnection(MysqlCon, Mysqluser, MysqlPass);// maKIng connectIon wIth mysql
            preStat = connection
                    .prepareStatement("SELECT * from tblRegister where Email= ?"); // selectIng records from the table
            preStat.setString(1, email); // assIgIng the parameter to the where column
            rs = preStat.executeQuery(); // executIng query
            while (rs.next()) { // runnIng loop untIl results exIsts

                pass = rs.getString("Password"); // assIgIng value to the varIable

            }
            return pass;
        } catch (Exception e) {
            return "";
        }
    }
}