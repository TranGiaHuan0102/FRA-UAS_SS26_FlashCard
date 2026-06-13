package com.frauas.huankiet.database.connection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public interface ConnectionManager {
	// Configurations	
	Dotenv dotenv = Dotenv.load();
	final String POSTGRES_URL = dotenv.get("POSTGRES_URL");
	final String DB_URL = dotenv.get("DB_URL");
	final String USERNAME = dotenv.get("DB_USERNAME");
	final String PASSWORD = dotenv.get("DB_PASSWORD");

	public Connection returnConnection() throws FailedConnectionException;
}
