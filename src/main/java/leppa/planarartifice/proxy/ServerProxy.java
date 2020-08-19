package leppa.planarartifice.proxy;

import leppa.planarartifice.PlanarArtifice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Mod.EventBusSubscriber
public class ServerProxy extends CommonProxy{
	
	public void preInit(FMLPreInitializationEvent e){
		super.preInit(e);
		NetworkRegistry.INSTANCE.registerGuiHandler(PlanarArtifice.instance, new GuiProxy());
	}
	
	public void init(FMLInitializationEvent e){
		super.init(e);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		super.postInit(e);
	}
	
	public static String localize(String s, Object... args){
		return I18n.translateToLocalFormatted(s, args);
	}
	
	public void onTick(){
		
	}
	
	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
		return parContext.getServerHandler().player;
	}
	
	public static EntityPlayer getPlayerEntityFromContextStatic(MessageContext parContext) {
		return parContext.getServerHandler().player;
	}
}