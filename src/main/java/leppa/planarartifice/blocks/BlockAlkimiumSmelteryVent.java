package leppa.planarartifice.blocks;

import java.util.ArrayList;
import java.util.List;

import leppa.planarartifice.PlanarTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.blocks.essentia.BlockSmelter;
import thaumcraft.common.blocks.essentia.BlockSmelterVent;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class BlockAlkimiumSmelteryVent extends Block implements IBlockFacingHorizontal{
	
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return true;
        //return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() instanceof BlockAlkimiumSmeltery && BlockStateUtils.getFacing(worldIn.getBlockState(pos.offset(side.getOpposite()))) != side;
    }
	
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		tooltip.add("§3Alkimium compatible");
	}
	
	//
	
	public BlockAlkimiumSmelteryVent() {
        super(Material.IRON);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)IBlockFacingHorizontal.FACING, (Comparable)EnumFacing.NORTH));
        this.setHardness(1.0f);
        this.setResistance(10.0f);
    }

    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState bs = this.getDefaultState();
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }
        bs = bs.withProperty((IProperty)IBlockFacingHorizontal.FACING, (Comparable)facing.getOpposite());
        return bs;
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState bs = this.getDefaultState();
        bs = bs.withProperty((IProperty)IBlockFacingHorizontal.FACING, (Comparable)EnumFacing.getHorizontal((int)BlockStateUtils.getFacing(meta).getHorizontalIndex()));
        return bs;
    }

    public int getMetaFromState(IBlockState state) {
        return 0 | ((EnumFacing)state.getValue((IProperty)IBlockFacingHorizontal.FACING)).getIndex();
    }

    protected BlockStateContainer createBlockState() {
        ArrayList<PropertyDirection> ip = new ArrayList<PropertyDirection>();
        ip.add(IBlockFacingHorizontal.FACING);
        return ip.size() == 0 ? super.createBlockState() : new BlockStateContainer((Block)this, ip.toArray(new IProperty[ip.size()]));
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = BlockStateUtils.getFacing(state);
        switch (facing.ordinal()) {
            default: {
                return new AxisAlignedBB(0.125, 0.125, 0.0, 0.875, 0.875, 0.5);
            }
            case 3: {
                return new AxisAlignedBB(0.125, 0.125, 0.5, 0.875, 0.875, 1.0);
            }
            case 4: {
                return new AxisAlignedBB(0.0, 0.125, 0.125, 0.5, 0.875, 0.875);
            }
            case 5: 
        }
        return new AxisAlignedBB(0.5, 0.125, 0.125, 1.0, 0.875, 0.875);
    }
    
    @SideOnly(value=Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack((Block)this, 1, 0));
    }
}