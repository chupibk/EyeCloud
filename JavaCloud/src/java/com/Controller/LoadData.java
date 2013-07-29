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
import javax.print.DocFlavor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.commons.collections.MultiHashMap;

/**
 *
 * @author samsalman
 */
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    private static final long serialVersionUID = 1L;
    static String hdnlblfilename = "", hdnfilename = "", hdntimestamp = null, hdnxleft = null, hdnxright = null, hdnyleft = null, hdnyright = null,
            hdndleft = null, hdndright = null, hdnvleft = null, hdnvright = null, hdnstname = null, hdnpart = null,
            hdnstartpnt = null, hdnduration = null, hdnlblstlname = null;
    int partcountrow = 0, partcountcell = 0;
    LinkedHashMap<String, String> arrls = new LinkedHashMap<String, String>();
    LinkedHashMap<String, String> partarrls = new LinkedHashMap<String, String>();
    MultiHashMap partarrlsvalue = new MultiHashMap();
    LinkedHashMap<String, String> lblarrls = new LinkedHashMap<String, String>();
    ArrayList<String> strholdexcel = new ArrayList<String>();
    // StringBuffer largeStr= new StringBuffer();
    String largeStr;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List<FileItem> fields = upload.parseRequest(request);
            for (FileItem fielditem : fields) {
                if (fielditem.isFormField()) {
                    if ("btnload".equals(fielditem.getFieldName())) {
                        if (arrls.size() != 0) {
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
                        if (partarrls.size() != 0) {
                            partarrls.clear();
                        }
                        if (partarrlsvalue.size() != 0) {
                            partarrlsvalue.clear();
                        }
                        if (lblarrls.size() != 0) {
                            lblarrls.clear();
                        }

                        Iterator<FileItem> it = fields.iterator();
                        while (it.hasNext()) {
                            FileItem fileItem = it.next();
                            boolean isFormField = fileItem.isFormField();
                            if (!isFormField) {

//                                  System.out.println(System.getProperty("user.dir"));
//                                  System.out.println("<td>file form field</td><td>FIELD NAME: " + fileItem.getFieldName() +
//							"<br/>STRING: " + fileItem.getString() +
//							"<br/>NAME: " + fileItem.getName() +
//							"<br/>CONTENT TYPE: " + fileItem.getContentType() +
//							"<br/>SIZE (BYTES): " + fileItem.getSize() +
//							"<br/>TO STRING: " + fileItem.toString()+
//                                          "<br/>TO STRING: " + fileItem.get()
//							);
//					System.out.println("</td>");
                                int holdloop = 0;
                                BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream()));
                                String str, strLine;
                                LineNumberReader ln = new LineNumberReader(br);
                                while ((strLine = br.readLine()) != null) {
                                    //  while (ln.getLineNumber() == 0) {
                                    if (holdloop == 0) {
                                        //  str = ln.readLine();
                                        if (strLine != null) {
                                            String[] strArr = strLine.split("	");
                                            for (int a = 0; a <= strArr.length - 1; a++) {
                                                // out.println(strArr[a] + "<br/>");
                                                // arrls.put("Select", "Select");
                                                arrls.put(strArr[a], strArr[a]);
                                                request.setAttribute("fileload", "0");
                                            }
                                        } else {
                                            request.setAttribute("fileload", "1");
                                            break;
                                        }
                                    } else {
                                        //rs.updates
                                        //  largeStr.append(fileItem.getString());
                                        largeStr = fileItem.getString();
                                        System.out.println(largeStr);
                                        break;
                                    }
                                    holdloop++;
                                }
                                br.close();
                            }

                        }

                    } else if ("btnpart".equals(fielditem.getFieldName())) {
                        Iterator<FileItem> it = fields.iterator();
                        partcountrow = 0;
                        while (it.hasNext()) {
                            FileItem fileItem = it.next();
                            System.out.println(fileItem.getName());
                            boolean isFormField = fileItem.isFormField();
                            if (!isFormField) {

                                //     FileInputStream file = new FileInputStream(new File(fields.));
                                //FileInputStream file = new FileInputStream(new File("C:\\test.xls"));

                                //Get the workbook instance for XLS file 
                                if (fileItem.getName().endsWith("xlsx")) {
                                    XSSFWorkbook workbook = new XSSFWorkbook(fileItem.getInputStream());
                                    //Get first sheet from the workbook
                                    XSSFSheet sheet = workbook.getSheetAt(0);
                                    //Iterate through each rows from first sheet
                                    Iterator<Row> rowIterator = sheet.iterator();
                                    partarrls.put("Select", "Select");
                                    while (rowIterator.hasNext()) {
                                        Row row = rowIterator.next();
                                        //For each row, iterate through each columns
                                        Iterator<Cell> cellIterator = row.cellIterator();
                                        while (cellIterator.hasNext()) {
                                            Cell cell = cellIterator.next();
                                            if (partcountrow == 0) {
                                                partarrls.put(cell.getStringCellValue(), cell.getStringCellValue());
                                                strholdexcel.add(cell.getStringCellValue());
                                            } else {

                                                switch (cell.getCellType()) {
//                                                case Cell.CELL_TYPE_BOOLEAN:
//                                                    System.out.print(cell.getBooleanCellValue() + "\t\t");
//                                                    break;

                                                    case Cell.CELL_TYPE_NUMERIC:
                                                        partarrlsvalue.put(strholdexcel.get(partcountcell), String.valueOf(cell.getNumericCellValue()));
                                                        break;
                                                    case Cell.CELL_TYPE_STRING:
                                                        partarrlsvalue.put(strholdexcel.get(partcountcell), cell.getStringCellValue());
                                                        break;

                                                }
                                                partcountcell++;
                                            }

                                        }
                                        partcountcell = 0;
                                        partcountrow++;

                                    }
                                } else {
                                    HSSFWorkbook workbook = new HSSFWorkbook(fileItem.getInputStream());
                                    //Get first System.out.println("xlsx");sheet from the workbook
                                    HSSFSheet sheet = workbook.getSheetAt(0);
                                    //Iterate through each rows from first sheet
                                    Iterator<Row> rowIterator = sheet.iterator();
                                    partarrls.put("Select", "Select");
                                    while (rowIterator.hasNext()) {
                                        Row row = rowIterator.next();
                                        //For each row, iterate through each columns
                                        Iterator<Cell> cellIterator = row.cellIterator();
                                        while (cellIterator.hasNext()) {
                                            Cell cell = cellIterator.next();
                                            if (partcountrow == 0) {
                                                partarrls.put(cell.getStringCellValue(), cell.getStringCellValue());
                                                strholdexcel.add(cell.getStringCellValue());
                                                //    hold  =(String) strholdexcel.get(0);
                                            } else {

                                                switch (cell.getCellType()) {
//                                                case Cell.CELL_TYPE_BOOLEAN:
//                                                    System.out.print(cell.getBooleanCellValue() + "\t\t");
//                                                    break;

                                                    case Cell.CELL_TYPE_NUMERIC:
                                                        partarrlsvalue.put(strholdexcel.get(partcountcell), String.valueOf(cell.getNumericCellValue()));
                                                        break;
                                                    case Cell.CELL_TYPE_STRING:
                                                        partarrlsvalue.put(strholdexcel.get(partcountcell), cell.getStringCellValue());
                                                        break;

                                                }
                                                partcountcell++;
                                            }

                                        }
                                        partcountcell = 0;
                                        partcountrow++;
                                        //System.out.println("xls");
                                        //  break;
                                    }
                                }
                            }
                        }


                    } else if ("btnlblfiles".equals(fielditem.getFieldName())) {
                        Iterator<FileItem> it = fields.iterator();
                        lblarrls.put("Select", "Select");
                        while (it.hasNext()) {

                            FileItem fileItem = it.next();
                            System.out.println(fileItem.getName());
                            boolean isFormField = fileItem.isFormField();
                            if (!isFormField) {

                                BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream()));
                                String str;
                                // System.out.println(br.);
                                LineNumberReader ln = new LineNumberReader(br);
                                // while((str=br.readLine())!=null){
                                //arrls.clear();

                                while (ln.getLineNumber() == 0) {
                                    str = ln.readLine();
                                    if (str != null) {

                                        String[] strArr = str.split("	");
                                        for (int a = 0; a <= strArr.length - 1; a++) {
                                            // out.println(strArr[a] + "<br/>");
                                            // arrls.put("Select", "Select");
                                            lblarrls.put(strArr[a], strArr[a]);
                                            request.setAttribute("fileload", "0");
                                        }
                                    } else {
                                        request.setAttribute("fileload", "1");
                                        break;
                                    }
                                }
                                br.close();
                            }

                        }
                        break;

                    } else if ("btnsave".equals(fielditem.getFieldName())) {
                        //System.out.println(fielditem.getString());
                        String[] str = largeStr.split(" ");
                        for (int a = 0; a <= str.length - 1; a++) {
                            // out.println(strArr[a] + "<br/>");
                            // arrls.put("Select", "Select");
                            System.out.println(str[a]);
                            
                        }

                        break;
                    }
                    if ("hdntimestamp".equals(fielditem.getFieldName())) {
                        hdntimestamp = fielditem.getString();
                    } else if ("hdnxleft".equals(fielditem.getFieldName())) {
                        hdnxleft = fielditem.getString();
                    } else if ("hdnxright".equals(fielditem.getFieldName())) {
                        hdnxright = fielditem.getString();
                    } else if ("hdnyleft".equals(fielditem.getFieldName())) {
                        hdnyleft = fielditem.getString();
                    } else if ("hdnyright".equals(fielditem.getFieldName())) {
                        hdnyright = fielditem.getString();
                    } else if ("hdndleft".equals(fielditem.getFieldName())) {
                        hdndleft = fielditem.getString();
                    } else if ("hdndright".equals(fielditem.getFieldName())) {
                        hdndright = fielditem.getString();
                    } else if ("hdnvleft".equals(fielditem.getFieldName())) {
                        hdnvleft = fielditem.getString();
                    } else if ("hdnvright".equals(fielditem.getFieldName())) {
                        hdnvright = fielditem.getString();
                    } else if ("hdnstname".equals(fielditem.getFieldName())) {
                        hdnstname = fielditem.getString();
                    } else if ("hdnpart".equals(fielditem.getFieldName())) {
                        hdnpart = fielditem.getString();
                    } else if ("hdnfilename".equals(fielditem.getFieldName())) {
                        hdnfilename = fielditem.getString();
                    } else if ("hdnlblfilename".equals(fielditem.getFieldName())) {
                        hdnlblfilename = fielditem.getString();
                    } else if ("hdnstartpnt".equals(fielditem.getFieldName())) {
                        hdnstartpnt = fielditem.getString();
                    } else if ("hdnduration".equals(fielditem.getFieldName())) {
                        hdnduration = fielditem.getString();
                    } else if ("hdnlblstlname".equals(fielditem.getFieldName())) {
                        hdnlblstlname = fielditem.getString();
                    }

                }
            }

            List lstpart = null;
            if (hdnpart != null && !hdnpart.isEmpty()) {
                request.setAttribute("selectedpart", hdnpart);
                lstpart = (List) partarrlsvalue.get(hdnpart);
            } else {
                request.setAttribute("selectedpart", "ID");
                lstpart = (List) partarrlsvalue.get("ID");
            }
            if (hdnfilename.contains("\\")) {
                String[] holdpath = hdnfilename.split("[\\\\]");
                hdnfilename = holdpath[2];
            }
            if (hdnlblfilename.contains("\\")) {
                String[] holdpath = hdnlblfilename.split("[\\\\]");
                hdnlblfilename = holdpath[2];
            }

            request.setAttribute("lstpart", lstpart);
            request.setAttribute("arrls", arrls);
            request.setAttribute("lblarrls", lblarrls);
            request.setAttribute("partarrls", partarrls);
            request.setAttribute("hdnfilename", hdnfilename);
            request.setAttribute("hdnlblfilename", hdnlblfilename);
            request.setAttribute("hdntimestamp", hdntimestamp);
            request.setAttribute("hdnxleft", hdnxleft);
            request.setAttribute("hdnxright", hdnxright);
            request.setAttribute("hdnyleft", hdnyleft);
            request.setAttribute("hdnyright", hdnyright);
            request.setAttribute("hdndleft", hdndleft);
            request.setAttribute("hdndright", hdndright);
            request.setAttribute("hdnvleft", hdnvleft);
            request.setAttribute("hdnvright", hdnvright);
            request.setAttribute("hdnstname", hdnstname);

            RequestDispatcher rd = request.getRequestDispatcher("/LoadData.jsp");
            rd.forward(request, response);
            //	out.println("</tr>");
            //out.println("</table>");
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

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