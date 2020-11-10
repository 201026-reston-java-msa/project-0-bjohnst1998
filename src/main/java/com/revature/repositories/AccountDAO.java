package com.revature.repositories;

import java.util.List;

import com.revature.models.Account;

public interface AccountDAO {
	
	public List<Account> findAll();
	public List<Account> findByUser(int userID);
	public Account findAccount(int accountID);
	public boolean insert(Account a);
	public boolean update(Account a);
	public boolean transact(Account a, Account b);

}
