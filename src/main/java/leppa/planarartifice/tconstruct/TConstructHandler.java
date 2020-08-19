package leppa.planarartifice.tconstruct;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.blocks.PAFluid;
import leppa.planarartifice.blocks.PAFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TConstructHandler{
	static PAFluidBlock alkimiumFluidBlock;
	static PAFluid alkimiumFluid;
	
	static PAFluidBlock bismuthFluidBlock;
	static PAFluid bismuthFluid;
	
	static AbstractTrait transmutative = new TraitTransmutative();
	static AbstractTrait auraInfusing = new TraitAuraInfusing();
	
	static{
		alkimiumFluid = (PAFluid)new PAFluid("molten_alkimium", new ResourceLocation("tconstruct:blocks/fluids/molten_metal"), new ResourceLocation("tconstruct:blocks/fluids/molten_metal_flow")).setColor(0xFF0DCE53).setLuminosity(15).setTemperature(1700);
		FluidRegistry.registerFluid(alkimiumFluid);
		FluidRegistry.addBucketForFluid(alkimiumFluid);
		
		alkimiumFluidBlock = (PAFluidBlock)new PAFluidBlock(alkimiumFluid, net.minecraft.block.material.Material.LAVA).setUnlocalizedName("molten_alkimium").setRegistryName("molten_alkimium");
		
		bismuthFluid = (PAFluid)new PAFluid("molten_bismuth", new ResourceLocation("tconstruct:blocks/fluids/molten_metal"), new ResourceLocation("tconstruct:blocks/fluids/molten_metal_flow")).setColor(0xFFF0FFF0).setLuminosity(15).setTemperature(2500);
		FluidRegistry.registerFluid(bismuthFluid);
		FluidRegistry.addBucketForFluid(bismuthFluid);
		
		bismuthFluidBlock = (PAFluidBlock)new PAFluidBlock(bismuthFluid, net.minecraft.block.material.Material.LAVA).setUnlocalizedName("molten_bismuth").setRegistryName("molten_bismuth");
		
		MinecraftForge.EVENT_BUS.register(transmutative);
		MinecraftForge.EVENT_BUS.register(auraInfusing);
	}
	
	public static void preInit(){
		Material alkimium = new Material("alkimium", 0xFF0DCE53);
		alkimium.setCraftable(false).setCastable(true);
		alkimium.setFluid(alkimiumFluid);
		PlanarArtifice.proxy.setMetalMaterialRenderInfo(alkimium, 0xFF0DCE53, 0.7f, 0f, 0.1f);
		
		alkimium.addTrait(transmutative);
		
		TinkerRegistry.addMaterialStats(alkimium, new HeadMaterialStats(302, 9, 6f, OBSIDIAN), new HandleMaterialStats(1.3f, 24), new ExtraMaterialStats(92), new BowMaterialStats(0.6f, 1.7f, 11));
		
		MaterialIntegration mi = new MaterialIntegration(alkimium, alkimiumFluid);
		mi.oreSuffix = "Alchemical";
		mi.toolforge();
		mi.setRepresentativeItem("ingotAlchemical");
		TinkerRegistry.integrate(mi).preInit();
		
		Material bismuth = new Material("bismuth", 0xFFFF0000);
		bismuth.setCraftable(false).setCastable(true);
		bismuth.setFluid(bismuthFluid);
		PlanarArtifice.proxy.setMetalMaterialRenderInfo(bismuth, 0xFFC05353, 0.3f, 0.2f, 36f);
		
		bismuth.addTrait(auraInfusing);
		
		TinkerRegistry.addMaterialStats(bismuth, new HeadMaterialStats(450, 7.5f, 7f, COBALT), new HandleMaterialStats(1.1f, 55), new ExtraMaterialStats(125), new BowMaterialStats(0.7f, 1.9f, 8));
		
		MaterialIntegration mib = new MaterialIntegration(bismuth, bismuthFluid);
		mib.oreSuffix = "Bismuth";
		mib.setRepresentativeItem("ingotBismuth");
		TinkerRegistry.integrate(mib).preInit();
		
		TinkerRegistry.registerSmelteryFuel(new FluidStack(alkimiumFluid, 50), 120);
		TinkerRegistry.registerSmelteryFuel(new FluidStack(bismuthFluid, 50), 150);
		TraitTransmutative.load();
		TraitAuraInfusing.load();
	}
	
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		event.getRegistry().register(alkimiumFluidBlock);
		event.getRegistry().register(bismuthFluidBlock);
	}
	
	public static void registerRenders(){
		alkimiumFluidBlock.render();
		bismuthFluidBlock.render();
	}
}