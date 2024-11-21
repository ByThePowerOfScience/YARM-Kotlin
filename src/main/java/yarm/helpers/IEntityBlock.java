package btpos.mcmods.yarm.helpers;

import btpos.mcmods.yarm.YetAnotherRedstoneMod;
import btpos.mcmods.yarm.custom.components.AbstractComponentEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Helper class for creating block entities from their internal block entity types.
 * @param <ENT_TYPE>
 */
public interface IEntityBlock<ENT_TYPE extends AbstractComponentEntity> extends EntityBlock {
	@NotNull
	BlockEntityType<ENT_TYPE> getEntityType();
	
	@Override @Nullable
	default BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return getEntityType().create(pos, state);
	}
	
	default Optional<ENT_TYPE> getOurBlockEntity(BlockGetter level, BlockPos pos) {
		var ourEntity = level.getBlockEntity(pos, getEntityType());
		
		if (ourEntity.isEmpty()) {
			YetAnotherRedstoneMod.LOGGER.debug("No block entity found for block {} at {}", getClass().getSimpleName(), pos);
		}
		
		return ourEntity;
	}
}
