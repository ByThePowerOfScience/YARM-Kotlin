package btpos.mcmods.yarm.custom.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ItemWrench extends Item {
	public ItemWrench(Properties pProperties) {
		super(pProperties);
	}
	
	/**
	 * Called when this item is used when targeting a Block
	 *
	 * @param pContext
	 */
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		return super.useOn(pContext);
	}
}
