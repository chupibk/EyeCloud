/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import backtype.storm.generated.DRPCExecutionException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import backtype.storm.utils.DRPCClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift7.TException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author daothanhchung
 */
@WebServlet(urlPatterns = {"/DRPC"})
public class DRPC extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String callback = request.getParameter("callback") == null ? "" : request.getParameter("callback");
        String data = request.getParameter("data");
        Collection collection = new ArrayList();
        
        String HOSTNAME = "54.229.84.177";
        DRPCClient client = new DRPCClient(HOSTNAME, 3772);
        String result = "";
        try {
            result = client.execute("Intensity", data);
        } catch (TException ex) {
            Logger.getLogger(DRPC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DRPCExecutionException ex) {
            Logger.getLogger(DRPC.class.getName()).log(Level.SEVERE, null, ex);
        }

        client.close();

        collection.add(result);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(callback + "(");
        response.getWriter().write(new Gson().toJson(collection));
        response.getWriter().write(");");

        out.close();
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
