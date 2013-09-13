/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.BufferedReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NavigableMap;
import javax.print.DocFlavor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.*;
import javax.servlet.http.HttpSession;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.commons.collections.MultiHashMap;
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
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    // Declaring & Initializing all the Variables, arraylist, and hashmap 
    private static final long serialVersionUID = 1L;
    static String hdnlblfilename = "", hdnfilename = "", hdntimestamp = null, hdnxleft = null, hdnxright = null, hdnyleft = null, hdnyright = null,
            hdndleft = null, hdndright = null, hdnvleft = null, hdnvright = null, hdnstname = null, hdnpart = null,
            hdnstartpnt = null, hdnduration = null, hdnlblstlname = null, xscreen = null, yscreen = null, xresol = null, yresol = null,
            fxdr = null, velth = null, mstm = null, txtTimeInterval = null, txtrate = null;
    int partcountrow = 0, partcountcell = 0;
    LinkedHashMap<String, String> arrls = new LinkedHashMap<String, String>();
    LinkedHashMap<String, String> partarrls = new LinkedHashMap<String, String>();
    MultiHashMap partarrlsvalue = new MultiHashMap();
    LinkedHashMap<String, String> lblarrls = new LinkedHashMap<String, String>();
    ArrayList<String> strholdexcel = new ArrayList<String>();
    ArrayList<String> Alrd_value = new ArrayList<String>();
    ArrayList<String> Alrd_column = new ArrayList<String>();
    ArrayList<String> Alrd_lbl_value = new ArrayList<String>();
    ArrayList<String> Alrd_lbl_column = new ArrayList<String>();
    // StringBuffer largeStr= new StringBuffer();
    String largeStr = null, largeStr_lbl = null, largeStr_part;
    DataClass dc = new DataClass(); // inItIalIzIng the data class
    boolean msgloadflag = false;
    private static Configuration conf = null;

    static {
        conf = HBaseConfiguration.create(); // storing Hbase configuration instance In conf varIable
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    String UserId;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        HttpSession session = request.getSession(false);
        Integer ID = Integer.parseInt(session.getAttribute("userId").toString()); // settIng value of sessIon Into the varaIable
        UserId = String.valueOf(ID);
        
        /*I have used Appache File Uploader for uploading Files bcoz of this all the 
         * controls have become the Items of this uploader that is why I have to use if Condition to get values of each 
         * control
         */
        FileItemFactory factory = new DiskFileItemFactory(); // 
        ServletFileUpload upload = new ServletFileUpload(factory); // Inializing file uploader 
        try {
            List<FileItem> fields = upload.parseRequest(request); // gettIng lIst of controls Into the lIst
            for (FileItem fielditem : fields) { // runnIng loop as many control u fInd from the JSp
                if (fielditem.isFormField()) {
                    if ("btnload".equals(fielditem.getFieldName())) { /* If Load button of Eye tracker File is 
                         * clicked do this.
                         */
                        if (arrls.size() != 0) { //clear the hidden files & array if they are already set
                            hdntimestamp = null;
                            hdnxleft = null;
                            hdnxright = null;
                            hdnyleft = null;
                            hdnyright = null;
                            hdndleft = null;
                            hdndright = null;
                            hdnvleft = null;
                            hdnvright = null;
                            hdnstname = null;
                            hdnpart = null;
                            arrls.clear();
                        }
//
//                        if (lblarrls.size() != 0) { // uncomment iT If label FIle uploadIng Is used, It basIcally clear the Label array
//                            lblarrls.clear();
//                        }
                        Iterator<FileItem> it = fields.iterator(); // takIng FIleItem Into the FIeld Iterator
                        while (it.hasNext()) { // run the loop untIll It found the Value
                            FileItem fileItem = it.next(); // settIng control InformatIon Into the FIleItem
                            boolean isFormField = fileItem.isFormField(); 
                            if (!isFormField) { // checkIng If Its the form control
                                // if (fileItem.getName().endsWith("txt")) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream())); // settIng buffer reader to read text fIle data vIa stream
                                String str;
                                LineNumberReader ln = new LineNumberReader(br); // to get total line Number of Buffer reader
                                while (ln.getLineNumber() == 0) { // runniNG loop only one tIme to get the column name from the text FIle
                                    str = ln.readLine(); // reading one liNe & storIng Into the 
                                    if (str != null) { //If files is selected
                                        request.setAttribute("fileload", "0"); // if iS SELECTEd to upload
                                        largeStr = fileItem.getString(); //set textfile to string 
                                        String[] strArr = str.split("\t"); // spliting string as per /t Into string array
                                        for (int a = 0; a <= strArr.length - 1; a++) {
                                            arrls.put(strArr[a], strArr[a]); // SETTING THE FIRST LINE OF TEXT FILE(HEADER) IN hashmap
                                        }
                                    } else {
                                        request.setAttribute("fileload", "1"); //if file is not selected
                                        break;
                                    }
                                    //holdloop++;
                                }

                                br.close(); // closing the buffer reader

//                                } else {
//                                    if (!msgloadflag) {
//                                        request.setAttribute("fileload", "2"); //if file txt is not selected
//                                    }
//                                }
                            }

                        }

                    } else if ("btnpart".equals(fielditem.getFieldName())) { // uncomment these lines and lines in loadData.jsp file, participant file will start working 
//                        if (partarrls.size() != 0) {
//                            partarrls.clear();
//                        }
//                        if (partarrlsvalue.size() != 0) {
//                            partarrlsvalue.clear();
//                        }
//                        Iterator<FileItem> it = fields.iterator();
//                        partcountrow = 0;
//                        while (it.hasNext()) {
//                            FileItem fileItem = it.next();
//                            boolean isFormField = fileItem.isFormField();
//                            if (!isFormField) {
//                                //Get the workbook instance for XLS file 
//                             //   if (fileItem.getName().endsWith("txt")) {
//                                    BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream()));
//                                    String str;
//                                    LineNumberReader ln = new LineNumberReader(br);
//                                    partarrls.put("Select", "Select");
//                                    while ((str = ln.readLine()) != null) {
//
//                                        if (str != null) { //If files is selected
//
//                                            request.setAttribute("filepart", "0");
//                                            String[] strArr = str.split("	");
//                                            for (int a = 0; a <= strArr.length - 1; a++) {
//                                                if (partcountrow == 0) {
//                                                    partarrls.put(strArr[a], strArr[a]); //Set only the columns name
//                                                    strholdexcel.add(strArr[a]);
//                                                } else {
//                                                    partarrlsvalue.put(strholdexcel.get(partcountcell), strArr[a]);
//                                                    partcountcell++;
//                                                }
//                                            }
//                                        } else {
//                                            request.setAttribute("filepart", "1");
//                                            break;
//                                        }
//                                        //holdloop++;
//                                        partcountcell = 0;
//                                        partcountrow++;
//                                    }
//                                    br.close();
//                                   
////                                } else {
////                                    request.setAttribute("filepart", "2"); //if file txt is not selected
////                                   
////                                }
//                            }
//                        }
                    } else if ("btnlblfiles".equals(fielditem.getFieldName())) { // uncomment these lines and lines in loadData.jsp file, label file will start working 
//                        Iterator<FileItem> it = fields.iterator();
//                        lblarrls.put("Select", "Select");
//                        while (it.hasNext()) {
//                            FileItem fileItem = it.next();
//                            boolean isFormField = fileItem.isFormField();
//                            if (!isFormField) {
//                               // if (fileItem.getName().endsWith("txt")) {
//                                    BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream()));
//                                    String str;
//                                    LineNumberReader ln = new LineNumberReader(br);
//                                    while (ln.getLineNumber() == 0) {
//                                        str = ln.readLine();
//                                        if (str != null) {
//                                            largeStr_lbl = fileItem.getString();
//                                            String[] strArr = str.split("	");
//                                            for (int a = 0; a <= strArr.length - 1; a++) {
//                                                lblarrls.put(strArr[a], strArr[a]);
//                                                request.setAttribute("filelabel", "0");
//                                            }
//                                        } else {
//                                            request.setAttribute("filelabel", "1");
//                                            break;
//                                        }
//                                    }
//                                    br.close();
//                                   
////                                } else {
////                                    request.setAttribute("filelabel", "2"); //if file txt is not selected
////                                  
////                                }
//                            }
                        //  }
                        // break;
                    } else if ("btnsave".equals(fielditem.getFieldName())) { // Start saving data into Hbase & then reading that
                        if (!hdnxleft.isEmpty() && !hdnxleft.equals("") && !hdnyleft.isEmpty() && !hdnyleft.equals("") && !hdntimestamp.isEmpty() && !hdntimestamp.equals("") 
                                && !hdnstname.isEmpty() && !hdnstname.equals("") && !hdndleft.isEmpty() && !hdndleft.equals("")) { // checking if all the required fields are selected
                            addrawData(); // Adding raw Data into Hbase database from the Text File
                            //addLabelrawData(); // uncomment it FOR Adding Label Data from the Text File
                            Alrd_column.clear(); // clear the array
                            Alrd_value.clear(); // clear the array
                            Alrd_lbl_column.clear(); // clear the column array for label data, NB It Isnt currently In used
                            Alrd_lbl_value.clear(); // clear the label array for label data, NB It Isnt currently In used
                            dc.get_DataHbase_common(0, 1000, "ok", UserId, "RawData", hdnfilename, "MF", Alrd_column, Alrd_value); // getting raw data from Hbase
                            //   dc.get_DataHbase_common(0, 0, "", UserId, "RawData", hdnlblfilename, "MF", Alrd_lbl_column, Alrd_lbl_value); // uncomment it FOR gettIng Label Data from the hbase
                            request.setAttribute("Alrd_column", Alrd_column); // setting array List for forwarding data to next page
                            request.setAttribute("Alrd_value", Alrd_value);  // setting array List for forwarding data to next page
                            request.setAttribute("Alrd_lbl_column", Alrd_lbl_column);  // NB It Isnt currently In used, FOR Label Data, setting array List for forwarding data to next page
                            request.setAttribute("Alrd_lbl_value", Alrd_lbl_value);  // NB It Isnt currently In used, FOR Label Data, setting array List for forwarding data to next page
                            session.setAttribute("hdnxleft", hdnxleft);  // setting session of GazePointXLeft
                            session.setAttribute("hdnyleft", hdnyleft); // setting session of GazePointYLeft
                            session.setAttribute("hdnxright", hdnxright); // setting session of GazePointXright
                            session.setAttribute("hdnyright", hdnyright); // setting session of GazePointyright
                            session.setAttribute("hdndleft", hdndleft);// setting session of DistanceLeft
                            session.setAttribute("hdndright", hdndright);// setting session of Distanceright
                            session.setAttribute("hdnfilename", hdnfilename);// setting session of filename of the textfile that is uploaded
                            //session.setAttribute("hdnlblfilename", hdnlblfilename); // uncomment it if label text file is used
                            session.setAttribute("xscreen", xscreen); // setting session of xscreen
                            session.setAttribute("yscreen", yscreen); // setting session of yscreen
                            session.setAttribute("xresol", xresol); // setting session of xresolution
                            session.setAttribute("yresol", yresol); // setting session of yresolution
                            session.setAttribute("fxdr", fxdr); // setting session of Fixation duration Threshold
                            session.setAttribute("velth", velth); // setting session of vel Threshold
                            session.setAttribute("mstm", mstm); // setting session of missing time Threshold
                            session.setAttribute("timeInterval", txtTimeInterval); // setting session of TimeInterval
                            session.setAttribute("rate", txtrate); // setting session of rate
                            request.setAttribute("error", "1"); // setting attribute of error
                            RequestDispatcher rd = request.getRequestDispatcher("/ShowRawData.jsp"); // redirecting to the next page
                            rd.forward(request, response);
                            break;
                        } else {
                            request.setAttribute("error", "0");
                            request.setAttribute("arrls", arrls); //setting arrls array lIst of columns Into the varIable for bIdIng It wIth the dropdown
                            //request.setAttribute("lblarrls", lblarrls); //uncomment it label array
                            //request.setAttribute("partarrls", partarrls); //uncomment it participant array
                            request.setAttribute("hdnfilename", hdnfilename); //setting attribute to forward to the next page
                            //request.setAttribute("hdnlblfilename", hdnlblfilename); //uncomment it label file
                            request.setAttribute("hdntimestamp", hdntimestamp); //setting attribute to forward to the next page
                            request.setAttribute("hdnxleft", hdnxleft);//setting attribute to forward to the next page
                            request.setAttribute("hdnxright", hdnxright);//setting attribute to forward to the next page
                            request.setAttribute("hdnyleft", hdnyleft);//setting attribute to forward to the next page
                            request.setAttribute("hdnyright", hdnyright);//setting attribute to forward to the next page
                            request.setAttribute("hdndleft", hdndleft);//setting attribute to forward to the next page
                            request.setAttribute("hdndright", hdndright);//setting attribute to forward to the next page
                            request.setAttribute("hdnvleft", hdnvleft);//setting attribute to forward to the next page
                            request.setAttribute("hdnvright", hdnvright);//setting attribute to forward to the next page
                            request.setAttribute("hdnstname", hdnstname);//setting attribute to forward to the next page
                            RequestDispatcher rd = request.getRequestDispatcher("/LoadData.jsp"); // redirecting to the same page incase of error
                            rd.forward(request, response);
                        }

                    }
                    if ("hdntimestamp".equals(fielditem.getFieldName())) { // checking if condition & setting hidden fields hdntimestamp values to show it back to the user
                        hdntimestamp = fielditem.getString();
                    } else if ("hdnxleft".equals(fielditem.getFieldName())) { // checking if condition & setting hidden fields hdnxleft values to show it back to the user
                        hdnxleft = fielditem.getString();
                    } else if ("hdnxright".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdnxright values to show it back to the user
                        hdnxright = fielditem.getString();
                    } else if ("hdnyleft".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdnyleft values to show it back to the user
                        hdnyleft = fielditem.getString();
                    } else if ("hdnyright".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdnyright values to show it back to the user
                        hdnyright = fielditem.getString();
                    } else if ("hdndleft".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdndleft values to show it back to the user
                        hdndleft = fielditem.getString();
                    } else if ("hdndright".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdndright values to show it back to the user
                        hdndright = fielditem.getString();
                    } else if ("hdnvleft".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdnvleft values to show it back to the user
                        hdnvleft = fielditem.getString();
                    } else if ("hdnvright".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdnvright values to show it back to the user
                        hdnvright = fielditem.getString();
                    } else if ("hdnstname".equals(fielditem.getFieldName())) { // checking if condition & setting hidden fields hdnstname values to show it back to the user
                        hdnstname = fielditem.getString();
                    } else if ("hdnfilename".equals(fielditem.getFieldName())) {// checking if condition & setting hidden fields hdnfilename values to show it back to the user
                        hdnfilename = fielditem.getString();
//                    } else if ("hdnpart".equals(fielditem.getFieldName())) {  //uncomment it if you want to want to work on label & participant file
//                        hdnpart = fielditem.getString();
//                    } else if ("hdnlblfilename".equals(fielditem.getFieldName())) { // here I am doiNg the same checkg the condition &  storing the 
//                        hdnlblfilename = fielditem.getString();                     // values into the varaiable
//                    } else if ("hdnstartpnt".equals(fielditem.getFieldName())) {
//                        hdnstartpnt = fielditem.getString();
//                    } else if ("hdnduration".equals(fielditem.getFieldName())) {
//                        hdnduration = fielditem.getString();
//                    } else if ("hdnlblstlname".equals(fielditem.getFieldName())) {
//                        hdnlblstlname = fielditem.getString();
                    } else if ("txtxscreen".equals(fielditem.getFieldName())) { // checking if condition & setting txtxscreen values to the variable to pass to the session
                        xscreen = fielditem.getString();
                    } else if ("txtyscreen".equals(fielditem.getFieldName())) {// checking if condition & setting txtyscreen values to the variable to pass to the session
                        yscreen = fielditem.getString();
                    } else if ("txtxresol".equals(fielditem.getFieldName())) {// checking if condition & setting txtxresol values to the variable to pass to the session
                        xresol = fielditem.getString();
                    } else if ("txtyresol".equals(fielditem.getFieldName())) {// checking if condition & setting txtyresol values to the variable to pass to the session
                        yresol = fielditem.getString();
                    } else if ("txtfxdr".equals(fielditem.getFieldName())) { // checking if condition & setting txtfxdr values to the variable to pass to the session
                        fxdr = fielditem.getString();
                    } else if ("txtvelth".equals(fielditem.getFieldName())) { // checking if condition & setting txtvelth values to the variable to pass to the session
                        velth = fielditem.getString();
                    } else if ("txtmstm".equals(fielditem.getFieldName())) { // checking if condition & setting txtmstm values to the variable to pass to the session
                        mstm = fielditem.getString();
                    } else if ("txtTimeInterval".equals(fielditem.getFieldName())) { // checking if condition & setting txtTimeInterval values to the variable to pass to the session
                        txtTimeInterval = fielditem.getString();
                    } else if ("txtrate".equals(fielditem.getFieldName())) { // checking if condition & setting txtrate value to the variable to pass to the session
                        txtrate = fielditem.getString();
                    }
                }
            }
            //Uncomment these lInes for addIng partIcIpant LIst
//            List lstpart = null;
//            if (hdnpart != null && !hdnpart.isEmpty()) { // set list to show the participant list
//                request.setAttribute("selectedpart", hdnpart);
//                lstpart = (List) partarrlsvalue.get(hdnpart);
//            } else {
//                request.setAttribute("selectedpart", "ID");
//                lstpart = (List) partarrlsvalue.get("ID");
//            }
            if (hdnfilename.contains("\\")) { // remove the \ sign from the file name of eye tracker
                String[] holdpath = hdnfilename.split("[\\\\]");
                hdnfilename = holdpath[2];
            }
            if (hdnlblfilename.contains("\\")) { // remove the \ sign from the file name of label
                String[] holdpath = hdnlblfilename.split("[\\\\]");
                hdnlblfilename = holdpath[2];
            }

            //request.setAttribute("lstpart", lstpart); //Uncomment these lInes for addIng partIcIpant LIst
            request.setAttribute("arrls", arrls); //setting arrls array lIst of columns Into the varIable for bIdIng It wIth the dropdown
            //request.setAttribute("lblarrls", lblarrls); //uncomment it label file
            //request.setAttribute("partarrls", partarrls); //uncomment it participant file
            request.setAttribute("hdnfilename", hdnfilename);//setting attribute to forward to the next page
            //request.setAttribute("hdnlblfilename", hdnlblfilename); //uncomment it label file
            request.setAttribute("hdntimestamp", hdntimestamp);//setting attribute to forward to the next page
            request.setAttribute("hdnxleft", hdnxleft);//setting attribute to forward to the next page
            request.setAttribute("hdnxright", hdnxright);//setting attribute to forward to the next page
            request.setAttribute("hdnyleft", hdnyleft);//setting attribute to forward to the next page
            request.setAttribute("hdnyright", hdnyright);//setting attribute to forward to the next page
            request.setAttribute("hdndleft", hdndleft);//setting attribute to forward to the next page
            request.setAttribute("hdndright", hdndright);//setting attribute to forward to the next page
            request.setAttribute("hdnvleft", hdnvleft);//setting attribute to forward to the next page
            request.setAttribute("hdnvright", hdnvright);//setting attribute to forward to the next page
            request.setAttribute("hdnstname", hdnstname);//setting attribute to forward to the next page

            RequestDispatcher rd = request.getRequestDispatcher("/LoadData.jsp"); // showing load data page agaiN
            rd.forward(request, response);
            //	out.println("</tr>");
            //out.println("</table>");
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }

    public void addrawData() {
        try {

            if (largeStr != null && !largeStr.isEmpty()) { // If the strIng Isn't null & empty
                int countcoulmn = 0; // varIable for counting column in a textfile
                long countrow = 0; // varIable for counting nos of rows in a textfile
                largeStr = largeStr.replaceAll("\\r\\n", "\t"); // remove \n from the  text file
                String[] strArr = largeStr.split("\t");// spliting text into the array
                ArrayList<String> list = new ArrayList<String>(arrls.values()); // setting all values of array into another array
                HTable table = new HTable(conf, "RawData"); // here RawData Is the table name whIch I am assIng It here
                table.setAutoFlush(false);
                table.setWriteBufferSize(1024 * 1024 * 12); //setting the buffer sIze
                Put put = new Put(Bytes.toBytes(UserId + ":" + hdnfilename + ":" + countrow)); //setting rowkey in this format userID + FIleName + RowNumber
                for (int a = 0; a <= strArr.length - 1; a++) { // run the loop untl Str arry has value
                    for (int b = countcoulmn; b <= list.size() - 1; b++) { // run the loop untl list has column value, It means thIs loop wIll run as long as there are columns
                        if (list.get(b).equals(strArr[a])) { // here I checked if in str arry there are header like list header then dont do anythng
                        } else {
                            put.add(Bytes.toBytes("EF"), Bytes.toBytes(list.get(b)), Bytes.toBytes(strArr[a])); // else I add the value into PUT with this Column family  is EF(EyeTracker File)
                        }
                        if ((countcoulmn + 1) == list.size()) { // here I Check if lIST Size are equal to the header columns count 
                            countcoulmn = 0;
                            if (put.size() != 0) {
                                countrow++; // incrment the row
                                table.put(put); // now addIng all rows In table
                                put = new Put(Bytes.toBytes(UserId + ":" + hdnfilename + ":" + countrow)); //INIALIzIng NEW PUT WITH THIS ROWKEY userID + FIleName + RowNumber
                            }

                        } else {
                            b++; // incrementing the variable
                            countcoulmn = b; //set countcolumn to b
                        }
                        break;
                    }
                }
                table.flushCommits(); // flushIng the commmIt of table
                table.close(); //closIng the table
                dc.InsertMapRecord("RawData", hdnfilename, "MF", UserId, String.valueOf(countrow)); //Insert total nos of rows into the Raw data with the name of eye tracker file  under MF column family
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // uncomment it FOR adding Label Data
//    public void addLabelrawData() {
//        try {
//            if (largeStr_lbl != null && !largeStr_lbl.isEmpty()) {
//                int countcoulmn = 0;
//                long countrow = 0;
//                largeStr_lbl = largeStr_lbl.replaceAll("\\r\\n", "\t");
//                String[] strArr = largeStr_lbl.split("\t");
//                ArrayList<String> list = new ArrayList<String>(lblarrls.values());
//                list.remove("Select");
//                HTable table = new HTable(conf, "RawData");
//                table.setAutoFlush(false);
//                table.setWriteBufferSize(1024 * 1024 * 12);
//                Put put = new Put(Bytes.toBytes(UserId + ":" + hdnlblfilename + ":" + countrow)); // userID + FIleName + RowNumber
//                for (int a = 0; a <= strArr.length - 1; a++) {
//                    for (int b = countcoulmn; b <= list.size() - 1; b++) {
//                        if (list.get(b).equals(strArr[a])) {
//                        } else {
//                            put.add(Bytes.toBytes("LF"), Bytes.toBytes(list.get(b)), Bytes.toBytes(strArr[a])); //LF stands for label file
//
//                        }
//                        if ((countcoulmn + 1) == list.size()) {
//                            countcoulmn = 0;
//                            if (put.size() != 0) {
//                                countrow++;
//                                table.put(put);
//                                put = new Put(Bytes.toBytes(UserId + ":" + hdnlblfilename + ":" + countrow)); // userID + FIleName + RowNumber
//                            }
//
//                        } else {
//                            b++;
//                            countcoulmn = b;
//                        }
//                        break;
//                    }
//                }
//                table.flushCommits();
//                table.close();
//                dc.InsertMapRecord("RawData", hdnfilename, "LF", UserId, hdnlblfilename); //Insert RawData file name under LF Column family to link label file name to the user
//                dc.InsertMapRecord("RawData", hdnlblfilename, "MF", UserId, String.valueOf(countrow)); //Insert total nos of rows into the Label data with the name of label file under MF column family with  is userID
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
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
    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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