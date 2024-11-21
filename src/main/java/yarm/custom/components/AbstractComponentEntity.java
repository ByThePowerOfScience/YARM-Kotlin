package btpos.mcmods.yarm.custom.components;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractComponentEntity extends BlockEntity {
	public static final String NBT_DATA_KEY = "be";
	
	public AbstractComponentEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}
	
	
	public void saveNBTToItem(ItemStack item) {
		CompoundTag data = new CompoundTag();
		
		this.saveAdditional(data);
		
		item.getOrCreateTag().put(NBT_DATA_KEY, data);
	}
	
	public boolean shouldSaveNBTToDroppedItem() {
		return true;
	}
}
