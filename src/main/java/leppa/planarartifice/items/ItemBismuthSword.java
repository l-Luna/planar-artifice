package leppa.planarartifice.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.aura.AuraHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ItemBismuthSword extends ItemSword{

	public static final float BONUS_THRESHOLD = 10;
	public static final float BONUS_DAMAGE = 20f;

	public ItemBismuthSword(ToolMaterial material){
		super(material);
	}

	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		// Get local aura
		float aura = AuraHelper.getVis(target.world, new BlockPos(target));
		float baseaura = AuraHelper.getAuraBase(target.world, new BlockPos(target));
		// If high enough above baseline,
		if(baseaura - aura >= BONUS_THRESHOLD){
			// Deal bonus damage
			target.hurtResistantTime = 0;
			target.hurtTime = 0;
			target.attackEntityFrom(DamageSource.MAGIC, BONUS_DAMAGE);
			// Show some fancy particles? Not yet
		}
		return super.hitEntity(stack, target, attacker);
	}
}