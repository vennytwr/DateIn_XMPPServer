package payload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import server.GcmServer;
import server.XmppMessage;
import database.DatabaseConnection;

public class FriendProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(XmppMessage msg) {
		String regId = msg.getPayload().get(Constants.REGISTRATION_ID);
		String fromDisplayName;
		ArrayList<String> friendList = new ArrayList<String>();
		ArrayList<String> requestList = new ArrayList<String>();
		ArrayList<String> pendingList = new ArrayList<String>();
		ArrayList<String> ignoreList = new ArrayList<String>();

		try {
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE registrationId = ?");
			ps.setString(1, regId);
			ResultSet rs = ps.executeQuery();

			if(rs.first()) {
				fromDisplayName = rs.getString("displayName");
				
				// Get all friends..
				ps = con.prepareStatement("SELECT * FROM friend_list WHERE friendAlpha = ?");
				ps.setString(1, fromDisplayName);
				rs = ps.executeQuery();
				while(rs.next())
					friendList.add(rs.getString("friendBeta"));
				
				// Get all request..
				ps = con.prepareStatement("SELECT * FROM request_list WHERE friendAlpha = ?");
				ps.setString(1, fromDisplayName);
				rs = ps.executeQuery();
				while(rs.next())
					requestList.add(rs.getString("friendBeta"));
				
				// Get all pending..
				ps = con.prepareStatement("SELECT * FROM pending_list WHERE friendBeta = ?");
				ps.setString(1, fromDisplayName);
				rs = ps.executeQuery();
				while(rs.next())
					requestList.add(rs.getString("friendAlpha"));
				
				// Get all ignore..
				ps = con.prepareStatement("SELECT * FROM ignore_list WHERE friendBeta = ?");
				ps.setString(1, fromDisplayName);
				rs = ps.executeQuery();
				while(rs.next())
					requestList.add(rs.getString("friendAlpha"));
				
				System.out.println("OK!");
				sendMessage(regId, Constants.ACTION_LIST_OK, friendList, requestList, pendingList, ignoreList);
				return;				
			}

			System.out.println("FAIL!");
			sendMessage(regId, Constants.ACTION_LIST_FAIL, null, null, null, null);

			ps.close();
			rs.close();
		} catch(SQLException e) {
			System.err.println(new StringBuilder().append("ERROR: ").append(e.toString()));
		}
	}

	private void sendMessage(String to, String action, 
			ArrayList<String> friendList, ArrayList<String> requestList,
			ArrayList<String> pendingList, ArrayList<String> ignoreList) {
		GcmServer gcm = GcmServer.getInstance();
		String messageId = gcm.getMessageId();
		Map<String, String> payload = new HashMap<String, String>();
		Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
		Boolean delayWhileIdle = true;
		payload.put("action", action);

		if(friendList != null) {
			payload.put("friendListIndex", String.valueOf(friendList.size()));
			payload.put("requestListIndex", String.valueOf(requestList.size()));
			payload.put("pendingListIndex", String.valueOf(pendingList.size()));
			payload.put("ignoreListIndex", String.valueOf(ignoreList.size()));
			int i = 0, j = 0;
			for(; j < friendList.size(); i++, j++)
				payload.put(String.valueOf(i), friendList.get(j));
			for(j = 0; j < requestList.size(); i++, j++)
				payload.put(String.valueOf(i), requestList.get(j));
			for(j = 0; j < pendingList.size(); i++, j++)
				payload.put(String.valueOf(i), pendingList.get(j));
			for(j = 0; j < ignoreList.size(); i++, j++)
				payload.put(String.valueOf(i), ignoreList.get(j));
		}

		gcm.send(GcmServer.createJsonMessage(to, messageId, payload, null,
				timeToLive, delayWhileIdle));
	}
}
