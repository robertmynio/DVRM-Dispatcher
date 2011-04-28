package vdrm.pred.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static DBManager dbM;
	private DBManager(){
		databaseConnection = DBConnection.getInstance();
		openDataBase("vdrm");
	}
	
	public static DBManager getInstance(){
		if(dbM==null){
			dbM = new DBManager();
		}
		return dbM;
	}
	
	private Connection databaseConnection; // = DBConnection.getInstance();

	public void openDataBase(String database) {
		Statement statement = prepareSqlStatement();
		try {
			statement.executeUpdate("use " + database);
		} catch (SQLException e) {
			System.out.println("SQL Exception: open database");
			e.printStackTrace();
		}
	}

	public void closeDatabaseConnection() {
		if (databaseConnection != null) {
			try {
				databaseConnection.close();
				// databaseConnection.setClosed(true);
			} catch (SQLException e) {
				System.out.println("SQL Exception: end connection");
				e.printStackTrace();
			}
		}
	}

	public Statement prepareSqlStatement() {
		Statement statement = null;
		try {
			statement = databaseConnection.createStatement();
		} catch (SQLException e) {
			System.out.println("SQL exception: createStatement()");
			e.printStackTrace();
		}
		return statement;
	}

}
