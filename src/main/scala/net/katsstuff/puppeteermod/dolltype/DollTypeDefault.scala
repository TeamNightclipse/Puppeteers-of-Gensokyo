package net.katsstuff.puppeteermod.dolltype

import net.katsstuff.puppeteermod.client.ModelDollBase
import net.katsstuff.puppeteermod.entity.EntityDoll
import net.katsstuff.puppeteermod.entity.ai.EntityAIDollFollowOwner
import net.katsstuff.puppeteermod.item.ItemDoll
import net.katsstuff.puppeteermod.network.{DollJumping, DollSneaking, PuppeteersPacketHandler}
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.{EntityAILookIdle, EntityAISwimming, EntityAIWander, EntityAIWatchClosest}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

trait DollTypeDefault extends DollType {

  protected var serverJumpState = false
  protected var serverSneakState = false

  override def health:    Double = 8D
  override def speed:     Double = 0.075D
  override def width:     Float  = 0.35F
  override def height:    Float  = 0.9F
  override def eyeHeight: Float  = height * 0.8F

  @SideOnly(Side.CLIENT)
  override def model(doll: EntityDoll): ModelBiped = ModelDollBase
  override def itemModel: ModelResourceLocation = {
    val name = getRegistryName
    new ModelResourceLocation(new ResourceLocation(name.getResourceDomain, s"doll/${name.getResourcePath}"), "inventory")
  }

  override def initializeAI(doll: EntityDoll): Unit = {
    doll.tasks.addTask(1, new EntityAISwimming(doll))
    doll.tasks.addTask(4, new EntityAIDollFollowOwner(doll, 4F, 10F, 3F))
    doll.tasks.addTask(7, new EntityAIWander(doll, 3F))
    doll.tasks.addTask(10, new EntityAIWatchClosest(doll, classOf[EntityLivingBase], 8F))
    doll.tasks.addTask(12, new EntityAILookIdle(doll))
  }

  override def createStack(doll: EntityDoll): ItemStack = ItemDoll.createStack(this)

  override def canControlOtherDolls(doll: EntityDoll): Boolean = false

  @SideOnly(Side.CLIENT) override def onJump(doll: EntityDoll, isPressed: Boolean): Unit = {
    if(isPressed != serverJumpState) {
      PuppeteersPacketHandler.sendToServer(DollJumping(doll.getEntityId, isPressed))
      serverJumpState = isPressed
    }
  }

  @SideOnly(Side.CLIENT) override def onSneak(doll: EntityDoll, isPressed: Boolean): Unit = {
    if(isPressed != serverSneakState) {
      PuppeteersPacketHandler.sendToServer(DollSneaking(doll.getEntityId, isPressed))
      serverSneakState = isPressed
    }
  }


  @SideOnly(Side.CLIENT) override def onAttack(doll: EntityDoll, isPressed: Boolean): Unit = ()
  @SideOnly(Side.CLIENT) override def onUseItem(doll: EntityDoll, isPressed: Boolean): Unit = ()
  @SideOnly(Side.CLIENT) override def onPickBlock(doll: EntityDoll, isPressed: Boolean): Unit = ()

  override def onStringed(doll: EntityDoll): Unit = {}
  override def onUnstringed(doll: EntityDoll): Unit = {}

  override def needStringToMove(doll: EntityDoll): Boolean = true

  override def onTick(doll: EntityDoll): Unit = {}
}
