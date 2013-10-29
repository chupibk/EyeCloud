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
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import sun.misc.BASE64Encoder;

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
    static ArrayList<String> arrDist = new ArrayList<String>();
    static ArrayList<String> arrX = new ArrayList<String>();
    static ArrayList<String> arrY = new ArrayList<String>();
    static ArrayList<String> arrT = new ArrayList<String>();
    static ArrayList<String> arrXsac = new ArrayList<String>();
    static ArrayList<String> arrYsac = new ArrayList<String>();
    static ArrayList<String> arrColumn = new ArrayList<String>();
    static ArrayList<String> arrValue = new ArrayList<String>();
    static ArrayList<String> arrTime = new ArrayList<String>();
    static ArrayList<String> arrColumn_lbl = new ArrayList<String>();
    static ArrayList<String> arrValue_lbl = new ArrayList<String>();

    public static float pixalToCenti(int x1, int y1, int x2, int y2) { //this function convert pixal to centimeter
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
        //degree = (float) (2 * (Math.atan((cent / 2) / (d1))));
        degree = (float) (2 * (Math.atan(cent / (2 * d1))));
        return (float) ((degree / (2 * Math.PI))) * 360; // it should be in mili second
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
    static Boolean FlagSccade;
    static long counter = 0, counterSaccade = 0, counterEF = 0;
    static String UserId = "1";
    static int FixCount = 0, SacCount = 0, timeInterval = 0,timeIntervalToIncrement=0;

    public static void addData_inHbase(String tablename, String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, tablename);
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes(UserId + ":" + rowKey + ":" + counter)); //userId + Filename+rownumber
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

    public static void addScade_inHbase(String tablename, String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, tablename);
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes(UserId + ":" + rowKey + ":" + counterSaccade)); //userId + Filename+rownumber
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
    static boolean IsStartTimefix = true, IsStartTimeSac = true;
    static String startTimefix, startTimeSac;

    public static void FixAlgorithm(String filename) throws IOException {
        FlagSccade = false;
        boolean IsrowsToAdd = true;
        arrDist.clear();
        arrX.clear();
        arrY.clear();
        arrT.clear();
        counter = 0;
        counterSaccade = 0;
        long looprunner = 0, loopStarter = 0;
        long rowsTorun_fix = 0;
        timeInterval = 2000;
        timeIntervalToIncrement=2000;
        rowsTorun_fix = Math.round(timeInterval * 120 / 1000);
        looprunner = rowsTorun_fix;
        long NosRow = Integer.valueOf(get_MapFile("ValidData", filename, "MD")); // Getting total nos of value
        HTable table = new HTable(conf, "ValidData");
        int count = 0;
        //  for (long z = loopStarter; z <= NosRow - 1; z++) 
        while (loopStarter <= NosRow) {

            for (long a = loopStarter; a <= looprunner - 1; a++) {
                Get get = new Get(Bytes.toBytes(UserId + ":" + filename + ":" + a)); // setting rowkey
                Result result = table.get(get);
                for (KeyValue kv : result.raw()) {
                    if (Bytes.toString(kv.getQualifier()).equals("AvgDist")) {
                        arrDist.add(new String(kv.getValue())); //adding AvgDist into arraylist
                    } else if (Bytes.toString(kv.getQualifier()).equals("AvgGxleft")) {
                        arrX.add(new String(kv.getValue())); //adding AvgGxleft into arraylist
                    } else if (Bytes.toString(kv.getQualifier()).equals("AvgGyleft")) {
                        arrY.add(new String(kv.getValue())); //adding AvgGyleft into arraylist
                    } else if (Bytes.toString(kv.getQualifier()).equals("Timestamp")) {
                        String time = new String(kv.getValue()); //adding timestamp into arraylist
                        time = time.replace("\n", ""); // remove \n frm the timestamp
                        arrT.add(time);
                        count++;
                        if (count == 2) {
                            int durtmp = Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0)); // difference of time
                            if (durtmp <= Missing_Time_THRESHOLD) {
                                float tmp = VT_Degree(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)),
                                        Integer.parseInt(arrX.get(1)), Integer.parseInt(arrY.get(1)),
                                        Integer.parseInt(arrDist.get(0)), Integer.parseInt(arrDist.get(1)),
                                        Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0))); // setting velocity into the var

                                if (tmp <= VELOCITY_THRESHOLD) {
                                    if (FlagSccade == true) {
                                        IsStartTimeSac = true;
                                        addfix_Sac_IntoHbase(filename); // This time adding saccade into hbase                                    
                                    }
                                    if (IsStartTimefix) {
                                        startTimefix = arrT.get(0);
                                        IsStartTimefix = false;
                                    }
                                    putXY(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)),
                                            Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0))); //passing parameter
                                    FlagSccade = false;
                                } else {
                                    if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {
                                        IsStartTimefix = true;
                                        addfix_Sac_IntoHbase(filename); // This time adding fixation into hbase
                                    }
                                    if (IsStartTimeSac) {
                                        startTimeSac = arrT.get(0);
                                        IsStartTimeSac = false;
                                    }
                                    FlagSccade = true;
                                    arrXsac.add(arrX.get(0));
                                    arrYsac.add(arrY.get(0));
                                    durationSac += Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0));
                                    countxySac++;
                                }
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
            if ((loopStarter + rowsTorun_fix) > NosRow && IsrowsToAdd) {

                loopStarter = loopStarter + 1;
                looprunner = NosRow;
                IsrowsToAdd = false;
            } else {
                loopStarter = looprunner;
                looprunner = looprunner + rowsTorun_fix;
            }
            if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {
                addfix_Sac_IntoHbase(filename); // This time adding fixation into hbase last time
            }
            addingEyeFeature_inHbase("EyeFeature",filename,FixCount,SacCount,timeIntervalToIncrement); // adding numbers of fixatIon and saccade In eyeFeature table
            FixCount=0;
            SacCount=0;

        }

    }

    public static void addfix_Sac_IntoHbase(String filename) {
        if (FlagSccade == true) { // when its Saccade
            IsStartTimeSac = true;
            arrColumn.add("StartTimeStamp");
            arrValue.add(startTimeSac);
            arrColumn.add("XsacBeginning");
            arrValue.add(arrXsac.get(0));
            arrColumn.add("XsacEnd");
            arrValue.add(arrXsac.get(countxySac - 1));
            arrColumn.add("SumDuration");
            arrValue.add(String.valueOf(durationSac));
            arrColumn.add("YsacBeginning");
            arrValue.add(arrYsac.get(0));
            arrColumn.add("YsacEnd");
            arrValue.add(arrYsac.get(countxySac - 1));
            SacCount = SacCount + 1;
            addScade_inHbase("FixData", filename + "-S-", "SC", arrColumn, arrValue);// Inserting Saccade with adding "S" in file name
            arrXsac.clear();
            arrYsac.clear();
            countxySac = 0;
            durationSac = 0;
        } else { // When Its fixation
            IsStartTimefix = true;
            arrColumn.clear();
            arrValue.clear();
            arrColumn.add("StartTimeStamp");
            arrValue.add(startTimefix);
            arrColumn.add("SumX");
            arrValue.add(String.valueOf(sumX / countxy));
            arrColumn.add("SumY");
            arrValue.add(String.valueOf(sumY / countxy));
            arrColumn.add("SumDuration");
            arrValue.add(String.valueOf(duration));
            FixCount = FixCount + 1;
            addData_inHbase("FixData", filename, "FX", arrColumn, arrValue); /// Inserting Fixtation
            arrColumn.clear();
            arrValue.clear();
        }
        countxy = 0;
        sumX = 0;
        sumY = 0;
        duration = 0;

    }

    public static void addingEyeFeature_inHbase(String tablename, String rowKey, int FixCount, int SacCount, int time) {
        try {
            HTable table = new HTable(conf, tablename);
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes(UserId + ":" + rowKey + ":" + counterEF)); //userId + Filename+rownumber
            put.add(Bytes.toBytes("FS"), Bytes.toBytes("GivenTime"), Bytes.toBytes(String.valueOf(time)));
            put.add(Bytes.toBytes("FS"), Bytes.toBytes("NumbersOfFixation"), Bytes.toBytes(String.valueOf(FixCount)));
            put.add(Bytes.toBytes("FS"), Bytes.toBytes("NumbersOfSaccade"), Bytes.toBytes(String.valueOf(SacCount)));
            
            table.put(put);
            counterEF++;
            timeIntervalToIncrement = timeIntervalToIncrement + timeInterval;
            table.flushCommits();
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InsertMapRecord(String tablename, String rowkey, String CF, String qualifier, String value) { // TO Insert one record

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

    public static void getlogin() throws SQLException {
        connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/CloudLogin", "root", "Sa1234");
        preStat = connection
                .prepareStatement("SELECT * from CloudLogin.tblRegister where Email= ? and Password= ?");
        preStat.setString(1, "abc@ymail.com");
        preStat.setString(2, "karachi");
        rs = preStat.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("Fullname"));
        }


    }

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

    public static void getrowkey() throws IOException {
        HTable table = new HTable(conf, "ValidData");
        Scan s = new Scan();
        s.addColumn(Bytes.toBytes("MD"), Bytes.toBytes("1"));
        ResultScanner scanner = table.getScanner(s);
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                System.out.println(new String(rr.getRow()));
                System.out.println(new String(rr.getValue(Bytes.toBytes("MD"), Bytes.toBytes("1"))));
            }
        } finally {
            scanner.close();
        }
    }

    public static int UpdateUser_detail(String name, String Id, String pass, String country, String state,
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
    
  private static void encrypt()
{
    try
    {
    String plainData="salm",cipherText,decryptedText;
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();
    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
    byte[] byteDataToEncrypt = plainData.getBytes();
    byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
    cipherText = new BASE64Encoder().encode(byteCipherText);
    aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
    byte[] byteDecryptedText = aesCipher.doFinal(byteCipherText);
    decryptedText = new String(byteDecryptedText);
    System.out.println("\n Plain Data : "+plainData+" \n Cipher Data : "+cipherText+" \n Decrypted Data : "+decryptedText);
    }
    catch(Exception e)
    {
         
    }
}

  public static void getfileNames(String tablename, String CF, String column) throws IOException {
        HTable table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon 
        Scan s = new Scan(); // makIng Instance of Scan
        s.addColumn(Bytes.toBytes(CF), Bytes.toBytes(column)); // addIng columnFamIly and Column Into the Scan
        ResultScanner scanner = table.getScanner(s); // assIgIng the scanner to scan 
      //  arrls.put("Select", "Select"); // addIng select value to the arrayLIst
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) { // runnInt the loop untIl the RR Is not null
                if (tablename.equals("FixData")) { // If there Is a request for FIXDATA table 
                    if (!new String(rr.getRow()).contains("-S-")) { // In FIXDATA table I STore two records for fIleName one Is for saccade and another Is for FIxatIon
                        // In Saccade I add -S- after the FIle name, that's why here I check & only show 
        //                arrls.put(new String(rr.getValue(Bytes.toBytes(CF), Bytes.toBytes(column))), new String(rr.getRow())); // addIng the values Into the ArrayLIst
                    }
                } else {
                  System.out.print(new String(rr.getValue(Bytes.toBytes(CF), Bytes.toBytes(column)))); // addIng the values Into the ArrayLIst
                }

            }
        } finally {
            scanner.close();
        }
    }


    public static void main(String[] args) {
        try {
            // System.out.println(get_MapFile("01-01-All-Data.txt"));
//            FixAlgorithm("Rec 01-All-Data.txt");
//            InsertMapRecord("FixData", "Rec 01-All-Data.txt", "MD", UserId, String.valueOf(counter));// inserting Nos of Rows of fixation
//            InsertMapRecord("FixData", "Rec 01-All-Data.txt" + "-S-", "MD", UserId, String.valueOf(counterSaccade)); // inserting Nos of Rows of Sccade
//            InsertMapRecord("EyeFeature", "Rec 01-All-Data.txt", "MD", UserId, String.valueOf(counterEF));// inserting Nos of Rows of fixation
//            System.out.println("done");
            getfileNames("EyeFeature","MD","5");

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
