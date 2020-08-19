package leppa.planarartifice.tiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import leppa.planarartifice.potion.PotionCatalouges;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.blocks.BlocksTC;

public class TilePotionMixer extends TileEntity implements ITickable, IAspectContainer, IEssentiaTransport{
	
	/**
	 * 0, 1, 2: Input Potions. 
	 * 3: Middle Item. 
	 * 4, 5, 6: Output Potions. 
	 */
	public ItemStackHandler inventory = new ItemStackHandler(7);
	public int currentProgress;
	public int maxProgress = 200;
	public AspectList recipe = new AspectList();
	public AspectList recipeProgress = new AspectList();
	public Aspect currentSuction = null;
	public PotionCatalouges currentPossibilities;
	
	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setInteger("currentProgress", currentProgress);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		currentProgress = compound.getInteger("currentProgress");
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}
	
	@Override
	public void update(){
		ItemStack catalyst = inventory.getStackInSlot(3);
		
		if(catalyst == null){
			currentPossibilities = null;
		}else if(catalyst.getItem() == Item.getItemFromBlock(Blocks.PISTON)){
			currentPossibilities = PotionCatalouges.piston;
		}else if(catalyst.getItem() == Item.getItemFromBlock(Blocks.ICE)){
			currentPossibilities = PotionCatalouges.ice;
		}else if(catalyst.getItem() == Items.IRON_INGOT){
			currentPossibilities = PotionCatalouges.iron;
		}else if(catalyst.getItem() == Items.APPLE){
			currentPossibilities = PotionCatalouges.apple;
		}else if(catalyst.getItem() == Items.FISH && catalyst.getItemDamage() == 3){
			currentPossibilities = PotionCatalouges.pufferfish;
		}else if(catalyst.getItem() == Item.getItemFromBlock(Blocks.GLASS)){
			currentPossibilities = PotionCatalouges.glass;
		}else if(catalyst.getItem() == Items.ROTTEN_FLESH){
			currentPossibilities = PotionCatalouges.rotten_flesh;
		}else if(catalyst.getItem() == Items.GOLD_INGOT){
			currentPossibilities = PotionCatalouges.gold;
		}
		//TODO: Fix, make, add Nitor
	}
	
	@Override
	public boolean isConnectable(EnumFacing face){
		return face != EnumFacing.UP && face != EnumFacing.DOWN;
	}
	
	@Override
	public boolean canInputFrom(EnumFacing face){
		return face != EnumFacing.UP && face != EnumFacing.DOWN;
	}
	
	@Override
	public boolean canOutputTo(EnumFacing face){
		return false;
	}
	
	@Override
	public void setSuction(Aspect aspect, int amount){
		currentSuction = aspect;
	}
	
	@Override
	public Aspect getSuctionType(EnumFacing face){
		return currentSuction;
	}
	
	@Override
	public int getSuctionAmount(EnumFacing face){
		return currentSuction != null ? 128 : 0;
	}
	
	@Override
	public int takeEssentia(Aspect aspect, int amount, EnumFacing face){
		return 0;
	}
	
	@Override
	public int addEssentia(Aspect aspect, int amount, EnumFacing face){
		return 0;
	}
	
	@Override
	public Aspect getEssentiaType(EnumFacing face){
		return null;
	}
	
	@Override
	public int getEssentiaAmount(EnumFacing face){
		return 0;
	}
	
	@Override
	public int getMinimumSuction(){
		return 0;
	}
	
	@Override
	public AspectList getAspects(){
		return recipeProgress;
	}
	
	@Override
	public void setAspects(AspectList aspects){
		
	}
	
	@Override
	public boolean doesContainerAccept(Aspect tag){
		return true;
	}
	
	@Override
	public int addToContainer(Aspect tag, int amount){
		int ce = recipe.getAmount(tag) - recipeProgress.getAmount(tag);
		if (this.recipe == null || ce <= 0) {
		return amount; }
		int add = Math.min(ce, amount);
		recipeProgress.add(tag, add);
		markDirty();
		return amount - add;
	}
	
	@Override
	public boolean takeFromContainer(Aspect tag, int amount){
		if(recipeProgress.getAmount(tag) >= amount){
			recipeProgress.remove(tag, amount);
			markDirty();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean takeFromContainer(AspectList ot){
		return false;
	}
	
	@Override
	public boolean doesContainerContainAmount(Aspect tag, int amount){
		return recipeProgress.getAmount(tag) >= amount;
	}
	
	@Override
	public boolean doesContainerContain(AspectList ot){
		return false;
	}
	
	@Override
	public int containerContains(Aspect tag){
		return recipeProgress.getAmount(tag);
	}
}