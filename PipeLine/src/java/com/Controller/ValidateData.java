/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
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

    //declarIng all the publIc varIables
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
    long loopStarter, looprunner = 1000, counter = 0, counterSaccade = 0, counterEF = 0;
    private static Configuration conf = null;
    boolean IsStartTimefix = true, IsStartTimeSac = true;
    String startTimefix, startTimeSac;
    int FixCount = 0, SacCount = 0, timeInterval = 0, timeIntervalToIncrement = 0, sample_rate = 0;

    // 
    static {
        conf = HBaseConfiguration.create(); // storing Hbase configuration instance In conf varIable
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    String UserId;
    
    // uncomment it FOR reading from rawData table for Label text fIle, valIdatIng It and adding them
    // Into ValidData table

//    public void Read_LabelData_forValdiation(String Efilename, String filename) { // Here I am reading label data for validation
//        HTable table = null;
//        try {
//
//            counter = 0;
//            int begin, end;
//            long NosRow = Integer.valueOf(dc.get_MapFile("RawData", filename, "MF"));
//            boolean breakLoop;
//            table = new HTable(conf, "RawData");
//            for (int a = 0; a <= NosRow - 1; a++) {
//                breakLoop = false;
//                Get get = new Get(Bytes.toBytes(UserId + ":" + filename + ":" + a)); // USER ID + filename +  nos of row
//                Result result = table.get(get);
//                arrColumn.clear();
//                arrValue.clear();
//                for (KeyValue kv : result.raw()) { //adding column and values
//                    arrColumn.add(new String(kv.getQualifier()));
//                    arrValue.add(new String(kv.getValue()));
//                }
//                begin = arrColumn.indexOf("Begin Time - msec");
//                end = arrColumn.indexOf("End Time - msec");
//                if ((arrValue.get(begin).equals("") && arrValue.get(end).equals(""))) // If row is blank or contains garbage
//                {
//                    breakLoop = true;
//                }
//                if (!breakLoop) {
//                    addData_inHbase("ValidData", filename, "LD", arrColumn, arrValue);
//                }
//            }
//            table.close();
//            dc.InsertMapRecord("ValidData", Efilename, "LD", UserId, filename); //Insert validData file name as row and Label data Filename as value 
//            dc.InsertMapRecord("ValidData", filename, "MD", UserId, String.valueOf(counter)); // here I am storing nos of rows of ValIDData Into LD(Label Data) column FamIly
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public float pixalToCenti(int x1, int y1, int x2, int y2) { //this function convert pixal to centimeter
        float xf1, xf2, yf1, yf2; 
        xf1 = (float) x1 * SCREEN_WIDTH / RESOLUTION_WIDTH;
        xf2 = (float) x2 * SCREEN_WIDTH / RESOLUTION_WIDTH;
        yf1 = (float) y1 * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
        yf2 = (float) y2 * SCREEN_HEIGHT / RESOLUTION_HEIGHT;
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)); //makIng square root by gIven varIables
    }

    public float VT_Degree(int x1, int y1, int x2, int y2, float d1, float d2, int dur) {
        float cent = pixalToCenti(x1, y1, x2, y2);
        float degree;
        //degree = (float) (2 * (Math.atan((cent / 2) / (d1))));
        degree = (float) (2 * (Math.atan(cent / (2 * d1))));
        return (float) ((degree / (2 * Math.PI))) * 360; // it should be in mili second
    }

    public void putXY(int x, int y, int time) { //setting and incrementing value for FIxatIon 
        countxy++; // countIng as many tIme thIs FuncatIon Is called
        sumX += x;
        sumY += y;
        duration += time;
    }
    Boolean FlagSccade;

    //here FIxatIon & Saccade AlgorIthm 
    public void FixAlgorithm(String filename) throws IOException {
        FlagSccade = false; // settIng Saccade flag to false
        boolean IsrowsToAdd = true; // thIs varIable Is to check wheater Rows to Add or not
        arrDist.clear(); // clearIng all arrayLIst
        arrX.clear();// clearIng all arrayLIst
        arrY.clear();// clearIng all arrayLIst
        arrT.clear();// clearIng all arrayLIst
        counter = 0; // makIng counter to 0
        counterEF = 0;// makIng counterEF to 0
        counterSaccade = 0;// makIng counterSaccade to 0
        long looprunner = 0, loopStarter = 0;
        long rowsTorun_fix = 0;
        timeIntervalToIncrement = timeInterval;// settIng TIme Interval to Increment
        rowsTorun_fix = Math.round(timeInterval * sample_rate / 1000); //CalculatIng rows to run by tHIs formula 
        looprunner = rowsTorun_fix; // assIgIng rowsTorun_fix to looprunner to run the loop
        long NosRow = Integer.valueOf(dc.get_MapFile("ValidData", filename, "MD")); // Getting total nos of record
        HTable table = new HTable(conf, "ValidData"); // assIgIng tablename and confIguratIon 
        int count = 0;
        while (loopStarter <= NosRow) { // run loop untIl nos of Row
            for (long a = loopStarter; a <= looprunner - 1; a++) { // run loop UntIll loopRunner
                Get get = new Get(Bytes.toBytes(UserId + ":" + filename + ":" + a)); // setting rowkey
                Result result = table.get(get); // gettIng data from Hbase by GIVEn get
                for (KeyValue kv : result.raw()) {// runnINg loop untIll result has value
                    if (Bytes.toString(kv.getQualifier()).equals("AvgDist")) {
                        arrDist.add(new String(kv.getValue())); //adding AvgDist into arraylist
                    } else if (Bytes.toString(kv.getQualifier()).equals("AvgGxleft")) {
                        arrX.add(new String(kv.getValue())); //adding AvgGxleft into arraylist
                    } else if (Bytes.toString(kv.getQualifier()).equals("AvgGyleft")) {
                        arrY.add(new String(kv.getValue())); //adding AvgGyleft into arraylist
                    } else if (Bytes.toString(kv.getQualifier()).equals("Timestamp")) {
                        String time = new String(kv.getValue()); //adding timestamp into arraylist
                        time = time.replace("\n", ""); // remove \n frm the timestamp
                        arrT.add(time); //addIng tIme to the ArrayLIst tIme
                        count++; // IncrementIng count
                        if (count == 2) {
                            int durtmp = Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0)); // difference of time
                            if (durtmp <= Missing_Time_THRESHOLD) { // checKIng the dIfference of TIme Is Greater then or equal to gIven the TIme threshold
                                float tmp = VT_Degree(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)), //calculatIng VelcoITY
                                        Integer.parseInt(arrX.get(1)), Integer.parseInt(arrY.get(1)),
                                        Integer.parseInt(arrDist.get(0)), Integer.parseInt(arrDist.get(1)),
                                        Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0))); // setting velocity into the var

                                if (tmp <= VELOCITY_THRESHOLD) { // checkIng the velocIty degree Is less than or equal to the GIven VELOCITY_THRESHOLD
                                    //IF Its VELOCITY_THRESHOLD Is greater or equal to the calculated velocIty then add FIXATION else Add Saccade
                                    if (FlagSccade == true) { // IF flagSaccde Is true than Add Saccade Into database
                                        IsStartTimeSac = true; // MakIng It to true to check the TIme when Start TIME saccade Is Started
                                        addfix_Sac_IntoHbase(filename); // This time adding saccade into hbase                                    
                                    }
                                    if (IsStartTimefix) { //If Its true then
                                        startTimefix = arrT.get(0);// set startTimefix to the start TIme of the FIXATION
                                        IsStartTimefix = false; //settIng It false so that we can only recIeve startIng TIME
                                    } 
                                    putXY(Integer.parseInt(arrX.get(0)), Integer.parseInt(arrY.get(0)),
                                            Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0))); //addIng & IncrementIng FIxatIon 
                                    FlagSccade = false;
                                } else {
                                    if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) { //checkIng DuratION IS GEater than FIXATION_DURATION_THRESHOLD
                                        IsStartTimefix = true; //settIng IsStartTimefix to true so that we can 1st tIme of the FIXatIon
                                        addfix_Sac_IntoHbase(filename); // This time adding fixation into hbase
                                    }
                                    if (IsStartTimeSac) { // If Its true then 
                                        startTimeSac = arrT.get(0); // // set startTimeSac to the start TIme of the saccade
                                        IsStartTimeSac = false;//settIng It false so that we can only recIeve startIng TIME
                                    }
                                    FlagSccade = true;
                                    arrXsac.add(arrX.get(0)); // addIng 1st value of gazepoInt x Into arrXsac
                                    arrYsac.add(arrY.get(0));  // addIng 1st value of gazepoInt x Into arrYsac
                                    durationSac += Integer.parseInt(arrT.get(1)) - Integer.parseInt(arrT.get(0)); //IncrementIng duratIon By makIng dIfference of arrT1 and arrT2
                                    countxySac++; // IncrementIng countxySac
                                }
                            }
                            count = 1;
                            arrDist.remove(0); // remove 1st value from arrDist
                            arrX.remove(0);// remove 1st value from arrX
                            arrY.remove(0);// remove 1st value from arrY
                            arrT.remove(0);// remove 1st value from arrT
                        }
                    }
                }
            }
            if ((loopStarter + rowsTorun_fix) > NosRow && IsrowsToAdd) { // checkIng If loopstarter and rowsTorun_fix become more than NosRow and Its IsrowsToAdd then

                loopStarter = loopStarter + 1; // Just Increment loopStarter wIth 1
                looprunner = NosRow; // set looprunner to NosRow
                IsrowsToAdd = false; // make IsrowsToAdd to false so that ThIs pIece of Code should not run agaIn
            } else {
                loopStarter = looprunner; // settIng loopStarter to Looprunner
                looprunner = looprunner + rowsTorun_fix; //InCremenTIng looprunner wIth rowsTorun_fix
            }
            if (countxy > 0 && duration > FIXATION_DURATION_THRESHOLD) {
                addfix_Sac_IntoHbase(filename); // This time adding fixation or saccade into hbase last time
            }
            addingEyeFeature_inHbase("EyeFeature", filename, FixCount, SacCount, timeIntervalToIncrement); // adding numbers of fixatIon and saccade In eyeFeature table
            FixCount = 0; // settIng FixCount to 0 so that Next TIme we have refreshed value
            SacCount = 0; // settIng SacCount to 0 so that Next TIme we have refreshed value

        }

    }

    public void addfix_Sac_IntoHbase(String filename) {
        if (FlagSccade == true) { // when its Saccade
            IsStartTimeSac = true; //settIng It to True
            arrColumn.add("StartTimeStamp"); // addIng StartTimeStamp column INto the arrColumn arrayLIst
            arrValue.add(startTimeSac); // addIng startTimeSac value INto the arrValue arrayLIst
            arrColumn.add("XsacBeginning"); // addIng XsacBeginning column INto the arrColumn arrayLIst
            arrValue.add(arrXsac.get(0)); // addIng zero Index value of  arrXsac INto the arrValue arrayLIst
            arrColumn.add("XsacEnd"); // addIng XsacEnd column INto the arrColumn arrayLIst
            arrValue.add(arrXsac.get(countxySac - 1)); // addIng last Index value of  arrXsac INto the arrValue arrayLIst
            arrColumn.add("SumDuration"); // addIng SumDuration column INto the arrColumn arrayLIst
            arrValue.add(String.valueOf(durationSac)); // addIng durationSac value INto the arrValue arrayLIst
            arrColumn.add("YsacBeginning"); // addIng YsacBeginning column INto the arrColumn arrayLIst
            arrValue.add(arrYsac.get(0)); // addIng zero Index value of  arrYsac INto the arrValue arrayLIst
            arrColumn.add("YsacEnd"); // addIng YsacEnd column INto the arrColumn arrayLIst
            arrValue.add(arrYsac.get(countxySac - 1));// addIng last Index value of  arrYsac INto the arrValue arrayLIst
            SacCount = SacCount + 1; //IncremenTIng SacCount wIth 1
            addScade_inHbase("FixData", filename + "-S-", "SC", arrColumn, arrValue);// Inserting Saccade with adding "S" in file name
            arrXsac.clear(); //clearIng arrXsac
            arrYsac.clear(); //clearIng arrYsac
            countxySac = 0; 
            durationSac = 0;
        } else { // When Its fixation
            IsStartTimefix = true; 
            arrColumn.clear(); 
            arrValue.clear();
            arrColumn.add("StartTimeStamp"); // addIng StartTimeStamp column INto the arrColumn arrayLIst
            arrValue.add(startTimefix); //addIng startTimefix value Into arrValue
            arrColumn.add("SumX"); // addIng SumX column INto the arrColumn arrayLIst
            arrValue.add(String.valueOf(sumX / countxy)); //dIvIdIng sumx/countxy and then addIng the result Into arrValue
            arrColumn.add("SumY"); // addIng SumY column INto the arrColumn arrayLIst
            arrValue.add(String.valueOf(sumY / countxy)); //dIvIdIng sumY / countxy and then addIng the result Into arrValue
            arrColumn.add("SumDuration"); // addIng SumDuration column INto the arrColumn arrayLIst
            arrValue.add(String.valueOf(duration)); // addIng duratIon Into the arrValue
            FixCount = FixCount + 1;  //IncremenTIng FixCount wIth 1
            addData_inHbase("FixData", filename, "FX", arrColumn, arrValue); /// Inserting Fixtation
            arrColumn.clear();//clearIng arrColumn
            arrValue.clear();
        }
        countxy = 0;
        sumX = 0;
        sumY = 0;
        duration = 0;

    }

    //ThIs functIon wIll Insert nos of FIxaTIon & Saccade taken place In each round
    public void addingEyeFeature_inHbase(String tablename, String rowKey, int FixCount, int SacCount, int time) { 
        try {
            HTable table = new HTable(conf, tablename);// assIgIng tablename and confIguratIon 
            table.setAutoFlush(false); //settIng table to autoflush false
            table.setWriteBufferSize(1024 * 1024 * 12); // settIng buffer sIze
            Put put = new Put(Bytes.toBytes(UserId + ":" + rowKey + ":" + counterEF)); // makIng Instance of the Put WIth the rowkey, rowkey format Is userId + Filename+rownumber
            put.add(Bytes.toBytes("FS"), Bytes.toBytes("GivenTime"), Bytes.toBytes(String.valueOf(time)));// addIng columnFamIly, Column name and Values Into the Put
            put.add(Bytes.toBytes("FS"), Bytes.toBytes("NumbersofFixation"), Bytes.toBytes(String.valueOf(FixCount))); // addIng columnFamIly, Column name and Values Into the Put
            put.add(Bytes.toBytes("FS"), Bytes.toBytes("NumbersofSaccade"), Bytes.toBytes(String.valueOf(SacCount))); // addIng columnFamIly, Column name and Values Into the Put
            table.put(put); // InsertIng record Into the table now wIth the Put
            counterEF++;
            timeIntervalToIncrement = timeIntervalToIncrement + timeInterval;
            table.flushCommits();
            table.close(); // fInally closIng the table 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public void Read_RawData_forValidation(String filename, String gxleft, String gxright,
            String gyleft, String gyright, String dleft, String dright) {
        HTable table = null;
        try {
            counter = 0;
            long NosRow = Integer.valueOf(dc.get_MapFile("RawData", filename, "MF")); //gettIng nos of rows of RawData 
            int vLindex, vRindex, dLindex, dRindex, gpXLindex,
                    gpXRindex, gpYLindex, gpYRindex; // declarIng varIables of gettIng Index Of columns
            Double DLeft, DRight, gpXleft, gpYleft; //declarIng varIables of keepIng the double values
            int DLlength, DRlenght; 
            boolean breakflag; // ThIs flag Is used to check wheater data Is valId or not, If Its true It means Data Is InvaLId and data wIll not be Inserted  In Hbase, else Its Inserted
            table = new HTable(conf, "RawData"); // assIgIng tablename and confIguratIon 
            for (long a = 0; a <= NosRow - 1; a++) { //runnIng loop untIl nosRow
                breakflag = false; // make It false In every cIrcle
                Get get = new Get(Bytes.toBytes(UserId + ":" + filename + ":" + a)); // assIgIng rowkey to fIltr frm the table
                Result result = table.get(get);// gettIng result frm the database and assIgIng that to the result varIable
                arrColumn.clear(); // clearIng the arrcolumn
                arrValue.clear(); // clearIng the arrvalue
                for (KeyValue kv : result.raw()) { // runnIng loop as per the value we fInd & storIng It to the varIable
                    arrColumn.add(new String(kv.getQualifier())); // addIng column Into arrColumn arrayLIst
                    arrValue.add(new String(kv.getValue()));// addIng value Into arrValue arrayLIst
                }
                vLindex = arrColumn.indexOf("ValidityLeft"); // getting Index of the ValidityLeft column from arrColumn arrayLIst
                vRindex = arrColumn.indexOf("ValidityRight");// getting Index of the ValidityRight column from arrColumn arrayLIst
                dLindex = arrColumn.indexOf("DistanceLeft");// getting Index of the DistanceLeft column from arrColumn arrayLIst
                dRindex = arrColumn.indexOf("DistanceRight");// getting Index of the DistanceRight column from arrColumn arrayLIst
                gpXLindex = arrColumn.indexOf("GazePointXLeft");// getting Index of the GazePointXLeft column from arrColumn arrayLIst
                gpXRindex = arrColumn.indexOf("GazePointXRight");// getting Index of the GazePointXRight column from arrColumn arrayLIst
                gpYLindex = arrColumn.indexOf("GazePointYLeft");// getting Index of the GazePointYLeft column from arrColumn arrayLIst
                gpYRindex = arrColumn.indexOf("GazePointYRight"); // getting Index of the GazePointYRight column from arrColumn arrayLIst

                if (arrValue.get(vLindex).equals("4") && arrValue.get(vRindex).equals("4")) // if ValidityLeft & ValidityRight=4 then data Is totally InvalId, It means we have to skip thIs record
                {
                    breakflag = true; // settIng thIs flag to true, so that ThIs record Is not Inserted In Hbase
                } else if (Double.parseDouble(arrValue.get(gpXLindex).equals("") ? "0" : arrValue.get(gpXLindex)) < 1) // For Validity if XLeft is less than 1
                {
                    if (gxleft != null && !gxleft.equals("")) {
                        breakflag = true; // settIng thIs flag to true, so that ThIs record Is not Inserted In Hbase
                    }
                } else if (Double.parseDouble(arrValue.get(gpXRindex).equals("") ? "0" : arrValue.get(gpXRindex)) < 1) // // For Validity if XRIGHT is less than 1
                {
                    if (gxright != null && !gxright.equals("")) {
                        breakflag = true; // settIng thIs flag to true, so that ThIs record Is not Inserted In Hbase
                    }
                } else if (Double.parseDouble(arrValue.get(gpYLindex).equals("") ? "0" : arrValue.get(gpYLindex)) < 1) // For Validity if YLeft is less than 1
                {
                    if (gyleft != null && !gyleft.equals("")) {
                        breakflag = true; // settIng thIs flag to true, so that ThIs record Is not Inserted In Hbase
                    }

                } else if (Double.parseDouble(arrValue.get(gpYRindex).equals("") ? "0" : arrValue.get(gpYRindex)) < 1) // For Validity if YRGHT is less than 1
                {
                    if (gyright != null && !gyright.equals("")) {
                        breakflag = true; // settIng thIs flag to true, so that ThIs record Is not Inserted In Hbase
                    }
                } else if ((arrValue.get(vLindex).equals("0") || arrValue.get(vLindex).equals("3")) && arrValue.get(vRindex).equals("4")) // For Validity L=0 or 3 & then Update R, wIth the values OF Left
                {
                    arrValue.set(gpXRindex, arrValue.get(gpXLindex)); //updating gazePoInt XRIght with Xleft
                    arrValue.set(gpYRindex, arrValue.get(gpYLindex));//updating  gazePoInt YRIght with Yleft
                    arrValue.set(dRindex, arrValue.get(dLindex));//updating  dIstance RIght with dIstance left

                } else if (arrValue.get(vLindex).equals("4") && (arrValue.get(vRindex).equals("0") || arrValue.get(vRindex).equals("3"))) // For Validity R=0 or 3 & then Update L, wIth the values OF RIGHT
                {
                    arrValue.set(gpXLindex, arrValue.get(gpXRindex)); //updating gazePoInt Xleft with XRIght
                    arrValue.set(gpYLindex, arrValue.get(gpYRindex));//updating gazePoInt yleft with yRIght
                    arrValue.set(dLindex, arrValue.get(dRindex));//updating DIStance left with DIStance RIght

                } else if ((arrValue.get(vLindex).equals("") && arrValue.get(vRindex).equals(""))) // If row is blank or contains garbage
                {
                    breakflag = true;// settIng thIs flag to true, so that ThIs record Is not Inserted In Hbase
                }
                if (!breakflag) { // IF ITs false 
                    DLeft = new Double(arrValue.get(dLindex)); //converting left Distance into cm
                    DLlength = (int) (Math.log10(DLeft.intValue()) + 1); // gettIng length of the Dleft
                    if (DLlength != 2) { // If Its Not equal to 2 then 
                        DLeft = DLeft / 10; //dIvIde DLeft by 10
                    }
                    DRight = new Double(arrValue.get(dRindex)); //converting RIght Distance into cm
                    DRlenght = (int) (Math.log10(DRight.intValue()) + 1); // gettIng length of the DRIGHT
                    if (DRlenght != 2) {// If Its Not equal to 2 then 
                        DRight = DRight / 10;//dIvIde DRight by 10
                    }

                    arrValue.set(dLindex, String.valueOf(Math.round(DLeft))); //roundIng Left dIstance 
                    arrValue.set(dRindex, String.valueOf(Math.round(DRight)));  //roundIng RIght dIstance 
                    //arrAvgColumn.add("Timestamp");
                    // arrAvgValue.add(arrValue.get(arrColumn.indexOf("Timestamp")));

                    arrColumn.add("AvgDist");
                    if ((dright != null && !dright.equals("")) && (dleft != null && !dleft.equals(""))) { // checkIng If users select Both L & R dIstance
                        DLeft = (DLeft + DRight) / 2; // nw takIng avg Dleft & DrIght & addIng Into the lIst
                        arrValue.add(String.valueOf(Math.round(DLeft)));
                    } else if (dright != null && !dright.equals("")) { //when user select DIstance RIght
                        arrValue.add(String.valueOf(Math.round(DRight)));
                    } else if (dleft != null && !dleft.equals("")) { //when user select DIstance left
                        arrValue.add(String.valueOf(Math.round(DLeft)));
                    }

                    arrColumn.add("AvgGxleft");
                    if ((gxleft != null && !gxleft.equals("")) && (gxright != null && !gxright.equals(""))) { // checkIng If users select Both gazePoIntXLeft & gazePoIntXRIGHT dIstance
                        gpXleft = (Double.valueOf(arrValue.get(gpXLindex)) + Double.valueOf(arrValue.get(gpXRindex))) / 2; // nw takIng avg gazePoIntXLeft & gazePoIntXRIGHT & addIng Into the lIst
                        arrValue.add(String.valueOf(Math.round(gpXleft)));
                    } else if (gxleft != null && !gxleft.equals("")) { //when user select gazePoIntXRIGHT
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpXLindex)))));
                    } else if (gxright != null && !gxright.equals("")) { //when user select gazePoIntXLeft
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpXRindex)))));
                    }

                    arrColumn.add("AvgGyleft");
                    if ((gyleft != null && !gyleft.equals("")) && (gyright != null && !gyright.equals(""))) { // checkIng If users select Both gazePoIntYLeft & gazePoIntYRIGHT dIstance
                        gpYleft = (Double.valueOf(arrValue.get(gpYLindex)) + Double.valueOf(arrValue.get(gpYRindex))) / 2; // nw takIng avg gazePoIntYLeft & gazePoIntYRIGHT & addIng Into the lIst
                        arrValue.add(String.valueOf(Math.round(gpYleft)));
                    } else if (gyleft != null && !gyleft.equals("")) { //when user select gazePoIntYRIGHT
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpYLindex)))));
                    } else if (gyright != null && !gyright.equals("")) { //when user select gazePoIntYLeft
                        arrValue.add(String.valueOf(Math.round(Double.valueOf(arrValue.get(gpYRindex)))));
                    }

                    addData_inHbase("ValidData", filename, "VD", arrColumn, arrValue); //now addIng valIdated Data Into Hbase 

                }
            }
            table.close();
            dc.InsertMapRecord("ValidData", filename, "MD", UserId, String.valueOf(counter)); // here I am storig nos of rows Into table ValIDData under MD(Map Data) column FamIly
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();

        } catch (IOException e1) {
        }

    }

    // ThIs FuncatIon Is Used For addIng record In Hbase
    public void addData_inHbase(String tablename, String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, tablename);// assIgIng tablename and confIguratIon 
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes(UserId + ":" + rowKey + ":" + counter));// makIng Instance of the Put WIth the rowkey, WIth the followIng rowKey //userId + Filename+rownumber
            for (int a = 0; a <= arrColumn.size() - 1; a++) { // runnIng loop UntIll arrCOlumn sIze
                put.add(Bytes.toBytes(CQ), Bytes.toBytes(arrColumn.get(a)), Bytes.toBytes(arrValue.get(a))); // addIng columnfamIly, column and value Into the Put
            }
            table.put(put); // InsertIng record Into the table now wIth the Put
            counter++; // IncrementIng counter In each Call of thIs funcatIOn
            table.flushCommits();// flushIng the commIt
            table.close(); // fInally closIng the table 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ThIs FuncatIon Is Used For addIng record In saccade Hbase
    public void addScade_inHbase(String tablename, String rowKey, String CQ, ArrayList<String> arrColumn, ArrayList<String> arrValue) {

        try {
            HTable table = new HTable(conf, tablename); // assIgIng tablename and confIguratIon 
            table.setAutoFlush(false);
            table.setWriteBufferSize(1024 * 1024 * 12);
            Put put = new Put(Bytes.toBytes(UserId + ":" + rowKey + ":" + counterSaccade)); // makIng Instance of the Put WIth the rowkey, WIth the followIng rowKey //userId + Filename+rownumber
            for (int a = 0; a <= arrColumn.size() - 1; a++) { // runnIng loop UntIll arrCOlumn sIze
                put.add(Bytes.toBytes(CQ), Bytes.toBytes(arrColumn.get(a)), Bytes.toBytes(arrValue.get(a))); // addIng columnfamIly, column and value Into the Put
            }
            table.put(put); // InsertIng record Into the table now wIth the Put
            counterSaccade++; // IncrementIng counterSaccade In each Call of thIs funcatIOn
            table.flushCommits(); // flushIng the commIt
            table.close(); // fInally closIng the table 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //out.println("System ");

        HttpSession session = request.getSession(false); // creatIng Instance of sessIon
        Integer ID = Integer.parseInt(session.getAttribute("userId").toString()); //gettIng UserId by SessIOn
        UserId = String.valueOf(ID); // conertIng It to StrIng

        String Efilename = (String) session.getAttribute("hdnfilename"); //settIng FIleName sessIons Into Efilename
        //String Lfilename = (String) session.getAttribute("hdnlblfilename");// Uncomment It If u want to work on Label,settIng sessIons Into StrIng
        String gxleft = (String) session.getAttribute("hdnxleft"); //settIng hdnxleft sessIons Into gxleft
        String gxright = (String) session.getAttribute("hdnxright"); //settIng hdnxright sessIons Into gxright
        String gyleft = (String) session.getAttribute("hdnyleft"); //settIng hdnyleft sessIons Into gyleft
        String gyright = (String) session.getAttribute("hdnyright"); //settIng hdnyright sssIons Into gyright
        String dleft = (String) session.getAttribute("hdndleft"); //settIng hdndleft sessIons Into dleft
        String dright = (String) session.getAttribute("hdndright"); //settIng hdndright sessIons Into dright
        SCREEN_WIDTH = Integer.parseInt(session.getAttribute("xscreen").toString());//settIng xscreen sessIons Into SCREEN_WIDTH
        SCREEN_HEIGHT = Integer.parseInt(session.getAttribute("yscreen").toString());//settIng yscreen sessIons Into SCREEN_HEIGHT
        RESOLUTION_WIDTH = Integer.parseInt(session.getAttribute("xresol").toString());//settIng xresol sessIons Into RESOLUTION_WIDTH
        RESOLUTION_HEIGHT = Integer.parseInt(session.getAttribute("yresol").toString());//settIng yresol sessIons Into RESOLUTION_HEIGHT
        FIXATION_DURATION_THRESHOLD = Integer.parseInt(session.getAttribute("fxdr").toString());//settIng fxdr sessIons Into FIXATION_DURATION_THRESHOLD
        VELOCITY_THRESHOLD = Integer.parseInt(session.getAttribute("velth").toString());//settIng velth sessIons Into VELOCITY_THRESHOLD
        Missing_Time_THRESHOLD = Integer.parseInt(session.getAttribute("mstm").toString());//settIng mstm sessIons Into Missing_Time_THRESHOLD
        sample_rate = Integer.parseInt(session.getAttribute("rate").toString());//settIng rate sessIons Into sample_rate
        timeInterval = Integer.parseInt(session.getAttribute("timeInterval").toString());//settIng timeInterval sessIons Into timeInterval

        String hdnData = request.getParameter("hdnData"); // gettIng hdnData parameter value
        arrColumn.clear(); //clearng the arrColumn arrayLIst
        arrValue.clear();//clearng the arrValue arrayLIst
        arrTime.clear();//clearng the arrTime arrayLIst
        String btnNext = request.getParameter("btnNext"); // gettIng btnNext parameter value
        String btnRun = request.getParameter("btnRun");// gettIng btnRun parameter value
        String btnEyeF = request.getParameter("btnEyeF");// gettIng btnEyeF parameter value

        if ("Next".equalsIgnoreCase(btnNext)) { // when next button Is clIcked  
            loopStarter = looprunner; // set loopstarter to looprunner
            looprunner = looprunner + 1000; // Increment loopRunner wIth 1000
            dc.get_DataHbase(loopStarter, looprunner, UserId, "ValidData", Efilename, arrColumn, arrValue, arrTime); //THIS functIon wIll get 1000 records In each Request from ValID Data table as per gven FIlename and user ID
        } else if ("Run Fixation".equalsIgnoreCase(btnRun)) { // when button of Run FixatIon Is clIcked 
            FixAlgorithm(Efilename); // run FIXaTION ANd saccade alogrIthm
            DataClass dc = new DataClass(getServletContext().getRealPath("/")); // get the path of thIS servlet
            dc.InsertMapRecord("FixData", Efilename, "MD", UserId, String.valueOf(counter));// inserting total Nos of Rows of fixation
            dc.InsertMapRecord("FixData", Efilename + "-S-", "MD", UserId, String.valueOf(counterSaccade)); // inserting total Nos of Rows of Sccade
            dc.InsertMapRecord("EyeFeature", Efilename, "MD", UserId, String.valueOf(counterEF));// inserting total Nos of Rows of EyeFeature
            arrColumn.clear(); //clearng the arrColumn arrayLIst
            arrValue.clear(); //clearng the arrValue arrayLIst
            arrColumn_lbl.clear(); //clearng the arrColumn_lbl arrayLIst
            arrValue_lbl.clear(); //clearng the arrValue_lbl arrayLIst
            dc.get_DataHbase_WriteTextFile(0, 0, "fix", UserId, "FixData", Efilename, "MD", arrColumn, arrValue);//THIS functIon wIll get all the records of FIxatIon from FIxData table and also WrIte them Into text FIle to download as per gven FIlename and UserID
            dc.get_DataHbase_WriteTextFile(0, 0, "sac", UserId, "FixData", Efilename + "-S-", "MD", arrColumn_lbl, arrValue_lbl);//THIS functIon wIll get all the records of saccade from FIxData table and also WrIte them Into text FIle to download as per gven FIlename and UserID
            request.setAttribute("arrColumn", arrColumn); // settIng result FIXatIOn of column Into arrColumn  for forwarding data to next page
            request.setAttribute("arrValue", arrValue);// settIng result FIXatIOn of value Into arrValue  for forwarding data to next page
            request.setAttribute("arrColumn_lbl", arrColumn_lbl);// settIng result saccade of column Into arrColumn_lbl  for forwarding data to next page
            request.setAttribute("arrValue_lbl", arrValue_lbl);// settIng result saccade of value Into arrValue_lbl  for forwarding data to next page
            RequestDispatcher rd = request.getRequestDispatcher("/ShowFixData.jsp");
            rd.forward(request, response);
        } else if ("EyeFeature".equalsIgnoreCase(btnEyeF)) { // If eyeFeature ButtOn Is clIked
            arrColumn.clear();//clearng the arrColumn arrayLIst
            arrValue.clear();//clearng the arrValue arrayLIst
            dc.get_DataHbase_common(0, 0, "", UserId, "EyeFeature", Efilename, "MD", arrColumn, arrValue); //THIS functIon wIll get all records from eyeFeature table as per gven FIlename and user ID
            request.setAttribute("Alrd_column", arrColumn); // setting array List for forwarding data to next page
            request.setAttribute("Alrd_value", arrValue);// setting array List for forwarding data to next page
            RequestDispatcher rd = request.getRequestDispatcher("/ShowEyeFeature.jsp"); // redirecting to the next page
            rd.forward(request, response);
        } else if ("DF".equalsIgnoreCase(hdnData)) { // If request of fIxatIon FIle Is requested
            DownloadFile(response, hdnData); // download FixatIon FILe
        } else if ("DS".equalsIgnoreCase(hdnData)) { // If request of saccade FIle Is requested
            DownloadFile(response, hdnData); // download saccade FILe
        } else {
            Read_RawData_forValidation(Efilename, gxleft, gxright, gyleft, gyright, dleft, dright); //ReadIng Raw Data for valIdatIon 
           // Read_LabelData_forValdiation(Efilename, Lfilename); // uncomment It for ReadIng label Data from RawData table for valIdatIng It 
            arrColumn.clear(); //clearng the arrColumn arrayLIst
            arrValue.clear();//clearng the arrValue arrayLIst
            arrTime.clear();//clearng the arrTime arrayLIst
            arrColumn_lbl.clear(); //clearng the arrColumn_lbl arrayLIst
            arrValue_lbl.clear(); //clearng the arrValue_lbl arrayLIst
            dc.get_DataHbase(0, 1000, UserId, "ValidData", Efilename, arrColumn, arrValue, arrTime); // readIng Eye tracker valId data to show on page
           // dc.get_DataHbase_common(0, 0, "", UserId, "ValidData", Lfilename, "MD", arrColumn_lbl, arrValue_lbl);// uncommentIng for readIng label valId data to show on page
        }
        request.setAttribute("arrColumn", arrColumn); // setting array List for forwarding data to next page
        request.setAttribute("arrValue", arrValue); // setting array List for forwarding data to next page
        request.setAttribute("arrTime", arrTime); // setting array List for forwarding data to next page
        request.setAttribute("arrColumn_lbl", arrColumn_lbl); // setting array List for forwarding data to next page
        request.setAttribute("arrValue_lbl", arrValue_lbl); // setting array List for forwarding data to next page
        RequestDispatcher rd = request.getRequestDispatcher("/ShowValidData.jsp");
        rd.forward(request, response);
    }

    private void DownloadFile(HttpServletResponse response, String filetype) throws FileNotFoundException, IOException {
        response.setContentType("text/plain");
         int len=getServletContext().getRealPath("/").length();
        String filepath = getServletContext().getRealPath("/").substring(0, len - 11);
        
        File file;
        if (filetype.equals("DF")) { // IF FILE type Is FIxaTIOn
            response.setHeader("Content-Disposition",
                    "attachment;filename=Fixation.txt"); // make FIxaTIOn FILE
            file = new File(filepath + "/fix.txt"); // gettIng path to download fIxatIon FIle
        } else { // IF FILE type Is saccade
            response.setHeader("Content-Disposition",
                    "attachment;filename=Saccade.txt");  // make saccade FILE
            file = new File(filepath + "/sac.txt"); // gettIng path to download saccade FIle
        }

        FileInputStream fileIn = new FileInputStream(file); // assIGN FILE varIable to FileInputStream
        OutputStream out = response.getOutputStream(); // get OutPut response 

        byte[] outputByte = new byte[4096]; //set bytes
        int byteRead;
        while ((byteRead = fileIn.read(outputByte, 0, 4096)) != -1) { // run loop UntIL bytes ExISts
            out.write(outputByte, 0, byteRead); // WrIte the Bytes
        }
        fileIn.close(); // Close the FileInputStream varaIable
        out.flush(); // flush the FILE to download
        out.close(); // close the OutputStream object
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
