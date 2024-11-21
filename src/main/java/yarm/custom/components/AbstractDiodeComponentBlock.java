package btpos.mcmods.yarm.custom.components;

import btpos.mcmods.yarm.custom.components.gates.AbstractGateBlock;
import btpos.mcmods.yarm.helpers.IEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;

/**
 * Implementation of the base Minecraft {@link net.minecraft.world.level.block.DiodeBlock DiodeBlock} class that also extends AbstractComponentBlock for various other interactions.
 * NOT necessarily a "diode".
 * @param <ENT_TYPE> The type of BlockEntity the block will have. Used for internal type-checking with {@link #newBlockEntity(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState)}
 */
public abstract class AbstractDiodeComponentBlock<ENT_TYPE extends AbstractComponentEntity> extends AbstractComponentBlock implements IEntityBlock<ENT_TYPE> {
	public AbstractDiodeComponentBlock(Properties properties) {super(properties);}
	
	protected int getDelay(BlockState ourState) {
		return 0;
	}
	
	protected boolean shouldTurnOn(Level world, BlockPos ourPos, BlockState ourState) {
		Direction value = ourState.getValue(FACING);
		return world.hasSignal(ourPos.relative(value), value);
	}
	
	/**
	 * Called on block update.
	 */
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (this.isLocked(level, pos, state))
			return;
		
		boolean isPowered = state.getValue(POWERED);
		boolean shouldTurnOn = this.shouldTurnOn(level, pos, state);
		
		if (isPowered && !shouldTurnOn) {
			level.setBlock(pos, state.setValue(POWERED, false), 2);
		}
		else if (!isPowered) {
			level.setBlock(pos, state.setValue(POWERED, true), 2);
			if (!shouldTurnOn) {
				level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
			}
		}
	}
	
	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getSignal(level, pos, direction);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (state.canSurvive(level, pos)) {
			this.checkTickOnNeighbor(level, pos, state);
			return;
		}
		
		// If it can't survive on its current surface, drop it
		BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
		DiodeBlock.dropResources(state, level, pos, blockEntity);
		level.removeBlock(pos, false);
		for (Direction direction : Direction.values()) {
			level.updateNeighborsAt(pos.relative(direction), this);
		}
	}
	
	
	
	protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
		if (this.isLocked(level, pos, state))
			return;
		
		
		boolean isPowered = state.getValue(POWERED);
		if (isPowered != this.shouldTurnOn(level, pos, state)
				    && !level.getBlockTicks().willTickThisTick(pos, this))
		{
			TickPriority tickPriority = TickPriority.HIGH;
			if (this.shouldPrioritize(level, pos, state)) {
				tickPriority = TickPriority.EXTREMELY_HIGH;
			} else if (isPowered) {
				tickPriority = TickPriority.VERY_HIGH;
			}
			level.scheduleTick(pos, this, this.getDelay(state), tickPriority);
		}
	}
	
	public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
		return false;
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (this.shouldTurnOn(level, pos, state)) {
			level.scheduleTick(pos, this, 1);
		}
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		this.updateNeighborsInFront(level, pos, state);
	}
	
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (movedByPiston || state.is(newState.getBlock())) {
			return;
		}
		
		super.onRemove(state, level, pos, newState, movedByPiston);
		this.updateNeighborsInFront(level, pos, state);
	}
	
	protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		level.neighborChanged(blockPos, this, pos);
		level.updateNeighborsAtExceptFromFacing(blockPos, this, direction);
	}
	
	protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
		return 15;
	}
	
	/**
	 * Check if we're activating another diode and should therefore be ticked with higher priority.
	 * This makes certain monostable circuits more consistent.
	 * @param level The world for the dimension we're in.
	 * @param pos Our position in the world.
	 * @param state Our blockstate.
	 * @return true if we should be ticked with higher priority
	 */
	public boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
		Direction ourOutputDirection = state.getValue(FACING).getOpposite();
		BlockState otherState = level.getBlockState(pos.relative(ourOutputDirection));
		
		Boolean ifCustomDiode_shouldPrioritize = AbstractGateBlock.prioritizeYarmGates(level, state, pos, ourOutputDirection, otherState);
		
		if (ifCustomDiode_shouldPrioritize == null)
			return prioritizeVanillaDiodes(otherState, ourOutputDirection);
		else
			return ifCustomDiode_shouldPrioritize;
	}
	
	public static boolean prioritizeVanillaDiodes(BlockState diodeState, Direction ourOutputDirection) {
		return DiodeBlock.isDiode(diodeState) && diodeState.getValue(FACING) != ourOutputDirection;
	}
}
