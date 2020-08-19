package leppa.planarartifice.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.common.items.resources.ItemCrystalEssence;

public class BlockTeleporterMiddle extends Block implements ITileEntityProvider{
	
	public BlockTeleporterMiddle(){
		super(Material.ROCK);
		setHardness(3);
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.AIR);
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		if(worldIn.getBlockState(pos.up()).getBlock() == PABlocks.teleporter_placeholder && worldIn.getBlockState(pos.down()).getBlock() == PABlocks.teleporter_placeholder){
			worldIn.setBlockState(pos.down(), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos, BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.up(), PABlocks.teleporter_matrix.getDefaultState());
		}
		worldIn.removeTileEntity(pos);
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
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileTeleporter();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
			TileTeleporter tile = (TileTeleporter)world.getTileEntity(pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			if(!player.isSneaking()){
				if(player.getHeldItem(hand).isEmpty()){
					player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
				}else if(player.getHeldItem(hand).getItem() instanceof ItemCrystalEssence){
					player.setHeldItem(hand, itemHandler.insertItem(0, player.getHeldItem(hand), false));
				}
				tile.markDirty();
			}else{
				ItemStack stack = itemHandler.getStackInSlot(0);
				if (stack != null){
					if(stack.getItem() instanceof ItemCrystalEssence) player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE.toString() + ((ItemGenericEssentiaContainer)stack.getItem()).getAspects(stack).getAspects()[0].getName()));
				}else{
					player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "Empty"));
				}
			}
		}
		return true;
	}
}