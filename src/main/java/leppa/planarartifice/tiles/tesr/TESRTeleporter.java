package leppa.planarartifice.tiles.tesr;

import org.lwjgl.opengl.GL11;

import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public class TESRTeleporter extends TileEntitySpecialRenderer<TileTeleporter>{
	
	@Override
	public void render(TileTeleporter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha){
		ItemStack stack = te.crystal.getStackInSlot(0);
		if(stack != null){
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.pushMatrix();
			double offset = Math.sin((te.getWorld().getTotalWorldTime() - te.lastChangeTime + partialTicks) / 8) / 4.0;
			double offsetX = Math
					.sin(15.8 + ((te.getWorld().getTotalWorldTime() - te.lastChangeTime + partialTicks) / 8)) / 1.5;
			double offsetZ = Math
					.sin(7.9 + ((te.getWorld().getTotalWorldTime() - te.lastChangeTime + partialTicks) / 8)) / 1.5;
			GlStateManager.translate(x + 0.5 + offsetX, y + 0.5, z + 0.5 + offsetZ);
			// GL11.glTranslatef((float)x + 0.5f + (float)facing.getFrontOffsetX() / 1.99f),
			// (float)((float)y + 1.125f), (float)((float)z + 0.5f +
			// (float)facing.getFrontOffsetZ() / 1.99f));
			GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);
			
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(),
					null);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
			
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}
}