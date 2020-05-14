package net.shadowfacts.forgelin

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.ModContainer
import cpw.mods.fml.common.discovery.ASMDataTable
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData
import cpw.mods.fml.common.discovery.asm.ModAnnotation
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.jvmName

object ForgelinAutomaticEventSubscriber {
	private val DEFAULT_SUBSCRIPTION_SIDES = EnumSet.allOf(Side::class.java)
	private val LOGGER = LogManager.getLogger(ForgelinAutomaticEventSubscriber::class.java)

	private val unregistered = mutableSetOf<Class<*>>()
	private val registered = mutableSetOf<Any>()

	fun subscribeAutomatic(mod: ModContainer, asm: ASMDataTable, currentSide: Side) {
		val modAnnotations = asm.getAnnotationsFor(mod) ?: return

		val containedMods = modAnnotations.get(Mod::class.java.name)
		val subscribers = modAnnotations.get(EventBusSubscriber::class.jvmName)
				.filter { parseTargetSides(it).contains(currentSide) }

		val loader = Loader.instance().modClassLoader

		containedMods.forEach { containedMod ->
			val containedModId = containedMod.annotationInfo["modid"] as String

			if (containedMod.annotationInfo["modLanguageAdapter"] != KotlinAdapter::class.qualifiedName) {
				LOGGER.debug("Skipping @EventBusSubscriber injection for {} since it does not use KotlinAdapter", containedModId)
				return@forEach
			}

			LOGGER.debug("Attempting to register Kotlin @EventBusSubscriber objects for {}", containedModId)

			subscribers.forEach subscriber@{  subscriber ->
				try {
					val ownerModId = parseModId(containedMods, subscriber)
					if (ownerModId.isNullOrEmpty()) {
						LOGGER.debug("Could not determine owning mod for @EventBusSubscriber on {} for mod {}", subscriber.className, mod.modId)
						return@subscriber
					}

					if (containedModId != ownerModId) {
						LOGGER.debug("Skipping @EventBusSubscriber injection for {} since it is not for mod {}", subscriber.className, containedModId)
						return@subscriber
					}

					val subscriberClass = Class.forName(subscriber.className, false, loader) ?: return@subscriber
					val kotlinClass = subscriberClass.kotlin
					val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: return@subscriber

					if (!hasStaticEventHandlers(subscriberClass) && subscriberClass !in unregistered) {
						FMLCommonHandler.instance().bus().unregister(subscriberClass)
						MinecraftForge.EVENT_BUS.unregister(subscriberClass)
						unregistered += subscriberClass
						LOGGER.debug("Unregistered static @EventBusSubscriber class {}", subscriber.className)
					}
					if (hasObjectEventHandlers(objectInstance) && objectInstance !in registered) {
						FMLCommonHandler.instance().bus().register(objectInstance)
						MinecraftForge.EVENT_BUS.register(objectInstance)
						registered += objectInstance
						LOGGER.debug("Registered @EventBusSubscriber object instance {}", subscriber.className)
					}

				} catch (e: Throwable) {
					LOGGER.error("An error occurred trying to load an @EventBusSubscriber object {} for modid {}", mod.modId, e)
					throw Exception(e)
				}
			}
		}
	}

	private fun hasObjectEventHandlers(objectInstance: Any): Boolean {
		return objectInstance.javaClass.methods.any {
			!Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(SubscribeEvent::class.java)
		}
	}

	private fun hasStaticEventHandlers(clazz: Class<*>): Boolean {
		return clazz.methods.any {
			Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(SubscribeEvent::class.java)
		}
	}

	private fun parseModId(containedMods: MutableSet<ASMData>, subscriber: ASMData): String? {
		val parsedModId: String? = subscriber.annotationInfo["modid"] as? String
		if (parsedModId.isNullOrEmpty()) {
			return parsedModId
		}

		return getOwnerModID(containedMods, subscriber)
	}

	@Suppress("UNCHECKED_CAST")
	private fun parseTargetSides(subscriber: ASMData): EnumSet<Side> {
		val parsedSides: List<ModAnnotation.EnumHolder>? = subscriber.annotationInfo["value"] as? List<ModAnnotation.EnumHolder>
		if (parsedSides != null) {
			val targetSides = EnumSet.noneOf(Side::class.java)
			val value = ModAnnotation.EnumHolder::class.java.getDeclaredField("value")
			value.isAccessible = true
			for (parsed in parsedSides) {
				targetSides.add(Side.valueOf(value.get(parsed) as String))
			}
			return targetSides
		}

		return DEFAULT_SUBSCRIPTION_SIDES
	}

	private fun getOwnerModID(mods: Set<ASMData>, targ: ASMData): String? {
		return when (mods.size) {
			1 -> {
				mods.first().annotationInfo["modid"] as String?
			}
			else -> {
				mods.firstOrNull{ e -> targ.className.startsWith(e.className)}?.annotationInfo?.get("modid") as String?
			}
		}
	}

}
