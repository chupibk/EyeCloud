/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author samsalman
 */
@WebServlet(name = "registeruser", urlPatterns = {"/registeruser"})
public class registeruser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    //declarIng all the publIc varIables
    DataClass dc = new DataClass();
    String txtfname, txtemail, txtpass, txtCpass, txtcountry, txtcity, txtadd,
            txtphone, txtmob, txtpostal, txtstate, btnlogin, btnregister,btnsubmit;
    boolean flagupdate = false;
    int result = 0;

     private static byte[] key = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };//"thisIsASecretKey";

     // ThIs Is the encrypt functIon for password
    public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        }
        catch (Exception e)
        {
            
        }
        return null;

    }
    // ThIs Is the Decrypt functIon for password
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        }
        catch (Exception e)
        {
            

        }
        return null;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        txtfname = request.getParameter("txtfname"); // gettIng txtfname value from the request parameter
        txtemail = request.getParameter("txtemail");// gettIng txtemail value from the request parameter
        txtpass = request.getParameter("txtpass");// gettIng txtfname value from the request parameter
        txtCpass = request.getParameter("txtCpass");// gettIng txtpass value from the request parameter
        txtcountry = request.getParameter("txtcountry");// gettIng txtcountry value from the request parameter
        txtcity = request.getParameter("txtcity");// gettIng txtcity value from the request parameter
        txtadd = request.getParameter("txtadd");// gettIng txtadd value from the request parameter
        txtphone = request.getParameter("txtphone");// gettIng txtphone value from the request parameter
        txtmob = request.getParameter("txtmob");// gettIng txtmob value from the request parameter
        txtpostal = request.getParameter("txtpostal");// gettIng txtpostal value from the request parameter
        txtstate = request.getParameter("txtstate");// gettIng txtstate value from the request parameter
        btnlogin = request.getParameter("btnlogin");// gettIng btnlogin value from the request parameter
        btnregister = request.getParameter("btnregister");// gettIng btnregister value from the request parameter
        btnsubmit = request.getParameter("btnsubmit");// gettIng btnsubmit value from the request parameter
        
        if ("kill".equalsIgnoreCase(request.getParameter("hdnData"))) { // checkIng the HdnData If Its KIll then kIllIng sessIon
            request.getSession().invalidate(); //kIllIng the sessIon 
            response.sendRedirect(request.getContextPath() + "/loginuser.jsp"); //forwardIng Into the logInUser.Jsp
        } else if ("edit".equalsIgnoreCase(request.getParameter("hdnData"))) { // users wants to see hIs profIle
            HttpSession session = request.getSession(false);
            session.setAttribute("hdnData", "edit");
            getUserDetails(request, response); //ReadINg user Data of user profIle
            flagupdate = true;
        }else if ("submit".equalsIgnoreCase(btnsubmit)) { //If recover button Is pressed & user forgets hIs password
            String password=dc.recoverPassword(txtemail); // recovIng user Password
            if (!password.equals("")) { // If password Isnot "" then decrypt p/assword and show to the user
                request.setAttribute("password",decrypt(password)); //decryptIng the password & set Into the attrIbute
                request.setAttribute("txtemail",txtemail);// set txtEmaIl Into the attrIbute
                RequestDispatcher rd = request.getRequestDispatcher("/password.jsp"); // forward to the next page
                rd.forward(request, response);
            } else { // emaIl ID Is not correct
                request.setAttribute("error", "1");
                RequestDispatcher rd = request.getRequestDispatcher("/password.jsp");
                rd.forward(request, response);
            }
        } else if (txtpass.equals(txtCpass)) { // if password is same
            if (!txtemail.isEmpty() && !txtpass.isEmpty() && !txtfname.isEmpty()) { // if required field Is not empty
                if (flagupdate) { // users wants to update his profle
                    UpdateUser(request, response);
                    flagupdate=false; //setting flag false 
                } else { // users wants to register
                    registerUser(request, response);
                }

            } else { // requIre fIeld Is empty
                request.setAttribute("error", "1");
                setAttribute(request); //settIng all the attrIbutes to pass to the next page
                RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                rd.forward(request, response);
            }
        } else if ("login".equalsIgnoreCase(btnlogin)) { //If login button Is pressed

            int loginUser=dc.loginUser(txtemail, encrypt(txtpass)); // encrypt password and send to check If user Is valId
            if (loginUser > 0) { // successfull logiN
                HttpSession session = request.getSession(false); // generate Instance of sessIon 
                session.setAttribute("username", dc.username); // addIng username Into the sessIon 
                session.setAttribute("userId", loginUser);  // addIng logInUser Into the sessIon 
                RequestDispatcher rd = request.getRequestDispatcher("/Dashboard.jsp");
                rd.forward(request, response);
            } else { // there Is some error In logIn
                request.setAttribute("error", "1");
                RequestDispatcher rd = request.getRequestDispatcher("/loginuser.jsp");
                rd.forward(request, response);
            }
        } else if ("register".equalsIgnoreCase(btnregister)) { //If regIster button Is pressed
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);

        } else { // if password doesnt match wIth confIrm password
            request.setAttribute("error", "2");
            setAttribute(request);
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        }
    }

    // regIsterIng user FunctIon
    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (dc.CheckEmaIl(txtemail) != 0) { //If emaIl already exIsts
            request.setAttribute("error", "3"); //settIng error Number
            setAttribute(request); // settIng all the attrIbutes
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        } else {
            result = dc.RegIsteruser_mysql(txtfname, txtemail,
                   encrypt(txtpass), txtcountry, txtstate,
                    txtcity, txtadd, txtmob,
                    txtphone, txtpostal); // ThIs methOd wIll regIster User Into the Mysql table

            if (result == 1) { //iF user iS regIster successfully
                RequestDispatcher rd = request.getRequestDispatcher("/loginuser.jsp");
                rd.forward(request, response);
            } else { //if user can not regIster
                setAttribute(request); // settIng all the attrIbutes
                request.setAttribute("error", "5"); //settIng error Number
                RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
                rd.forward(request, response);
            }
        }

    }
    // ThIs funcaton wIll Update the ProfIle of the user
    private void UpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // makIng Instance of SessIon
        String Id = String.valueOf(session.getAttribute("userId")); // gettIng UserId sessIon value and stOrg Into the VaraIale
        result = dc.UpdateUser_detail(txtfname, Id, encrypt(txtpass), txtcountry, txtstate, txtcity, txtadd, txtmob, txtphone, txtpostal);// updatIng UserPrfOle
        if (result == 1) { // If result Is 1
            session.setAttribute("username", txtfname); //settIng UserName AttrIbutes
            request.setAttribute("success", "1"); //settIng success AttrIbute
            request.setAttribute("error", "0"); //settIng error AttrIbute
            setAttribute(request); // settIng all the attrIbutes
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("error", "6");
            setAttribute(request); // settIng all the attrIbutes
            RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
            rd.forward(request, response);
        }
    }

    // ThIs funcatIon wIll set all the attrIbutes
    private void setAttribute(HttpServletRequest request) {
        request.setAttribute("name", txtfname); // settIng attrIbute for name
        request.setAttribute("email", txtemail); // settIng attrIbute for email
        request.setAttribute("country", txtcountry);// settIng attrIbute for country
        request.setAttribute("state", txtstate);// settIng attrIbute for state
        request.setAttribute("city", txtcity);// settIng attrIbute for city
        request.setAttribute("address", txtadd);// settIng attrIbute for address
        request.setAttribute("mobile", txtmob);// settIng attrIbute for mobile
        request.setAttribute("phone", txtphone);// settIng attrIbute for phone
        request.setAttribute("postal", txtpostal);// settIng attrIbute for postal
        if(flagupdate){
            request.setAttribute("btnlabel", "1");
            request.setAttribute("hdnData", "edit");
            
        }
    }
    //ThIs funcatIon wIll get All the User DetaIls
    private void getUserDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userid = Integer.parseInt(session.getAttribute("userId").toString()); // gettIng userID from the sessIon
        dc.getUserDetails(userid); // GettIng user DetaIls by the gIven ID
        
        request.setAttribute("name", dc.username); // settIng attrIbute for name
        request.setAttribute("email", dc.email);// settIng attrIbute for email
        request.setAttribute("country", dc.country);// settIng attrIbute for country
        request.setAttribute("state", dc.state);// settIng attrIbute for state
        request.setAttribute("city", dc.city);// settIng attrIbute for city
        request.setAttribute("address", dc.address);// settIng attrIbute for address
        request.setAttribute("mobile", dc.mobileNO);// settIng attrIbute for mobile
        request.setAttribute("phone", dc.phoneNo);// settIng attrIbute for phone
        request.setAttribute("postal", dc.postalcode);// settIng attrIbute for postal
        request.setAttribute("btnlabel", "1");// settIng attrIbute for btnlabel
        RequestDispatcher rd = request.getRequestDispatcher("/registeruser.jsp");
        rd.forward(request, response);

    }
    
    

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
