package soii.warwolves.entities;

import java.util.UUID;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
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
	public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
	    ItemStack itemstack = player.getHeldItem(hand);
	    Item item = itemstack.getItem();
	    if (this.world.isRemote) {
	    	boolean flag = (isOwner((LivingEntity)player) || isTamed() );
	    	return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
	    } 
	    if (isTamed()) {
	    	if (isBreedingItem(itemstack) && getHealth() < getMaxHealth()) {
	    		if (!player.abilities.isCreativeMode)
	    			itemstack.shrink(1);
	    			heal(item.getFood().getHealing());
	    			return ActionResultType.SUCCESS;
	    	} 
	    	if (!(item instanceof DyeItem)) {
	    		ActionResultType actionresulttype = super.func_230254_b_(player, hand);
	    		if ((!actionresulttype.isSuccessOrConsume() || isChild()) && isOwner((LivingEntity)player)) {
	    			if(player.isSneaking()) {
		    			func_233687_w_(!isSitting());
		    			this.isJumping = false;
		    			this.navigator.clearPath();
		    			setAttackTarget((LivingEntity)null);
		    			return ActionResultType.SUCCESS;
	    			}
	    		} 
	    		return actionresulttype;
	    	} 
	    	DyeColor dyecolor = ((DyeItem)item).getDyeColor();
	    	if (dyecolor != getCollarColor()) {
	    		setCollarColor(dyecolor);
	    		if (!player.abilities.isCreativeMode)
	    			itemstack.shrink(1); 
	    		return ActionResultType.SUCCESS;
	    	} 
	    }
	    return super.func_230254_b_(player, hand);
	}
  
	@Override
	public int getMaxSpawnedInChunk() {
		return 0;
	}
	
	@Override
	public boolean canMateWith(AnimalEntity p_70878_1_) {
		return false;
	}
	
	@Override
	public UUID getAngerTarget() {
		return null;
	}
	
	@Override
	public boolean boost() {
		return false;
	}
	
	@Override
	public float getMountedSpeed() {
		return 0;
	}
	
	@Override
	public void travelTowards(Vector3d arg0) {
		
	}
}
