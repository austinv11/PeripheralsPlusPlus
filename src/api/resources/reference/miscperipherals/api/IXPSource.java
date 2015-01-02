package miscperipherals.api;

import java.util.ArrayList;
import java.util.List;

import dan200.turtle.api.ITurtleAccess;

/**
 * Used by the XP turtle upgrade to determine XP sources for the get() functions.
 * 
 * @author Richard
 */
public interface IXPSource {
	/**
	 * Handler list - add your handlers here.
	 */
	public static final List<IXPSource> handlers = new ArrayList<IXPSource>();
	
	/**
	 * Take XP from the given block.
	 * 
	 * @param turtle Turtle taking XP
	 * @param x Block X
	 * @param y Block Y
	 * @param z Block Z
	 * @param side Block side
	 * @return Amount of XP taken, or 0 if none
	 */
	public int get(ITurtleAccess turtle, int x, int y, int z, int side);
}
