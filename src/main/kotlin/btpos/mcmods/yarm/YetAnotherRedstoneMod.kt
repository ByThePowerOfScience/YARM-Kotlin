package btpos.mcmods.yarm

import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val MOD_ID = "yarm"
val LOGGER: Logger = LogManager.getLogger()

fun rl(s: String) = ResourceLocation(MOD_ID, s)

@Mod(MOD_ID)
object YetAnotherRedstoneMod {
	init {
	
	}
}