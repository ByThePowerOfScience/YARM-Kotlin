package btpos.mcmods.yarm.custom.components.gates;

import btpos.mcmods.yarm.YetAnotherRedstoneMod;
import btpos.mcmods.yarm.custom.components.AbstractComponentBlock;
import btpos.mcmods.yarm.custom.components.AbstractDiodeComponentBlock;
import btpos.mcmods.yarm.custom.components.AbstractSidedIOComponentEntity;
import btpos.mcmods.yarm.helpers.enums.RelativeDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractGateBlock<ENT_TYPE extends AbstractSidedIOComponentEntity> extends AbstractDiodeComponentBlock<ENT_TYPE> {
	public AbstractGateBlock(Properties properties) {
		this(properties, false);
	}
	
	public AbstractGateBlock(Properties properties, boolean isPoweredByDefault) {
		super(properties);
		this.registerDefaultState(
				this.getStateDefinition()
				    .any()
				    .setValue(POWERED, isPoweredByDefault));
	}
	
	@Override
	protected boolean shouldTurnOn(Level world, BlockPos ourPos, BlockState ourState) {
		Optional<ENT_TYPE> couldBeOurEntity = world.getBlockEntity(ourPos, getEntityType());
		if (couldBeOurEntity.isEmpty())
			return false;
		
		return shouldPower(world, ourPos, couldBeOurEntity.get(), ourState.getValue(FACING));
	}
	
	/** I'll worry about getting truth tables/boolean operations when we get to rasterizing */
	public abstract boolean shouldPower(Level world, BlockPos ourPos, ENT_TYPE ourEntity, Direction facing);
	
	
	/**
	 * Returns the signal this block emits in the given direction.
	 *
	 * <p>
	 * NOTE: directions in redstone signal related methods are backwards, so this method
	 * checks for the signal emitted in the <i>opposite</i> direction of the one given.
	 */
	@Override
	public int getSignal(BlockState ourState, BlockGetter world, BlockPos ourPos, Direction oppositeRequestingFace) {
		if (!ourState.getValue(AbstractComponentBlock.POWERED))
			return 0;
		
		Optional<ENT_TYPE> ourEntity = world.getBlockEntity(ourPos, getEntityType());
		if (ourEntity.isEmpty())
			return 0;
		
		RelativeDirection requestingSide = RelativeDirection.from(ourState.getValue(AbstractComponentBlock.FACING), oppositeRequestingFace.getOpposite());
		if (!ourEntity.get().isSideOutputOnly(requestingSide)) {
			YetAnotherRedstoneMod.LOGGER.debug("Output side requested: {}", requestingSide.toString());
			return 0;
		}
		
		return getOutputSignal(world, ourPos, ourState);
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
	@Override
	public boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
		Optional<ENT_TYPE> ourBlockEntity = getOurBlockEntity(level, pos);
		if (ourBlockEntity.isEmpty())
			return false;
		
		for (Direction ourOutputFace : ourBlockEntity.get().getOutputFaces(state.getValue(FACING))) {
			BlockState target = level.getBlockState(pos.relative(ourOutputFace));
			
			Boolean b = prioritizeYarmGates(level, state, pos, ourOutputFace, target);
			
			if ((b == null && prioritizeVanillaDiodes(target, ourOutputFace))
	                || b == Boolean.TRUE)
				return true;
		}
		return false;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AbstractComponentBlock.SHAPE;
	}
	
	
	/**
	 * Variant of {@link net.minecraft.world.level.block.DiodeBlock#isDiode(net.minecraft.world.level.block.state.BlockState)}
	 * for checking YARM diodes.
	 * @return {@code TRUE} if the block should be prioritized, {@code FALSE} if the block should definitely NOT be prioritized, and {@code null} if this method's return value should be ignored.
	 * @see btpos.mcmods.yarm.custom.components.AbstractDiodeComponentBlock#shouldPrioritize(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState)
	 * @see btpos.mcmods.yarm.mixin.minecraft.MDiodeBlock MDiodeBlock
	 */
	@Nullable
	public static Boolean prioritizeYarmGates(BlockGetter level, BlockState ourState, BlockPos pos, Direction checkingDirection, BlockState otherState) {
		if (otherState.getBlock() instanceof AbstractGateBlock<? extends AbstractSidedIOComponentEntity> gate) {
			var couldBeEntity = level.getBlockEntity(pos.relative(checkingDirection), gate.getEntityType());
			if (couldBeEntity.isEmpty()) {
				return Boolean.FALSE;
			}
			
			if (couldBeEntity.get().isSideOutputOnly(gate.getRelative(ourState, checkingDirection))) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return null;
	}
}
