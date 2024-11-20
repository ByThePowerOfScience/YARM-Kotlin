package btpos.mcmods.yarm.custom.components.gates.impl;

import btpos.mcmods.yarm.custom.components.gates.AbstractGateBlock;
import btpos.mcmods.yarm.registry.YARMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GateNOT_Block extends AbstractGateBlock<GateNOT_Entity> {
	public GateNOT_Block(Properties pProperties) {
		super(pProperties, true);
		this.registerDefaultState(
				this.getStateDefinition()
				    .any()
				    .setValue(FACING, Direction.NORTH)
				    .setValue(POWERED, false));
	}
	
	@Override
	public boolean shouldPower(Level world, BlockPos ourPos, GateNOT_Entity ourEntity, Direction facing) {
		for (Direction direction : ourEntity.getInputFaces(facing)) {
			if (getSignalReceivedFromFace(world, ourPos, direction) != 0) {
				return false;
			}
		}
		
		return true;
	}

	
	@Override
	public BlockEntityType<GateNOT_Entity> getEntityType() {
		return YARMBlockEntities.GATE_NOT.get();
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
