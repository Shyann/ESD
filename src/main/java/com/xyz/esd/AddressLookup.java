package com.xyz.esd;

import com.xyz.esd.beans.Address;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Created by Computer-1 on 29/11/2016.
 */
public class AddressLookup {
    private String key = "xyRg-ky_RkmdNImMN5cS5g6558";
    private String api = "https://api.getAddress.io/v2/uk/";
    private String postcode;

    public AddressLookup(String postcode){
        this.postcode = postcode;
    }
    public ArrayList<Address> getAddresses() {
        ArrayList<Address> addresses = new ArrayList<>();
        try {
            postcode = postcode.replace(" ", ""); // removes whitespace
            URL url = new URL(api + URLEncoder.encode(postcode, "UTF-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("api-key", key);

            if(con.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Raw data from api : " + response.toString());

                JSONObject o = (JSONObject) (new JSONParser()).parse(response.toString());
                JSONArray JSONAddresses = (JSONArray) o.get("Addresses");

                for (Object a : JSONAddresses) {
                    String address = a + ", " + postcode;
                    addresses.add(new Address(address));
                }
            }else{
                System.out.println("API error, Could not find any addresses");
            }

        }catch (Exception e){
            System.out.println("Could not find any addresses");
        }
        return addresses;
    }
}
