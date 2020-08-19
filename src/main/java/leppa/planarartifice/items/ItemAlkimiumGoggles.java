package leppa.planarartifice.items;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import leppa.planarartifice.PlanarArtifice;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.IVisDiscountGear;

public class ItemAlkimiumGoggles extends ItemArmor implements IRevealer, IGoggles, IVisDiscountGear, IBauble{
	
	public ItemAlkimiumGoggles(){
		super(PAItems.alkimium, 3, EntityEquipmentSlot.HEAD);
		setUnlocalizedName("alkimium_goggles");
		setRegistryName("alkimium_goggles");
		setCreativeTab(PlanarArtifice.creativetab);
		setMaxStackSize(1);
	}
	
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player){
		return true;
	}

	public int getVisDiscount(ItemStack stack, EntityPlayer player){
		return 10;
	}

	public BaubleType getBaubleType(ItemStack itemstack){
		return BaubleType.HEAD;
	}
	
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("§3Alkimium");
	}
	
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.RARE;
	}
	
	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player){
		return true;
	}
}
