/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class MP3Work extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
                System.out.println("entro");
       String info = request.getParameter("descargar");
       String inforArray[] = info.split(",");
       String fileName = inforArray[0];
      if (fileName == null || fileName.equals(""))
           throw new ServletException(
            "Invalid or non-existent file parameter in SendMp3 servlet.");
      
      if (fileName.indexOf(".mp3") == -1)
          fileName = fileName + ".mp3";
          
     String mp3Dir = inforArray[1];
        System.out.println(mp3Dir);
     if (mp3Dir == null || mp3Dir.equals(""))
           throw new ServletException(
             "Invalid or non-existent mp3Dir context-param.");
          
      ServletOutputStream stream = null;
      BufferedInputStream buf = null;
      try{
     
      stream = response.getOutputStream();
      File mp3 = new File(mp3Dir + "/" + fileName);
     
      response.setContentType("audio/mpeg");
      
      response.addHeader("Content-Disposition","attachment; filename="+fileName );

      response.setContentLength( (int) mp3.length() );
      
      FileInputStream input = new FileInputStream(mp3);
      buf = new BufferedInputStream(input);
      int readBytes = 0;

      while((readBytes = buf.read()) != -1)
         stream.write(readBytes);

     } catch (IOException ioe){
     
        throw new ServletException(ioe.getMessage());
         
     } finally {
     if(stream != null)
         stream.close();
      if(buf != null)
          buf.close();
          }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
    } //end doGet

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
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
