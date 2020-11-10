package com.revature;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
import com.revature.services.AccountServiceImpl;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

public class Driver {

	static User loggedInUser = new User();
	static UserService userServ = new UserServiceImpl();
	static AccountService aServ = new AccountServiceImpl();

	public static void main(String[] args) {
		run();
	
	}

	static void run() {
		Scanner in = new Scanner(System.in);
		String userInput;
		boolean wantsToQuit = false;

		while (!wantsToQuit) {
			System.out.println("Welcome to Ben & Co Banking Services\nDo you have an existing account? [Y/N]");
			userInput = in.nextLine();

			if (userInput.equalsIgnoreCase("y")) {
				// Do Login
				logIn(in);
			} else {
				// Create account
				System.out.println("Would you like to create an account Y/N?");
				userInput = in.nextLine();
				if (userInput.equalsIgnoreCase("y")) {
					createAccount(in);
				}				
			}
			System.out.println("Would you like to quit? Y/N");
			userInput=in.nextLine();
			if(userInput.equalsIgnoreCase("y"))
			{
				wantsToQuit=true;
			}
		}

	}

	static void logIn(Scanner in)
	{
		System.out.println("Please enter your Username\n");
		String username = in.nextLine();
		System.out.println("Please enter your Password\n");
		String password = in.nextLine();
		loggedInUser = userServ.loginUser(username, password);
		if (loggedInUser != null) {
			// Generate options based on permissions.
			System.out.println("Log in successful!\nWelcome " + loggedInUser.getFirstName() + "\n");
			System.out.println("What would you like to do?");
			if (loggedInUser.getPermissionLevel().equals("customer")) {
				// Customer Logic
				customerCommands(in);
			} else if (loggedInUser.getPermissionLevel().equals("employee")) {
				// Employee Logic
				employeeCommands(in);
			} else if (loggedInUser.getPermissionLevel().equals("admin")) {
				// admin logic
				adminCommands(in);
			}
		}
	}
	
	static void createAccount(Scanner in) {
		System.out.println("Please enter a username: \n");
		String username = in.nextLine();
		System.out.println("Please enter a password: \n");
		String password = in.nextLine();
		System.out.println("Please enter your first name: \n");
		String firstName = in.nextLine();
		System.out.println("Please enter your last name: \n");
		String lastName = in.nextLine();
		System.out.println("Please enter your email: \n");
		String email = in.nextLine();
		
		if(userServ.checkIfUsernameExists(username))
		{
			System.out.println("An account with that username already exists.\n");
			createAccount(in);
		}
		else {
			User u = new User();
			u.setUsername(username);
			u.setPassword(password);
			u.setEmail(email);
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setPermissionLevel("customer");
			if(userServ.insert(u))
			{
				System.out.println("Your account has been created.\nPlease log in");
				logIn(in);
			}
			else {
				System.out.println("Account creation failed! Please try again\n");
			}
			
		}
		
	}
	
	static void customerCommands(Scanner in)
	{
		System.out.println("1: Create bank account\n2: View your accounts\n3: View personal information\n4: Perform transaction\n5: Sign out");
	}
	
	static void employeeCommands(Scanner in)
	{
		System.out.println("1:Look up user\n2:Look up account\n3:Approve accounts\n4: Sign out");
	}
	static void adminCommands(Scanner in)
	{
		System.out.println("1:Look up user\n2:Look up account\n3:Approve accounts\n4:Perform transaction\n5:Cancel accounts\n6: Sign out");

	}
	
}
