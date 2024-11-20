package btpos.mcmods.yarm.helpers.enums;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum RelativeDirection implements StringRepresentable {
	BOTTOM((d) -> Direction.DOWN),
	TOP((d) -> Direction.UP), // TODO make this orient properly in case we decide to let them face upwards
	FACING(Function.identity()),
	OPPOSITE(Direction::getOpposite),
	LEFT(Direction::getCounterClockWise),
	RIGHT(Direction::getClockWise);
	
	// Map to get relative directions from their NBT representations
	private static final Map<String, RelativeDirection> nameMap
			= Arrays.stream(values())
			        .collect(ImmutableMap.toImmutableMap(RelativeDirection::name, Function.identity()));
	
	RelativeDirection(Function<Direction, Direction> f) {
		this.func_offset = f;
	}
	
	private final Function<Direction, Direction> func_offset;
	
	public Direction offset(Direction absolute) {
		return func_offset.apply(absolute);
	}
	
	public static RelativeDirection from(Direction base, Direction offset) {
		// TODO
//		return values()[(offset.get3DDataValue() - base.get3DDataValue()) % values().length];
		
		if (offset == LEFT.offset(base))
			return LEFT;
		else if (offset == RIGHT.offset(base))
			return RIGHT;
		else if (offset == FACING.offset(base))
			return FACING;
		else if (offset == OPPOSITE.offset(base))
			return OPPOSITE;
		else if (offset == TOP.offset(base))
			return TOP;
		else
			return BOTTOM;
	}
	
	public static RelativeDirection byName(String name) {
		return nameMap.get(name);
	}
	
	@Override
	public String getSerializedName() {
		return name();
	}
}
