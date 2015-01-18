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

public class FriendRequestProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(XmppMessage msg) {
		String regId = msg.getPayload().get(Constants.REGISTRATION_ID);
		String requestTo = msg.getPayload().get("requestTo");
		String requestFrom = null;
		
		try {
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE registrationId = ?");
			ps.setString(1, regId);
			ResultSet rs = ps.executeQuery();

			if(rs.first()) {
				requestFrom = rs.getString("displayName");
				ps = con.prepareStatement("INSERT into request_list (friendAlpha, friendBeta) VALUES (?, ?)");
				ps.setString(1, requestFrom);
				ps.setString(2, requestTo);
				ps.executeUpdate();
				
				ps = con.prepareStatement("INSERT into pending_list (friendAlpha, friendBeta) VALUES (?, ?)");
				ps.setString(1, requestFrom);
				ps.setString(2, requestTo);
				ps.executeUpdate();
				
				System.out.println("OK!");
				sendMessage(regId, Constants.ACTION_ADD_OK);
				return;
			}
			
			System.out.println("FAIL!");
			sendMessage(regId, Constants.ACTION_ADD_FAIL);

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
