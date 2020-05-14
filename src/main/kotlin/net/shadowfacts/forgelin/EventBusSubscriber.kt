package net.shadowfacts.forgelin

/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * Taken from forge for Minecraft v1.12.2 net.minecraftforge.fml.common.Mod;
 *
 * https://github.com/MinecraftForge/MinecraftForge/blob/1.12.x/src/main/java/net/minecraftforge/fml/common/Mod.java
 */


import cpw.mods.fml.relauncher.Side

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
annotation class EventBusSubscriber(vararg val value: Side = [Side.CLIENT, Side.SERVER],
                                             /**
                                                  * Optional value, only nessasary if tis annotation is not on the same class that has a @Mod annotation.
                                                  * Needed to prevent early classloading of classes not owned by your mod.
                                                  */
                                             val modid: String = "")