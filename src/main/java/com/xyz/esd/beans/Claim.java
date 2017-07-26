package com.xyz.esd.beans;

import java.sql.Date;

public class Claim {

    public enum State {
        SUBMITTED,
        APPROVED,
        REJECTED
    }
    private int id;
    private Date date;
    private String memberID;
    private String rationale;
    private State status;
    private float amount;
    private boolean validity = true; //default = valid claim
    public Claim(){
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }
	
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setValidity(boolean validity){
        this.validity = validity;
    }

    public boolean getValidity(){
        return this.validity;
    }

	@Override
	public String toString() {
		return "Claim [id=" + id + ", date=" + date + ", rationale=" + rationale + ", status=" + status + ", amount=" + amount + "]";
	}
    
    
}
