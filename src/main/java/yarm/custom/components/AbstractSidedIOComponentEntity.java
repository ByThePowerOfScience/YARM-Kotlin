package btpos.mcmods.yarm.custom.components;

import btpos.mcmods.yarm.helpers.enums.RelativeDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public abstract class AbstractSidedIOComponentEntity extends AbstractComponentEntity {
	public static final String NBTKEY_SIDEIO = "side_io";
	
	// TODO: Just serialize it to NBT as relativedirection and store it in memory as direction
	//  since we're only using relative direction for saving to nbt and not while it's actively being used,
	//              unless rotation would be.... no that happens less often than it being retrieved
	protected final Map<RelativeDirection, IOType> SIDE_IO;
	
	
	public AbstractSidedIOComponentEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		setInitialFaceIO((SIDE_IO = new EnumMap<>(RelativeDirection.class)));
		setChanged();
	}
	
	
	@Contract(pure=true)
	public boolean isSideOutputOnly(RelativeDirection side) {
		return getIOForSide(side) == IOType.OUTPUT;
	}
	
	@Contract(pure=true)
	public EnumSet<RelativeDirection> getInputSides() {
		final EnumSet<RelativeDirection> ret = EnumSet.noneOf(RelativeDirection.class);
		getSideIOMap().forEach((dir, state) -> {
			if (state == IOType.INPUT) {
				ret.add(dir);
			}
		});
		return ret;
	}
	
	@Contract(pure=true)
	public EnumSet<Direction> getInputFaces(Direction facing) {
		final EnumSet<Direction> ret = EnumSet.noneOf(Direction.class);
		getSideIOMap().forEach((rd, state) -> {
			if (state == IOType.INPUT) {
				ret.add(rd.offset(facing));
			}
		});
		return ret;
	}
	
	@Contract(pure=true)
	public EnumSet<Direction> getOutputFaces(Direction facing) {
		final EnumSet<Direction> ret = EnumSet.noneOf(Direction.class);
		getSideIOMap().forEach((rd, state) -> {
			if (state == IOType.OUTPUT) {
				ret.add(rd.offset(facing));
			}
		});
		return ret;
	}
	
	
	
	/* ======== NBT ======== */
	@Override @Contract(pure=true) @MustBeInvokedByOverriders
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		CompoundTag sideStates = new CompoundTag();
		getSideIOMap().forEach((dir, state) -> sideStates.putByte(dir.name(), (byte) state.ordinal()));
		pTag.put(AbstractSidedIOComponentEntity.NBTKEY_SIDEIO, sideStates);
	}
	
	@Override @MustBeInvokedByOverriders
	public void load(CompoundTag pTag) {
		super.load(pTag);
		final CompoundTag sideiotag = pTag.getCompound(AbstractSidedIOComponentEntity.NBTKEY_SIDEIO);
		sideiotag.getAllKeys().forEach(key -> setSideIOValueFromNBT(key, sideiotag));
	}
	
	
	
	
	/* ======== Input/Output ======== */
	private IOType getIOForSide(RelativeDirection side) {
		return SIDE_IO.get(side);
	}
	
	protected abstract void setInitialFaceIO(Map<RelativeDirection, IOType> faceio);
	
	public Map<RelativeDirection, IOType> getSideIOMap() {
		return SIDE_IO;
	}
	
	public void setSideIOValue(RelativeDirection side, IOType value) {
		getSideIOMap().put(side, value);
		setChanged();
	}
	
	private void setSideIOValueFromNBT(String key, CompoundTag tag) {
		setSideIOValue(RelativeDirection.byName(key), IOType.values()[tag.getByte(key)]);
	}
	
	
	public enum IOType {
		INPUT,
		OUTPUT,
		NONE;
	}
	
}
