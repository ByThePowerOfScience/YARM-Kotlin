package btpos.mcmods.yarm.custom.components.util;

import btpos.mcmods.yarm.custom.components.AbstractSidedIOComponentEntity.IOType;
import btpos.mcmods.yarm.helpers.enums.RelativeDirection;

import java.util.Map;

import static btpos.mcmods.yarm.helpers.enums.RelativeDirection.*;

public final class IOTemplates {
	public static void oneOutputElseInput(Map<RelativeDirection, IOType> faceio) {
		faceio.put(RelativeDirection.FACING, IOType.INPUT);
		faceio.put(RelativeDirection.LEFT, IOType.INPUT);
		faceio.put(RelativeDirection.RIGHT, IOType.INPUT);
		faceio.put(RelativeDirection.OPPOSITE, IOType.OUTPUT);
	}
	
	public static void oneInputElseOutput(Map<RelativeDirection, IOType> faceio) {
		faceio.put(FACING, IOType.INPUT);
		faceio.put(LEFT, IOType.OUTPUT);
		faceio.put(RIGHT, IOType.OUTPUT);
		faceio.put(OPPOSITE, IOType.OUTPUT);
	}
	
	public static void unidirectionalIO(Map<RelativeDirection, IOType> faceio) {
		faceio.put(FACING, IOType.INPUT);
		faceio.put(LEFT, IOType.NONE);
		faceio.put(RIGHT, IOType.NONE);
		faceio.put(OPPOSITE, IOType.OUTPUT);
	}
}
