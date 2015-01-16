package server;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import database.DatabaseConnection;

public class Start {

	private static final String PROJECT_ID = "994318371798";
	private static final String PASSWORD = "AIzaSyBTmnl03o7jqdE40N8df4cMCsoWCNtBoRM";

	private static final String QUIT = "QUIT";
	private static final String QUIT1 = "Q";

	public static void main(String args[]) {
		GcmServer xmppServer = GcmServer.prepareServer(PROJECT_ID, PASSWORD, true);
		
		try {
			System.out.println("Connecting to database..");
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0");
			ps.executeUpdate();
			ps.close();
			System.out.println("Database Connection Established."); // Good connection;
		} catch (SQLException ex) {
			throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.");
		}

		try {
			System.out.println("Connecting to GCM..");
			xmppServer.connect();
		} catch (XMPPException | SmackException | IOException e) {
			System.out.println("ERROR: " + e);
		}

//		// Space between information and input
//		System.out.println();
//		Scanner sc = new Scanner(System.in);
//		while(true) {
//			System.out.print(" > ");
//			String command = sc.next().toUpperCase();
//			switch(command) {
//			case QUIT:
//			case QUIT1:
//				sc.close();
//				xmppServer.disconnect();
//				return;
//			}
//		}
	}
}
