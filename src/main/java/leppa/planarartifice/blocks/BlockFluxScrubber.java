package leppa.planarartifice.blocks;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.tiles.TileFluxScrubber;
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

public class BlockFluxScrubber extends Block{
	
	public static final int GUI_ID = 2;
	
	public BlockFluxScrubber(){
		super(Material.IRON);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if (world.isRemote) {
            return true;
        }
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileFluxScrubber)) {
            return false;
        }
		player.openGui(PlanarArtifice.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFluxScrubber();
	}
}