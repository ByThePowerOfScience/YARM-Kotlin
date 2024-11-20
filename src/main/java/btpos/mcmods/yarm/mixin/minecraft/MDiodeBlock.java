package btpos.mcmods.yarm.mixin.minecraft;

import btpos.mcmods.yarm.custom.components.AbstractDiodeComponentBlock;
import btpos.mcmods.yarm.custom.components.gates.AbstractGateBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DiodeBlock.class)
public abstract class MDiodeBlock {
	@Inject(
			method = "isDiode",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void recognizeYarmDiodes(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.getBlock() instanceof AbstractDiodeComponentBlock<?>)
			cir.setReturnValue(Boolean.TRUE);
	}
	
	@Inject(
			method = "shouldPrioritize",
			at = @At(
					target = "Lnet/minecraft/world/level/block/DiodeBlock;isDiode(Lnet/minecraft/world/level/block/state/BlockState;)Z",
					value = "INVOKE"
			),
			locals = LocalCapture.CAPTURE_FAILSOFT,
			cancellable = true
	)
	private void recognizeYarmDiodes2(BlockGetter level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir,
	                                  Direction direction, BlockState otherState) {
		Boolean result = AbstractGateBlock.prioritizeYarmGates(level, state, pos, direction, otherState);
		if (result == null)
			return;
		
		cir.setReturnValue(result);
	}
}

