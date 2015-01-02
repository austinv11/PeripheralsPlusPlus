package miscperipherals.api;


/**
 * Factory for {@link ISmEntity} objects.
 * 
 * @author Richard
 */
public interface ISmEntityFactory<T> {
	/**
	 * Gets a ISmEntity out of the given entity.
	 * 
	 * @param entity Entity
	 * @return {@link ISmEntity} object
	 */
	public ISmEntity getEntity(T entity);
}
