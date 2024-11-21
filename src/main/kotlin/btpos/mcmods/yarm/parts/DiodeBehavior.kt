@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package btpos.mcmods.yarm.parts

import btpos.mcmods.yarm.components.gates.AbstractComponentBlock
import btpos.mcmods.yarm.components.gates.AbstractComponentEntity
import btpos.mcmods.yarm.components.gates.AbstractGateBlock
import btpos.mcmods.yarm.components.gates.FACING
import btpos.mcmods.yarm.components.gates.POWERED
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DiodeBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.ticks.TickPriority.EXTREMELY_HIGH
import net.minecraft.world.ticks.TickPriority.HIGH
import net.minecraft.world.ticks.TickPriority.VERY_HIGH


/**
 * Implementation of the base Minecraft [DiodeBlock][net.minecraft.world.level.block.DiodeBlock] class that also extends AbstractComponentBlock for various other interactions.
 * NOT necessarily a "diode".
 * @param <ENT_TYPE> The type of BlockEntity the block will have. Used for internal type-checking with [.newBlockEntity]
</ENT_TYPE> */
abstract class AbstractDiodeComponentBlock<ENT_TYPE : AbstractComponentEntity>(properties: Properties) :
	AbstractComponentBlock<ENT_TYPE>(properties)
{
	protected fun getDelay(ourState: BlockState): Int {
		return 0
	}
	
	protected fun shouldTurnOn(world: Level, ourPos: BlockPos, ourState: BlockState): Boolean {
		val value = ourState.getValue(FACING)
		return world.hasSignal(ourPos.relative(value), value)
	}
	
	/**
	 * Called on block update.
	 */
	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		if (this.isLocked(level, pos, state)) return
		
		val isPowered = state.getValue<Boolean>(POWERED)
		val shouldTurnOn = this.shouldTurnOn(level, pos, state)
		
		if (isPowered && !shouldTurnOn) {
			level.setBlock(pos, state.setValue<Boolean, Boolean>(POWERED, false), 2)
		} else if (!isPowered) {
			level.setBlock(pos, state.setValue<Boolean, Boolean>(POWERED, true), 2)
			if (!shouldTurnOn) {
				level.scheduleTick(pos, this, this.getDelay(state), VERY_HIGH)
			}
		}
	}
	
	override fun getDirectSignal(state: BlockState, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Int {
		return state.getSignal(level, pos, direction)
	}
	
	override fun neighborChanged(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		neighborBlock: Block?,
		neighborPos: BlockPos?,
		movedByPiston: Boolean
	) {
		if (state.canSurvive(level, pos)) {
			this.checkTickOnNeighbor(level, pos, state)
			return
		}
		
		
		// If it can't survive on its current surface, drop it
		val blockEntity = if (state.hasBlockEntity()) level.getBlockEntity(pos) else null
		DiodeBlock.dropResources(state, level, pos, blockEntity)
		level.removeBlock(pos, false)
		for (direction in Direction.entries) {
			level.updateNeighborsAt(pos.relative(direction), this)
		}
	}
	
	
	protected fun checkTickOnNeighbor(level: Level, pos: BlockPos, state: BlockState) {
		if (this.isLocked(level, pos, state)) return
		
		
		val isPowered = state.getValue<Boolean>(POWERED)
		if (isPowered != this.shouldTurnOn(level, pos, state)
		    && !level.blockTicks.willTickThisTick(pos, this)
		) {
			var tickPriority = HIGH
			if (this.shouldPrioritize(level, pos, state)) {
				tickPriority = EXTREMELY_HIGH
			} else if (isPowered) {
				tickPriority = VERY_HIGH
			}
			level.scheduleTick(pos, this, this.getDelay(state), tickPriority)
		}
	}
	
	fun isLocked(level: LevelReader?, pos: BlockPos?, state: BlockState?): Boolean {
		return false
	}
	
	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack?) {
		if (this.shouldTurnOn(level, pos, state)) {
			level.scheduleTick(pos, this, 1)
		}
	}
	
	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState?, movedByPiston: Boolean) {
		this.updateNeighborsInFront(level, pos, state)
	}
	
	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
		if (movedByPiston || state.`is`(newState.block)) {
			return
		}
		
		super.onRemove(state, level, pos, newState, movedByPiston)
		this.updateNeighborsInFront(level, pos, state)
	}
	
	protected fun updateNeighborsInFront(level: Level, pos: BlockPos, state: BlockState) {
		val direction = state.getValue<Direction>(FACING)
		val blockPos = pos.relative(direction.opposite)
		level.neighborChanged(blockPos, this, pos)
		level.updateNeighborsAtExceptFromFacing(blockPos, this, direction)
	}
	
	protected fun getOutputSignal(level: BlockGetter?, pos: BlockPos?, state: BlockState?): Int {
		return 15
	}
	
	/**
	 * Check if we're activating another diode and should therefore be ticked with higher priority.
	 * This makes certain monostable circuits more consistent.
	 * @param level The world for the dimension we're in.
	 * @param pos Our position in the world.
	 * @param state Our blockstate.
	 * @return true if we should be ticked with higher priority
	 */
	open fun shouldPrioritize(level: BlockGetter, pos: BlockPos, state: BlockState): Boolean {
		val ourOutputDirection: Direction = state.getValue(FACING).opposite
		val otherState = level.getBlockState(pos.relative(ourOutputDirection))
		
		val ifCustomDiode_shouldPrioritize: Boolean? = AbstractGateBlock.isYarmGate(level, state, pos, ourOutputDirection, otherState)
		
		return ifCustomDiode_shouldPrioritize ?: isVanillaDiode(otherState, ourOutputDirection)
	}
	
	companion object {
		fun isVanillaDiode(diodeState: BlockState, ourOutputDirection: Direction): Boolean {
			return DiodeBlock.isDiode(diodeState) && diodeState.getValue(FACING) != ourOutputDirection
		}
	}
}