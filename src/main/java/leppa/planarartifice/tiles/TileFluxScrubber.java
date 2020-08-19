package leppa.planarartifice.tiles;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.items.ItemsTC;

public class TileFluxScrubber extends TileEntity implements ITickable{
	
	public ItemStackHandler inventory = new ItemStackHandler(1);
	public int currentSaltTime = 0;
	public int maxSaltTime = 300;
	
	public int getSaltTimeScaled(){
		if(currentSaltTime == 0){ return 0; }
		return (int)Math.round(currentSaltTime / 12.5);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setInteger("currentSaltTime", currentSaltTime);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		currentSaltTime = compound.getInteger("currentSaltTime");
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory
				: super.getCapability(capability, facing);
	}
	
	@Override
	public void update(){
		boolean on = currentSaltTime > 0;
		if(on){
			currentSaltTime--;
			if(AuraHelper.drainVis(world, getPos(), 1, true) > 0 && AuraHelper.drainFlux(world, getPos(), 1, true) > 0){
				float f = AuraHelper.getFlux(getWorld(), getPos());
				AuraHelper.drainFlux(world, getPos(), 1, false);
				AuraHelper.drainVis(world, getPos(), 1, false);
				System.out.println("Drained " + (f - AuraHelper.getFlux(getWorld(), getPos())));
			}
			if(this.getWorld().getTotalWorldTime() % 2 == 1){
				if(AuraHelper.drainFlux(world, getPos(), 1, true) > 0){
					AuraHelper.drainVis(world, getPos(), 1, false);
				}
			}
		}else{
			if(inventory.getStackInSlot(0) != null && inventory.getStackInSlot(0).getItem() == ItemsTC.salisMundus
					&& currentSaltTime == 0 && AuraHelper.drainVis(world, getPos(), 1, true) > 0
					&& AuraHelper.drainFlux(world, getPos(), 1, true) > 0){
				inventory.extractItem(0, 1, false);
				currentSaltTime = maxSaltTime;
			}
		}
	}
}