package leppa.planarartifice.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;

public class BlockTeleporterPlaceholder extends Block{

	public BlockTeleporterPlaceholder(){
		super(Material.ROCK);
		setHardness(3);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(world.getBlockState(pos.down()).getBlock() == PABlocks.teleporter){
			TileTeleporter tep = (TileTeleporter)world.getTileEntity(pos.down());
			TileTeleporter dep = null;
			int count = 0;
			List<TileEntity> allTEs = world.loadedTileEntityList;
			for(TileEntity e : allTEs){
				if(e instanceof TileTeleporter){
					TileTeleporter t = (TileTeleporter)e;
					if(tep.getAspect() == t.getAspect() && tep.getPos() != t.getPos()){
						dep = t;
						count++;
					}
				}
			}
			if(count == 1){
				int diffX = dep.getPos().getX() - tep.getPos().getX();
				int diffY = dep.getPos().getY() - tep.getPos().getY();
				int diffZ = dep.getPos().getZ() - tep.getPos().getZ();
				player.setPositionAndUpdate(player.getPosition().getX() + diffX + 0.5, player.getPosition().getY() + diffY + 0.5, player.getPosition().getZ() + diffZ + 0.5);
			}else if(count == 0){
				if(world.isRemote){
					//TODO: Translate!
					player.sendMessage(new TextComponentString("There are no Waystones with this Aspect."));
				}
			}else if(count > 1){
				if(world.isRemote){
					//TODO: Translate!
					player.sendMessage(new TextComponentString("There are too many Waystones with this Aspect."));
				}
			}
		}
		return false;
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.AIR);
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		if(worldIn.getBlockState(pos.down()).getBlock() == PABlocks.teleporter){
			worldIn.setBlockState(pos, PABlocks.teleporter_matrix.getDefaultState());
			worldIn.setBlockState(pos.down(), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.down(2), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.removeTileEntity(pos.down());
		}else if(worldIn.getBlockState(pos.up()).getBlock() == PABlocks.teleporter){
			worldIn.setBlockState(pos, BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.up(), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.up(2), PABlocks.teleporter_matrix.getDefaultState());
			worldIn.removeTileEntity(pos.up());
		}
	}
	
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	public boolean isFullyOpaque(IBlockState state){
        return false;
    }
	
	public boolean isNormalCube(IBlockState state){
		return false;
	}
	
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	public boolean isBlockNormalCube(IBlockState state){
		return false;
	}
	
	public boolean isFullBlock(IBlockState state){
		return false;
	}
	
	public boolean isTranslucent(IBlockState state){
		return false;
	}
	
	public boolean isVisuallyOpaque(){
		return false;
	}
	
	public boolean isFullCube(IBlockState state){
        return false;
    }
}