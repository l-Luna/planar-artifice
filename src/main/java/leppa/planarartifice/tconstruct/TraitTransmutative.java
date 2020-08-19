package leppa.planarartifice.tconstruct;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.capability.projectile.CapabilityTinkerProjectile;
import slimeknights.tconstruct.library.capability.projectile.ITinkerProjectile;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTraits;
import thaumcraft.api.items.ItemsTC;

import java.util.ArrayList;
import java.util.HashMap;

public class TraitTransmutative extends AbstractTrait{
	
	public static HashMap<Item, Item> transmutations = new HashMap<>();
	static boolean loaded = false;
	
	public TraitTransmutative(){
		super("transmutative", 0xFF0DCE53);
	}
	
	static{
		transmutations.put(Items.ROTTEN_FLESH, Items.CHICKEN);
		transmutations.put(Items.GUNPOWDER, Items.GLOWSTONE_DUST);
		transmutations.put(Items.STRING, Items.FEATHER);
		transmutations.put(Items.ENDER_PEARL, Items.ENDER_EYE);
		transmutations.put(Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE);
		transmutations.put(Items.SLIME_BALL, Items.MAGMA_CREAM);
		transmutations.put(Items.GLOWSTONE_DUST, Items.BLAZE_POWDER);
		transmutations.put(Items.IRON_INGOT, Items.GOLD_INGOT);
		transmutations.put(Items.GOLD_INGOT, Items.IRON_INGOT);
		transmutations.put(Items.IRON_NUGGET, Items.GOLD_NUGGET);
		transmutations.put(Items.GOLD_NUGGET, Items.IRON_NUGGET);
		transmutations.put(Items.SUGAR, Items.REDSTONE);
		transmutations.put(Items.REDSTONE, ItemsTC.salisMundus);
		transmutations.put(Items.BONE, Items.COAL);
		transmutations.put(Items.DIAMOND, Items.EMERALD);
		transmutations.put(Items.EMERALD, Items.DIAMOND);
		transmutations.put(Items.BEETROOT_SEEDS, Items.WHEAT_SEEDS);
		transmutations.put(Items.WHEAT_SEEDS, Items.MELON_SEEDS);
	}
	
	@Override
	public boolean canApplyTogether(IToolMod toolmod){
		// Incompatible with Squeaky, Silk Touch, and Autosmelt
		return !toolmod.getIdentifier().equals(TinkerTraits.squeaky.getIdentifier()) && !toolmod.getIdentifier().equals(TinkerModifiers.modSilktouch.getIdentifier()) && !toolmod.getIdentifier().equals(TinkerTraits.autosmelt.getIdentifier());
	}
	
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event){
		if(event.getSource().getTrueSource() instanceof EntityPlayer){
			ItemStack item = CapabilityTinkerProjectile.getTinkerProjectile(event.getSource()).map(ITinkerProjectile::getItemStack).orElse(((EntityPlayer)event.getSource().getTrueSource()).getHeldItem(EnumHand.MAIN_HAND));
			NBTTagCompound tag = TinkerUtil.getModifierTag(item, "transmutative");
			int level = ModifierNBT.readTag(tag).level;
			if(level > 0){
				ArrayList<ItemStack> items = new ArrayList<>();
				for(EntityItem i : event.getDrops()){
					ItemStack tem = i.getItem();
					if(transmutations.containsKey(tem.getItem())) items.add(new ItemStack(transmutations.get(tem.getItem()), tem.getCount()));
					else items.add(tem);
				}
				for(int i = 0; i < event.getDrops().size(); i++) event.getDrops().get(i).setItem(items.get(i));
			}
		}
	}
	
	public static void load(){
		loaded = true;
	}
}