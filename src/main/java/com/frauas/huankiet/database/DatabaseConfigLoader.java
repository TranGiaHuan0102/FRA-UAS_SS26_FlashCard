package com.frauas.huankiet.database;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfigLoader implements DatabaseConfig{
	private final Dotenv dotenv = Dotenv.load();
	
	@Override	
	public String getURL(){return dotenv.get("DB_URL");}
	@Override	
	public String getUsername(){return dotenv.get("DB_USERNAME");}
	@Override	
	public String getPassword(){return dotenv.get("DB_PASSWORD");}
}
