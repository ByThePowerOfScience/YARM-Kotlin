package btpos.mcmods.yarm.registry;

import btpos.mcmods.yarm.YetAnotherRedstoneMod;
import btpos.mcmods.yarm.custom.components.gates.impl.GateAND_Block;
import btpos.mcmods.yarm.custom.components.gates.impl.GateNAND_Block;
import btpos.mcmods.yarm.custom.components.gates.impl.GateNOR_Block;
import btpos.mcmods.yarm.custom.components.gates.impl.GateNOT_Block;
import btpos.mcmods.yarm.custom.components.gates.impl.GateOR_Block;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class YARMBlocks {
	public static void doRegister() {
		REGISTRY.register();
	}
	
	public static final BlockBehaviour.Properties GATE_PROPS = BlockBehaviour.Properties.of().instabreak().sound(SoundType.STONE).pushReaction(PushReaction.DESTROY);
	
	
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(YetAnotherRedstoneMod.MOD_ID, Registries.BLOCK);
	
	
	public static final RegistrySupplier<GateAND_Block> GATE_AND = REGISTRY.register("and_gate", () -> new GateAND_Block(GATE_PROPS));
	public static final RegistrySupplier<GateOR_Block> GATE_OR = REGISTRY.register("or_gate", () -> new GateOR_Block(GATE_PROPS));
	public static final RegistrySupplier<GateNOT_Block> GATE_NOT = REGISTRY.register("not_gate", () -> new GateNOT_Block(GATE_PROPS));
	public static final RegistrySupplier<GateNAND_Block> GATE_NAND = REGISTRY.register("nand_gate", () -> new GateNAND_Block(GATE_PROPS));
	public static final RegistrySupplier<GateNOR_Block> GATE_NOR = REGISTRY.register("nor_gate", () -> new GateNOR_Block(GATE_PROPS));
}
