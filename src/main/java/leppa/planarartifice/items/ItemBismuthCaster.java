package leppa.planarartifice.items;

import java.util.List;

import leppa.planarartifice.PlanarArtifice;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.FocusPackage;
import thaumcraft.api.casters.IFocusBlockPicker;
import thaumcraft.api.casters.IFocusElement;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.items.casters.ItemFocus;

public class ItemBismuthCaster extends ItemCaster{
	
	float costMultiplier = 1.5f;
	
	public ItemBismuthCaster(){
		super("bismuth_caster", 0);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack focusStack = this.getFocusStack(player.getHeldItem(hand));
		ItemFocus focus = this.getFocus(player.getHeldItem(hand));
		if(focus != null && !PAFocusEngine.isOnCooldown((EntityLivingBase)player)){
			PAFocusEngine.setCooldown((EntityLivingBase)player, focus.getActivationTime(focusStack));
			FocusPackage core = ItemFocus.getPackage(focusStack);
			if(player.isSneaking()){
				for(IFocusElement fe : core.nodes){
					if(!(fe instanceof IFocusBlockPicker) || !player.isSneaking())
						continue;
					return new ActionResult(EnumActionResult.PASS, (Object)player.getHeldItem(hand));
				}
			}
			if(world.isRemote){ return new ActionResult(EnumActionResult.SUCCESS, (Object)player.getHeldItem(hand)); }
			if(this.consumeVis(player.getHeldItem(hand), player, focus.getVisCost(focusStack) * costMultiplier, false, false)){
				PAFocusEngine.castFocusPackageWithMultiplier((EntityLivingBase)player, core, 2);
				player.swingArm(hand);
				return new ActionResult(EnumActionResult.SUCCESS, (Object)player.getHeldItem(hand));
			}
			return new ActionResult(EnumActionResult.FAIL, (Object)player.getHeldItem(hand));
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add("2x" + PlanarArtifice.proxy.localize("tooltip.casterpower"));
		tooltip.add("1.5x" + PlanarArtifice.proxy.localize("tooltip.castercost"));
    }
}