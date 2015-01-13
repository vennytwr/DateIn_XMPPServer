package payload;

public interface Constants {

	// Receive.
	String PACKAGE = "com.datein.date_in.gcm";
	String ACTION_REGISTER = PACKAGE + ".REGISTER";

	// Send.
	String ACTION_REGISTER_OK = PACKAGE + ".REGISTER_OK";
	String ACTION_REGISTER_EMAIL_TAKEN = PACKAGE + ".REGISTER_EMAIL_TAKEN";

}
