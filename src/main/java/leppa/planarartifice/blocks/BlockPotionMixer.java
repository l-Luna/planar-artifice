package leppa.planarartifice.blocks;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.tiles.TileFluxScrubber;
import leppa.planarartifice.tiles.TilePotionMixer;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPotionMixer extends Block implements ITileEntityProvider{
	
	public static final int GUI_ID = 3;
	
	public BlockPotionMixer(){
		super(Material.IRON);
		setHardness(3);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if (world.isRemote) {
            return true;
        }
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TilePotionMixer)) {
            return false;
        }
		player.openGui(PlanarArtifice.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TilePotionMixer();
	}
}