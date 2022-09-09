package net.shadowfacts.forgelin

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent

/**
 * @author shadowfacts
 */
// kotlin doesn't work with the current variable substitute system, so...
@Mod(modid = Forgelin.MOD_ID, name = Forgelin.NAME, version = "1.9.6-GTNH", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object Forgelin {

	const val MOD_ID = "forgelin"
	const val NAME = "Forgelin"

	@Mod.EventHandler
	fun onPreInit(event: FMLPreInitializationEvent) {
		Loader.instance().modList.forEach {
			ForgelinAutomaticEventSubscriber.subscribeAutomatic(it, event.asmData, FMLCommonHandler.instance().side)
		}
	}
}
