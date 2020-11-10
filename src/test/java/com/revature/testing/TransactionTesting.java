package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.models.Account;
import com.revature.repositories.AccountDAOImpl;
import com.revature.services.AccountServiceImpl;

public class TransactionTesting {

	@Mock 
	private AccountDAOImpl accountMock;
	
	@InjectMocks 
	private AccountServiceImpl aServMock;
	
	
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void testAddAccount_returnTrue()
	{
		Account a = Mockito.mock(Account.class);
		assertEquals(aServMock.insert(a), 0);
	}
	@Test
	public void testAddAccount_returnFalse()
	{
		Account a = Mockito.mock(Account.class);
		assertNotEquals(aServMock.insert(a), 21);
	}
@Test
	public void testPerformDeposit_returnFalse()
	{
		Account a = Mockito.mock(Account.class);
		a.setBalance(50);
		a.setAccountStatus("open");
		
		when(accountMock.update(a)).thenReturn(true);
		assertEquals(true,aServMock.deposit(a, 20));
		
	}
	@Test
	public void testPerfomWithdrawl_returnFalse()
	{
	Account a = Mockito.mock(Account.class);
	a.setBalance(50);
	a.setAccountStatus("open");
	
	when(accountMock.update(a)).thenReturn(true);
	assertEquals(false,aServMock.withdraw(a, 100));
		
	}
	@Test 
	public void testPerfomWithdrawl_returnTrue()
	{
	Account a = new Account();
	a.setBalance(50);
	a.setAccountStatus("open");
	System.out.println(a.getBalance());
	when(accountMock.update(a)).thenReturn(true);
	assertEquals(true,aServMock.withdraw(a, 25));
		
	}	
	@Test
	public void testPerformTransfer_returnFalse()
	{
		Account a = new Account();
		Account b = new Account();
		a.setBalance(50);
		b.setBalance(23);
		when(accountMock.transact(a, b)).thenReturn(true);
		assertEquals(false,aServMock.transfer(a, b, 100));
	}
	@Test
	public void testPerformTransfer_returnTrue()
	{
		Account a = new Account();
		Account b = new Account();
		a.setBalance(50);
		System.out.println(a.getBalance());
		b.setBalance(23);
		when(accountMock.transact(a, b)).thenReturn(true);
		assertEquals(true,aServMock.transfer(a, b, 10));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
