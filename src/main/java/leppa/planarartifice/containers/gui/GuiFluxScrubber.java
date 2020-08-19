package leppa.planarartifice.containers.gui;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.containers.ContainerFluxScrubber;
import leppa.planarartifice.tiles.TileFluxScrubber;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiFluxScrubber extends GuiContainer{
	
	protected int xSize = 176;
	protected int ySize = 166;
	
	protected TileFluxScrubber tile;
	ResourceLocation tex = new ResourceLocation(PlanarArtifice.MODID, "textures/gui/gui_flux_scrubber.png");
	ResourceLocation extra = new ResourceLocation(PlanarArtifice.MODID, "textures/gui/gui_flux_scrubber_extra.png");
	
	public GuiFluxScrubber(Container inventorySlotsIn){
		super(inventorySlotsIn);
	}
	
	public GuiFluxScrubber(InventoryPlayer player, TileFluxScrubber tileEntity){
		this(new ContainerFluxScrubber(player, tileEntity));
		tile = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		GlStateManager.enableBlend();
		super.drawDefaultBackground();
		this.mc.getTextureManager().bindTexture(tex);
        this.drawModalRectWithCustomSizedTexture((width / 2) - (xSize / 2), (height / 2) - (ySize / 2), 0, 0, xSize, ySize, xSize, ySize);
        GlStateManager.disableBlend();
        int prog = tile.getSaltTimeScaled();
        if(prog != 0){
        	this.mc.getTextureManager().bindTexture(extra);
        	this.drawModalRectWithCustomSizedTexture(76 + ((width / 2) - (xSize / 2)), ((width / 2) - (ySize / 2)) - 47, 0, 0, prog, 5, 24, 5);
        }
	}
}