package leppa.planarartifice;

import leppa.planarartifice.items.PAItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PlanarTab extends CreativeTabs{
	
	public PlanarTab(){
		super(PlanarArtifice.MODID + ".tab.name");
		setBackgroundImageName("planar.png");
	}
	
	@Override
	public ItemStack getTabIconItem(){
		return new ItemStack(PAItems.magic_apple);
	}
}