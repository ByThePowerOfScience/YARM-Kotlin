package btpos.mcmods.yarm.custom.wire;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.EnumSet;

public class Wire_Entity extends BlockEntity {
	// I wish this could hold null keys for the internal colors too...
	EnumMap<Direction, EnumSet<DyeColor>> colorConnectionsBySide;
	EnumSet<DyeColor> containedColors;
	
	
	public Wire_Entity(DyeColor placementColor, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState); // TODO
		this.colorConnectionsBySide = new EnumMap<>(Direction.class);
		this.containedColors = EnumSet.of(placementColor);
	}
	
	/**
	 * Adds a new color to the wire
	 */
	public void addNewColor(DyeColor color) {
		containedColors.add(color);
	}
	
	/**
	 * Adds a connection of this color type to an adjacent block.
	 */
	public void connectToOtherBlock(Direction d, DyeColor channel) {
		if (!containedColors.contains(channel))
			return;
		
		EnumSet<DyeColor> colorsForSide = colorConnectionsBySide.get(d);
		if (colorsForSide == null)
			colorConnectionsBySide.put(d, EnumSet.of(channel));
		else
			colorsForSide.add(channel);
		
		updateRendering();
	}
	
	public void disconnectFromOtherBlock(Direction d, DyeColor channel) {
		if (!containedColors.contains(channel))
			return;
		
		EnumSet<DyeColor> colorsForSide = colorConnectionsBySide.get(d);
		if (colorsForSide != null) {
			colorsForSide.remove(channel);
		}
		
		updateRendering();
	}
	
	// Sideonly: Client
	public void updateRendering() {
		// TODO
	}
	
	
}
