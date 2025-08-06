package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManagerDriver {

	private Connection con;
	private Scanner sc;

	public AccountManagerDriver(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}

	public void credit_money(long account_number) throws SQLException {
		System.out.print("Enter Amount: ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.print("Security Pin: ");
		String security_pin = sc.nextLine().trim();
		//sc.nextLine();

		try {
			con.setAutoCommit(false);

			if (account_number != 0) {
				PreparedStatement ps = con
						.prepareCall("SELECT * FROM accounts WHERE account_number = ? AND security_pin =?");
				ps.setLong(1, account_number);
				ps.setString(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ? ";
					PreparedStatement creditps = con.prepareStatement(credit_query);
					creditps.setDouble(1, amount);
					creditps.setLong(2, account_number);

					int rowsaffected = creditps.executeUpdate();
					if (rowsaffected > 0) {
						System.out.println("RS " + amount + " Credited successfully");
						con.commit();
						con.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction failed ");
						con.rollback();
						con.setAutoCommit(true);
					}

				} else {
					System.out.println("Invalid security PIN");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException{
		
		System.out.print("Enter the amount:");
		double amount = sc.nextDouble() ;
		sc.nextLine() ;
		System.out.print("Enter Security PIN: ");
		String security_pin = sc.nextLine() ;
		
		try {
			con.setAutoCommit(false);
			if(account_number != 0) {
				PreparedStatement ps = con.prepareCall("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?") ;
				ps.setLong(1, account_number);
				ps.setString(2, security_pin);
				ResultSet rs = ps.executeQuery() ;
				
				if(rs.next()) {
					double current_balance = rs.getDouble("balance") ;
					
					if(amount <= current_balance) {
						String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement debitps = con.prepareCall(debit_query) ;
						debitps.setDouble(1, amount);
						debitps.setLong(2, account_number);
						
						int rowsaffected = debitps.executeUpdate() ;
						if(rowsaffected > 0 ) {
							System.out.println("Rs "+amount+" debited successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						}
						else {
							System.out.println("Transaction failed!");
							con.rollback();
							con.setAutoCommit(true);
						}
					}
					else {
						System.out.println("Insufficient balance");
					}
					
				}
				else {
					System.out.println("Invalid PIN");
				}
				
				
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	
	public void transfer_money(long sender_account_number) throws SQLException {
		
		System.out.print("Enter receiver account Number: ");
		long receiver_account_number = sc.nextLong() ;
	//	sc.nextLine() ;
		
		System.out.println("Enter amount to transfer: ");
		double amount = sc.nextDouble() ;
		sc.nextLine();
		System.out.print("Enter security PIN: ");
		String security_pin = sc.nextLine().trim();
		
		try {
			con.setAutoCommit(false);
			if(sender_account_number != 0 && receiver_account_number !=0) {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?") ;
				ps.setLong(1, sender_account_number);
				ps.setString(2, security_pin);
				ResultSet rs = ps.executeQuery() ;
				if(rs.next()) {
					double current_balance = rs.getDouble("balance") ;
					
					if(amount <= current_balance) {
						
						String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?" ;
						String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?" ;
						
						PreparedStatement debitps = con.prepareStatement(debit_query) ;
						PreparedStatement creditps = con.prepareStatement(credit_query) ;
						debitps.setDouble(1, amount);
						debitps.setLong(2, sender_account_number);
						
						creditps.setDouble(1, amount);
						creditps.setLong(2, receiver_account_number);
						
						int rowsaffected1 = debitps.executeUpdate() ;
						int rowsaffected2 = creditps.executeUpdate() ;
						
						if(rowsaffected1 > 0 && rowsaffected2 > 0 ) {
							System.out.println("Transaction successfull");
							System.out.println("Rs "+ amount +" Transferred Successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						}
						else {
							System.out.println("Transaction failed!");
							con.rollback();
							con.setAutoCommit(true);
						}
						
					}else {
						System.out.println("Insufficient balance");
					}
				}
				else
				{
					System.out.println("Invalid security PIN");
				}
				
			}
			else {
				System.out.println("Invalid account number");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	public void getbalnce(long account_number) {
		System.out.print("Enter security PIN: ");
		String security_pin = sc.nextLine().trim() ;
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? and security_pin = ?");
			ps.setLong(1, account_number);
			ps.setString(2, security_pin);
			
			ResultSet rs = ps.executeQuery() ;
			
			if(rs.next()) {
				double balance = rs.getDouble("balance") ;
				System.out.println("Balance: "+balance);
			}
			else {
				System.out.println("Invalid PIN!");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
