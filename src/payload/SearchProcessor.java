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

public class SearchProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(XmppMessage msg) {
		String regId = msg.getPayload().get(Constants.REGISTRATION_ID);
		String searchString = msg.getPayload().get("search");

		try {
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM accounts WHERE displayName = ? OR registrationId = ?");
			ps.setString(1, searchString);
			ps.setString(2, regId);
			ResultSet rs = ps.executeQuery();

			if(rs.first()) {
				String displayName = rs.getString("displayName");
				String regIdFromDB = rs.getString("registrationId");
				if(displayName.equals(searchString) && !regIdFromDB.equals(regId)) {
					System.out.println("OK!");
					sendMessage(regId, Constants.ACTION_SEARCH_OK, displayName);
					return;
				}
			}
			
			System.out.println("No match found!");
			sendMessage(regId, Constants.ACTION_SEARCH_FAIL, null);

			ps.close();
			rs.close();
		} catch(SQLException e) {
			System.err.println(new StringBuilder().append("ERROR: ").append(e.toString()));
		}

	}

	private void sendMessage(String to, String action, String displayName) {
		GcmServer gcm = GcmServer.getInstance();
		String messageId = gcm.getMessageId();
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("action", action);
		payload.put("displayName", displayName);
		Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
		Boolean delayWhileIdle = true;
		gcm.send(GcmServer.createJsonMessage(to, messageId, payload, null,
				timeToLive, delayWhileIdle));
	}

}
