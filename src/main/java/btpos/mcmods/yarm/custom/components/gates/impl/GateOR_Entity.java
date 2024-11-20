package btpos.mcmods.yarm.custom.components.gates.impl;

import btpos.mcmods.yarm.custom.components.AbstractSidedIOComponentEntity;
import btpos.mcmods.yarm.custom.components.util.IOTemplates;
import btpos.mcmods.yarm.helpers.enums.RelativeDirection;
import btpos.mcmods.yarm.registry.YARMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class GateOR_Entity extends AbstractSidedIOComponentEntity {
	public GateOR_Entity(BlockPos pos, BlockState blockState) {
		super(YARMBlockEntities.GATE_OR.get(), pos, blockState);
	}
	
	@Override
	protected void setInitialFaceIO(Map<RelativeDirection, IOType> faceio) {
		IOTemplates.oneOutputElseInput(faceio);
	}
}
