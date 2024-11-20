package btpos.mcmods.yarm.custom.components.gates.impl;

import btpos.mcmods.yarm.custom.components.gates.AbstractGateBlock;
import btpos.mcmods.yarm.registry.YARMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GateNAND_Block extends AbstractGateBlock<GateNAND_Entity> {
	public GateNAND_Block(Properties properties) {
		super(properties, true);
	}
	
	
	@Override // !(x & y & z) => !x | !y | !z
	public boolean shouldPower(Level world, BlockPos ourPos, GateNAND_Entity ourEntity, Direction facing) {
		for (Direction direction : ourEntity.getInputFaces(facing)) {
			if (getSignalReceivedFromFace(world, ourPos, direction) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public BlockEntityType<GateNAND_Entity> getEntityType() {
		return YARMBlockEntities.GATE_NAND.get();
	}
}
