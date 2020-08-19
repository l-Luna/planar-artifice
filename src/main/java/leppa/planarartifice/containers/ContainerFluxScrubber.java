package leppa.planarartifice.containers;

import leppa.planarartifice.tiles.TileFluxScrubber;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class ContainerFluxScrubber extends Container{
	
	protected TileFluxScrubber tile;
	
	public ContainerFluxScrubber(InventoryPlayer player, TileFluxScrubber tileEntity){
		addSlotToContainer(new SlotItemHandler(tileEntity.inventory, 0, 80, 29));
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
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
			
			if(index < containerSlots){
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)){
					return ItemStack.EMPTY;
				}
			}else if(!this.mergeItemStack(itemstack1, 0, containerSlots, false)){
				return ItemStack.EMPTY;
			}
			
			if(itemstack1.isEmpty()){
				slot.putStack(ItemStack.EMPTY);
			}else{
				slot.onSlotChanged();
			}
			
			if(itemstack1.getCount() == itemstack.getCount()){
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}