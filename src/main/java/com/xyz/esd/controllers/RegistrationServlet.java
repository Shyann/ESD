package com.xyz.esd.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xyz.esd.AddressLookup;
import com.xyz.esd.beans.Address;
import com.xyz.esd.beans.User;
import com.xyz.esd.models.RegistrationModel;

/**
 *
 * @author user_pc
 */
public class RegistrationServlet extends HttpServlet {

    private static final String REGISTRATION_MESSAGE = "registrationMessage";
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null){
            session = request.getSession();
        }
        session.setAttribute(REGISTRATION_MESSAGE, "");
        request.getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Store the users details, so they can be used to either create the user, or repopulate the view if needed */
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String dob = request.getParameter("dob");

        // Determine which button they selected
        String buttonVal = request.getParameter("submit");
        switch(buttonVal){
            case "lookupAddress":
                String postcode = request.getParameter("postcode");
                AddressLookup lookup = new AddressLookup(postcode);
                // use web API to create a list of addresses based on post code
                ArrayList<Address> addresses = lookup.getAddresses();
                if(addresses.size() == 0){
                    System.out.println("Could not find any address, letting user manually enter address");
                    //If the API is not accessible or did not find any addresses, then let the user manually add the address
                    request.setAttribute("manualAddress", true);
                }
                // Send the users values back to view so they can repopulate the form
                request.setAttribute("fname", fname);
                request.setAttribute("lname", lname);
                request.setAttribute("dob", dob);
                request.setAttribute("addresses", addresses);
                request.getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
                break;
            case "createUser":
                /* Validates the users inputs and creates an account */
                HttpSession session = request.getSession(false);
                String address = request.getParameter("address");
                Address tAddress = new Address(address);
                //protects against user changing the html
                if(!tAddress.isValid()){
                    System.out.println("Address in not valid!");
                }
                Date dobDate = Date.valueOf(request.getParameter("dob"));
                RegistrationModel reg = new RegistrationModel(fname + " "+lname, tAddress, dobDate);
                User newUser = reg.registerUser();
                if(newUser == null){
                    String errors = "";
                    for(String error : reg.getErrors()){
                        errors += error + "\n";
                    }
                    request.setAttribute(REGISTRATION_MESSAGE,errors);
                    request.getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
                }
                String lmsg = "Registration Successful.<br>Username: " + newUser.getUsername() + "<br>Password: " + newUser.getPassword() 
                            + "<br>Congratulations, you have now been registered as a PROVISIONAL member."
                            + " Once payment for membership has been received, you will become a full member."
                            + "<br>Claims can be made after 6 months of membership.<br>Welcome to XYZ Insurance.";
                /* Forward the user to login page, and tell them they are a provisional user, untill they pay and admin accepts */
                request.setAttribute(LoginServlet.LOGIN_MESSAGE, lmsg);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                break;
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
