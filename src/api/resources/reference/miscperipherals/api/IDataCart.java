package miscperipherals.api;

/**
 * Allows minecarts to be interacted with from the Rail Reader.
 * 
 * @author Richard
 */
public interface IDataCart {
	/**
	 * Sets the minecart's data.
	 * 
	 * @param data Data
	 */
	public void setData(Object[] data) throws Throwable;
	
	/**
	 * Returns the minecart's data.
	 * 
	 * @return Data
	 */
	public Object[] getData();
}
