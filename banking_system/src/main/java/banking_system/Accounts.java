package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {

	private Connection con;
	private Scanner sc;

	public Accounts(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}

	public long open_account(String email) {
	    if (!account_exist(email)) {
	        String open_acc_query = 
	            "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";

	        // Input full name
	        sc.nextLine() ;
	        System.out.print("Enter full_name: ");
	        String full_name = sc.nextLine().trim();

	        // Input initial balance
	        System.out.print("Enter initial Amount as Balance: ");
	        double balance = sc.nextDouble();
	        sc.nextLine(); // consume leftover newline

	        // Input security PIN
	        System.out.print("Enter security pin: ");
	        String security_pin = sc.nextLine().trim();

	        try {
	            long account_number = generateAccountNumber();  // generate next account number

	            PreparedStatement ps = con.prepareStatement(open_acc_query);
	            ps.setLong(1, account_number);
	            ps.setString(2, full_name);
	            ps.setString(3, email);
	            ps.setDouble(4, balance);
	            ps.setString(5, security_pin);

	            int rowsaffected = ps.executeUpdate();
	            if (rowsaffected > 0) {
	                System.out.println(" Account created successfully! ");
	                System.out.println("Your Account Number: " + account_number);
	                return account_number;
	            } else {
	                System.out.println("Account creation failed!");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        throw new RuntimeException("Account creation failed!");
	    }
	    throw new RuntimeException("Account Already exists");
	}


	public long generateAccountNumber() {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
			if (rs.next()) {
				long acc_no = rs.getLong("account_number");
				return acc_no+1;
			} else {
				return 10000100;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 10000100;
	}
	
	public long getAccountNumber(String email) {
		
		try {
			String query = "SELECT account_number FROM accounts where email = ?" ;
			PreparedStatement ps = con.prepareCall(query) ;
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getLong("account_number");
			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account does not Exist") ;
		
		
	}

	public boolean account_exist(String email) {
		String query = "SELECT * FROM accounts where email = ?";

		try {
			PreparedStatement ps = con.prepareCall(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return true;
			else
				return false;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

}
