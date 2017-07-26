package com.xyz.esd;

import com.xyz.esd.beans.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Computer-1 on 24/11/2016.
 */
public class AuthFilter implements Filter {
    public void init(FilterConfig arg0) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String url = request.getServletPath();
        System.out.println("Running AuthFilter for "+url);

        HttpSession session = request.getSession(false);
        User tUser = null;
        if(session != null) {
            tUser = (User)session.getAttribute("user");
        }

        switch(url){
            case "/memberDashboard":
                if(tUser == null || tUser.getStatus() == User.State.ADMIN){
                    req.setAttribute("errorMessage", "You are not authorized for this page!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }else{
                    System.out.println("Letting user continue");
                    chain.doFilter(request, response); // Pass request back down the filter chain
                }
                break;
            case "/adminDashboard":
                if(tUser == null || tUser.getStatus() != User.State.ADMIN){
                    req.setAttribute("errorMessage", "You are not authorized for this page!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }else{
                    System.out.println("Letting user continue");
                    chain.doFilter(request, response); // Pass request back down the filter chain
                }
                break;
            default:
                System.out.println("Letting user continue");
                chain.doFilter(request, response); // Pass request back down the filter chain
                break;
        }
    }

    public void destroy(){

    }
}
