package leppa.planarartifice.blocks;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.tconstruct.TConstructHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class PABlocks{
	
	@GameRegistry.ObjectHolder("planarartifice:alkimium_block")
	public static Block alkimium_block;
	@GameRegistry.ObjectHolder("planarartifice:alchemical_alkimium_construct")
	public static Block alchemical_alkimium_construct;
	@GameRegistry.ObjectHolder("planarartifice:alkimium_smeltery")
	public static Block alkimium_smeltery;
	@GameRegistry.ObjectHolder("planarartifice:alkimium_smeltery_thaumium")
	public static Block alkimium_smeltery_thaumium;
	@GameRegistry.ObjectHolder("planarartifice:alkimium_smeltery_void")
	public static Block alkimium_smeltery_void;
	@GameRegistry.ObjectHolder("planarartifice:smelter_aux")
	public static Block smelter_aux;
	@GameRegistry.ObjectHolder("planarartifice:smelter_vent")
	public static Block smelter_vent;
	@GameRegistry.ObjectHolder("planarartifice:teleporter")
	public static Block teleporter;
	@GameRegistry.ObjectHolder("planarartifice:teleporter_matrix")
	public static Block teleporter_matrix;
	@GameRegistry.ObjectHolder("planarartifice:teleporter_placeholder")
	public static Block teleporter_placeholder;
	@GameRegistry.ObjectHolder("planarartifice:potion_mixer")
	public static Block potion_mixer;
	@GameRegistry.ObjectHolder("planarartifice:flux_scrubber")
	public static Block flux_scrubber;
	
	public static void init(){
		alkimium_block = new BlockAlkimium().setUnlocalizedName("alkimium_block").setRegistryName("alkimium_block").setCreativeTab(PlanarArtifice.creativetab).setHardness(3);
		alchemical_alkimium_construct = new Block(Material.IRON).setUnlocalizedName("alchemical_alkimium_construct").setRegistryName("alchemical_alkimium_construct").setCreativeTab(PlanarArtifice.creativetab).setHardness(3);
		alkimium_smeltery = (BlockAlkimiumSmeltery)new BlockAlkimiumSmeltery().setRegistryName("alkimium_smeltery").setUnlocalizedName("alkimium_smeltery").setCreativeTab(PlanarArtifice.creativetab);
		alkimium_smeltery_thaumium = (BlockAlkimiumSmeltery)new BlockAlkimiumSmeltery().setRegistryName("alkimium_smeltery_thaumium").setUnlocalizedName("alkimium_smeltery_thaumium").setCreativeTab(PlanarArtifice.creativetab);
		alkimium_smeltery_void = (BlockAlkimiumSmeltery)new BlockAlkimiumSmeltery().setRegistryName("alkimium_smeltery_void").setUnlocalizedName("alkimium_smeltery_void").setCreativeTab(PlanarArtifice.creativetab);
		//alkimium_smeltery_thaumium = (BlockAlkimiumSmeltery)new BlockAlkimiumSmeltery(13, 0.85f).setUnlocalizedName("alkimium_smeltery_thaumium").setRegistryName("alkimium_smeltery_thaumium").setCreativeTab(PlanarArtifice.creativetab).setHardness(3);
		//alkimium_smeltery_void = (BlockAlkimiumSmeltery)new BlockAlkimiumSmeltery(10, 0.95f).setUnlocalizedName("alkimium_smeltery_void").setRegistryName("alkimium_smeltery_void").setCreativeTab(PlanarArtifice.creativetab).setHardness(3);
		smelter_aux = new BlockAlkimiumSmelteryAux().setCreativeTab(PlanarArtifice.creativetab).setUnlocalizedName("smelter_aux").setRegistryName("smelter_aux");
		smelter_vent = new BlockAlkimiumSmelteryVent().setCreativeTab(PlanarArtifice.creativetab).setUnlocalizedName("smelter_vent").setRegistryName("smelter_vent");
		teleporter = new BlockTeleporterMiddle().setUnlocalizedName("teleporter").setRegistryName("teleporter");
		teleporter_matrix = new Block(Material.ROCK).setCreativeTab(PlanarArtifice.creativetab).setHardness(3).setUnlocalizedName("teleporter_matrix").setRegistryName("teleporter_matrix");
		teleporter_placeholder = new BlockTeleporterPlaceholder().setUnlocalizedName("teleporter_placeholder").setRegistryName("teleporter_placeholder");
		potion_mixer = new BlockPotionMixer().setUnlocalizedName("potion_mixer").setRegistryName("potion_mixer");//.setCreativeTab(PlanarArtifice.creativetab);
		flux_scrubber = new BlockFluxScrubber().setHardness(3).setUnlocalizedName("flux_scrubber").setRegistryName("flux_scrubber").setHardness(3).setCreativeTab(PlanarArtifice.creativetab);
	}
	
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		init();
		event.getRegistry().registerAll(alkimium_block, alchemical_alkimium_construct, smelter_aux,
				smelter_vent, teleporter, teleporter_matrix, teleporter_placeholder, potion_mixer, flux_scrubber, alkimium_smeltery, alkimium_smeltery_thaumium, alkimium_smeltery_void);
	}
	
	public static void registerItemBlocks(RegistryEvent.Register<Item> event){
		event.getRegistry().registerAll(makeItemBlocks(alkimium_block, alchemical_alkimium_construct, smelter_aux,
				smelter_vent, teleporter, teleporter_matrix, teleporter_placeholder, potion_mixer, flux_scrubber, alkimium_smeltery, alkimium_smeltery_thaumium, alkimium_smeltery_void));
	}
	
    public static Item[] makeItemBlocks(Block... blocks){
    	Item[] itemBlocksList = new Item[blocks.length];
    	for(int i = 0; i < blocks.length; i++){
    		itemBlocksList[i] = new ItemBlock(blocks[i]).setRegistryName(blocks[i].getRegistryName());
    	}
    	return itemBlocksList;
    }
    
	public static void registerModels(){
		registerRender(alkimium_block);
		OreDictionary.registerOre("blockAlchemical", alkimium_block);
		registerRender(alchemical_alkimium_construct);
		registerRender(smelter_aux);
		registerRender(smelter_vent);
		registerRender(teleporter);
		registerRender(teleporter_matrix);
		registerRender(teleporter_placeholder);
		registerRender(potion_mixer);
		registerRender(flux_scrubber);
		registerRender(alkimium_smeltery);
		registerRender(alkimium_smeltery_thaumium);
		registerRender(alkimium_smeltery_void);
		
		if(Loader.isModLoaded("tconstruct")){
			TConstructHandler.registerRenders();
		}
	}
	
	public static void registerRender(Block block){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}