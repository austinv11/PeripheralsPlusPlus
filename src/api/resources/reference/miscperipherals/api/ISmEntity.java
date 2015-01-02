package miscperipherals.api;

/**
 * Smallnet entity handler.
 * 
 * @author Richard
 */
public interface ISmEntity extends ISmNode {
	/**
	 * Receive data.
	 * 
	 * @param from Sender
	 * @param payload Payload received
	 */
	public void receive(ISmSender from, String type, String payload);
}
