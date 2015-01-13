package payload;

public class ProcessorFactory {
	
	public static PayloadProcessor getProcessor(String action) {
		if (action == null)
			throw new IllegalStateException("Action must not be null");
		
		if (action.equals(Constants.ACTION_REGISTER)) {
			System.out.print("Registering a new account..");
			return new RegisterProcessor();
		}
		
		throw new IllegalStateException("Action " + action + " is unknown");
	}

}