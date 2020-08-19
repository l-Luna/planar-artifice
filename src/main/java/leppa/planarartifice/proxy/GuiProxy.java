package leppa.planarartifice.proxy;

import leppa.planarartifice.containers.ContainerAlkimiumSmeltery;
import leppa.planarartifice.containers.ContainerFluxScrubber;
import leppa.planarartifice.containers.ContainerPotionMixer;
import leppa.planarartifice.containers.gui.GuiAlkimiumSmeltery;
import leppa.planarartifice.containers.gui.GuiFluxScrubber;
import leppa.planarartifice.containers.gui.GuiPotionMixer;
import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import leppa.planarartifice.tiles.TileFluxScrubber;
import leppa.planarartifice.tiles.TilePotionMixer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler{
	
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileFluxScrubber){
			return new ContainerFluxScrubber(player.inventory, (TileFluxScrubber)te);
		}else if(te instanceof TilePotionMixer){
			return new ContainerPotionMixer(player.inventory, (TilePotionMixer)te);
		}else if(te instanceof TileAlkimiumSmeltery){ return new ContainerAlkimiumSmeltery(player.inventory,
				(TileAlkimiumSmeltery)te); }
		return null;
	}
	
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileFluxScrubber){
			TileFluxScrubber containerTileEntity = (TileFluxScrubber)te;
			return new GuiFluxScrubber(player.inventory, containerTileEntity);
		}else if(te instanceof TilePotionMixer){
			TilePotionMixer containerTileEntity = (TilePotionMixer)te;
			return new GuiPotionMixer(player.inventory, containerTileEntity);
		}else if(te instanceof TileAlkimiumSmeltery){
			TileAlkimiumSmeltery containerTileEntity = (TileAlkimiumSmeltery)te;
			return new GuiAlkimiumSmeltery(player.inventory, containerTileEntity);
		}
		return null;
	}
}
