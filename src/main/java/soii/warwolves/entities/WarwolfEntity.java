package soii.warwolves.entities;

import java.util.UUID;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WarwolfEntity extends WolfEntity implements IRideable{
	public WarwolfEntity(EntityType<? extends WolfEntity> entityType, World world) {
		super(entityType, world);
		getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
		getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D);
	}
	@Override
	protected float getStandingEyeHeight(Pose pose, EntitySize size) {
		return size.height * 0.8F;
    }
	
	@Override
	public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
    ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
    Item item = itemstack.getItem();
    if (this.world.isRemote) {
    	boolean flag = (isOwner((LivingEntity)p_230254_1_) || isTamed() || (item == Items.BONE && !isTamed() && !func_233678_J__()));
    	return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
    } 
    if (isTamed()) {
      if (isBreedingItem(itemstack) && getHealth() < getMaxHealth()) {
        if (!p_230254_1_.abilities.isCreativeMode)
          itemstack.shrink(1); 
        heal(item.getFood().getHealing());
        return ActionResultType.SUCCESS;
      } 
      if (!(item instanceof DyeItem)) {
        ActionResultType actionresulttype = super.func_230254_b_(p_230254_1_, p_230254_2_);
        if ((!actionresulttype.isSuccessOrConsume() || isChild()) && isOwner((LivingEntity)p_230254_1_)) {
          func_233687_w_(!isSitting());
          this.isJumping = false;
          this.navigator.clearPath();
          setAttackTarget((LivingEntity)null);
          return ActionResultType.SUCCESS;
        } 
        return actionresulttype;
      } 
      DyeColor dyecolor = ((DyeItem)item).getDyeColor();
      if (dyecolor != getCollarColor()) {
        setCollarColor(dyecolor);
        if (!p_230254_1_.abilities.isCreativeMode)
          itemstack.shrink(1); 
        return ActionResultType.SUCCESS;
      } 
    } else if (item == Items.BONE && !func_233678_J__()) {
      if (!p_230254_1_.abilities.isCreativeMode)
        itemstack.shrink(1); 
      if (this.rand.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, p_230254_1_)) {
        setTamedBy(p_230254_1_);
        this.navigator.clearPath();
        setAttackTarget((LivingEntity)null);
        func_233687_w_(true);
        this.world.setEntityState((Entity)this, (byte)7);
      } else {
        this.world.setEntityState((Entity)this, (byte)6);
      } 
      return ActionResultType.SUCCESS;
    } 
    return super.func_230254_b_(p_230254_1_, p_230254_2_);
  }
  
  @OnlyIn(Dist.CLIENT)
  public void handleStatusUpdate(byte p_70103_1_) {
    if (p_70103_1_ == 8) {
      this.isShaking = true;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
    } else if (p_70103_1_ == 56) {
      func_242326_eZ();
    } else {
      super.handleStatusUpdate(p_70103_1_);
    } 
  }
  
  @OnlyIn(Dist.CLIENT)
  public float getTailRotation() {
    if (func_233678_J__())
      return 1.5393804F; 
    return isTamed() ? ((0.55F - (getMaxHealth() - getHealth()) * 0.02F) * 3.1415927F) : 0.62831855F;
  }
  
  public boolean isBreedingItem(ItemStack p_70877_1_) {
    Item item = p_70877_1_.getItem();
    return (item.isFood() && item.getFood().isMeat());
  }
  
  public int getMaxSpawnedInChunk() {
    return 8;
  }
  
  public int getAngerTime() {
    return ((Integer)this.dataManager.get(field_234232_bz_)).intValue();
  }
  
  public void setAngerTime(int p_230260_1_) {
    this.dataManager.set(field_234232_bz_, Integer.valueOf(p_230260_1_));
  }
  
  public void func_230258_H__() {
    setAngerTime(field_234230_bG_.getRandomWithinRange(this.rand));
  }
  
  @Nullable
  public UUID getAngerTarget() {
    return this.field_234231_bH_;
  }
  
  public void setAngerTarget(@Nullable UUID p_230259_1_) {
    this.field_234231_bH_ = p_230259_1_;
  }
  
  public DyeColor getCollarColor() {
    return DyeColor.byId(((Integer)this.dataManager.get(COLLAR_COLOR)).intValue());
  }
  
  public void setCollarColor(DyeColor p_175547_1_) {
    this.dataManager.set(COLLAR_COLOR, Integer.valueOf(p_175547_1_.getId()));
  }
  
  public WolfEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
    WolfEntity wolfentity = (WolfEntity)EntityType.WOLF.create((World)p_241840_1_);
    UUID uuid = getOwnerId();
    if (uuid != null) {
      wolfentity.setOwnerId(uuid);
      wolfentity.setTamed(true);
    } 
    return wolfentity;
  }
  
  public void setBegging(boolean p_70918_1_) {
    this.dataManager.set(BEGGING, Boolean.valueOf(p_70918_1_));
  }
  
  public boolean canMateWith(AnimalEntity p_70878_1_) {
    if (p_70878_1_ == this)
      return false; 
    if (!isTamed())
      return false; 
    if (!(p_70878_1_ instanceof WolfEntity))
      return false; 
    WolfEntity wolfentity = (WolfEntity)p_70878_1_;
    if (!wolfentity.isTamed())
      return false; 
    if (wolfentity.isEntitySleeping())
      return false; 
    return (isInLove() && wolfentity.isInLove());
  }
  
  public boolean isBegging() {
    return ((Boolean)this.dataManager.get(BEGGING)).booleanValue();
  }
  
  public boolean shouldAttackEntity(LivingEntity p_142018_1_, LivingEntity p_142018_2_) {
    if (!(p_142018_1_ instanceof net.minecraft.entity.monster.CreeperEntity) && !(p_142018_1_ instanceof net.minecraft.entity.monster.GhastEntity)) {
      if (p_142018_1_ instanceof WolfEntity) {
        WolfEntity wolfentity = (WolfEntity)p_142018_1_;
        return (!wolfentity.isTamed() || wolfentity.getOwner() != p_142018_2_);
      } 
      if (p_142018_1_ instanceof PlayerEntity && p_142018_2_ instanceof PlayerEntity && !((PlayerEntity)p_142018_2_).canAttackPlayer((PlayerEntity)p_142018_1_))
        return false; 
      if (p_142018_1_ instanceof AbstractHorseEntity && ((AbstractHorseEntity)p_142018_1_).isTame())
        return false; 
      return (!(p_142018_1_ instanceof TameableEntity) || !((TameableEntity)p_142018_1_).isTamed());
    } 
    return false;
  }
  
  public boolean canBeLeashedTo(PlayerEntity p_184652_1_) {
    return (!func_233678_J__() && super.canBeLeashedTo(p_184652_1_));
  }
  
  @OnlyIn(Dist.CLIENT)
  public Vector3d func_241205_ce_() {
    return new Vector3d(0.0D, (0.6F * getEyeHeight()), (getWidth() * 0.4F));
  }
  
  class AvoidEntityGoal<T extends LivingEntity> extends net.minecraft.entity.ai.goal.AvoidEntityGoal<T> {
    private final WolfEntity wolf;
    
    public AvoidEntityGoal(WolfEntity p_i47251_2_, Class<T> p_i47251_3_, float p_i47251_4_, double p_i47251_5_, double p_i47251_7_) {
      super((CreatureEntity)p_i47251_2_, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
      this.wolf = p_i47251_2_;
    }
    
    public boolean shouldExecute() {
      if (super.shouldExecute() && this.avoidTarget instanceof LlamaEntity)
        return (!this.wolf.isTamed() && avoidLlama((LlamaEntity)this.avoidTarget)); 
      return false;
    }
    
    private boolean avoidLlama(LlamaEntity p_190854_1_) {
      return (p_190854_1_.getStrength() >= WolfEntity.this.rand.nextInt(5));
    }
    
    public void startExecuting() {
      WolfEntity.this.setAttackTarget((LivingEntity)null);
      super.startExecuting();
    }
    
    public void tick() {
      WolfEntity.this.setAttackTarget((LivingEntity)null);
      super.tick();
    }
  }

@Override
public UUID getAngerTarget() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public boolean boost() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public float getMountedSpeed() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void travelTowards(Vector3d arg0) {
	// TODO Auto-generated method stub
	
}
}
