package com.xyz.esd.beans;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private int memberId;
    private String type;
    private float amount;
    private Timestamp date;
    public Payment(){

    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

	@Override
	public String toString() {
		return "Payment [id=" + id + ", type=" + type + ", amount=" + amount + ", date=" + date + "]";
	}
    
    
}
