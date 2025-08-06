package banking_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingAppDriver {

	private static final String url = "jdbc:postgresql://localhost:5432/banking_system";
	private static final String user = "postgres";
	private static final String pass = "root";

	public static void main(String[] args) {

		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Connection con = DriverManager.getConnection(url, user, pass);
			Scanner sc = new Scanner(System.in);

			UserDriver userDriver = new UserDriver(con, sc);
			Accounts accounts = new Accounts(con,sc) ;
			AccountManagerDriver amd = new AccountManagerDriver(con,sc) ;

			String email;
			long account_number;
	

				while (true) {
					System.out.println("***** Welcome to Banking Management System *****");
					System.out.println();
					System.out.println("1. Register");
					System.out.println("2. Login");
					System.out.println("3. Exit");
					System.out.println("Enter your choice");
					int choice = sc.nextInt();
					switch (choice) {
					case 1:
						userDriver.register();
						break;

					case 2:
						email = userDriver.login();

						if (email != null) {
							System.out.println();
							System.out.println("User logged In");

							if(!accounts.account_exist(email)) {
								System.out.println();
								System.out.println("1. Open new Account");
								System.out.println("2. Exit");
								System.out.println("Enter the choice");
								
								if(sc.nextInt() == 1) {
									account_number = accounts.open_account(email) ;
									System.out.println("Account created successfully");
									System.out.println("your account number is :"+account_number);
									
								}else {
									break;
								}
									
								
							}
							account_number = accounts.getAccountNumber(email);
							int choice2 = 0;
							
							while(choice2!=5) {
								System.out.println();
								System.out.println("1. Debit Money");
								System.out.println("2. Credit Money");
								System.out.println("3. Transfer Money");
								System.out.println("4. Check Balance");
								System.out.println("5. Logout");
								System.out.println("Enter your choice"); 
								choice2 = sc.nextInt() ;
								sc.nextLine() ;
								
								switch(choice2) {
								case 1: 
									amd.debit_money(account_number);
									break;
								case 2: 
									amd.credit_money(account_number);
									break;
								case 3: 
									amd.transfer_money(account_number);
									break;
								case 4: 
									amd.getbalnce(account_number);
									break;
								case 5:
									break;
								default: 
									System.out.println("Enter valid choice");
									break;
								
								}
							}
						}
						else{
							System.out.println("Invalid Email or Password");
						}

					case 3:
						System.out.println("Thank you for using Banking System !");
						System.out.println("Exiting System !");
						return;
					default: 
						System.out.println("Enter valid Choice !");
						break;
					}
				}
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
