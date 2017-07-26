package com.xyz.esd.controllers;

import com.xyz.esd.beans.Claim;
import com.xyz.esd.beans.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author user_pc
 */
public class AdminDashboardServlet extends HttpServlet {

    /* Var name in view that will be displayed */
    public static final String ANNUAL_MESSAGE = "annualMessage";
    public static final String DASHBOARD_MESSAGE = "dashboardMessage";
    public static final String FINANCIAL_STATS = "financialStats";

    /* managers and other controllers for models */
    private static final ClaimManager claimManager = new ClaimManager();
    private static final UserManager userManager = new UserManager();
    private static final PaymentManager paymentManager = new PaymentManager();
    /**
     *
     */
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
        /* show main page */
        request.getRequestDispatcher("/WEB-INF/adminDashboard.jsp").forward(request, response);
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
        String act = request.getParameter("act"); /* act is name of button containing the value of desired action passed from view */
        int numUsers = userManager.getUsers(User.State.APPROVED).size();
        /*
        settings request attributes allows the view to check if they exists and triggers it to create the relevant table.
         */
        if (act != null) {
            switch (act) {
                case "listMembers": /* button to show users table */
                    request.setAttribute("listMembers", userManager.getUsers());
                    break;
                case "listClaims": /* button clicked to list claim table */
                    request.setAttribute("listClaims", claimManager.getClaims());
                    break;
                case "listProvisionals": /* button clicked to list proovisional table users */
                    request.setAttribute("listProvisionals", userManager.getUsers(User.State.APPLIED));
                    break;
                case "listApprovedClaims": /* button clicked to list approved claims table */
                    request.setAttribute("listApprovedClaims", claimManager.getClaims(Claim.State.APPROVED));
                    request.setAttribute(ANNUAL_MESSAGE, getAverageClaimsRounded(numUsers));
                    break;
                case "listBalances": /* list outstanding balances */
                    ArrayList<User> oweMoney = userManager.getUsers().stream().filter(user -> user.getBalance() > 0).collect(Collectors.toCollection(ArrayList::new));
                    request.setAttribute("listBalances", oweMoney);
                    break;
                case "listFinancialStats": /* lists financial stats */
                    request.setAttribute("sumClaims", claimManager.sumClaims());
                    request.setAttribute("sumPayments", paymentManager.sumPayments());
                    request.setAttribute("numUsers", numUsers);
                    break;
                case "chargeMembershipFee": /* charge fees */
                    for (User u : userManager.getUsers(User.State.APPROVED)) {
                        u.setBalance(u.getBalance() + 10);
                        userManager.updateUser(u);
                    }
                    request.setAttribute(DASHBOARD_MESSAGE, "All Approved Users Charged £10.00 Membership Fee");
                    request.setAttribute("listMembers", userManager.getUsers());
                    break;
                default:
                    break;
            }
        }
        /*
        Charges members by dividing the total cost of approved claims / number of users
         */
        String chargeMembers = request.getParameter("chargeMembers");
        if (chargeMembers != null) {
            System.out.println("Charging memebers");
            ArrayList<User> users = userManager.getUsers(User.State.APPROVED);
            for (User u : users) {
                u.setBalance(u.getBalance() + (claimManager.sumClaims() / userManager.getUsers(User.State.APPROVED).size()));
                userManager.updateUser(u);
            }
            request.setAttribute(DASHBOARD_MESSAGE, "All Approved Users Charged " + getAverageClaimsRounded(numUsers) + " Yearly Fees"); // pass success message back to view
            request.setAttribute("listMembers", userManager.getUsers());
        }

        /*
        * approveMember string will contain the users ID,
        * the user then fetches the User object so that the member can be approved
        */
        String approveMember = request.getParameter("approveMember");
        if (approveMember != null) {
            System.out.println("Approving member = " + approveMember);
            userManager.approveMember(userManager.getUser(approveMember));
            request.setAttribute("listProvisionals", userManager.getUsers(User.State.APPLIED));
        }

        /*
        uses the same mechanism as approveMember
         */
        String suspendMember = request.getParameter("suspendMember");
        if (suspendMember != null) {
            System.out.println("Rejecting member = " + suspendMember);
            userManager.suspendMember(userManager.getUser(suspendMember));
        }

        String adminMember = request.getParameter("adminMember");
        if (adminMember != null) {
            System.out.println("Admin-ing member = " + adminMember);
            userManager.adminMember(userManager.getUser(adminMember));
        }

        String approveClaim = request.getParameter("approveClaim");
        if (approveClaim != null) {
            System.out.println("Approving claim = " + approveClaim);
            claimManager.approveClaim(claimManager.getClaim(Integer.parseInt(approveClaim)));
            request.setAttribute("listClaims", claimManager.getClaims());
        }

        String rejectClaim = request.getParameter("rejectClaim");
        if (rejectClaim != null) {
            System.out.println("rejecting claim = " + rejectClaim);
            claimManager.rejectClaim(claimManager.getClaim(Integer.parseInt(rejectClaim)));
            request.setAttribute("listClaims", claimManager.getClaims());
        }
        request.getRequestDispatcher("/WEB-INF/adminDashboard.jsp").forward(request, response);
    }

    private String getAverageClaimsRounded(int numUsers) {
        return "£" + new DecimalFormat("#.##").format(claimManager.sumClaims() / numUsers);
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
