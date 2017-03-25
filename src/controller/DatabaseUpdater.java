package controller;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import model.BookingModel;

public class DatabaseUpdater {
	private Connection connection = null;
	private String URL = "jdbc:postgresql://kuldbinstance.czsqr6wtrtap.us-west-2.rds.amazonaws.com:5432/kuldb";
	private String USER = "kuluser";
	private String PASS = "kulpassword";
	
	public DatabaseUpdater(){
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");

		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");


		
		try {

			connection = DriverManager.getConnection(URL, USER, PASS);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	
	public static void main(String[] argv) {
	
	}
	//Function that adds a booking to the table BOOKINGS
	public void insertBooking(BookingModel booking){
		String insertTableSQL = "INSERT INTO BOOKING"
				+ "(tripId, bookerEmail, numPeople, bookerSSN) VALUES"
				+ "(?,?,?,?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, booking.getTripId());
			preparedStatement.setString(2, booking.getBookerEmail());
			preparedStatement.setInt(3, booking.getNumPeople());
			preparedStatement.setInt(4, booking.getBookerSSN());
			//execute insert SQL statement
			preparedStatement .executeUpdate();
			System.out.print("Awwww yeeeeaaaaaaaah");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createTables(){
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
		      
			String sql0 = "DROP TABLE BOOKING;";
			
			stmt.executeUpdate(sql0);
			
		      /*String sql1 = "CREATE TABLE TRIP " +
		                   	"(tripId VARCHAR(256) NOT NULL, " +
		                   	" tripName VARCHAR(256) NOT NUll, " +
		                   	" dateBegin DATE NOT NULL, " +
		                   	" dateEnd DATE NOT NULL, " +
		                   	" description VARCHAR(2000), " +
		                   	" maxPeople INT NOT NULL, " +
		                   	" minPeople INT NOT NULL, " +
		                   	" location VARCHAR(256) NOT NULL, " +
		                   	" price INT NOT NULL)";
		                   
		      stmt.executeUpdate(sql1);
		      
		      String sql2 = "CREATE TABLE ADMIN " +
	                   		"(adminId VARCHAR(256) NOT NULL, " +
	                   		" adminPassword VARCHAR(256) NOT NUll)";
		      
		      stmt.executeUpdate(sql2);*/
		      
		      String sql3 = "CREATE TABLE BOOKING " +
		    		  		"(bookingId VARCHAR(256) SERIAL PRIMARY KEY, " +
		    		  		" tripId VARCHAR(256) NOT NUll, " +
		    		  		"bookerEmail VARCHAR(256) NOT NULL, " + 
		    		  		"numPeople INT NOT NULL, " +
		    		  		"bookerSSN INT NOT NULL);";
		      
		      stmt.executeUpdate(sql3);
		      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		}
	}
}
