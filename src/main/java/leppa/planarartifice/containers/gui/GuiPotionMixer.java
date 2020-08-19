package leppa.planarartifice.containers.gui;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.containers.ContainerFluxScrubber;
import leppa.planarartifice.containers.ContainerPotionMixer;
import leppa.planarartifice.tiles.TileFluxScrubber;
import leppa.planarartifice.tiles.TilePotionMixer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiPotionMixer extends GuiContainer{
	
	protected int xSize = 176;
	protected int ySize = 166;
	
	protected TilePotionMixer tile;
	
	ResourceLocation tex = new ResourceLocation(PlanarArtifice.MODID, "textures/gui/gui_potion_mixer.png");
	
	public GuiPotionMixer(Container inventorySlotsIn){
		super(inventorySlotsIn);
	}
	
	public GuiPotionMixer(InventoryPlayer player, TilePotionMixer tileEntity){
		this(new ContainerPotionMixer(player, tileEntity));
		tile = tileEntity;
	}
	
	public void initGui(){
		buttonList.add(new GuiButton(0, (width / 2) - (xSize / 2), (height / 2) - (ySize / 2), 24, 5, ""));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		GlStateManager.enableBlend();
		this.mc.getTextureManager().bindTexture(tex);
        this.drawModalRectWithCustomSizedTexture((width / 2) - (xSize / 2), (height / 2) - (ySize / 2), 0, 0, xSize, ySize, xSize, ySize);
        GlStateManager.disableBlend();
	}
}