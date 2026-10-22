package net.shadowfacts.forgelin

import cpw.mods.fml.common.FMLModContainer
import cpw.mods.fml.common.ILanguageAdapter
import cpw.mods.fml.common.ModContainer
import cpw.mods.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Forge {@link ILanguageAdapter} for Kotlin
 * Usage: Set the {@code modLanguageAdapter} field in your {@code @Mod} annotation to {@code net.shadowfacts.forgelin.KotlinAdapter}
 * @author shadowfacts
 */
class KotlinAdapter : ILanguageAdapter {

	private val log = LogManager.getLogger("KotlinAdapter")

	override fun supportsStatics(): Boolean {
		return false
	}

	override fun setProxy(target: Field, proxyTarget: Class<*>, proxy: Any) {
		log.debug("Setting proxy: ${target.declaringClass.simpleName}.${target.name} -> $proxy")

		// objectInstance is not null if it's a Kotlin object, so set the value on the object
		// if it is null, set the value on the static field
		target.set(proxyTarget.kotlin.objectInstance, proxy)
	}

	override fun getNewInstance(container: FMLModContainer, objectClass: Class<*>, classLoader: ClassLoader, factoryMarkedAnnotation: Method?): Any {
		log.debug("FML has asked for ${objectClass.simpleName} to be constructed")
		return declaredObjectInstance(objectClass) ?: objectClass.kotlin.objectInstance ?: objectClass.newInstance()
	}

	// A Kotlin object is a class with a static INSTANCE of itself and no public constructor. Reading that field
	// directly saves starting up kotlin-reflect just to find it.
	private fun declaredObjectInstance(objectClass: Class<*>): Any? {
		val field = try {
			objectClass.getDeclaredField("INSTANCE")
		} catch (e: NoSuchFieldException) {
			return null
		}
		if (field.type != objectClass || !Modifier.isStatic(field.modifiers)) return null
		if (objectClass.declaredConstructors.any { Modifier.isPublic(it.modifiers) }) return null
		field.isAccessible = true
		return field.get(null)
	}

	override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) {
		// Nothing to do; FML's got this covered for Kotlin
	}

}