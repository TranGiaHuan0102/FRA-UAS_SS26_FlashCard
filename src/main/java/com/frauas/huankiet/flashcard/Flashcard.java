package com.frauas.huankiet.flashcard;
import com.frauas.huankiet.database.DatabaseInitialization;

public class Flashcard {
	public static void main(String args[]) {
		// Initialize database on first run
		DatabaseInitialization.initializeDatabase();
	}
}
