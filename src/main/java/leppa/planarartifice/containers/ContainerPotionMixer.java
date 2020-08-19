package leppa.planarartifice.containers;

import leppa.planarartifice.tiles.TileFluxScrubber;
import leppa.planarartifice.tiles.TilePotionMixer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPotionMixer extends Container{
	
	protected TileFluxScrubber tile;
	
	public ContainerPotionMixer(InventoryPlayer player, TilePotionMixer tileEntity){
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 0, 46, 7));
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 1, 35, 28));
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 2, 46, 49));
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 3, 79, 28));
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 4, 115, 7));
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 5, 125, 28));
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 5, 115, 49));
		for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 9; ++j){
                this.addSlotToContainer(new Slot((IInventory)player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
		for (int i = 0; i < 9; ++i){
            this.addSlotToContainer(new Slot((IInventory)player, i, 8 + i * 18, 142));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
			
			if(index < containerSlots){
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)){
					return null;
				}
			}else if(!this.mergeItemStack(itemstack1, 0, containerSlots, false)){
				return null;
			}
			
			if(itemstack1.isEmpty()){
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			
			if(itemstack1.getCount() == itemstack.getCount()){
				return null;
			}
			
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}