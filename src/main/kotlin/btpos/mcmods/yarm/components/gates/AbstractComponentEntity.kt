@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package btpos.mcmods.yarm.components.gates

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState


abstract class AbstractComponentEntity(type: BlockEntityType<*>, pos: BlockPos, blockState: BlockState) : BlockEntity(type, pos, blockState) {
	
	fun saveNBTToItem(item: ItemStack) {
		val data = CompoundTag()
		
		this.saveAdditional(data)
		
		item.getOrCreateTag().put(NBT_DATA_KEY, data)
	}
	
	fun shouldSaveNBTToDroppedItem(): Boolean {
		return true
	}
	
	companion object {
		const val NBT_DATA_KEY: String = "be"
	}
}
