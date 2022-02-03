package net.shadowfacts.forgelin.preloader

import cpw.mods.fml.relauncher.IFMLCallHook

/**
 * @author shadowfacts
 */
class ForgelinSetup : IFMLCallHook {
    override fun injectData(data: Map<String?, Any?>?) {
        val loader = data?.get("classLoader") as ClassLoader?
        try {
            loader?.loadClass("net.shadowfacts.forgelin.KotlinAdapter")
        } catch (e: ClassNotFoundException) {
            // this should never happen
            throw RuntimeException("Couldn't find Forgelin language adapter, this shouldn't be happening", e)
        }
    }

    @Throws(Exception::class)
    override fun call(): Void? {
        return null
    }
}