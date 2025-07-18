package net.shadowfacts.forgelin.coroutines

import com.gtnewhorizon.gtnhlib.util.ServerThreadUtil
import cpw.mods.fml.common.Loader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

internal val isGTNHLibLoaded by lazy { Loader.isModLoaded("gtnhlib") }

/**
 * The dispatcher that runs the block in the server main thread.
 *
 * It's based on the implementation of GTNHLib in [ServerThreadUtil]. Thus GTNHLib is required for this dispatcher.
 * The coroutine is invoked immediately if the caller is on the server thread; otherwise, it's invoked in the next tick before the pre-tick phase.
 *
 * Also be aware that if you dispatch coroutines when the server isn't running, it throws [IllegalStateException].
 * For the details when the dispatcher is ready, see [ServerThreadUtil].
 */
@Suppress("UnusedReceiverParameter", "unused") // use the receiver to prevent polluting the top-level functions
val Dispatchers.MinecraftServer: CoroutineDispatcher
    get() = if(isGTNHLibLoaded) MinecraftServerDispatcher
    else error("GTNHLib is required for Dispatchers.MinecraftServer.")

internal object MinecraftServerDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        ServerThreadUtil.addScheduledTask(block)
    }
}
