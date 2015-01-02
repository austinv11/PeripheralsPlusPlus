package miscperipherals.speech;

import java.util.Comparator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PriorityComparator implements Comparator<ISpeechProvider> {
	@Override
	public int compare(ISpeechProvider arg0, ISpeechProvider arg1) {
		return arg0.getPriority() < arg1.getPriority() ? 1 : (arg0.getPriority() == arg1.getPriority() ? arg0.getName().compareToIgnoreCase(arg1.getName()) : -1);
	}
}
