package btpos.mcmods.yarm.parts

import btpos.mcmods.yarm.LOGGER
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.Optional


interface BlockEntityProvider<ENT_TYPE : BlockEntity> : EntityBlock {
	fun getEntityType(): BlockEntityType<ENT_TYPE>
	
	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		return getEntityType().create(pos, state)
	}
	
	fun getOurBlockEntity(level: BlockGetter, pos: BlockPos): Optional<ENT_TYPE> {
		val ourEntity: Optional<ENT_TYPE> = level.getBlockEntity(pos, getEntityType())
		
		if (ourEntity.isEmpty) {
			LOGGER.debug("No block entity found for block {} at {}", javaClass.simpleName, pos)
		}
		
		return ourEntity
	}
	
	class Impl<T : BlockEntity>(val entityType: BlockEntityType<T>) : BlockEntityProvider<T> {
		override fun getEntityType(): BlockEntityType<T> {
			return entityType
		}
	}
}

