package btpos.mcmods.yarm;

import btpos.mcmods.yarm.registry.YARMBlockEntities;
import btpos.mcmods.yarm.registry.YARMBlocks;
import btpos.mcmods.yarm.registry.YARMCreativeTab;
import btpos.mcmods.yarm.registry.YARMItems;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class YetAnotherRedstoneMod
{
	public static final String MOD_ID = "yarm";
	
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		YARMBlocks.doRegister();
		YARMBlockEntities.doRegister();
		YARMCreativeTab.doRegister();
		YARMItems.doRegister();
	}
	
	public static ResourceLocation resloc(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
