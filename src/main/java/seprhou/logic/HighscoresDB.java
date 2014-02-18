package seprhou.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HighscoresDB {

	private static HighscoresDB instance;
	private Connection connection = null;

	private HighscoresDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:highscores");

			Statement stmt = this.connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `highscores` (	`id` integer primary key,	`name` VARCHAR(50),	`time` integer,	`score` integer, t TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	public static Connection getConnection() {
		if (HighscoresDB.instance == null) {
			HighscoresDB.instance = new HighscoresDB();
		}
		return HighscoresDB.instance.connection;
	}

}
