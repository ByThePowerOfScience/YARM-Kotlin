package btpos.mcmods.yarm.registry;

import btpos.mcmods.yarm.YetAnotherRedstoneMod;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Row;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class YARMCreativeTab {
	private static DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(YetAnotherRedstoneMod.MOD_ID, Registries.CREATIVE_MODE_TAB);
	
	public static void doRegister() {
		REGISTRY.register();
	}
	
	public static final RegistrySupplier<CreativeModeTab> YARM_MAIN_TAB = REGISTRY.register(
			"yarmtab",
			() -> CreativeTabRegistry.create(
					Component.translatable("yarm.creative_tab.name"),
					() -> new ItemStack(Items.COMPARATOR)));
}
