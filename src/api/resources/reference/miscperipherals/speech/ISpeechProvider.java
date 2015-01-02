package miscperipherals.speech;

import java.io.File;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpeechProvider {
	public String getName();
	
	public int getPriority();
	
	public boolean canUse(String text, double x, double y, double z, double speed);
	
	public File speak(String text, double speed);
}
