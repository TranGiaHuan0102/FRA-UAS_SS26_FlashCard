package com.frauas.huankiet.database.connection;

public class FailedConnectionException extends Exception{
	private final String dbName;

	public FailedConnectionException(String dbName){
		this.dbName = dbName;
	}

	public String getMessage(){
		return "Unable to connect to database: " + this.dbName;
	}	
}
