package miscperipherals.api;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Callback interface for registering upgrade-related Icons.
 * 
 * Icons registered through this interface apply to the block namespace.
 * 
 * @author Richard
 */
public interface IUpgradeIcons {
	/**
	 * Similarly to Block and Item, called when the TextureMap is ready to receive your icons.
	 * 
	 * @param reg Icon registry
	 */
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg);
}
