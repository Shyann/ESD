package com.xyz.esd.controllers;

import com.xyz.esd.beans.Claim;
import com.xyz.esd.JDBCConnectionHandler;
import com.xyz.esd.beans.Payment;
import com.xyz.esd.beans.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentManager {

    private JDBCConnectionHandler handler;

    public PaymentManager() {
        handler = new JDBCConnectionHandler();
    }

    public float sumPayments(){
        float sum = 0;
        ArrayList<Payment> payments = getPayments();
        sum = payments.stream().map((p) -> p.getAmount()).reduce(sum, (accumulator, _item) -> accumulator + _item);
        return sum;
    }
    
    public ArrayList<Payment> getPayments(){
        String query = "SELECT * FROM payments";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        ArrayList<Payment> payments = new ArrayList<>();
        for (HashMap<String, Object> row : resultMap) {
            Payment tPayment = new Payment();
            tPayment.setId((Integer) row.get("id"));
            tPayment.setType((String) row.get("type_of_payment"));
            tPayment.setAmount((Float) row.get("amount"));
            tPayment.setDate((Timestamp) row.get("date"));
            payments.add(tPayment);
        }
        return payments;
    }

    public ArrayList<Payment> getPayments(Claim.State state) {
        String query = "SELECT * FROM payments WHERE status = \"" + state + "\"";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        ArrayList<Payment> payments = new ArrayList<>();
        for (HashMap<String, Object> row : resultMap) {
            Payment tPayment = new Payment();
            tPayment.setId((Integer) row.get("id"));
            tPayment.setType((String) row.get("type_of_payment"));
            tPayment.setAmount((Float) row.get("amount"));
            tPayment.setDate((Timestamp) row.get("date"));
            payments.add(tPayment);
        }
        return payments;
    }

    public ArrayList<Payment> getPayments(String username) {
        String query = "SELECT * FROM payments WHERE mem_id = \"" + username + "\"";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        ArrayList<Payment> payments = new ArrayList<>();
        for (HashMap<String, Object> row : resultMap) {
            Payment tPayment = new Payment();
            tPayment.setId((Integer) row.get("id"));
            tPayment.setType((String) row.get("type_of_payment"));
            tPayment.setAmount((Float) row.get("amount"));
            tPayment.setDate((Timestamp) row.get("date"));
            payments.add(tPayment);
        }
        return payments;
    }

    public void addPayment(User tUser, Payment tPayment) {
        String query = "INSERT INTO payments (mem_id,type_of_payment,amount,date)"
                + " VALUES ('" + tUser.getUsername() + "',\"" + tPayment.getType() + "\"," + tPayment.getAmount() + ",'" + tPayment.getDate() + "')";
        handler.executeQuery(query);

        tUser.setBalance(tUser.getBalance() - tPayment.getAmount());

        /* TODO make static? */
        UserManager tUserManager = new UserManager();
        tUserManager.updateUser(tUser);
    }

}
