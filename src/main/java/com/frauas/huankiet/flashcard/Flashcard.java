package com.frauas.huankiet.flashcard;
import com.frauas.huankiet.database.setup.DBSetup;
import java.sql.*;

public class Flashcard {
	public static void main(String args[]) {
		Connection flashcard = DBSetup.setup();
	}
}
