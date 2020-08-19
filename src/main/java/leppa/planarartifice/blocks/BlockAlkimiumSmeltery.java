package leppa.planarartifice.blocks;

import java.util.ArrayList;
import java.util.Random;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import leppa.planarartifice.tiles.TileFluxScrubber;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.blocks.essentia.BlockSmelter;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class BlockAlkimiumSmeltery extends Block implements IBlockEnabled, IBlockFacingHorizontal {
	
    protected static boolean keepInventory = false;
    protected static boolean spillEssentia = true;
	
	public BlockAlkimiumSmeltery() {
		super(Material.IRON);
		setHardness(4);
		setResistance(6);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileAlkimiumSmeltery)) {
			return false;
		}
		player.openGui(PlanarArtifice.instance, 5, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileAlkimiumSmeltery();
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		IBlockState bs = this.getDefaultState();
		bs = bs.withProperty((IProperty) IBlockFacingHorizontal.FACING,
				(Comparable) placer.getHorizontalFacing().getOpposite());
		bs = bs.withProperty((IProperty) IBlockEnabled.ENABLED, (Comparable) Boolean.valueOf(false));
		return bs;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos pos2) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileSmelter) {
			((TileSmelter) te).checkNeighbours();
		}
	}

	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return BlockStateUtils.isEnabled(world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)))
				? 13
				: super.getLightValue(state, world, pos);
	}

	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof IInventory) {
			return Container.calcRedstoneFromInventory((IInventory) ((IInventory) te));
		}
		return 0;
	}

	public static void setFurnaceState(World world, BlockPos pos, boolean state) {
		if (state == BlockStateUtils
				.isEnabled(world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)))) {
			return;
		}
		TileEntity tileentity = world.getTileEntity(pos);
		keepInventory = true;
		world.setBlockState(pos, world.getBlockState(pos).withProperty((IProperty) IBlockEnabled.ENABLED,
				(Comparable) Boolean.valueOf(state)), 3);
		world.setBlockState(pos, world.getBlockState(pos).withProperty((IProperty) IBlockEnabled.ENABLED,
				(Comparable) Boolean.valueOf(state)), 3);
		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(pos, tileentity);
		}
		keepInventory = false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileSmelter && !worldIn.isRemote && ((TileSmelter) tileentity).vis > 0) {
			int ess = ((TileSmelter) tileentity).vis;
			AuraHelper.polluteAura(worldIn, pos, ess, true);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@SideOnly(value = Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World w, BlockPos pos, Random r) {
		if (BlockStateUtils.isEnabled(state)) {
			float f = (float) pos.getX() + 0.5f;
			float f1 = (float) pos.getY() + 0.2f + r.nextFloat() * 5.0f / 16.0f;
			float f2 = (float) pos.getZ() + 0.5f;
			float f3 = 0.52f;
			float f4 = r.nextFloat() * 0.5f - 0.25f;
			if (BlockStateUtils.getFacing(state) == EnumFacing.WEST) {
				w.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0,
						0.0, 0.0, new int[0]);
				w.spawnParticle(EnumParticleTypes.FLAME, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0, 0.0,
						0.0, new int[0]);
			}
			if (BlockStateUtils.getFacing(state) == EnumFacing.EAST) {
				w.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0,
						0.0, 0.0, new int[0]);
				w.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0, 0.0,
						0.0, new int[0]);
			}
			if (BlockStateUtils.getFacing(state) == EnumFacing.NORTH) {
				w.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0,
						0.0, 0.0, new int[0]);
				w.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0, 0.0,
						0.0, new int[0]);
			}
			if (BlockStateUtils.getFacing(state) == EnumFacing.SOUTH) {
				w.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0,
						0.0, 0.0, new int[0]);
				w.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0, 0.0,
						0.0, new int[0]);
			}
		}
	}

	// COPIED MORE

	protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
		if (this instanceof IBlockEnabled) {
			boolean flag;
			boolean bl = flag = !worldIn.isBlockPowered(pos);
			if (flag != (Boolean) state.getValue((IProperty) IBlockEnabled.ENABLED)) {
				worldIn.setBlockState(pos,
						state.withProperty((IProperty) IBlockEnabled.ENABLED, (Comparable) Boolean.valueOf(flag)), 3);
			}
		}
	}

	public void updateFacing(World world, BlockPos pos, EnumFacing face) {
		if (this instanceof IBlockFacing || this instanceof IBlockFacingHorizontal) {
			if (face == BlockStateUtils.getFacing(world.getBlockState(pos))) {
				return;
			}
			if (this instanceof IBlockFacingHorizontal && face.getHorizontalIndex() >= 0) {
				world.setBlockState(pos, world.getBlockState(pos)
						.withProperty((IProperty) IBlockFacingHorizontal.FACING, (Comparable) face), 3);
			}
			if (this instanceof IBlockFacing) {
				world.setBlockState(pos,
						world.getBlockState(pos).withProperty((IProperty) IBlockFacing.FACING, (Comparable) face), 3);
			}
		}
	}

	public IBlockState getStateFromMeta(int meta) {
		IBlockState bs = this.getDefaultState();
		try {
			if (this instanceof IBlockFacingHorizontal) {
				bs = bs.withProperty((IProperty) IBlockFacingHorizontal.FACING,
						(Comparable) BlockStateUtils.getFacing(meta));
			}
			if (this instanceof IBlockFacing) {
				bs = bs.withProperty((IProperty) IBlockFacing.FACING, (Comparable) BlockStateUtils.getFacing(meta));
			}
			if (this instanceof IBlockEnabled) {
				bs = bs.withProperty((IProperty) IBlockEnabled.ENABLED,
						(Comparable) Boolean.valueOf(BlockStateUtils.isEnabled(meta)));
			}
		} catch (Exception exception) {
			// empty catch block
		}
		return bs;
	}

	public int getMetaFromState(IBlockState state) {
		int i = 0;
		int b0 = 0;
		int n = this instanceof IBlockFacingHorizontal
				? b0 | ((EnumFacing) state.getValue((IProperty) IBlockFacingHorizontal.FACING)).getIndex()
				: (i = this instanceof IBlockFacing
						? b0 | ((EnumFacing) state.getValue((IProperty) IBlockFacing.FACING)).getIndex()
						: b0);
		if (this instanceof IBlockEnabled
				&& !((Boolean) state.getValue((IProperty) IBlockEnabled.ENABLED)).booleanValue()) {
			i |= 8;
		}
		return i;
	}

	protected BlockStateContainer createBlockState() {
		ArrayList<Object> ip = new ArrayList<Object>();
		if (this instanceof IBlockFacingHorizontal) {
			ip.add((Object) IBlockFacingHorizontal.FACING);
		}
		if (this instanceof IBlockFacing) {
			ip.add((Object) IBlockFacing.FACING);
		}
		if (this instanceof IBlockEnabled) {
			ip.add((Object) IBlockEnabled.ENABLED);
		}
		return ip.size() == 0 ? super.createBlockState()
				: new BlockStateContainer((Block) this, ip.toArray(new IProperty[ip.size()]));
	}

	// MORE

	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack((Block) this, 1, 0));
	}
}