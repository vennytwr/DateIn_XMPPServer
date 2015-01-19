package payload;

public class ProcessorFactory {

	public static PayloadProcessor getProcessor(String action) {
		if (action == null)
			throw new IllegalStateException("Action must not be null");

		if (action.equals(Constants.ACTION_LOGIN)) {
			System.out.print("Handling login packet..");
			return new LoginProcessor();
		}

		if (action.equals(Constants.ACTION_REGISTER)) {
			System.out.print("Handling register packet..");
			return new RegisterProcessor();
		}

		if (action.equals(Constants.ACTION_SEARCH)) {
			System.out.print("Handling search packet..");
			return new SearchProcessor();
		}

		if (action.equals(Constants.ACTION_FRIEND)) {
			System.out.print("Handling friend packet..");
			return new FriendProcessor();
		}

		if (action.equals(Constants.ACTION_ADD_FRIEND)) {
			System.out.print("Handling friend request packet..");
			return new FriendRequestProcessor();
		}

		throw new IllegalStateException("Action " + action + " is unknown");
	}

}