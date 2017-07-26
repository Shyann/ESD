/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xyz.esd.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 *
 * @author josh
 */
public class Address {
    private String address;

    public Address(String address){
        String lines[] = address.split(",");
        String newAddress = "";
        for(String line : lines){
            if(line.length() > 0 && (!line.isEmpty()) && (!line.equals(" "))){
                newAddress += line +", ";
            }
        }
        if(newAddress.endsWith(", "))
        {
            newAddress = newAddress.substring(0,newAddress.length() - 2);
        }
        this.address = newAddress;
    }

    public boolean isValid() throws MalformedURLException, IOException{
        String url = ("http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(this.toString(), "UTF-8"));
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader((
                        new URL(url)).openConnection().getInputStream(), Charset.forName("UTF-8")))){
            String inputLine;

            while ((inputLine = in.readLine()) != null){
                if(inputLine.contains("\"status\" : \"ZERO_RESULTS\"")){
                    return false;
                }
            }
            return true;
        }
    }

    @Override
	public String toString() {
        return this.address;
	}
    
}
