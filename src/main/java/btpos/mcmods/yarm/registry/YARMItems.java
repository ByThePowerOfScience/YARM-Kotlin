package btpos.mcmods.yarm.registry;

import btpos.mcmods.yarm.YetAnotherRedstoneMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class YARMItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(YetAnotherRedstoneMod.MOD_ID, Registries.ITEM);
	
	public static void doRegister() {
		REGISTRY.register();
	}
	
	public static RegistrySupplier<Item> fromBlock(RegistrySupplier<? extends Block> blockRegistrar) {
		return REGISTRY.register(blockRegistrar.getId(), () -> new BlockItem(blockRegistrar.get(), new Item.Properties().arch$tab(YARMCreativeTab.YARM_MAIN_TAB)));
	}
	
	
	public static final RegistrySupplier<Item> GATE_AND = fromBlock(YARMBlocks.GATE_AND);
	public static final RegistrySupplier<Item> GATE_OR = fromBlock(YARMBlocks.GATE_OR);
	public static final RegistrySupplier<Item> GATE_NOT = fromBlock(YARMBlocks.GATE_NOT);
	public static final RegistrySupplier<Item> GATE_NOR = fromBlock(YARMBlocks.GATE_NOR);
	public static final RegistrySupplier<Item> GATE_NAND = fromBlock(YARMBlocks.GATE_NAND);
}
