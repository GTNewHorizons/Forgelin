package net.shadowfacts.forgelin

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent

/**
 * @author shadowfacts
 */
@Mod(modid = Forgelin.MOD_ID, name = Forgelin.NAME, version = Forgelin.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object Forgelin {

	const val MOD_ID = "forgelin"
	const val NAME = "Forgelin"
	const val VERSION = "1.9.3-GTNH-1.7.10-Edition"

	@Mod.EventHandler
	fun onPreInit(event: FMLPreInitializationEvent) {
		Loader.instance().modList.forEach {
			ForgelinAutomaticEventSubscriber.subscribeAutomatic(it, event.asmData, FMLCommonHandler.instance().side)
		}
	}
}
