package btpos.mcmods.yarm.custom.components;

import btpos.mcmods.yarm.helpers.enums.RelativeDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.List;

public abstract class AbstractComponentBlock extends Block {
	public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	/**
	 * The "Backside" of the block. Because a repeater looking east's "facing" direction is actually west.
	 */
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public AbstractComponentBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		return true;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder params) {
		if (!itemHasNBT())
			return super.getDrops(state, params);
		
		// TODO: Make this sync the tile's NBT to the item
		return super.getDrops(state, params);
	}
	
	public boolean itemHasNBT() {
		return false;
	}
	
	@Override
	@MustBeInvokedByOverriders
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWERED, FACING);
	}
	
	protected int getSignalReceivedFromFace(Level world, BlockPos ourPos, Direction direction) {
		return world.getSignal(ourPos.relative(direction), direction);
	}
	
	public RelativeDirection getRelative(BlockState state, Direction offset) {
		return RelativeDirection.from(state.getValue(FACING), offset);
	}
}
