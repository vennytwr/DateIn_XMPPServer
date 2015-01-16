package payload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import database.DatabaseConnection;
import server.GcmServer;
import server.XmppMessage;

public class LoginProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(XmppMessage msg) {
		String regId = msg.getPayload().get(Constants.REGISTRATION_ID);
		String email = msg.getPayload().get("email");
		String password = msg.getPayload().get("password");
		
		try {
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE email = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			
			// If email address exists..
			if(rs.first()) {
				String pwd = rs.getString("password");
				// If password is matched..
				if(password.equals(pwd)) {
					sendMessage(regId, Constants.ACTION_LOGIN_OK);
					System.out.println("OK!");
					return;
				}
			}
			
			// If either the email or password is wrong..
			sendMessage(regId, Constants.ACTION_LOGIN_FAIL);
			System.out.println("ERROR: Incorrect email or password.");
			
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
