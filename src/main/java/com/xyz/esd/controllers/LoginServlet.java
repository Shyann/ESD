/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xyz.esd.controllers;

import com.xyz.esd.beans.User;
import com.xyz.esd.models.LoginModel;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user_pc
 */
public class LoginServlet extends HttpServlet {

    public static final String LOGIN_MESSAGE = "loginMessage";
    private static final long serialVersionUID = 1L;

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
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

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

        HttpSession session = request.getSession(false);
        if(session == null){
            session = request.getSession();
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User tUser = LoginModel.authenticate(username, password);

        if (tUser != null) {
            session.setAttribute("user", tUser);
            request.setAttribute(LOGIN_MESSAGE, "Login Succeeded! Hello, " + tUser.getUsername());
            
            // Choose the correct dashboard
            if (tUser.getStatus() == User.State.ADMIN) {
            	request.getRequestDispatcher("/adminDashboard").forward(request, response);
			}else{
				request.getRequestDispatcher("/memberDashboard").forward(request, response);
			}
            
        } else {
            String currentLoginMessage = (String) request.getAttribute(LOGIN_MESSAGE);
            if (currentLoginMessage == null || currentLoginMessage == "") {
                request.setAttribute(LOGIN_MESSAGE, "Login Failed!");
            }
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
