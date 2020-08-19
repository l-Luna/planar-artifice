package leppa.planarartifice.proxy;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.blocks.PABlocks;
import leppa.planarartifice.items.PAItems;
import leppa.planarartifice.tconstruct.TConstructHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import slimeknights.tconstruct.library.materials.Material;
import net.minecraft.util.text.translation.I18n;

@Mod.EventBusSubscriber
public class CommonProxy{
	
	public void preInit(FMLPreInitializationEvent e){
		
	}
	
	public void init(FMLInitializationEvent e){
		
	}
	
	public void postInit(FMLPostInitializationEvent e){
		
	}
	
	public void onTick(){
		
	}
	
	public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
		return null; 
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		PABlocks.registerBlocks(event);
		
		if(Loader.isModLoaded("tconstruct")){
			TConstructHandler.registerBlocks(event);
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		PABlocks.registerItemBlocks(event);
		PAItems.registerItems(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event){
		PABlocks.registerModels();
	}
	
	public static String localize(String s, Object... args){
		return I18n.translateToLocalFormatted(s, args);
	}
	
	public void registerRenderers(){}

	public void setMetalMaterialRenderInfo(Material material, int colour, float shinyness, float brightness, float hueshift){}
}