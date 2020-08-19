package leppa.planarartifice.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.IVisDiscountGear;

public class ItemMirroredAmulet extends Item implements IBauble, IVisDiscountGear{
	
	@Override
	public int getVisDiscount(ItemStack var1, EntityPlayer var2){
		return 3;
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0){
		return BaubleType.AMULET;
	}
	
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.RARE;
	}
}