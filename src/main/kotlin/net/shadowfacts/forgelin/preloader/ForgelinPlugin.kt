package net.shadowfacts.forgelin.preloader

import cpw.mods.fml.relauncher.IFMLLoadingPlugin

/**
 * @author shadowfacts
 */
class ForgelinPlugin : IFMLLoadingPlugin {
    override fun getASMTransformerClass(): Array<String?>? {
        return arrayOfNulls(0)
    }

    override fun getModContainerClass(): String? {
        return null
    }

    override fun getSetupClass(): String? {
        return "net.shadowfacts.forgelin.preloader.ForgelinSetup"
    }

    override fun injectData(data: Map<String?, Any?>?) {}
    override fun getAccessTransformerClass(): String? {
        return null
    }
}