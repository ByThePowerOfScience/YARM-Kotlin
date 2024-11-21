package btpos.mcmods.yarm.util.ext

import btpos.mcmods.yarm.components.gates.FACING
import btpos.mcmods.yarm.components.gates.POWERED
import btpos.mcmods.yarm.parts.RelativeDirection
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

fun Direction.relativeTo(other: Direction): RelativeDirection = RelativeDirection.from(this, other)

val BlockState.facing: Direction
	get() = if (this.hasProperty(FACING)) this.getValue(FACING) else Direction.NORTH

val BlockState.isPowered: Boolean
	get() = if (this.hasProperty(POWERED)) this.getValue(POWERED) else false

fun BlockState.relativeFacing(other: Direction) = this.facing.relativeTo(other)