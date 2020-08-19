package leppa.planarartifice.tconstruct;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import thaumcraft.api.aura.AuraHelper;

public class TraitAuraInfusing extends AbstractTrait{
	
	static boolean loaded = false;
	
	public TraitAuraInfusing(){
		super("aurainfusing", 0xFFF442F1);
	}
	
	@SubscribeEvent
	public void onLivingExperienceDrop(LivingExperienceDropEvent event){
		EntityPlayer p = event.getAttackingPlayer();
		NBTTagCompound tag = TinkerUtil.getModifierTag(p.getHeldItemMainhand(), "aurainfusing");
		int level = ModifierNBT.readTag(tag).level;
		if(level > 0){
			int amount = event.getOriginalExperience();
			AuraHelper.addVis(p.getEntityWorld(), p.getPosition(), (float)(0.5*amount));
			event.setCanceled(true);
		}
	}
	
	public static void load(){
		loaded = true;
	}
}