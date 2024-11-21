@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package btpos.mcmods.yarm.util

class SetView<T, R>(val original: Set<T>, val getter: (T) -> R, val setter: (R) -> T) : Set<R> {
	override val size = original.size
	
}