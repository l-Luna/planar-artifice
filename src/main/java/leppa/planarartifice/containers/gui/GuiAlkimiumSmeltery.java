package leppa.planarartifice.containers.gui;

import org.lwjgl.opengl.GL11;

import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import thaumcraft.client.gui.GuiSmelter;
import thaumcraft.common.container.ContainerSmelter;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class GuiAlkimiumSmeltery extends GuiContainer{
	
	ResourceLocation tex = new ResourceLocation("planarartifice", "textures/gui/gui_smelter.png");
	
	TileAlkimiumSmeltery furnaceInventory;
	
	public GuiAlkimiumSmeltery(InventoryPlayer par1InventoryPlayer, TileAlkimiumSmeltery par2TileEntityFurnace){
		super((Container)new ContainerSmelter(par1InventoryPlayer, par2TileEntityFurnace));
		this.furnaceInventory = par2TileEntityFurnace;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2){}
	
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
		int i1;
		GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
		this.mc.renderEngine.bindTexture(this.tex);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		GL11.glEnable((int)3042);
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		if(this.furnaceInventory.getBurnTimeRemainingScaled(20) > 0){
			i1 = this.furnaceInventory.getBurnTimeRemainingScaled(20);
			this.drawTexturedModalRect(k + 80, l + 26 + 20 - i1, 176, 20 - i1, 16, i1);
		}
		i1 = this.furnaceInventory.getCookProgressScaled(46);
		this.drawTexturedModalRect(k + 106, l + 13 + 46 - i1, 216, 46 - i1, 9, i1);
		i1 = this.furnaceInventory.getVisScaled(48);
		this.drawTexturedModalRect(k + 61, l + 12 + 48 - i1, 200, 48 - i1, 8, i1);
		this.drawTexturedModalRect(k + 60, l + 8, 232, 0, 10, 55);
	}
}