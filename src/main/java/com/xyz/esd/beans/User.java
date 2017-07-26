/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xyz.esd.beans;

import java.sql.Date;
import java.text.DecimalFormat;

/**
 *
 * @author josh
 */
public class User {

	public enum State {
		APPLIED, SUSPENDED, APPROVED, ADMIN
	}

	private String name;
	private Date dob;
	private Date dor;
	private float balance;
	private String username;
	private State status;
	private Address address;
	private String password;
	private String balanceAsString;

	public User() {
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setStatus(State status) {
		this.status = status;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return this.address;
	}

	public String getUsername() {
		return this.username;
	}

	public State getStatus() {
		return this.status;
	}

	public String getName() {
		return name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDor() {
		return dor;
	}

	public void setDor(Date dor) {
		this.dor = dor;
	}

	public float getBalance() {
		return roundToSecondDecimal(this.balance);
	}

	public void setBalance(float balance) {
		this.balance = roundToSecondDecimal(balance);
		this.balanceAsString = new DecimalFormat("#.##").format(balance);
	}

	public String getBalanceAsString() {
		return balanceAsString;
	}

	public void setBalanceAsString(String balanceAsString) {
		this.balanceAsString = balanceAsString;
	}

	public boolean isAdmin() {
        return this.status == State.ADMIN;
    }

	private float roundToSecondDecimal(float balance) {
		return Math.round(balance * 100f) / 100f;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", dob=" + dob + ", dor=" + dor + ", balance=" + balance + ", username=" + username + ", status=" + status + ", address=" + address + ", password =" + password + "]";
	}

}
