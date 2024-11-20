package btpos.mcmods.yarm.custom.components.gates.impl;

import btpos.mcmods.yarm.custom.components.gates.AbstractGateBlock;
import btpos.mcmods.yarm.registry.YARMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GateAND_Block extends AbstractGateBlock<GateAND_Entity> {
	public GateAND_Block(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public boolean shouldPower(Level world, BlockPos ourPos, GateAND_Entity ourEntity, Direction facing) {
		for (Direction direction : ourEntity.getInputFaces(facing)) {
			if (getSignalReceivedFromFace(world, ourPos, direction) == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	
	@Override
	public BlockEntityType<GateAND_Entity> getEntityType() {
		return YARMBlockEntities.GATE_AND.get();
	}
	
	
	/**
	 * Returns the signal strength this block should output.
	 * If the signal strength can vary at ALL, we'll have to store it in the TE so it gets saved to NBT
	 */
	@Override
	protected int getOutputSignal(BlockGetter world, BlockPos ourPos, BlockState ourState) {
		return 15;
	}
}
