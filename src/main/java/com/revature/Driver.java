package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
import com.revature.services.AccountServiceImpl;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

public class Driver {

	static User loggedInUser = null;
	static UserService userServ = new UserServiceImpl();
	static AccountService aServ = new AccountServiceImpl();
	private static Logger log = Logger.getLogger(Driver.class);
	static boolean wantsToQuit = false;

	public static void main(String[] args) {

		run();

	}

	static void run() {
		Scanner in = new Scanner(System.in);
		String userInput;

		while (!wantsToQuit) {
			if (loggedInUser == null) {
				System.out.println("Welcome to Ben & Co Banking Services\nDo you have an existing account? [Y/N]");
				userInput = in.nextLine();

				if (userInput.equalsIgnoreCase("y")) {
					// Do Login
					logIn(in);
				} else {
					// Create account
					System.out.println("Would you like to create an account? [Y/N]");
					userInput = in.nextLine();
					if (userInput.equalsIgnoreCase("y")) {
						createAccount(in);
					}
				}
			}
			System.out.println("You are signed out! Exiting application");

		}

	}

	static void logIn(Scanner in) {
		// Get user data
		System.out.println("Please enter your Username\n");
		String username = in.nextLine();
		System.out.println("Please enter your Password\n");
		String password = in.nextLine();
		// Store the logged in user. Would eventually be pushed to a cookie or other
		// browser safe persistent storage.
		loggedInUser = userServ.loginUser(username, password);
		if (loggedInUser != null) { // Check if the log in returned a user or not
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
				// Admin logic
				adminCommands(in);
			}
		} else {
			System.out.println("Error: Invalid Credentials");
		}
	}

	// Retrieves information from user and attempts to generate an account
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
		// Check to make sure no usernames are repeated
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
			if (userServ.insert(u)) { // Attempt to push new user to the DB.
				System.out.println("Your account has been created.\nPlease log in");
				System.out.println("----------------------");
				logIn(in);
			} else {
				System.out.println("Account creation failed! Please try again\n");
			}

		}

	}

	// Set of commands that a Customer can access.
	static void customerCommands(Scanner in) {
		System.out.println(
				"1:Create bank account\n2:View your accounts\n3:View personal information\n4:Perform transaction\n5:Sign out");
		String input = in.nextLine();

		if (input.equals("1")) {
			openAccount(in);
		} else if (input.equals("2")) {
			viewUsersAccounts(in, loggedInUser);
		} else if (input.equals("3")) {
			viewUser(in, loggedInUser.getId());
		} else if (input.equals("4")) {
			performTransactions(in);
		} else if (input.equals("5")) {
			wantsToQuit = true;
		}

	}

	// Set of commands an employee can access
	static void employeeCommands(Scanner in) {
		System.out.println("1:Look up user\n2:Look up account\n3:Approve accounts\n4:Sign out");
		String input = in.nextLine();

		if (input.equals("1")) {
			System.out.println("Please enter the user's ID#\n");
			int id = in.nextInt();
			in.nextLine();
			viewUser(in, id);
		} else if (input.equals("2")) {
			System.out.println("Please enter the account #\n");
			int id = in.nextInt();
			in.nextLine();
			viewAccount(in, id);
		} else if (input.equals("3")) {
			confirmPendingAccounts(in);
		} else if (input.equals("4")) {
			wantsToQuit = true;
		}
	}

	// Set of commands an admin can access.
	static void adminCommands(Scanner in) {
		System.out.println(
				"1:Look up user\n2:Look up account\n3:Approve accounts\n4:Perform transaction\n5:Cancel accounts\n6:Sign out");
		String input = in.nextLine();

		if (input.equals("1")) {
			System.out.println("Enter the User's ID #");
			int id = in.nextInt();
			in.nextLine();
			viewUser(in, id);
		} else if (input.equals("2")) {
			System.out.println("Enter the Account #");
			int id = in.nextInt();
			in.nextLine();
			viewAccount(in, id);
		} else if (input.equals("3")) {
			confirmPendingAccounts(in);
		} else if (input.equals("4")) {
			performTransactions(in);
		} else if (input.equals("5")) {
			cancelAccount(in);
		} else if (input.equals("6")) {
			wantsToQuit = true;
		} else {
			adminCommands(in);
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
			// Admin logic
			adminCommands(in);
		}
	}

	// Below this point are all the commands set up in a modular design to prevent
	// repetition
	// Attempts to open a new account
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
			in.nextLine();
			if (aServ.deposit(a, d)) {
				System.out.println("You deposited: $" + d + "\n");
				System.out.println("--------------------");
				System.out.println(a.toString());
				System.out.println("--------------------");
				System.out.println(
						"Your account is now pending approval by an employee.\nWhile the account is pending you may not perform transactions.");
			}

		} else {
			log.warn("Failed to create account! Please try again");
			createAccount(in);
		}

		evaluatePermissionContent(in);

	}

	// Prints out list of all bank accounts owned by a user
	static void viewUsersAccounts(Scanner in, User u) {
		List<Account> list = aServ.findByUser(u.getId());
		for (Account a : list) {
			System.out.println("--------------------");
			System.out.println(a.toString());
			System.out.println("--------------------");
		}
		evaluatePermissionContent(in);

	}

	// Prints out a single account by it's ID
	static void viewAccount(Scanner in, int accountID) {
		try {
			Account a = aServ.findAccount(accountID);

			System.out.println("--------------------");
			System.out.println(a.toString());
			System.out.println("--------------------");
			evaluatePermissionContent(in);
		} catch (NullPointerException e) {
			log.warn("Could not find account", e);
			evaluatePermissionContent(in);

		}

	}

	// Prints out a user by ID and provides method to update user's personal
	// information
	static void viewUser(Scanner in, int userId) {
		try {
			User u = userServ.findUser(userId);
			System.out.println("--------------------");
			System.out.println(u.toString());
			System.out.println("--------------------");
			// Check to see if logged user can update information
			if (loggedInUser.getPermissionLevel().equalsIgnoreCase("customer") && loggedInUser.getId() == userId
					|| loggedInUser.getPermissionLevel().equalsIgnoreCase("admin")) {
				System.out.println("Would you like to update any information? [Y/N]");
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

					if (userServ.update(u)) { // Attempt to update information
						System.out.println("Information updated!");
						System.out.println("--------------------");
						System.out.println(u);
						System.out.println("--------------------");
						evaluatePermissionContent(in);

					} else {
						System.out.println("There was an issue updating your information. Please try again");
						viewUser(in, userId);
					}
				} else {
					evaluatePermissionContent(in);

				}
			} else {
				evaluatePermissionContent(in);
			}

		} catch (NullPointerException e) {
			log.warn("Could not find User", e);
			evaluatePermissionContent(in);

		}

	}

	// This helps redirect a user back to their correct set of commands.

	static void confirmPendingAccounts(Scanner in) {
		if (loggedInUser.getPermissionLevel().equals("customer")) {
			// Security checkpoint
			System.out.println("You do not have permission to do this! Please contact your system administrator");
			evaluatePermissionContent(in);
			return;
		}
		List<Account> list = aServ.findByStatus("Pending");
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
				System.out.println("Account status updated!\nWould you like to confirm another account? [Y/N]");
				String s = in.nextLine();
				if (s.equalsIgnoreCase("n")) {
					break;
				}

			}

		}
		evaluatePermissionContent(in);

	}

	static void performTransactions(Scanner in) {

		if (loggedInUser.getPermissionLevel().equals("employee")) {
			// security checkpoint
			System.out.println("You do not have permission to do this! Please contact your system administrator");
			evaluatePermissionContent(in);
			return;
		}
		System.out.println(
				"What kind of transaction would you like to perform?\n1: Deposit\n2: Withdraw\n3: Transfer\n4:Back to menu\n");
		String s = in.nextLine();
		if (s.equalsIgnoreCase("1")) {
			deposit(in);
		} else if (s.equalsIgnoreCase("2")) {
			withdraw(in);
		} else if (s.equalsIgnoreCase("3")) {
			transfer(in);
		} else if (s.equalsIgnoreCase("4")) {
			evaluatePermissionContent(in);
		}

	}

	static void deposit(Scanner in) {
		System.out.println("Please enter the account # you wish to deposit to. Type 0 to return to transaction menu");
		int id = in.nextInt();
		if (id == 0) {
			performTransactions(in);
		}
		else {
			Account a = null;

			try {
				a = aServ.findAccount(id);
				if (a.getAccountStatus().equalsIgnoreCase("open")) {
					// Make sure user can use this account
					if (a.getUser_id() == loggedInUser.getId()
							|| loggedInUser.getPermissionLevel().equalsIgnoreCase("admin")) {
						//display balance
						System.out.println("Current balance of Account #" + a.getId() + " is " + a.getBalance());
						//get deposit amount
						System.out.println("How much would you like to deposit?\n");
						double d = in.nextDouble();
						in.nextLine();
						if (d < 0) {
							System.out.println("Invalid amount");
							performTransactions(in);
						} else {
							System.out.println("Confirm deposit of $" + d + " to account #" + a.getId() + " [Y/N]");
							String s = in.nextLine();
							if (s.equalsIgnoreCase("y")) {
								if (aServ.deposit(a, d)) {
									System.out.println("Deposited $" + d + " into account #" + a.getId() + "\nNew Balance: "
											+ a.getBalance());
									performTransactions(in);
								} else {
									System.out.println("Failed to perform deposit. Please contact our support #");
								}
							} else {
								System.out.println("Transaction cancelled! Returning to transactions menu.");
								performTransactions(in);
							}
						}

					} else {
						System.out.println("You do not have permission to use this account!");
						deposit(in);

					}
				} else {
					System.out.println("This account is currently " + a.getAccountStatus()
							+ "\nYou cannot perform transactions at this time.");
					deposit(in);
				}
			} catch (NullPointerException e) {
				log.warn("Could not find account", e);
				performTransactions(in);

			}

		}
		
	}

	static void withdraw(Scanner in) {
		System.out
				.println("Please enter the account # you wish to withdraw from. Type 0 to return to transaction menu");
		int id = in.nextInt();
		in.nextLine();
		if (id == 0) {
			performTransactions(in);
		}
		else {
			Account a = null;
			try {
				a = aServ.findAccount(id);
				if (a.getAccountStatus().equalsIgnoreCase("open")) {
					// Make sure user can use this account
					if (a.getUser_id() == loggedInUser.getId()
							|| loggedInUser.getPermissionLevel().equalsIgnoreCase("admin")) {
						System.out.println("Current balance of Account #" + a.getId() + " is " + a.getBalance());
						System.out.println("How much would you like to withdraw?\n");
						double d = in.nextDouble();
						in.nextLine();
						if (d < 0) {
							System.out.println("Invalid transaction");
							performTransactions(in);
						} else {
							System.out.println("Confirm withdrawl of $" + d + " from account #" + a.getId() + " [Y/N]");
							String s = in.nextLine();
							if (s.equalsIgnoreCase("y")) {
								if (aServ.withdraw(a, d)) {
									System.out.println("Withdrew $" + d + " from account #" + a.getId() + "\nNew Balance: "
											+ a.getBalance());
									performTransactions(in);
								} else {
									System.out.println("Failed to perform withdraw. Please contact our support #");
									performTransactions(in);
								}
							} else {
								System.out.println("Transaction cancelled! Returning to transactions menu.");
								performTransactions(in);
							}
						}

					} else {
						System.out.println("You do not have permission to use this account!");
						withdraw(in);

					}
				} else {
					System.out.println("This account is currently " + a.getAccountStatus()
							+ "\nYou cannot perform transactions at this time.");
					withdraw(in);
				}
			} catch (NullPointerException e) {
				log.warn("Could not find account", e);
				performTransactions(in);

			}

		}
		
	}

	static void transfer(Scanner in) {
		System.out
				.println("Please enter the account # you wish to transfer from. Type 0 to return to transaction menu");
		int id = in.nextInt();
		if (id == 0) {
			performTransactions(in);
			
		}
		else {
			System.out.println("Please enter the account # you with to transfer to.");
			int id2 = in.nextInt();

			try {
				Account a = aServ.findAccount(id);
				Account b = aServ.findAccount(id2);
				if (a.getAccountStatus().equalsIgnoreCase("open") && b.getAccountStatus().equalsIgnoreCase("open")) {
					// Make sure user can use this account
					if (a.getUser_id() == loggedInUser.getId()
							|| loggedInUser.getPermissionLevel().equalsIgnoreCase("admin")) {
						System.out.println("Current balance of Account #" + a.getId() + " is " + a.getBalance());
						System.out.println("How much would you like to transfer?");
						double d = in.nextDouble();
						in.nextLine();
						if(d <0)
						{
							System.out.println("Invalid amount");
							performTransactions(in);
						}
						else {
							System.out.println("Confirm tranfer of $" + d + " from account #" + a.getId() + " to account #"
									+ b.getId() + " [Y/N]");
							String s = in.nextLine();
							if (s.equalsIgnoreCase("y")) {
								if (aServ.transfer(a, b, d)) {
									System.out.println("Transfer complete!");
									performTransactions(in);
								} else {
									System.out.println(
											"Transfer failed, please try again or if the issue persists contact our support #");
								}
							}
						}
						

					} else {
						System.out.println("You do not have permission to use this account!");
						deposit(in);

					}
				} else {
					System.out.println("This account is currently " + a.getAccountStatus()
							+ "\nYou cannot perform transactions at this time.");
					deposit(in);
				}
			} catch (NullPointerException e) {
				log.warn("Could not find account", e);
				performTransactions(in);

			}

		}
		
	}

	static void cancelAccount(Scanner in) {
		if (loggedInUser.getPermissionLevel().equalsIgnoreCase("admin")) {
			System.out.println("Please enter the account # of the account you would like to cancel");
			int id = in.nextInt();
			in.nextLine();
			Account a = aServ.findAccount(id);
			a.setAccountStatus("cancelled");
			if (aServ.update(a)) {
				System.out.println("Account terminated successfully!");
				evaluatePermissionContent(in);
			}
		} else {
			System.out.println("You do not have permission to do this!");
			evaluatePermissionContent(in);
		}
	}
}
