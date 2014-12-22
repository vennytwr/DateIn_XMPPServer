package server;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import database.DatabaseConnection;

public class Start {

	private static final String PROJECT_ID = "994318371798";
	private static final String PASSWORD = "AIzaSyBTmnl03o7jqdE40N8df4cMCsoWCNtBoRM";

	private static final String QUIT = "QUIT";
	private static final String QUIT1 = "Q";
static String a = "APA91bF_A_7OqphHoxp0eOHPwvxh3wmtkvLvNhWzMS7xaFXKkU4um0Hj0GNWskxVAKe34VsoBBIK-1cYNZa1QIsRfKMFPCWkCSK7EbR7rrkiA04Kkbr3BPR8wy_DRTVqPwsYHkVImni8c9Bgw6VfDznCxeT7jJ7YhQ";
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
			System.out.println("ERROR: e");
		}

		// Space between information and input
		System.out.println();
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print(" > ");
			String command = sc.next().toUpperCase();
			switch(command) {
			case "A":
				GcmServer gcm = GcmServer.getInstance();
				String messageId = gcm.getMessageId();
		        Map<String, String> payload = new HashMap<String, String>();
		        payload.put("action", "Testing");
		        Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
		        Boolean delayWhileIdle = true;
		        gcm.send(GcmServer.createJsonMessage(a, messageId, payload, "sample",
		                timeToLive, delayWhileIdle));
		        break;
			case QUIT:
			case QUIT1:
				sc.close();
				xmppServer.disconnect();
				return;
			}
		}
	}
}
