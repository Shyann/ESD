package com.xyz.esd.models;

import com.xyz.esd.beans.User;
import com.xyz.esd.controllers.UserManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dominic Howe
 */
public class LoginModel {

	public static User authenticate(String username, String password) {
        UserManager userManager = new UserManager();
        User tUser = userManager.getUser(username);
        if(tUser != null){
            System.out.println(tUser.toString());
            System.out.println(password);
            System.out.println(tUser.getPassword());
            if(tUser.getPassword().equals(password)) {
                System.out.println("Correct Password");
                return tUser;
            }
        }
        return null;

	}

}
