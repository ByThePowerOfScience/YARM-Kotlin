package btpos.mcmods.yarm.custom.wire;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Wire_Block extends Block {
	
	public Wire_Block(Properties pProperties) {
		super(pProperties);
		
	}
	
	public void onReceiveRedstoneSignal(int signalstrength, DyeColor color) {
		passOnSignal(signalstrength, color);
	}
	
	public void passOnSignal(int signalStrength, DyeColor color) {
		// TODO pass signal to nearby components and wires
	}
	
	
	
	@Override
	public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
		super.destroy(pLevel, pPos, pState);
		// If this is an endpoint, change adjacent connected wires (of each connected color) to be endpoints (or not, if it's like a rectangle of connected wires that would stay connected even without it present), assuming we ever get to that level of optimization
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
		super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
		// Naive implementation: check if we're still receiving a signal like vanilla redstone does
	}
	
	
	
}
