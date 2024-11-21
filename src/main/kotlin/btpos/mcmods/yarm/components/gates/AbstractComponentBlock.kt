@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package btpos.mcmods.yarm.components.gates

import btpos.mcmods.yarm.parts.BlockEntityProvider
import btpos.mcmods.yarm.parts.RelativeDirection
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.phys.shapes.VoxelShape
import org.jetbrains.annotations.MustBeInvokedByOverriders


val SHAPE: VoxelShape = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)

/**
 * The "Backside" of the block. Because a repeater looking east's "facing" direction is actually west.
 */
val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

val POWERED: BooleanProperty = BlockStateProperties.POWERED
abstract class AbstractComponentBlock<ENT_TYPE : AbstractComponentEntity>(props: Properties)
	: Block(props), BlockEntityProvider<ENT_TYPE>
{
	override fun canSurvive(pState: BlockState, pLevel: LevelReader, pPos: BlockPos): Boolean {
		return true
	}
	
	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		return defaultBlockState().setValue(FACING, context.horizontalDirection.opposite)
	}
	
	override fun isSignalSource(state: BlockState): Boolean {
		return true
	}
	
	override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack> {
		if (!itemHasNBT())
			return super.getDrops(state, params)


		// TODO: Make this sync the tile's NBT to the item
		return super.getDrops(state, params)
	}
	
	fun itemHasNBT(): Boolean {
		return false
	}
	
	@MustBeInvokedByOverriders
	override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block?, BlockState?>) {
		pBuilder.add(POWERED, FACING)
	}
	
	protected fun getSignalReceivedFromFace(world: Level, ourPos: BlockPos, direction: Direction): Int {
		return world.getSignal(ourPos.relative(direction), direction)
	}
}