package btpos.mcmods.yarm.registry;

import btpos.mcmods.yarm.YetAnotherRedstoneMod;
import btpos.mcmods.yarm.custom.components.gates.impl.GateAND_Entity;
import btpos.mcmods.yarm.custom.components.gates.impl.GateNAND_Entity;
import btpos.mcmods.yarm.custom.components.gates.impl.GateNOR_Entity;
import btpos.mcmods.yarm.custom.components.gates.impl.GateNOT_Entity;
import btpos.mcmods.yarm.custom.components.gates.impl.GateOR_Entity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class YARMBlockEntities {
	public static void doRegister() {
		REGISTRY.register();
	}
	
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(YetAnotherRedstoneMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
	
	public static final RegistrySupplier<BlockEntityType<GateAND_Entity>> GATE_AND = REGISTRY.register("and_gate", () -> BlockEntityType.Builder.of(GateAND_Entity::new, YARMBlocks.GATE_AND.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<GateOR_Entity>> GATE_OR = REGISTRY.register("or_gate", () -> BlockEntityType.Builder.of(GateOR_Entity::new, YARMBlocks.GATE_OR.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<GateNOT_Entity>> GATE_NOT = REGISTRY.register("not_gate", () -> BlockEntityType.Builder.of(GateNOT_Entity::new, YARMBlocks.GATE_NOT.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<GateNAND_Entity>> GATE_NAND = REGISTRY.register("nand_gate", () -> BlockEntityType.Builder.of(GateNAND_Entity::new, YARMBlocks.GATE_NAND.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<GateNOR_Entity>> GATE_NOR = REGISTRY.register("nor_gate", () -> BlockEntityType.Builder.of(GateNOR_Entity::new, YARMBlocks.GATE_NOR.get()).build(null));
	
}
