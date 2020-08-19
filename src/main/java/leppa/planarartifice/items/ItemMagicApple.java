package leppa.planarartifice.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemMagicApple extends ItemFood{

	public ItemMagicApple(){
		super(6, 1.4F, false);
	}
	
	public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }
	
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player){
        if (!worldIn.isRemote){
        		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
            	player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
            	player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
            	player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 4800, 0));
        }
    }
}