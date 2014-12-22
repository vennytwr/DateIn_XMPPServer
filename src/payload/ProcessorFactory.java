package payload;

public class ProcessorFactory {
	
	private static final String PACKAGE = "com.datein.date_in.gcm";
    private static final String ACTION_REGISTER = PACKAGE + ".REGISTER";
    private static final String ACTION_ECHO = PACKAGE + ".ECHO";
    private static final String ACTION_MESSAGE = PACKAGE + ".MESSAGE";

    public static PayloadProcessor getProcessor(String action) {
        if (action == null)
            throw new IllegalStateException("action must not be null");
        if (action.equals(ACTION_REGISTER)) {
        	System.out.println("Registering..");
            return new RegisterProcessor();
        }
        else if (action.equals(ACTION_ECHO))
        	System.out.println("ECHO!");
            //return new EchoProcessor();
        else if (action.equals(ACTION_MESSAGE))
        	System.out.println("MESSAGE!");
            //return new MessageProcessor();
        throw new IllegalStateException("Action " + action + " is unknown");
    }

}