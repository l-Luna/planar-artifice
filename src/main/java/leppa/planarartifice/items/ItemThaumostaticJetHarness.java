package leppa.planarartifice.items;

import java.lang.reflect.Method;

import leppa.planarartifice.PlanarArtifice;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;

public class ItemThaumostaticJetHarness extends ItemArmor{
	
	public ItemThaumostaticJetHarness() {
		super(PAItems.jetHarness, 3, EntityEquipmentSlot.CHEST);
	}
	
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return repair.getItem() == ItemsTC.ingots && repair.getItemDamage() == 2;
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemStackIn);
        ItemStack itemstack = playerIn.getItemStackFromSlot(entityequipmentslot);
        
        if (itemstack.isEmpty()){
            playerIn.setItemStackToSlot(entityequipmentslot, itemStackIn.copy());
            itemStackIn.shrink(1);;
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }else{
            return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        }
    }
    
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack){
    	if(!world.isRemote){
    		serverTick(world, player, itemStack);
    	}
    }
    
    private void serverTick(World world, EntityPlayer player, ItemStack itemStack){
    	try{
			Method m = Entity.class.getDeclaredMethod("setFlag", int.class, boolean.class);
			m.setAccessible(true);
			m.invoke(player, 7, true);
			m.setAccessible(false);
		}catch(Exception e){
			System.out.println("Thaumostatic Jet Harness just died. Welp.");
			e.printStackTrace();
		}
    }
}