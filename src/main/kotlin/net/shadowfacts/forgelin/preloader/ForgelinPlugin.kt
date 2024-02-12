package net.shadowfacts.forgelin.preloader

import cpw.mods.fml.relauncher.IFMLLoadingPlugin
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name

/**
 * @author shadowfacts
 */
@MCVersion("1.7.10")
@Name("Forgelin Core")
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