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

import com.revature.models.User;
import com.revature.repositories.UserDAOImpl;
import com.revature.services.UserServiceImpl;
public class UserTesting {

	@Mock
	private UserDAOImpl uDao;
	
	@InjectMocks
	private UserServiceImpl uServ;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddUser_returnTrue() {
		User u = new User(2, "hello", "123", "Josh", "Moneybags", "money@gmail.com", "customer");
		
		when(uDao.insert(u)).thenReturn(true);
		assertEquals(true,uServ.insert(u));
	}
	
	@Test
	public void testAddUser_returnFalse() {
		User u = new User(2, "hello", "123", "Josh", "Moneybags", "money@gmail.com", "customer");
		
		when(uDao.insert(u)).thenReturn(false);
		assertEquals(false,uServ.insert(u));
	}
	@Test
	public void testUpdateUser_returnTrue()
	{
		User u = new User(2, "hello", "123", "Josh", "Moneybags", "money@gmail.com", "customer");
		when(uDao.update(u)).thenReturn(true);
		assertEquals(true, uServ.update(u));
	}
	
	public void testUpdateUser_returnFalse()
	{
		User u = new User(2, "hello", "123", "Josh", "Moneybags", "money@gmail.com", "customer");
		when(uDao.update(u)).thenReturn(false);
		assertEquals(false, uServ.update(u));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
