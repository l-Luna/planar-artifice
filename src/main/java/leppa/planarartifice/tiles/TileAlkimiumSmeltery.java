package leppa.planarartifice.tiles;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import leppa.planarartifice.blocks.PABlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.essentia.BlockSmelter;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileBellows;
import thaumcraft.common.tiles.essentia.TileAlembic;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class TileAlkimiumSmeltery extends TileSmelter{
	
	private static final int[] slots_bottom = new int[]{1};
	private static final int[] slots_top = new int[0];
	private static final int[] slots_sides = new int[]{0};
	public AspectList aspects = new AspectList();
	public int vis;
	private int maxVis = 384;
	public int smeltTime = 100;
	boolean speedBoost = false;
	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;
	int count = 0;
	int bellows = -1;
	
	Class clazz = TileAlembic.class;
	Method processAlembics;
	
	public TileAlkimiumSmeltery(){
		super();
		try{
			processAlembics = clazz.getDeclaredMethod("processAlembics", World.class, BlockPos.class, Aspect.class);
			processAlembics.setAccessible(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer){
		return this.world.getTileEntity(this.getPos()) != this ? false
				: par1EntityPlayer.getDistanceSqToCenter(this.getPos()) <= 64.0;
	}
	
	public ItemStack getStackInSlot(int index){
		return (ItemStack)this.getItems().get(index);
	}
	
	public ItemStack decrStackSize(int index, int count){
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.getItems(), (int)index, (int)count);
		if(!itemstack.isEmpty() && this.isSyncedSlot(index)){
			this.syncTile(false);
		}
		this.markDirty();
		return itemstack;
	}
	
	public ItemStack removeStackFromSlot(int index){
		ItemStack s = ItemStackHelper.getAndRemove(this.getItems(), (int)index);
		if(this.isSyncedSlot(index)){
			this.syncTile(false);
		}
		this.markDirty();
		return s;
	}
	
	public void setInventorySlotContents(int index, @Nullable ItemStack stack){
		this.getItems().set(index, stack);
		if(stack.getCount() > this.getInventoryStackLimit()){
			stack.setCount(this.getInventoryStackLimit());
		}
		this.markDirty();
		if(this.isSyncedSlot(index)){
			this.syncTile(false);
		}
	}
	
	boolean isSyncedSlot(int slot){
		for(int s : this.syncedSlots){
			if(s != slot)
				continue;
			return true;
		}
		return false;
	}
	
	public String getName(){
		return this.hasCustomName() ? this.customName : "container.thaumcraft";
	}
	
	public boolean hasCustomName(){
		return this.customName != null && this.customName.length() > 0;
	}
	
	public int getInventoryStackLimit(){
		return 64;
	}
	
	@Override
	public void update(){
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		++this.count;
		if(this.furnaceBurnTime > 0){
			--this.furnaceBurnTime;
		}
		if(this.world != null){
			if(this.bellows < 0){
				this.checkNeighbours();
			}
			int speed = this.getSpeed();
			if(this.speedBoost){
				speed = (int)((double)speed * 0.8);
			}
			if(this.count % speed == 0 && this.aspects.size() > 0){
				for(Aspect aspect : this.aspects.getAspects()){
					try{
						if(this.aspects.getAmount(aspect) <= 0
								|| !(boolean)processAlembics.invoke(null, this.getWorld(), this.getPos(), aspect))
							continue;
					}catch(Exception e){
						e.printStackTrace();
					}
					this.takeFromContainer(aspect, 1);
					break;
				}
				block1: for(EnumFacing face : EnumFacing.HORIZONTALS){
					IBlockState aux = this.world.getBlockState(this.getPos().offset((EnumFacing)face));
					if(aux.getBlock() != PABlocks.smelter_aux || BlockStateUtils.getFacing(aux) != face.getOpposite())
						continue;
					for(Aspect aspect : this.aspects.getAspects()){
						try{
							if(this.aspects.getAmount(aspect) <= 0 || !(boolean)processAlembics.invoke(null,
									this.getWorld(), this.getPos().offset((EnumFacing)face), aspect))
								continue;
						}catch(Exception e){
							e.printStackTrace();
						}
						this.takeFromContainer(aspect, 1);
						continue block1;
					}
				}
			}
			if(this.furnaceBurnTime == 0){
				if(this.canSmelt()){
					this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace
							.getItemBurnTime((ItemStack)this.getStackInSlot(1));
					if(this.furnaceBurnTime > 0){
						BlockSmelter.setFurnaceState(this.world, this.getPos(), true);
						flag1 = true;
						this.speedBoost = false;
						if(this.getStackInSlot(1) != null){
							if(this.getStackInSlot(1).isItemEqual(new ItemStack(ItemsTC.alumentum))){
								this.speedBoost = true;
							}
							this.getStackInSlot(1).shrink(1);
							if(this.getStackInSlot(1).getCount() == 0){
								this.setInventorySlotContents(1,
										this.getStackInSlot(1).getItem().getContainerItem(this.getStackInSlot(1)));
							}
						}
					}else{
						BlockSmelter.setFurnaceState(this.world, this.getPos(), false);
					}
				}else{
					BlockSmelter.setFurnaceState(this.world, this.getPos(), false);
				}
			}
			if(BlockStateUtils.isEnabled(this.getBlockMetadata()) && this.canSmelt()){
				++this.furnaceCookTime;
				if(this.furnaceCookTime >= this.smeltTime){
					this.furnaceCookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			}else{
				this.furnaceCookTime = 0;
			}
			if(flag != this.furnaceBurnTime > 0){
				flag1 = true;
			}
		}
		if(flag1){
			this.markDirty();
		}
	}
	
	public boolean canSmelt(){
		if(this.getStackInSlot(0).isEmpty()){ return false; }
		AspectList al = ThaumcraftCraftingManager.getObjectTags(this.getStackInSlot(0));
		if(al == null || al.size() == 0){ return false; }
		int vs = al.visSize();
		if(vs > this.maxVis - this.vis){ return false; }
		this.smeltTime = (int)((float)(vs * 2) * (1.0f - 0.125f * (float)this.bellows));
		return true;
	}
	
	public float getEfficiency(){
		if(world.getBlockState(getPos()).getBlock() == PABlocks.alkimium_smeltery){
			return 0.85f;
		}else if(world.getBlockState(getPos()).getBlock() == PABlocks.alkimium_smeltery_thaumium){
			return 0.85f;
		}else if(world.getBlockState(getPos()).getBlock() == PABlocks.alkimium_smeltery_void){
			return 0.95f;
		}else{
			return 0.85f;
		}
	}
	
	public int getSpeed(){
		int speed = 20;
		if(world.getBlockState(getPos()).getBlock() == PABlocks.alkimium_smeltery){
			speed -= 6;
		}else if(world.getBlockState(getPos()).getBlock() == PABlocks.alkimium_smeltery_thaumium){
			speed -= 13;
		}else if(world.getBlockState(getPos()).getBlock() == PABlocks.alkimium_smeltery_void){
			speed -= 10;
		}
		return speed;
	}
	
	public void smeltItem(){
		if(this.canSmelt()){
			int flux = 0;
			AspectList al = ThaumcraftCraftingManager.getObjectTags(this.getStackInSlot(0));
			for(Aspect a : al.getAspects()){
				if(this.getEfficiency() < 1.0f){
					int qq = al.getAmount(a);
					for(int q = 0; q < qq; ++q){
						if(this.world.rand.nextFloat() <= (a == Aspect.FLUX ? this.getEfficiency() * 0.66f
								: this.getEfficiency()))
							continue;
						al.reduce(a, 1);
						++flux;
					}
				}
				this.aspects.add(a, al.getAmount(a));
			}
			if(flux > 0){
				int pp = 0;
				block2: for(int c = 0; c < flux; ++c){
					for(EnumFacing face : EnumFacing.HORIZONTALS){
						IBlockState vent = this.world.getBlockState(this.getPos().offset(face));
						if(vent.getBlock() != BlocksTC.smelterVent
								|| BlockStateUtils.getFacing(vent) != face.getOpposite()
								|| (double)this.world.rand.nextFloat() >= 0.333)
							continue;
						this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, face.getOpposite().ordinal());
						continue block2;
					}
					++pp;
				}
				AuraHelper.polluteAura(this.getWorld(), this.getPos(), pp, true);
			}
			this.vis = this.aspects.visSize();
			this.getStackInSlot(0).shrink(1);
			if(this.getStackInSlot(0).getCount() <= 0){
				this.setInventorySlotContents(0, ItemStack.EMPTY);
			}
		}
	}
	
	public boolean takeFromContainer(Aspect tag, int amount){
		if(this.aspects != null && this.aspects.getAmount(tag) >= amount){
			this.aspects.remove(tag, amount);
			this.vis = this.aspects.visSize();
			this.markDirty();
			return true;
		}
		return false;
	}
	
	public int getCookProgressScaled(int par1){
		if(this.smeltTime <= 0){
			this.smeltTime = 1;
		}
		return this.furnaceCookTime * par1 / this.smeltTime;
	}
	
	public int getVisScaled(int par1){
		return vis * par1 / this.maxVis;
	}
	
	public int getBurnTimeRemainingScaled(int par1){
		if(this.currentItemBurnTime == 0){
			this.currentItemBurnTime = 200;
		}
		// System.out.println(furnaceBurnTime);
		return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtCompound){
		super.readFromNBT(nbtCompound);
		this.speedBoost = nbtCompound.getBoolean("speedBoost");
		this.furnaceCookTime = nbtCompound.getShort("CookTime");
		this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime((ItemStack)this.getStackInSlot(1));
		this.aspects.readFromNBT(nbtCompound);
		this.vis = this.aspects.visSize();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtCompound){
		nbtCompound = super.writeToNBT(nbtCompound);
		nbtCompound.setBoolean("speedBoost", this.speedBoost);
		nbtCompound.setShort("CookTime", (short)this.furnaceCookTime);
		this.aspects.writeToNBT(nbtCompound);
		return nbtCompound;
	}
	
	// UMM
	
	@Override
	public void readSyncNBT(NBTTagCompound nbttagcompound){
		this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
		this.vis = nbttagcompound.getShort("vis");
	}
	
	@Override
	public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound){
		nbttagcompound.setShort("BurnTime", (short)this.furnaceBurnTime);
		nbttagcompound.setShort("vis", (short)this.vis);
		return nbttagcompound;
	}
	
	public void checkNeighbours(){
		EnumFacing[] faces = EnumFacing.HORIZONTALS;
		try{
			if(BlockStateUtils.getFacing(this.getBlockMetadata()) == EnumFacing.NORTH){
				faces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
			}
			if(BlockStateUtils.getFacing(this.getBlockMetadata()) == EnumFacing.SOUTH){
				faces = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
			}
			if(BlockStateUtils.getFacing(this.getBlockMetadata()) == EnumFacing.EAST){
				faces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST};
			}
			if(BlockStateUtils.getFacing(this.getBlockMetadata()) == EnumFacing.WEST){
				faces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.NORTH};
			}
		}catch(Exception exception){
			exception.printStackTrace();
		}
		this.bellows = TileBellows.getBellows(this.world, this.pos, faces);
	}
	
	private int getType(){
		return this.getBlockType() == BlocksTC.smelterBasic ? 0
				: (this.getBlockType() == BlocksTC.smelterThaumium ? 1 : 2);
	}
	
	public static boolean isItemFuel(ItemStack par0ItemStack){
		return TileEntityFurnace.getItemBurnTime((ItemStack)par0ItemStack) > 0;
	}
	
	@Override
	public boolean isItemValidForSlot(int par1, ItemStack stack2){
		AspectList al;
		if(par1 == 0 && (al = ThaumcraftCraftingManager.getObjectTags(stack2)) != null && al.size() > 0){ return true; }
		return par1 == 1 ? TileSmelter.isItemFuel(stack2) : false;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing par1){
		return par1 == EnumFacing.DOWN ? slots_bottom : (par1 == EnumFacing.UP ? slots_top : slots_sides);
	}
	
	@Override
	public boolean canInsertItem(int par1, ItemStack stack2, EnumFacing par3){
		return par3 == EnumFacing.UP ? false : this.isItemValidForSlot(par1, stack2);
	}
	
	@Override
	public boolean canExtractItem(int par1, ItemStack stack2, EnumFacing par3){
		return par3 != EnumFacing.UP || par1 != 1 || stack2.getItem() == Items.BUCKET;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		super.onDataPacket(net, pkt);
		if(this.world != null){
			this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
		}
	}
	
	public boolean receiveClientEvent(int i, int j){
		if(i == 1){
			if(this.world.isRemote){
				EnumFacing d = EnumFacing.VALUES[j];
				this.world.playSound((double)this.getPos().getX() + 0.5 + (double)d.getOpposite().getFrontOffsetX(),
						(double)this.getPos().getY() + 0.5,
						(double)this.getPos().getZ() + 0.5 + (double)d.getOpposite().getFrontOffsetZ(),
						SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.25f,
						2.6f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8f, true);
				for(int a = 0; a < 4; ++a){
					float fx = 0.1f - this.world.rand.nextFloat() * 0.2f;
					float fz = 0.1f - this.world.rand.nextFloat() * 0.2f;
					float fy = 0.1f - this.world.rand.nextFloat() * 0.2f;
					float fx2 = 0.1f - this.world.rand.nextFloat() * 0.2f;
					float fz2 = 0.1f - this.world.rand.nextFloat() * 0.2f;
					float fy2 = 0.1f - this.world.rand.nextFloat() * 0.2f;
					int color = 11184810;
					FXDispatcher.INSTANCE.drawVentParticles(
							(float)this.getPos().getX() + 0.5f + fx + (float)d.getOpposite().getFrontOffsetX(),
							(float)this.getPos().getY() + 0.5f + fy,
							(float)this.getPos().getZ() + 0.5f + fz + (float)d.getOpposite().getFrontOffsetZ(),
							(float)d.getOpposite().getFrontOffsetX() / 4.0f + fx2,
							(float)d.getOpposite().getFrontOffsetY() / 4.0f + fy2,
							(float)d.getOpposite().getFrontOffsetZ() / 4.0f + fz2, color);
				}
			}
			return true;
		}
		return super.receiveClientEvent(i, j);
	}
}
