/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xyz.esd.controllers;

import com.xyz.esd.beans.Claim;
import com.xyz.esd.beans.Payment;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyz.esd.beans.User;

/**
 *
 * @author user_pc
 */
public class MemberDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final PaymentManager paymentManger = new PaymentManager();
    private static final ClaimManager claimManager = new ClaimManager();

    protected void showTables(HttpServletRequest request, HttpServletResponse response){
        System.out.println(paymentManger.getPayments(((User)request.getSession().getAttribute("user")).getUsername()).toString());
        request.setAttribute("payments", paymentManger.getPayments(((User)request.getSession().getAttribute("user")).getUsername()));
        request.setAttribute("claims", claimManager.getClaims(((User)request.getSession().getAttribute("user")).getUsername()));
    }

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

        this.showTables(request, response);
        request.getRequestDispatcher("/WEB-INF/memberDashboard.jsp").forward(request, response);

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

        /* user object stored in session, since this page if Filtered, there's no need to check for null object */
        User user = (User) request.getSession().getAttribute("user");

        /* view sends controller the act var containing the desired action */
        String act = request.getParameter("action");
        if(act != null) {
            System.out.println("Processing action : "+act);
            switch (act) {
                    case "createPayment":
                    float amount = Float.parseFloat(request.getParameter("paymentAmount"));
                    System.out.println("Amount paid: " + amount);
                        /* Check the user has been charged for something */
                    if ((amount <= user.getBalance()) && (user.getBalance() > 0) && (amount > 0)) {
                        System.out.println("Process payment");
                        Payment p = new Payment();
                        p.setType(request.getParameter("paymentType"));
                        p.setAmount(amount);
                        p.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
                        paymentManger.addPayment(user, p);
                    } else {
                        System.out.println("User doesn't owe anything / user payment is over balance / payment is negative");
                        request.setAttribute("error", "Invalid payment.");
                    }
                    break;

                case "createClaim":
                    /*
                    Used to caluclate how many millis in 6 months
                     */
                    long months = 6;
                    long seconds = months * 31556952L / 12;
                    long sixMonths = seconds * 1000;
                    
                    // if their DoR + 6 months is greater than current time, then they have not been a member for 6 months
                    if((user.getDor().getTime() + sixMonths) > System.currentTimeMillis()){
                        request.setAttribute("error", "You have not been a member for more that 6 months.");
                        System.out.println("User cannot make claim!");
                    }else if (user.getStatus() != User.State.APPROVED ){
                    	request.setAttribute("error", "Membership Stat is invalid. Current Status: " + user.getStatus());
                        System.out.println("Membership Stat is invalid. Current Status: " + user.getStatus());
                    }
                    else{
                        System.out.println("Processing claim");
                        Claim c = new Claim();
                        c.setRationale(request.getParameter("claimRationale"));
                        c.setStatus(Claim.State.SUBMITTED);
                        c.setAmount(Float.parseFloat(request.getParameter("claimAmount")));
                        c.setDate(new java.sql.Date(System.currentTimeMillis()));
                        claimManager.addClaim(user, c);
                    }
                    break;
                default:
                    break;
            }

        }

        this.showTables(request, response);
        request.getRequestDispatcher("/WEB-INF/memberDashboard.jsp").forward(request, response);

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

