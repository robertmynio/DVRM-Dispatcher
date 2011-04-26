package vdrm.pred.dbaccess;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static Connection conn = null;

	private static Connection createConnection(){
		Connection conn = null;
		
		try {
			Class.forName(
			"com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
		    conn = 
		       DriverManager.getConnection("jdbc:mysql://localhost:3306/dvrm","user","pass");

		    // Do something with the Connection

		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
	
	public static Connection getInstance(){
		if(conn==null) conn=createConnection();
		return conn;
	}
}
