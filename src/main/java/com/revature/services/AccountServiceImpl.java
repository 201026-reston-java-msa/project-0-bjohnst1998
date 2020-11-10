package com.revature.services;

import java.util.List;

import org.apache.log4j.xml.Log4jEntityResolver;

import com.revature.models.Account;
import com.revature.repositories.AccountDAO;
import com.revature.repositories.AccountDAOImpl;

public class AccountServiceImpl implements AccountService {
	private static AccountDAO aDao = new AccountDAOImpl();

	@Override
	public List<Account> findAll() {

		return aDao.findAll();
	}

	@Override
	public List<Account> findByUser(int userID) {

		return aDao.findByUser(userID);
	}

	@Override
	public Account findAccount(int accountID) {

		return aDao.findAccount(accountID);
	}

	@Override
	public int insert(Account a) {

		return aDao.insert(a);
	}

	@Override
	public boolean update(Account a) {

		return aDao.update(a);
	}

	@Override
	public boolean transfer(Account a, Account b, double amount) {
		if (a.getBalance() >= amount) {
			a.setBalance(a.getBalance() - amount);
			b.setBalance(b.getBalance() + amount);
			return aDao.transact(a, b);
		} else {
			return false;
		}

	}

	public boolean deposit(Account a, double amount) {
		if (a.getAccountStatus().equals("open")) {
			a.setBalance(a.getBalance() + amount);
			return aDao.update(a);
		} else {
			return false;
		}

	}

	public boolean withdraw(Account a, double amount)
	{
		if(a.getAccountStatus().equals("open") && a.getBalance() >= amount)
		{
			a.setBalance(a.getBalance() - amount);
			return aDao.update(a);
		}
		else {
			return false;
		}	
	}

	@Override
	public List<Account> findByStatus(String status) {
		return aDao.findByStatus(status);
	}

}
