@file:Suppress("OVERRIDE_DEPRECATION")

package btpos.mcmods.yarm.components.gates

import btpos.mcmods.yarm.YetAnotherRedstoneMod
import btpos.mcmods.yarm.parts.AbstractDiodeComponentBlock
import btpos.mcmods.yarm.parts.RelativeDirection
import btpos.mcmods.yarm.util.ext.facing
import btpos.mcmods.yarm.util.ext.relativeTo
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.Optional


abstract class AbstractGateBlock<ENT_TYPE : AbstractComponentEntity>(properties: Properties, isPoweredByDefault: Boolean)
	: AbstractDiodeComponentBlock<ENT_TYPE>(properties)
{
	constructor(properties: Properties) : this(properties, false)
	
	init {
		this.registerDefaultState(
				this.getStateDefinition()
					.any()
					.setValue(POWERED, isPoweredByDefault)
		)
	}
	
	protected override fun shouldTurnOn(world: Level, ourPos: BlockPos?, ourState: BlockState): Boolean {
		val couldBeOurEntity: Optional<ENT_TYPE> = world.getBlockEntity(ourPos, getEntityType())
		if (couldBeOurEntity.isEmpty) return false
		
		return shouldPower(world, ourPos, couldBeOurEntity.get(), ourState.getValue(FACING))
	}
	
	/** I'll worry about getting truth tables/boolean operations when we get to rasterizing  */
	abstract fun shouldPower(world: Level?, ourPos: BlockPos?, ourEntity: ENT_TYPE, facing: Direction?): Boolean
	
	
	/**
	 * Returns the signal this block emits in the given direction.
	 *
	 *
	 *
	 * NOTE: directions in redstone signal related methods are backwards, so this method
	 * checks for the signal emitted in the *opposite* direction of the one given.
	 */
	override fun getSignal(ourState: BlockState, world: BlockGetter, ourPos: BlockPos, oppositeRequestingFace: Direction): Int {
		if (!ourState.getValue(POWERED))
			return 0
		
		val ourEntity: Optional<ENT_TYPE> = world.getBlockEntity(ourPos, getEntityType())
		if (ourEntity.isEmpty)
			return 0
		
		val requestingSide: RelativeDirection = ourState.facing.relativeTo(oppositeRequestingFace.opposite)
		if (!ourEntity.get().isSideOutputOnly(requestingSide)) {
			YetAnotherRedstoneMod.LOGGER.debug("Output side requested: {}", requestingSide.toString())
			return 0
		}
		
		return getOutputSignal(world, ourPos, ourState)
	}
	
	/**
	 * Check if we're activating another diode and should therefore be ticked with higher priority.
	 * This makes certain monostable circuits more consistent.
	 *
	 * @param level The world for the dimension we're in.
	 * @param pos Our position in the world.
	 * @param state Our blockstate.
	 * @return True if we should be ticked with higher priority.
	 */
	override fun shouldPrioritize(level: BlockGetter, pos: BlockPos, state: BlockState): Boolean {
		val ourBlockEntity: Optional<ENT_TYPE> = getOurBlockEntity(level, pos)
		
		if (ourBlockEntity.isEmpty)
			return false
		
		for (ourOutputFace in ourBlockEntity.get().getOutputSides(state.facing)) {
			val target = level.getBlockState(pos.relative(ourOutputFace))
			
			val b = isYarmGate(level, state, pos, ourOutputFace, target)
			
			if ((b == null && isVanillaDiode(target, ourOutputFace))
			    || b === java.lang.Boolean.TRUE
			) return true
		}
		return false
	}
	
	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}
	
	
	companion object {
		/**
		 * Variant of [net.minecraft.world.level.block.DiodeBlock.isDiode]
		 * for checking YARM diodes.
		 * @return `TRUE` if the block should be prioritized, `FALSE` if the block should definitely NOT be prioritized, and `null` if this method's return value should be ignored.
		 * @see AbstractDiodeComponentBlock.shouldPrioritize
		 * @see btpos.mcmods.yarm.mixin.minecraft.MDiodeBlock MDiodeBlock
		 */
		fun isYarmGate(level: BlockGetter, ourState: BlockState, pos: BlockPos, checkingDirection: Direction, otherState: BlockState): Boolean? {
			val gate = otherState.block
			
			if (gate is AbstractGateBlock<*>) {
				return level.getBlockEntity(pos.relative(checkingDirection), gate.getEntityType()).filter { it: AbstractComponentEntity ->
					!it.isInput(ourState.facing.relativeTo(checkingDirection))
				}.isPresent()
			}
			return null
		}
	}
}