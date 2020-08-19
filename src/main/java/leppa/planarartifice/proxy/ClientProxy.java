package leppa.planarartifice.proxy;

import baubles.api.BaublesApi;
import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.blocks.BlockAlkimiumSmeltery;
import leppa.planarartifice.blocks.PABlocks;
import leppa.planarartifice.items.PAItems;
import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import leppa.planarartifice.tiles.TileTeleporter;
import leppa.planarartifice.tiles.tesr.TESRTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.materials.Material;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.gui.GuiFocalManipulator;
import thaumcraft.client.gui.plugins.GuiSliderTC;

public class ClientProxy extends CommonProxy{
	
	public static GuiSliderTC sliderRed;
	public static GuiSliderTC sliderGreen;
	public static GuiSliderTC sliderBlue;
	
	static ResourceLocation tex = new ResourceLocation(PlanarArtifice.MODID, "textures/gui/colourizer_picker.png");
	
	public void preInit(FMLPreInitializationEvent e){
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(this.getClass());
	}
	
	public void init(FMLInitializationEvent e){
		super.init(e);
	}
	
	public static String localize(String s, Object... args){
		return I18n.format(s, args);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event){
		PABlocks.registerModels();
		PAItems.registerModels();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRenderers(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileTeleporter.class, new TESRTeleporter());
	}
	
	public void onTick(){
		if(Minecraft.getMinecraft().currentScreen instanceof GuiFocalManipulator){
			PlanarArtifice.currentColourPicked = ((int)sliderRed.getSliderValue() << 16) + ((int)sliderGreen.getSliderValue() << 8) + (int)sliderBlue.getSliderValue();
		}
	}
	
	@SubscribeEvent
	public static void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event){
		if(PlanarArtifice.isSingleplayer){
			if(event.getGui() instanceof GuiFocalManipulator){
				GlStateManager.enableBlend();
				GlStateManager.color(255, 255, 255, 255);
				event.getGui().mc.getTextureManager().bindTexture(tex);
				Gui.drawModalRectWithCustomSizedTexture(195, 219, 0, 0, 111, 40, 111, 40);
				
				Minecraft.getMinecraft().fontRenderer.drawString("#", 200, 235, PlanarArtifice.currentColourPicked);
				
				sliderRed.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
				Minecraft.getMinecraft().fontRenderer.drawString("R", 213, 225, (int)sliderRed.getSliderValue() << 16);
				
				sliderGreen.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
				Minecraft.getMinecraft().fontRenderer.drawString("G", 213, 235, (int)sliderGreen.getSliderValue() << 8);
				
				sliderBlue.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
				Minecraft.getMinecraft().fontRenderer.drawString("B", 213, 245, (int)sliderBlue.getSliderValue());
			}
		}
	}
	
	@SubscribeEvent
	public static void onGuiMouseClick(GuiScreenEvent.MouseInputEvent event){
		if(PlanarArtifice.isSingleplayer){
			if(event.getGui() instanceof GuiFocalManipulator){
				final ScaledResolution scaledresolution = new ScaledResolution(event.getGui().mc);
				final int scaledWidth = scaledresolution.getScaledWidth();
				final int scaledHeight = scaledresolution.getScaledHeight();
				int mouseX = Mouse.getX() * scaledWidth / event.getGui().mc.displayWidth;
				int mouseY = scaledHeight - Mouse.getY() * scaledHeight / event.getGui().mc.displayHeight - 1;
				
				if(Mouse.isButtonDown(0)){
					sliderRed.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
					sliderGreen.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
					sliderBlue.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
				}else{
					sliderRed.mouseReleased(mouseX, mouseY);
					sliderGreen.mouseReleased(mouseX, mouseY);
					sliderBlue.mouseReleased(mouseX, mouseY);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onGuiInit(GuiScreenEvent.InitGuiEvent event){
		if(PlanarArtifice.isSingleplayer){
			if(event.getGui() instanceof GuiFocalManipulator){
				if(sliderRed == null)
					sliderRed = new GuiSliderTC(998, 221, 225, 80, 6, "Red", 0, 255, 0, false);
				sliderRed.enabled = true;
				
				if(sliderGreen == null)
					sliderGreen = new GuiSliderTC(997, 221, 235, 80, 6, "Green", 0, 255, 0, false);
				sliderGreen.enabled = true;
				
				if(sliderBlue == null)
					sliderBlue = new GuiSliderTC(996, 221, 245, 80, 6, "Blue", 0, 255, 0, false);
				sliderBlue.enabled = true;
			}
		}
	}
	
	@SubscribeEvent
	public static void renderWorldLastEvent(RenderGameOverlayEvent.Post event){
		RayTraceResult mop = Minecraft.getMinecraft().objectMouseOver;
		if(mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK && (Minecraft.getMinecraft().player.inventory.armorItemInSlot(3).getItem() == PAItems.alkimium_goggles || BaublesApi.isBaubleEquipped(Minecraft.getMinecraft().player, PAItems.alkimium_goggles) >= 0)){
			TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos());
			if(Minecraft.getMinecraft().world.getBlockState(mop.getBlockPos()).getBlock() instanceof BlockAlkimiumSmeltery && tileEntity instanceof TileAlkimiumSmeltery){
				TileAlkimiumSmeltery tile = (TileAlkimiumSmeltery) tileEntity;
				String toDraw = localize("tooltip.willlastforseconds", tile.furnaceBurnTime / 20);
				if(tile.furnaceBurnTime > 1200){
					toDraw = localize("tooltip.willlastforminutes", (int)Math.floor(tile.furnaceBurnTime / 1200), (tile.furnaceBurnTime % 1200) / 20);// "Will
				}
				Minecraft.getMinecraft().fontRenderer.drawString(toDraw, (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) - Minecraft.getMinecraft().fontRenderer.getStringWidth(toDraw) / 2, (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2) - 12, 0xffffff);
			}
			if(Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos()) instanceof IEssentiaTransport){
				String toDraw2 = "Has " + ((IEssentiaTransport)Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos())).getSuctionAmount(mop.sideHit) + " suction on this opposite side.";
				String toDraw = "Has " + ((IEssentiaTransport)Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos())).getSuctionAmount(mop.sideHit.getOpposite()) + " suction on the opposite side.";
				Minecraft.getMinecraft().fontRenderer.drawString(toDraw, (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) - Minecraft.getMinecraft().fontRenderer.getStringWidth(toDraw) / 2, (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2) - 12, 0xffffff);
				Minecraft.getMinecraft().fontRenderer.drawString(toDraw2, (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) - Minecraft.getMinecraft().fontRenderer.getStringWidth(toDraw2) / 2, (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2) + 4, 0xffffff);
			}
		}
		boolean willRenderAuraMeter = false;
		for(ItemStack i : Minecraft.getMinecraft().player.inventory.mainInventory) if(i.getItem() == PAItems.aura_meter) willRenderAuraMeter = true;
		if(willRenderAuraMeter){
			String usableVis = localize("tooltip.usablevis") + " " + (int)(AuraHelper.getVis(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition()) - AuraHelper.getFlux(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition()));
			Minecraft.getMinecraft().fontRenderer.drawString(usableVis, 5, 5, 0xffffff);
			String baseVis = localize("tooltip.basevis") + " " + AuraHelper.getAuraBase(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
			Minecraft.getMinecraft().fontRenderer.drawString(baseVis, 5, 5 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1, 0xffffff);
		}
	}
	
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx){
		return(ctx.side.isClient() ? Minecraft.getMinecraft().player : ServerProxy.getPlayerEntityFromContextStatic(ctx));
	}
	
	public void setMetalMaterialRenderInfo(Material material, int colour, float shinyness, float brightness, float hueshift){
		material.setRenderInfo(new MaterialRenderInfo.Metal(colour, 0.7f, 0f, 0.1f));
	}
}