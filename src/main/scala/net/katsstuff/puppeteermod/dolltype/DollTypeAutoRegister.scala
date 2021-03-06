package net.katsstuff.puppeteermod.dolltype

import net.katsstuff.puppeteermod.PuppeteerMod
import net.minecraftforge.fml.common.registry.GameRegistry

abstract class DollTypeAutoRegister(val name: String) extends DollType {
  setRegistryName(name)
  PuppeteerMod.proxy.bakeDoll(this)
}
