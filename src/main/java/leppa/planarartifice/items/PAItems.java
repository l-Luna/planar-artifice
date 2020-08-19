package leppa.planarartifice.items;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.blocks.PABlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.parts.GolemMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;

public class PAItems{
	
	static ArmorMaterial alkimium = EnumHelper.addArmorMaterial("alkimium", "planarartifice:goggles", 325, new int[]{3, 3, 3, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);
	static ArmorMaterial jetHarness = EnumHelper.addArmorMaterial("thaumostaticJetHarness", "planarartifice:null_texture", 1, new int[]{6, 6, 6, 6}, 30, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
	static Item.ToolMaterial bismuth = EnumHelper.addToolMaterial("bismuth", 3, 1200, 8, 3.5f, 20);

	@ObjectHolder("planarartifice:magic_apple")
	public static Item magic_apple;
	@ObjectHolder("planarartifice:alkimium_ingot")
	public static Item alkimium_ingot;
	@ObjectHolder("planarartifice:alkimium_plate")
	public static Item alkimium_plate;
	@ObjectHolder("planarartifice:alkimium_goggles")
	public static Item alkimium_goggles;
	@ObjectHolder("planarartifice:alkimium_nugget")
	public static Item alkimium_nugget;
	@ObjectHolder("planarartifice:bismuth_ingot")
	public static Item bismuth_ingot;
	@ObjectHolder("planarartifice:aura_meter")
	public static Item aura_meter;
	@ObjectHolder("planarartifice:flux_venting_circuit")
	public static Item flux_venting_circuit;
	@ObjectHolder("planarartifice:condensed_crystal_cluster")
	public static Item condensed_crystal_cluster;
	
	@ObjectHolder("planarartifice:alchemical_scribing_tools")
	public static Item alchemical_scribing_tools;
	
	@ObjectHolder("planarartifice:dimensional_curiosity")
	public static Item dimensional_curiosity;
	@ObjectHolder("planarartifice:dimensional_singularity")
	public static Item dimensional_singularity;
	
	@ObjectHolder("planarartifice:belt_of_suspension")
	public static Item belt_of_suspension;
	@ObjectHolder("planarartifice:mirrored_amulet")
	public static Item mirrored_amulet;
	@ObjectHolder("planarartifice:bismuth_caster")
	public static Item bismuth_caster;
	@ObjectHolder("planarartifice:bismuth_claymore")
	public static Item bismuth_claymore;
	
	public static Item thaumostatic_jet_harness;
	
	public static void init(){
		magic_apple = ((ItemFood)new ItemMagicApple().setUnlocalizedName("magic_apple").setRegistryName("magic_apple").setCreativeTab(PlanarArtifice.creativetab)).setAlwaysEdible();
		alkimium_ingot = new Item().setUnlocalizedName("alkimium_ingot").setRegistryName("alkimium_ingot").setCreativeTab(PlanarArtifice.creativetab);
		alkimium_plate = new Item().setUnlocalizedName("alkimium_plate").setRegistryName("alkimium_plate").setCreativeTab(PlanarArtifice.creativetab);
		alkimium_goggles = new ItemAlkimiumGoggles();
		alkimium_nugget = new Item().setUnlocalizedName("alkimium_nugget").setRegistryName("alkimium_nugget").setCreativeTab(PlanarArtifice.creativetab);
		alchemical_scribing_tools = new ItemAlchemicalScribingTools().setRegistryName("alchemical_scribing_tools").setUnlocalizedName("alchemical_scribing_tools").setCreativeTab(PlanarArtifice.creativetab);
		bismuth_ingot = new Item().setUnlocalizedName("bismuth_ingot").setRegistryName("bismuth_ingot").setCreativeTab(PlanarArtifice.creativetab);
		aura_meter = new Item().setUnlocalizedName("aura_meter").setRegistryName("aura_meter").setMaxStackSize(1).setCreativeTab(PlanarArtifice.creativetab);
		flux_venting_circuit = new Item().setUnlocalizedName("flux_venting_circuit").setRegistryName("flux_venting_circuit").setCreativeTab(PlanarArtifice.creativetab);
		condensed_crystal_cluster = new Item().setUnlocalizedName("condensed_crystal_cluster").setRegistryName("condensed_crystal_cluster").setCreativeTab(PlanarArtifice.creativetab);
		
		dimensional_curiosity = new ItemDimensionalCuriosity().setUnlocalizedName("dimensional_curiosity").setRegistryName("dimensional_curiosity").setCreativeTab(PlanarArtifice.creativetab).setMaxStackSize(1);
		dimensional_singularity = new Item().setUnlocalizedName("dimensional_singularity").setRegistryName("dimensional_singularity").setCreativeTab(PlanarArtifice.creativetab).setContainerItem(dimensional_curiosity);
		
		belt_of_suspension = new ItemBeltOfSuspension().setUnlocalizedName("belt_of_suspension").setRegistryName("belt_of_suspension").setCreativeTab(PlanarArtifice.creativetab).setMaxStackSize(1);
		mirrored_amulet = new ItemMirroredAmulet().setUnlocalizedName("mirrored_amulet").setRegistryName("mirrored_amulet").setCreativeTab(PlanarArtifice.creativetab).setMaxStackSize(1);
		bismuth_caster = new ItemBismuthCaster().setUnlocalizedName("bismuth_caster").setCreativeTab(PlanarArtifice.creativetab).setMaxStackSize(1);
		bismuth_claymore = new ItemBismuthSword(bismuth).setUnlocalizedName("bismuth_claymore").setRegistryName("bismuth_claymore").setCreativeTab(PlanarArtifice.creativetab);
	}
	
	public static void registerItems(RegistryEvent.Register<Item> event){
		init();
		event.getRegistry().registerAll(magic_apple, alkimium_ingot, alkimium_plate, alkimium_goggles, alkimium_nugget, bismuth_ingot, aura_meter, alchemical_scribing_tools, dimensional_curiosity, dimensional_singularity, belt_of_suspension, mirrored_amulet, bismuth_caster, flux_venting_circuit, condensed_crystal_cluster, bismuth_claymore);
	}
	
	public static void registerModels(){
		registerRender(magic_apple);
		registerRender(alkimium_ingot);
		OreDictionary.registerOre("ingotAlchemical", alkimium_ingot);
		registerRender(alkimium_plate);
		OreDictionary.registerOre("plateAlchemical", alkimium_plate);
		registerRender(alkimium_goggles);
		registerRender(alkimium_nugget);
		OreDictionary.registerOre("nuggetAlchemical", alkimium_nugget);
		registerRender(alchemical_scribing_tools);
		registerRender(bismuth_ingot);
		OreDictionary.registerOre("ingotBismuth", bismuth_ingot);
		registerRender(aura_meter);
		registerRender(flux_venting_circuit);
		registerRender(condensed_crystal_cluster);
		
		registerRender(dimensional_curiosity);
		registerRender(dimensional_singularity);
		
		registerRender(belt_of_suspension);
		registerRender(mirrored_amulet);
		registerRender(bismuth_caster);
		registerRender(bismuth_claymore);
	}
	
	public static void registerRender(Item tem){
		ModelLoader.setCustomModelResourceLocation(tem, 0, new ModelResourceLocation(tem.getRegistryName(), "inventory"));
	}
}