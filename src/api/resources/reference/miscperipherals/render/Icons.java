package miscperipherals.render;

import java.util.ArrayList;
import java.util.List;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.api.IUpgradeToolIcons;
import net.minecraft.client.renderer.texture.TextureMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.turtle.api.ITurtleUpgrade;

public class Icons {
	private static List<ITurtleUpgrade> upgrades = new ArrayList<ITurtleUpgrade>();
	
	@SideOnly(Side.CLIENT)
	public static void addTextures(TextureMap reg) {
		boolean terrain = reg.textureName.equals("terrain");
		
		if (terrain) {
			for (ITurtleUpgrade upgrade : upgrades) {
				if (upgrade instanceof IUpgradeIcons) ((IUpgradeIcons) upgrade).registerIcons(reg);
			}
		} else {
			for (ITurtleUpgrade upgrade : upgrades) {
				if (upgrade instanceof IUpgradeToolIcons) ((IUpgradeToolIcons) upgrade).registerIcons(reg);
			}
		}
	}
	
	public static void registerUpgrade(ITurtleUpgrade upgrade) {
		upgrades.add(upgrade);
	}
}
