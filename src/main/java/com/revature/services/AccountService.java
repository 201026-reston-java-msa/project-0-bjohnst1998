package com.revature.services;

import java.util.List;

import com.revature.models.Account;

public interface AccountService {
	public List<Account> findAll();
	public List<Account> findByUser(int userId);
	public Account findAccount(int accountID);
	public int insert(Account a);
	public boolean update(Account a);
	public boolean transfer(Account a, Account b,double amount);
	public boolean deposit(Account a, double amount);
	public boolean withdraw(Account a,double amount);

}
