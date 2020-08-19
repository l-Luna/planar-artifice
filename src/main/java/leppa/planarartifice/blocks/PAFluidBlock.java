package leppa.planarartifice.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PAFluidBlock extends BlockFluidClassic{
	
	public PAFluidBlock(Fluid fluid, Material material){
		super(fluid, material);
	}
	
	@SideOnly(Side.CLIENT)
	public
	void render(){
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
	}
}