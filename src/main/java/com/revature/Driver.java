package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.repositories.EmployeeDAOImpl;
import com.revature.services.AccountService;
import com.revature.services.AccountServiceImpl;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

public class Driver {

	static User loggedInUser = new User();
	static UserService userServ = new UserServiceImpl();
	static AccountService aServ = new AccountServiceImpl();
	private static Logger log = Logger.getLogger(EmployeeDAOImpl.class);
	static boolean wantsToQuit = false;

	public static void main(String[] args) {
		run();

	}

	static void run() {
		Scanner in = new Scanner(System.in);
		String userInput;

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
			userInput = in.nextLine();
			if (userInput.equalsIgnoreCase("y")) {
				wantsToQuit = true;
			}
		}

	}

	static void logIn(Scanner in) {
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

		if (userServ.checkIfUsernameExists(username)) {
			System.out.println("An account with that username already exists.\n");
			createAccount(in);
		} else {
			User u = new User();
			u.setUsername(username);
			u.setPassword(password);
			u.setEmail(email);
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setPermissionLevel("customer");
			if (userServ.insert(u)) {
				System.out.println("Your account has been created.\nPlease log in");
				logIn(in);
			} else {
				System.out.println("Account creation failed! Please try again\n");
			}

		}

	}

	static void customerCommands(Scanner in) {
		System.out.println(
				"1: Create bank account\n2: View your accounts\n3: View personal information\n4: Perform transaction\n5: Sign out");
		String input = in.nextLine();

		if (input.equals("1")) {
			openAccount(in);
		} else if (input.equals("2")) {
			viewUsersAccounts(in, loggedInUser);
		} else if (input.equals("3")) {

		} else if (input.equals("4")) {

		} else if (input.equals("5")) {
			wantsToQuit = true;
		}

	}

	static void employeeCommands(Scanner in) {
		System.out.println("1:Look up user\n2:Look up account\n3:Approve accounts\n4: Sign out");
		String input = in.nextLine();

		if (input.equals("1")) {
			System.out.println("Please enter the user's ID#\n");
			int id = in.nextInt();
			viewUser(in, id);
		} else if (input.equals("2")) {
			System.out.println("Please enter the account #\n");
			int id = in.nextInt();
			viewAccount(in, id);
		} else if (input.equals("3")) {

		} else if (input.equals("4")) {

		} else if (input.equals("5")) {

		}
	}

	static void adminCommands(Scanner in) {
		System.out.println(
				"1:Look up user\n2:Look up account\n3:Approve accounts\n4:Perform transaction\n5:Cancel accounts\n6: Sign out");
		String input = in.nextLine();

		if (input.equals("1")) {

		} else if (input.equals("2")) {

		} else if (input.equals("3")) {
			confirmPendingAccounts(in);
		} else if (input.equals("4")) {

		} else if (input.equals("5")) {

		}
	}

	static void openAccount(Scanner in) {
		System.out.println("What kind of account would you like to open?\n1: Checkings\n2: Savings");
		String input = in.nextLine();
		String accountType = "checking";
		if (input.equals("1")) {
			accountType = "checking";
		} else if (input.equals("2")) {
			accountType = "savings";
		} else {
			System.out.println("That is not a valid account type");
			openAccount(in);
		}
		Account a = new Account(0, loggedInUser.getId(), accountType);
		a.setId(aServ.insert(a));
		if (a.getId() != 0) {
			System.out.println("Account #" + a.getId() + " created!");
			System.out.println("Please deposit a starting balance: $");
			double d = in.nextDouble();
			aServ.deposit(a, d);
			System.out.println("You deposited: $" + d + "\n");
			System.out.println(
					"Your account is now pending approval by an employee.\nWhile the account is pending you may not perform transactions.");
		} else {
			log.warn("Failed to create account! Please try again");
			createAccount(in);
		}

		evaluatePermissionContent(in);

	}

	static void viewUsersAccounts(Scanner in, User u) {
		List<Account> list = aServ.findByUser(u.getId());
		for (Account a : list) {
			System.out.println("--------------------");
			System.out.println(a.toString());
			System.out.println("--------------------");
		}

	}

	static void viewAccount(Scanner in, int accountID) {
		Account a = aServ.findAccount(accountID);

		System.out.println("--------------------");
		System.out.println(a.toString());
		System.out.println("--------------------");
	}

	static void viewUser(Scanner in, int userId) {
		User u = userServ.findUser(userId);
		System.out.println("--------------------");
		System.out.println(u.toString());
		System.out.println("--------------------");
		System.out.println("Would you like to update any information? Y/N");
		String s = in.nextLine();
		if (s.equalsIgnoreCase("y")) {
			System.out.println("If you do not want to change a field, leave it blank");
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

			if (!username.equalsIgnoreCase("")) {
				u.setUsername(username);
			}
			if (!password.equalsIgnoreCase("")) {
				u.setPassword(password);
			}
			if (!firstName.equalsIgnoreCase("")) {
				u.setFirstName(firstName);
			}
			if (!lastName.equalsIgnoreCase("")) {
				u.setLastName(lastName);
			}
			if (!email.equalsIgnoreCase("")) {
				u.setEmail(email);
			}

			if (userServ.update(u)) {
				System.out.println("Information updated!");
				System.out.println(u);
				evaluatePermissionContent(in);

			} else {
				System.out.println("There was an issue updating your information. Please try again");
				viewUser(in, userId);
			}

		}

	}

	static void evaluatePermissionContent(Scanner in) {
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

	static void confirmPendingAccounts(Scanner in) {
		List<Account> list = aServ.findByStatus("pending");
		for (Account a : list) {
			System.out.println("--------------------");
			System.out.println(a.toString());
			System.out.println("--------------------");
		}
		while (true) {
			System.out.println("Which account would you like to change?");
			int aId = in.nextInt();
			in.nextLine();
			System.out.println("Enter the new status (pending,open,closed,denied)");
			String status = in.nextLine();
			Account a = aServ.findAccount(aId);
			a.setAccountStatus(status);
			if (aServ.update(a)) {
				System.out.println("Account status updated!\nWould you like to confirm another account? Y/N");
				String s = in.nextLine();
				if (s.equalsIgnoreCase("n")) {
					break;
				}

			}

		}
		evaluatePermissionContent(in);

	}
}
