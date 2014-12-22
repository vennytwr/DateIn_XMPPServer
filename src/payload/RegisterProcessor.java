package payload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import server.XmppMessage;
import server.GcmServer;
import database.DatabaseConnection;

public class RegisterProcessor implements PayloadProcessor {
	
	private static final String ACTION_EMAIL_TAKEN = "ACTION_EMAIL_TAKEN";

	@Override
	public void handleMessage(XmppMessage msg) {
//		String username = msg.getPayload().get("username");
//		String password = msg.getPayload().get("password");
//		String email = msg.getPayload().get("email");
		String regId = msg.getPayload().get("register_id");
		GcmServer gcm = GcmServer.getInstance();
		
//		// If the query is successful, email existed;
//		String messageId = gcm.getMessageId();
//        Map<String, String> payload = new HashMap<String, String>();
//        payload.put("action", ACTION_EMAIL_TAKEN);
//        Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
//        Boolean delayWhileIdle = true;
//        gcm.send(GcmServer.createJsonMessage(regId, messageId, payload, "sample",
//                timeToLive, delayWhileIdle));
//        System.out.println("Email taken.");
		
//		System.out.println("Checking email address.. (" + email + ")");
//		try {
//			Connection con = DatabaseConnection.getConnection();
//			PreparedStatement ps = con.prepareStatement("SELECT email FROM accounts WHERE email = ?");
//			ps.setString(1, email);
//			ResultSet rs = ps.executeQuery();
//			if(rs.first()) {
//				// If the query is successful, email existed;
//				String messageId = gcm.getMessageId();
//		        Map<String, String> payload = new HashMap<String, String>();
//		        payload.put("action", ACTION_EMAIL_TAKEN);
//		        Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
//		        Boolean delayWhileIdle = true;
//		        gcm.send(GcmServer.createJsonMessage(regId, messageId, payload, null,
//		                timeToLive, delayWhileIdle));
//		        System.out.println("Email taken.");
//			}
//		} catch(SQLException e) {
//			System.err.println(new StringBuilder().append("ERROR: ").append(e.toString()));
//		}
	}

}
