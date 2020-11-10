package com.revature.models;

import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

public class Account {
	private int id;
	private double balance;
	private int user_id;
	private String accountType;
	private String accountStatus;
	private UserService uServ = new UserServiceImpl();
	public Account()
	{
		super();
	}
	public Account(int id, double balance, int user_id,String accountType,String accountStatus) {
		super();
		this.id = id;
		this.balance = balance;
		this.user_id = user_id;
		this.accountType=accountType;
		this.accountStatus = accountStatus;
	}
	public Account(double balance, int user_id,String accountType) {
		super();		
		this.balance = balance;
		this.user_id = user_id;
		this.accountType=accountType;
		accountStatus= "Pending";

	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	@Override
	public String toString() {
		return "Account #" + id + "\nBalance: " + balance + "\nOwner: " + uServ.findUser(user_id).getUsername() + "\nAccount Type: " + accountType
				+ "\nAccount Status: " + accountStatus;
	}
	
	
	
	
	
	
}
