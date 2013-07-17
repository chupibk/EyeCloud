/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author daothanhchung
 */
import com.google.gson.Gson;
import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(name = Constants.UPLOAD_NAME, urlPatterns = {Constants.UPLOAD_LINK})
public class UploadServlet extends HttpServlet {

    private boolean isMultipart;
    private String filePath;
    private File file;

    @Override
    public void init() {
        // Get the file location where it would be stored.
        filePath = getServletContext().getRealPath("") + Constants.UPLOAD_PATH;
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {
        // Response
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        java.io.PrintWriter out = response.getWriter();
        Map<String, String> result = new HashMap<String, String>();

        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            result.put("OK", "0");
            result.put("message", "No file uploaded");
            response.getWriter().write(new Gson().toJson(result));
            out.close();
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(Constants.maxMemSize);
        // Location to save data that is larger than maxMemSize.
        String tmp = getServletContext().getRealPath("") + Constants.TEMP;
        factory.setRepository(new File(tmp));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(Constants.maxFileSize);

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            String image    = "";

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    if (fieldName.equals("image")){
                        image = fileName;
                    }

                    // Write the file
                    if (fileName.lastIndexOf("\\") >= 0) {
                        file = new File(filePath
                                + fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        file = new File(filePath
                                + fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fi.write(file);
                }
            }

            result.put("OK", "1");
            result.put("message", "Upload successfully " + image);
            response.getWriter().write(new Gson().toJson(result));
            out.close();
        } catch (Exception ex) {
            result.put("OK", "0");
            result.put("message", "Failed to upload! " + ex.toString());
            response.getWriter().write(new Gson().toJson(result));
            out.close();
        }
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        java.io.PrintWriter out = response.getWriter();
        Map<String, String> result = new HashMap<String, String>();
        result.put("OK", "0");
        result.put("message", "POST method is required");
        response.getWriter().write(new Gson().toJson(result));
        out.close();
    }
}