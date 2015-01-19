package payload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import server.GcmServer;
import server.XmppMessage;
import database.DatabaseConnection;

public class RegisterProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(XmppMessage msg) {
		String regId = msg.getPayload().get(Constants.REGISTRATION_ID);
		String displayName = msg.getPayload().get("displayName");
		String email = msg.getPayload().get("email");
		String password = msg.getPayload().get("password");

		try {
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT email FROM accounts WHERE email = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			// If email does not exist, check for display name.
			if(rs.first()) {
				sendMessage(regId, Constants.ACTION_REGISTER_EMAIL_TAKEN);
				System.out.println("ERROR: Email address is already in used.");
			} else {
				ps = con.prepareStatement("SELECT displayName FROM accounts WHERE displayName = ?");
				ps.setString(1, displayName);
				rs = ps.executeQuery();

				// If display name does not exist, save account to database.
				if(rs.first()) {
					sendMessage(regId, Constants.ACTION_REGISTER_DISPLAY_NAME_TAKEN);
					System.out.println("ERROR: Display name is already in used.");
				} else {
					ps = con.prepareStatement("INSERT INTO accounts (registrationId, email, displayName, password) VALUES (?, ?, ?, ?)");
					ps.setString(1, regId);
					ps.setString(2, email);
					ps.setString(3, displayName);
					ps.setString(4, password);
					ps.executeUpdate();
					sendMessage(regId, Constants.ACTION_REGISTER_OK);
					System.out.println("OK!");
				}
			}
			
			ps.close();
			rs.close();
		} catch(SQLException e) {
			System.err.println(new StringBuilder().append("ERROR: ").append(e.toString()));
		}
	}

	private void sendMessage(String to, String action) {
		GcmServer gcm = GcmServer.getInstance();
		String messageId = gcm.getMessageId();
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("action", action);
		Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
		Boolean delayWhileIdle = true;
		gcm.send(GcmServer.createJsonMessage(to, messageId, payload, null,
				timeToLive, delayWhileIdle));
	}
}
