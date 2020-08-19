package leppa.planarartifice.multiblocks;

import leppa.planarartifice.blocks.PABlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApi.BluePrint;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.Part;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;

public class PAMultiblocks{
	
	public static BluePrint mirromerous_teleporter;
	
	public static void init(){
		//To name a Part, put where it is + what it turns into.
		Part BP = new Part(BlocksTC.stoneArcaneBrick, new ItemStack(PABlocks.teleporter_placeholder));
		Part MT = new Part(BlocksTC.stoneArcaneBrick, new ItemStack(PABlocks.teleporter));
		Part TP = new Part(PABlocks.teleporter_matrix, new ItemStack(PABlocks.teleporter_placeholder));
		mirromerous_teleporter = new BluePrint("MIRRORTELEPORTER", new Part[][][] {{{TP}}, {{MT}}, {{BP}}}, new ItemStack[]{new ItemStack(PABlocks.teleporter_matrix), new ItemStack(BlocksTC.stoneArcaneBrick, 2)});
		IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("MIRRORTELEPORTER", mirromerous_teleporter.getParts()));
		ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation("planarartifice:mirror_teleporter"), mirromerous_teleporter);
	}
}