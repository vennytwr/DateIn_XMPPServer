package payload;

import java.util.HashMap;
import java.util.Map;

import server.GcmServer;
import server.XmppMessage;

public class EchoProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(XmppMessage msg) {
		String regId = msg.getPayload().get("register_id");
		GcmServer gcm = GcmServer.getInstance();
    	String messageId = gcm.getMessageId();
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("testing", "OKAY");
        Long timeToLive = GcmServer.GCM_DEFAULT_TTL;
        Boolean delayWhileIdle = true;
        gcm.send(GcmServer.createJsonMessage(regId, messageId, payload, null,
                timeToLive, delayWhileIdle));
		
	}

}
