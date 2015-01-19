package payload;

import server.XmppMessage;

public interface PayloadProcessor {
    
    void handleMessage(XmppMessage msg);
    
}
