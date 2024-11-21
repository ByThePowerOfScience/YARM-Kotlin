package btpos.mcmods.yarm.parts

import btpos.mcmods.yarm.util.identity
import btpos.mcmods.yarm.parts.RelativeDirection.*
import com.google.common.collect.ImmutableMap
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import java.util.Arrays
import java.util.EnumMap


typealias SideHolder = MutableMap<RelativeDirection, IOType>

interface SidedIOProvider {
	fun init(map: SideHolder): SideHolder
	
	fun getForSide(dir: RelativeDirection): IOType {
		return getSides()[dir]!!
	}
	
	fun getSides(): SideHolder
}

enum class IOType { // TODO refactor this to a whole colorized io interface/class thing
	NONE,
	INPUT,
	OUTPUT,
	BOTH
}

object SidedIOProviders {
	abstract class AbstractSidedIOProvider : SidedIOProvider {
		val internal: SideHolder by lazy(LazyThreadSafetyMode.NONE) {
			init(EnumMap(RelativeDirection::class.java)) // delays the overridden function call to after the class is instantiated
		}
		
		override fun getSides(): SideHolder {
			return internal
		}
	}
	
	class OneInputOneOutput : AbstractSidedIOProvider() {
		override fun init(map: MutableMap<RelativeDirection, IOType>): SideHolder {
			map[TOP] = IOType.NONE
			map[BOTTOM] = IOType.NONE
			map[LEFT] = IOType.NONE
			map[RIGHT] = IOType.NONE
			map[FACING] = IOType.OUTPUT
			map[OPPOSITE] = IOType.INPUT
			return map
		}
	}
	
	class ThreeInputOneOutput : AbstractSidedIOProvider() {
		override fun init(map: SideHolder): SideHolder {
			map[TOP] = IOType.NONE
			map[BOTTOM] = IOType.NONE
			map[LEFT] = IOType.INPUT
			map[RIGHT] = IOType.INPUT
			map[FACING] = IOType.OUTPUT
			map[OPPOSITE] = IOType.INPUT
			return map
		}
	}
	
	class OneInputThreeOutput : AbstractSidedIOProvider() {
		override fun init(map: SideHolder): SideHolder {
			map[TOP] = IOType.NONE
			map[BOTTOM] = IOType.NONE
			map[LEFT] = IOType.OUTPUT
			map[RIGHT] = IOType.OUTPUT
			map[FACING] = IOType.OUTPUT
			map[OPPOSITE] = IOType.INPUT
			return map
		}
	}
}

enum class RelativeDirection(val offset: (Direction) -> Direction) : StringRepresentable {
	BOTTOM({ Direction.DOWN }),
	TOP({ Direction.UP }),  // TODO make this orient properly in case we decide to let them face upwards
	FACING(::identity),
	OPPOSITE(Direction::opposite),
	LEFT(Direction::counterClockWise),
	RIGHT(Direction::clockWise);
	
	override fun getSerializedName(): String {
		return name
	}
	
	companion object {
		// Map to get relative directions from their NBT representations
		private val nameMap: Map<String, RelativeDirection> =
			Arrays.stream(entries.toTypedArray())
				.collect(ImmutableMap.toImmutableMap(RelativeDirection::name, ::identity))
		
		fun byName(name: String): RelativeDirection? {
			return nameMap[name]
		}
		
		fun from(base: Direction, offset: Direction): RelativeDirection {
			// TODO
			// return values()[(offset.get3DDataValue() - base.get3DDataValue()) % values().length];
			
			return when (offset) {
				LEFT.offset(base) -> LEFT
				RIGHT.offset(base) -> RIGHT
				FACING.offset(base) -> FACING
				OPPOSITE.offset(base) -> OPPOSITE
				TOP.offset(base) -> TOP
				else -> BOTTOM
			}
		}
	}
}