package com.xyz.esd.models;

import com.xyz.esd.beans.Address;
import com.xyz.esd.beans.User;
import com.xyz.esd.controllers.UserManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dominic Howe
 */
public class RegistrationModel {


    UserManager db;
    String username, password; //Randomly generated
    String name;
    Address address;
    Date dob;
    ArrayList<String> errors;

    public RegistrationModel(String name, Address address, Date dob) {
        this.db = new UserManager();
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.username = generateUsername();
        this.password = generatePassword();

        this.errors = new ArrayList<String>();
    }

    private String generatePassword(){
        return UUID.randomUUID().toString().substring(0, 10);
    }
    private String generateUsername() {
        String user = (name.split("\\s+")[0].substring(0, 2) + "-" + name.split("\\s+")[1]).toLowerCase();
        int i = 1;
        while(db.isUser(user)){
            user = user.substring(0, 2) + i + user.substring(2, user.length());
            i++;
        }
        return user;
    }

    public ArrayList<String> getErrors(){
        return this.errors;
    }

    public User registerUser()  {
        User tUser = new User();

        tUser.setUsername(this.username);
        tUser.setName(this.name.trim());
        tUser.setAddress(this.address);
        tUser.setDob(this.dob);
        tUser.setDor(new java.sql.Date(System.currentTimeMillis()));
        tUser.setStatus(User.State.APPLIED);
        tUser.setBalance(0);
        tUser.setPassword(this.password);

        try{
            db.createUser(tUser);
        }catch (Exception E){
            errors.add("Error when creating user ... investigate");
        }
        return tUser;
    }
}
