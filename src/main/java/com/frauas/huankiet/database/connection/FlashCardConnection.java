package com.frauas.huankiet.database.connection;
import java.sql.*;

public class FlashCardConnection implements ConnectionManager{
	@Override	
	public Connection returnConnection() throws FailedConnectionException{
		try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);){
			return conn;	
		}
		catch (SQLException e){throw new FailedConnectionException("FlashCard");}
	}	
}
