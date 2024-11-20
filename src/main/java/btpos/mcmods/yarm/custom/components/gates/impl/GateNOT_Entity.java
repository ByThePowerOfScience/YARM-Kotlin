package btpos.mcmods.yarm.custom.components.gates.impl;

import btpos.mcmods.yarm.custom.components.AbstractSidedIOComponentEntity;
import btpos.mcmods.yarm.custom.components.util.IOTemplates;
import btpos.mcmods.yarm.helpers.enums.RelativeDirection;
import btpos.mcmods.yarm.registry.YARMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

/*
 * Four sides
 * - each with a color? Or just have any wire color that goes towards the gate activate it or receive its output?
 *      -maybe the latter... wouldn't have to store so much that way
 * Each side can be either input, output, or "none"
 */
public class GateNOT_Entity extends AbstractSidedIOComponentEntity {
	
	public GateNOT_Entity(BlockPos pPos, BlockState pBlockState) {
		super(YARMBlockEntities.GATE_NOT.get(), pPos, pBlockState);
	}
	
	@Override
	protected void setInitialFaceIO(Map<RelativeDirection, IOType> faceio) {
		IOTemplates.oneInputElseOutput(faceio);
	}
	
}
