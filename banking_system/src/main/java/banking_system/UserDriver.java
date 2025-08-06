package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserDriver {

	private Connection con;
	private Scanner sc;

	public UserDriver(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}

	public void register() {
		sc.nextLine();
		System.out.println("Full Name:");
		String full_name = sc.nextLine();
		System.out.print("Email: ");
		String email = sc.nextLine();
		System.out.println("Password");
		String pass = sc.nextLine();

		if (user_exists(email)) {
			System.out.println("User already Exists for this email address !! ");
			return;
		}

		String register_query = "INSERT INTO users (full_name,email,password) VALUES (?,?,?)";

		try {
			PreparedStatement ps = con.prepareStatement(register_query);
			ps.setString(1, full_name);
			ps.setString(2, email);
			ps.setString(3, pass);

			int rowsaffected = ps.executeUpdate();
			if (rowsaffected > 0)
				System.out.println("User registration successfull!");
			else
				System.out.println("Registration failed !");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String login() {
		sc.nextLine();
		System.out.print("Email: ");
		String email = sc.nextLine();
		System.out.println("PassWord: ");
		String pass = sc.nextLine();

		String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";

		try {
			PreparedStatement ps = con.prepareStatement(login_query);
			ps.setString(1, email);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return email;
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean user_exists(String email) {

		String query = "SELECT * FROM users WHERE email = ?";
		try {
			PreparedStatement ps = con.prepareCall(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
