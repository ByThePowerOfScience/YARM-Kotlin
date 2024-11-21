@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package btpos.mcmods.yarm.components.gates

import btpos.mcmods.yarm.parts.BlockEntityProvider
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class AndGateBlock(entType: BlockEntityType<AndGateEntity>, props: Properties)
	: Block(props), BlockEntityProvider<AndGateEntity> by BlockEntityProvider.Impl(entType) {
	
}

class AndGateEntity(entType: BlockEntityType<AndGateEntity>, pos: BlockPos, state: BlockState)
	: AbstractComponentEntity(entType, pos, state) {

}