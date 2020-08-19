package leppa.planarartifice;

import baubles.api.BaublesApi;
import leppa.planarartifice.aspects.CrucibleRecipieRandomCrystal;
import leppa.planarartifice.aspects.EnumInfusionEnchantmentII;
import leppa.planarartifice.aspects.InfusionEnchantmentRecipeII;
import leppa.planarartifice.aspects.PAAspects;
import leppa.planarartifice.blocks.PABlocks;
import leppa.planarartifice.foci.FocusEffectColourized;
import leppa.planarartifice.foci.FocusEffectPrismLight;
import leppa.planarartifice.items.PAItems;
import leppa.planarartifice.multiblocks.PAMultiblocks;
import leppa.planarartifice.network.MessageProjectingAttack;
import leppa.planarartifice.network.PacketRequestUpdateTeleporter;
import leppa.planarartifice.network.PacketUpdateTeleporter;
import leppa.planarartifice.proxy.CommonProxy;
import leppa.planarartifice.proxy.GuiProxy;
import leppa.planarartifice.tconstruct.TConstructHandler;
import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import leppa.planarartifice.tiles.TileFluxScrubber;
import leppa.planarartifice.tiles.TilePotionMixer;
import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.IFocusElement;
import thaumcraft.api.crafting.*;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.parts.GolemAddon;
import thaumcraft.api.golems.parts.GolemMaterial;
import thaumcraft.api.golems.parts.PartModel;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ScanBlock;
import thaumcraft.api.research.ScanningManager;
import thaumcraft.client.renderers.entity.construct.PartModelHauler;
import thaumcraft.common.items.casters.ItemCaster;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static thaumcraft.api.ThaumcraftApiHelper.makeCrystal;

@EventBusSubscriber
@Mod(modid = PlanarArtifice.MODID, version = PlanarArtifice.VERSION, name = PlanarArtifice.NAME, dependencies = "required-after:thaumcraft;after:tconstruct")
public class PlanarArtifice implements LoadingCallback{
    public static final String MODID = "planarartifice";
    public static final String NAME = "Planar Artifice";
    public static final String VERSION = "0.13";

    public static final PlanarTab creativetab = new PlanarTab();
    static ResourceLocation defaultGroup = new ResourceLocation("");

    public static SimpleNetworkWrapper network;
    public static boolean isSingleplayer;

    public static ArrayList<FocusEffect> focusEffects = new ArrayList<>();
    public static int currentFocusEffect = 0;
    public static int currentColourPicked = 0xffffff;

    static HashMap<String, Integer> p;

    public static HashMap<UUID, ArrayList<ItemStack>> inventoriesToConserve = new HashMap<>();
    public static HashMap<Item, Item> transmutations = new HashMap<>();
    EnumGolemTrait teleporter;

    @Instance(MODID)
    public static PlanarArtifice instance = new PlanarArtifice();

    @SidedProxy(serverSide = "leppa.planarartifice.proxy.ServerProxy", clientSide = "leppa.planarartifice.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static int serverTickCount = 0;

    static{
        p = (HashMap<String, Integer>)ReflectionUtils.getPrivateObject("elementColor", new FocusEngine());

        transmutations.put(Items.ROTTEN_FLESH, Items.CHICKEN);
        transmutations.put(Items.GUNPOWDER, Items.GLOWSTONE_DUST);
        transmutations.put(Items.STRING, Items.FEATHER);
        transmutations.put(Items.ENDER_PEARL, Items.ENDER_EYE);
        transmutations.put(Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE);
        transmutations.put(Items.SLIME_BALL, Items.MAGMA_CREAM);
        transmutations.put(Items.GLOWSTONE_DUST, Items.BLAZE_POWDER);
        transmutations.put(Items.IRON_INGOT, Items.GOLD_INGOT);
        transmutations.put(Items.GOLD_INGOT, Items.IRON_INGOT);
        transmutations.put(Items.IRON_NUGGET, Items.GOLD_NUGGET);
        transmutations.put(Items.GOLD_NUGGET, Items.IRON_NUGGET);
        transmutations.put(Items.SUGAR, Items.REDSTONE);
        transmutations.put(Items.REDSTONE, ItemsTC.salisMundus);
        transmutations.put(Items.BONE, Items.COAL);
        transmutations.put(Items.DIAMOND, Items.EMERALD);
        transmutations.put(Items.EMERALD, Items.DIAMOND);
        transmutations.put(Items.BEETROOT_SEEDS, Items.WHEAT_SEEDS);
        transmutations.put(Items.WHEAT_SEEDS, Items.MELON_SEEDS);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);
        GameRegistry.registerTileEntity(TileTeleporter.class, new ResourceLocation("planarartifice:mirrorTeleporter"));
        GameRegistry.registerTileEntity(TileFluxScrubber.class, new ResourceLocation("planarartifice:fluxScrubber"));
        GameRegistry.registerTileEntity(TilePotionMixer.class, new ResourceLocation("planarartifice:potionMixer"));
        GameRegistry.registerTileEntity(TileAlkimiumSmeltery.class, new ResourceLocation("planarartifice:alkimiumSmeltery"));

        // Assign Aspects
        PAAspects.init();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(new PacketUpdateTeleporter.Handler(), PacketUpdateTeleporter.class, 0, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdateTeleporter.Handler(), PacketRequestUpdateTeleporter.class, 1, Side.SERVER);
        network.registerMessage(new MessageProjectingAttack.Handler(), MessageProjectingAttack.class, 2, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(PlanarArtifice.instance, new GuiProxy());

        proxy.registerRenderers();

        if(Loader.isModLoaded("tconstruct")){
            // invoke via reflection so we dont break if unloaded
            TConstructHandler.preInit();
        }

        // Reflecting to add more golem properties
        teleporter = createOtherEnumGolemTrait("TELEPORTER", 20, new ResourceLocation("planarartifice", "textures/misc/golem/tag_teleporter.png"));
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);

        //////// RESEARCH TAB////////
        ThaumcraftApi.registerResearchLocation(new ResourceLocation("planarartifice:research/planarartifice.json"));
        ResearchCategories.registerCategory("PLANARARTIFICE", null, new AspectList().add(Aspect.AURA, 1), new ResourceLocation("planarartifice:textures/research/cat_planarartifice.png"), new ResourceLocation("planarartifice:textures/research/gui_research_back_2.jpg"));

        //////// RECIPES////////
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:alkimium_ingot"), new CrucibleRecipe("METALLURGY@2", new ItemStack(PAItems.alkimium_ingot), new ItemStack(ItemsTC.ingots, 1, 2), new AspectList().add(Aspect.ALCHEMY, 5).add(Aspect.ORDER, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:magic_apple"), new CrucibleRecipe("RAREITEMS@4", new ItemStack(PAItems.magic_apple), new ItemStack(Items.APPLE), new AspectList().add(Aspect.MAGIC, 10).add(Aspect.LIFE, 70).add(Aspect.BEAST, 25).add(Aspect.EARTH, 15)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:alchemical_scribing_tools"), new CrucibleRecipe("ALCHEMICALSCIBETOOLS", new ItemStack(PAItems.alchemical_scribing_tools), new ItemStack(ItemsTC.scribingTools, 1), new AspectList().add(Aspect.AURA, 15).add(Aspect.ALCHEMY, 15)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:bismuth"), new CrucibleRecipe("!Portal", new ItemStack(PAItems.bismuth_ingot), new ItemStack(ItemsTC.ingots, 1, 0), new AspectList().add(Aspect.AURA, 20).add(Aspect.ENERGY, 20)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:condensed_crystal_cluster1"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(PAItems.condensed_crystal_cluster), new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.FIRE, 50)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:condensed_crystal_cluster2"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(PAItems.condensed_crystal_cluster), new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.WATER, 50)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:condensed_crystal_cluster3"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(PAItems.condensed_crystal_cluster), new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.AIR, 50)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:condensed_crystal_cluster4"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(PAItems.condensed_crystal_cluster), new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.EARTH, 50)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:condensed_crystal_cluster5"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(PAItems.condensed_crystal_cluster), new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.ORDER, 50)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:condensed_crystal_cluster6"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(PAItems.condensed_crystal_cluster), new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.ENTROPY, 50)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:random_vis_crystal1"), new CrucibleRecipieRandomCrystal("MOREALCHEMY", new ItemStack(ItemsTC.salisMundus), new AspectList().add(Aspect.FLUX, 4)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:random_vis_crystal2"), new CrucibleRecipieRandomCrystal("MOREALCHEMY", new ItemStack(ItemsTC.salisMundus), new AspectList().add(PAAspects.COLOUR, 20)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:endereye"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.ENDER_EYE), new ItemStack(Items.SPIDER_EYE), new AspectList().add(Aspect.FIRE, 10).add(Aspect.ELDRITCH, 20)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:redstone"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER), new AspectList().add(Aspect.ENERGY, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:iron"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_INGOT), new AspectList().add(Aspect.DESIRE, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:gold"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new AspectList().add(Aspect.METAL, 7)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:blaze_powder"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.BLAZE_POWDER, 2), new ItemStack(Items.GUNPOWDER), new AspectList().add(Aspect.FIRE, 14).add(Aspect.MAGIC, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:ender_pearls"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.ENDER_PEARL, 2), new ItemStack(Items.ENDER_PEARL), new AspectList().add(Aspect.ELDRITCH, 20)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("planarartifice:redstone2"), new CrucibleRecipe("MOREALCHEMY", new ItemStack(Items.REDSTONE, 2), new ItemStack(Items.REDSTONE), new AspectList().add(Aspect.ENERGY, 20)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimium_smeltery"), new ShapedArcaneRecipe(defaultGroup, "ALKIMIUMAPPLICATIONS", 100, new AspectList().add(Aspect.WATER, 1), new ItemStack(PABlocks.alkimium_smeltery), "#C#", "ADA", "AAA", '#', new ItemStack(PAItems.alkimium_plate, 1, 0), 'A', "plateBrass", 'C', new ItemStack(BlocksTC.smelterBasic), 'D', new ItemStack(PABlocks.alchemical_alkimium_construct)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimium_smeltery_thaumium"), new ShapedArcaneRecipe(defaultGroup, "ALKIMIUMTHAUMIUMSMELTERY", 100, new AspectList().add(Aspect.WATER, 1), new ItemStack(PABlocks.alkimium_smeltery_thaumium), "#C#", "ADA", "AAA", '#', new ItemStack(PAItems.alkimium_plate), 'A', new ItemStack(ItemsTC.plate, 1, 2), 'C', new ItemStack(PABlocks.alkimium_smeltery), 'D', new ItemStack(PABlocks.alchemical_alkimium_construct)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimium_smeltery_void"), new ShapedArcaneRecipe(defaultGroup, "ALKIMIUMVOIDSMELTERY@2", 100, new AspectList().add(Aspect.WATER, 1), new ItemStack(PABlocks.alkimium_smeltery_void), "#C#", "ADA", "AAA", '#', new ItemStack(PAItems.alkimium_plate, 1, 0), 'A', new ItemStack(ItemsTC.plate, 1, 3), 'C', new ItemStack(PABlocks.alkimium_smeltery_thaumium), 'D', new ItemStack(BlocksTC.metalAlchemicalAdvanced)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alchemical_alkimium_construct"), new ShapedArcaneRecipe(defaultGroup, "ALKIMIUM@2", 100, new AspectList().add(Aspect.WATER, 1), new ItemStack(PABlocks.alchemical_alkimium_construct), "#v#", "pwp", "#v#", '#', PAItems.alkimium_plate, 'p', BlocksTC.tube, 'v', BlocksTC.tubeValve, 'w', BlocksTC.plankSilverwood));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:teleporter_matrix"), new ShapedArcaneRecipe(defaultGroup, "MIRRORTELEPORTER@2", 300, new AspectList().add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1), new ItemStack(PABlocks.teleporter_matrix), "AHA", "MDM", "AHA", 'A', BlocksTC.stoneArcaneBrick, 'M', ItemsTC.mirroredGlass, 'H', ItemsTC.alumentum, 'D', PAItems.dimensional_singularity));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:teleporter_matrix2"), new ShapedArcaneRecipe(defaultGroup, "MIRRORTELEPORTER@2", 300, new AspectList().add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1), new ItemStack(PABlocks.teleporter_matrix), "AHA", "MDM", "AHA", 'A', BlocksTC.stoneArcaneBrick, 'M', ItemsTC.mirroredGlass, 'H', ItemsTC.alumentum, 'D', PAItems.dimensional_curiosity));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:flux_scrubber"), new ShapedArcaneRecipe(defaultGroup, "FLUXSCRUBBER", 275, new AspectList().add(Aspect.ORDER, 1).add(Aspect.AIR, 1).add(Aspect.FIRE, 1).add(Aspect.WATER, 1).add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1), new ItemStack(PABlocks.flux_scrubber), "IGI", "IAI", "BQB", 'B', new ItemStack(PAItems.bismuth_ingot), 'A', new ItemStack(PABlocks.alchemical_alkimium_construct), 'G', new ItemStack(ItemsTC.mechanismSimple), 'I', "plateIron", 'Q', new ItemStack(ItemsTC.alumentum)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimiumimproveddistillationaux1"), new ShapelessArcaneRecipe(defaultGroup, "ALKIMIUMDISTILLATION", 70, new AspectList().add(Aspect.ORDER, 1), new ItemStack(PABlocks.smelter_aux), new Object[]{BlocksTC.smelterAux, PABlocks.alchemical_alkimium_construct}));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimiumimproveddistillationaux2"), new ShapelessArcaneRecipe(defaultGroup, "ALKIMIUMDISTILLATION", 70, new AspectList().add(Aspect.ORDER, 1), new ItemStack(PABlocks.smelter_vent), new Object[]{BlocksTC.smelterVent, PABlocks.alchemical_alkimium_construct}));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimiumimproveddistillationaux3"), new ShapelessArcaneRecipe(defaultGroup, "ALKIMIUMDISTILLATION", 70, new AspectList().add(Aspect.ENTROPY, 1), new ItemStack(BlocksTC.smelterAux), new Object[]{PABlocks.smelter_aux}));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:alkimiumimproveddistillationaux4"), new ShapelessArcaneRecipe(defaultGroup, "ALKIMIUMDISTILLATION", 70, new AspectList().add(Aspect.ENTROPY, 1), new ItemStack(BlocksTC.smelterVent), new Object[]{PABlocks.smelter_vent}));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:flux_vent"), new ShapedArcaneRecipe(defaultGroup, "BISMUTHCASTERSGAUNTLET@1", 75, new AspectList(), new ItemStack(PAItems.flux_venting_circuit), " # ", "QRV", '#', makeCrystal(Aspect.FLUX), 'Q', new ItemStack(Items.QUARTZ), 'R', new ItemStack(Items.REPEATER), 'V', new ItemStack(Blocks.REDSTONE_TORCH)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:bismuth_caster"), new ShapedArcaneRecipe(defaultGroup, "BISMUTHCASTERSGAUNTLET", 175, new AspectList().add(Aspect.ORDER, 1).add(Aspect.AIR, 1).add(Aspect.FIRE, 1).add(Aspect.WATER, 1).add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1), new ItemStack(PAItems.bismuth_caster), " VO", "MGB", "BB ", 'B', new ItemStack(PAItems.bismuth_ingot), 'V', new ItemStack(PAItems.flux_venting_circuit), 'M', new ItemStack(ItemsTC.mirroredGlass), 'O', new ItemStack(ItemsTC.visResonator), 'G', new ItemStack(ItemsTC.casterBasic)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:aura_meter"), new ShapedArcaneRecipe(defaultGroup, "BISMUTH", 25, new AspectList().add(Aspect.ORDER, 1).add(Aspect.AIR, 1).add(Aspect.FIRE, 1).add(Aspect.WATER, 1).add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1), new ItemStack(PAItems.aura_meter), "A", "B", "C", 'A', Blocks.GLASS_PANE, 'B', makeCrystal(Aspect.AIR), 'C', PAItems.bismuth_ingot));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation("planarartifice:bismuth_claymore"), new ShapedArcaneRecipe(defaultGroup, "BISMUTH", 0, new AspectList().add(Aspect.FIRE, 1), new ItemStack(PAItems.bismuth_claymore), " B ", "BSB", " S ", 'B', PAItems.bismuth_ingot, 'S', Items.STICK));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:alkimium_goggles"), new InfusionRecipe("ALKIMIUMGOGGLES", new ItemStack(PAItems.alkimium_goggles), 1, new AspectList().add(Aspect.ALCHEMY, 50).add(Aspect.AURA, 25), new ItemStack(ItemsTC.goggles), new ItemStack(PAItems.alkimium_plate), new ItemStack(PAItems.alkimium_plate), new ItemStack(PAItems.alkimium_plate), makeCrystal(Aspect.AURA)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:belt_of_suspension"), new InfusionRecipe("BELTOFSUSPENSION", new ItemStack(PAItems.belt_of_suspension), 5, new AspectList().add(Aspect.AURA, 50).add(Aspect.AIR, 75).add(Aspect.MECHANISM, 15).add(Aspect.MOTION, 75).add(Aspect.ENERGY, 65).add(Aspect.FLIGHT, 125).add(Aspect.TOOL, 15), new ItemStack(ItemsTC.baubles, 1, 2), new ItemStack(Items.FEATHER), new ItemStack(ItemsTC.ringCloud), new ItemStack(Items.SUGAR), new ItemStack(ItemsTC.alumentum), new ItemStack(BlocksTC.levitator), new ItemStack(BlocksTC.pavingStoneBarrier), new ItemStack(Blocks.PISTON), new ItemStack(BlocksTC.crystalAir)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:dimensional_singularity"), new InfusionRecipe("DIMENSIONALSINGULARITY", new ItemStack(PAItems.dimensional_singularity), 5, new AspectList().add(Aspect.AURA, 75).add(Aspect.ENTROPY, 15).add(PAAspects.DIMENSIONS, 45).add(PAAspects.TIME, 10).add(Aspect.EXCHANGE, 25).add(Aspect.ENERGY, 200), new ItemStack(ItemsTC.salisMundus), new ItemStack(ItemsTC.mirroredGlass), new ItemStack(Items.ENDER_PEARL), new ItemStack(Blocks.OBSIDIAN), new ItemStack(Blocks.GOLDEN_RAIL), new ItemStack(ItemsTC.alumentum), new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(ItemsTC.visResonator)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:mirrored_amulet"), new InfusionRecipe("MIRROREDAMULET", new ItemStack(PAItems.mirrored_amulet), 8, new AspectList().add(Aspect.AURA, 50).add(Aspect.CRYSTAL, 25).add(Aspect.ENERGY, 35).add(Aspect.TOOL, 20).add(PAAspects.DIMENSIONS, 65).add(Aspect.EXCHANGE, 64), new ItemStack(ItemsTC.baubles, 1, 4), new ItemStack(PAItems.dimensional_singularity), new ItemStack(Blocks.HOPPER), new ItemStack(BlocksTC.hungryChest), new ItemStack(BlocksTC.crystalOrder), new ItemStack(Items.NAME_TAG), new ItemStack(BlocksTC.mirror)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:mirrored_amulet2"), new InfusionRecipe("MIRROREDAMULET", new ItemStack(PAItems.mirrored_amulet), 8, new AspectList().add(Aspect.AURA, 50).add(Aspect.CRYSTAL, 25).add(Aspect.ENERGY, 35).add(Aspect.TOOL, 20).add(PAAspects.DIMENSIONS, 65).add(Aspect.EXCHANGE, 64), new ItemStack(ItemsTC.baubles, 1, 4), new ItemStack(PAItems.dimensional_curiosity), new ItemStack(Blocks.HOPPER), new ItemStack(BlocksTC.hungryChest), new ItemStack(BlocksTC.crystalOrder), new ItemStack(Items.NAME_TAG), new ItemStack(BlocksTC.mirror)));

        // Infusion Enchantments
        InfusionEnchantmentRecipeII IETransmutative = new InfusionEnchantmentRecipeII(EnumInfusionEnchantmentII.TRANSMUTATIVE, new AspectList().add(Aspect.ALCHEMY, 60).add(Aspect.FLUX, 45), new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)), new ItemStack(PABlocks.alkimium_block));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:IETransmutative"), IETransmutative);
        ItemStack recipeStack = new ItemStack(ItemsTC.thaumiumSword);
        recipeStack.setStackDisplayName(TextFormatting.RESET + recipeStack.getDisplayName() + " +" + TextFormatting.GOLD + "Transmutative");
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("planarartifice:IETransmutativeFake"), new InfusionEnchantmentRecipeII(IETransmutative, recipeStack));

        InfusionEnchantmentRecipeII IEAuraInfusing = new InfusionEnchantmentRecipeII(EnumInfusionEnchantmentII.AURAINFUSING, new AspectList().add(Aspect.AURA, 50).add(Aspect.ENERGY, 60), new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)), new ItemStack(ItemsTC.visResonator));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:IEAuraInfusing"), IEAuraInfusing);
        recipeStack = new ItemStack(ItemsTC.thaumiumSword);
        recipeStack.setStackDisplayName(TextFormatting.RESET + recipeStack.getDisplayName() + " +" + TextFormatting.GOLD + "Aura Infusing");
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("planarartifice:IEAuraInfusingFake"), new InfusionEnchantmentRecipeII(IEAuraInfusing, recipeStack));

        InfusionEnchantmentRecipeII IEProjecting = new InfusionEnchantmentRecipeII(EnumInfusionEnchantmentII.PROJECTING, new AspectList().add(Aspect.TOOL, 15).add(Aspect.AVERSION, 15).add(Aspect.MOTION, 15), new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)), new ItemStack(Items.ENDER_PEARL));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("planarartifice:IEProjecting"), IEProjecting);
        recipeStack = new ItemStack(ItemsTC.thaumiumSword);
        recipeStack.setStackDisplayName(TextFormatting.RESET + recipeStack.getDisplayName() + " +" + TextFormatting.GOLD + "Projecting");
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("planarartifice:IEProjectingFake"), new InfusionEnchantmentRecipeII(IEProjecting, recipeStack));
        // Scannables
        ScanningManager.addScannableThing(new ScanBlock("!Portal", Blocks.PORTAL));
        // Caster
        FocusEngine.registerElement(FocusEffectPrismLight.class, new ResourceLocation("planarartifice", "textures/foci/prism.png"), 0xff00ff);
        FocusEngine.registerElement(FocusEffectColourized.class, new ResourceLocation("planarartifice", "textures/foci/colourizer.png"), 0xffffff);

        // Golem Material
        GolemMaterial.register(new GolemMaterial("ALKIMIUM", new String[]{"MATSTUDYALKIMIUM"}, new ResourceLocation("planarartifice", "textures/models/golem/alkimium_golem.png"), 0x4CD482, 13, 12, 4, new ItemStack(PAItems.alkimium_ingot), new ItemStack(ItemsTC.mechanismSimple), new EnumGolemTrait[]{EnumGolemTrait.BLASTPROOF, EnumGolemTrait.LIGHT, EnumGolemTrait.FIREPROOF, EnumGolemTrait.FRAGILE}));

        // Golem Addon
        GolemAddon.register(new GolemAddon("TELEPORT_PACK", new String[]{"MATSTUDYALKIMIUM"}, new ResourceLocation("planarartifice", "textures/misc/golem/addon_teleport_pack.png"), new PartModelHauler(new ResourceLocation("thaumcraft", "models/obj/golem_hauler.obj"), new ResourceLocation("thaumcraft", "textures/entity/golems/golem_hauler.png"), PartModel.EnumAttachPoint.BODY), new Object[]{new ItemStack(PAItems.dimensional_singularity), new ItemStack(Blocks.OBSIDIAN)}, new EnumGolemTrait[]{teleporter}));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);

        ForgeChunkManager.setForcedChunkLoadingCallback(this, this);

        // More Aspects
        PAAspects.addAspectsToItems();

        // Multiblocks
        PAMultiblocks.init();

        for(int i = 0; i < FocusEngine.elements.values().size(); i++){
            try{
                if(((Class<IFocusElement>)FocusEngine.elements.values().toArray()[i]).newInstance() instanceof FocusEffect)
                    PlanarArtifice.focusEffects.add(((FocusEffect)((Class<IFocusElement>)FocusEngine.elements.values().toArray()[i]).newInstance()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world){
    }

    @SubscribeEvent
    public static void onTick(ServerTickEvent tick){
        serverTickCount++;
        PAAspects.setupColours(serverTickCount);
        proxy.onTick();

        isSingleplayer = !FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer();

        p.put("planarartifice.FOCUSCOLOURED", currentColourPicked);
    }

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent event){
        Item i = event.getEntityPlayer().getHeldItem(event.getHand()).getItem();
        if(i instanceof ItemCaster) if(event.getEntityPlayer().getHeldItem(event.getHand()).hasTagCompound()){
            NBTTagCompound l = event.getEntityPlayer().getHeldItem(event.getHand()).getTagCompound().getCompoundTag("focus").getCompoundTag("tag");
            if(l.hasKey("color")) currentColourPicked = l.getInteger("color");
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event){
        if(event.getEntityLiving() instanceof EntityPlayer && BaublesApi.isBaubleEquipped((EntityPlayer)event.getEntityLiving(), PAItems.mirrored_amulet) >= 0){
            ArrayList<ItemStack> arrayList = new ArrayList<>();
            for(int i = 0; i < ((EntityPlayer)event.getEntityLiving()).inventory.getSizeInventory(); i++) arrayList.add(((EntityPlayer)event.getEntityLiving()).inventory.getStackInSlot(i));
            inventoriesToConserve.put(event.getEntityLiving().getUniqueID(), arrayList);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if(inventoriesToConserve.containsKey(event.getEntityPlayer().getUniqueID())){
            for(int i = 0; i < inventoriesToConserve.get(event.getEntityPlayer().getUniqueID()).size(); i++)
                if(inventoriesToConserve.get(event.getEntityPlayer().getUniqueID()).get(i).getItem() != PAItems.mirrored_amulet)
                    event.getEntityPlayer().inventory.setInventorySlotContents(i, inventoriesToConserve.get(event.getEntityPlayer().getUniqueID()).get(i));
            inventoriesToConserve.remove(event.getEntityPlayer().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerDrop(PlayerDropsEvent event){
        if(inventoriesToConserve.containsKey(event.getEntityPlayer().getUniqueID())) event.setCanceled(true);
    }

    public static int[] interpolateInt(double start, double end, int count){
        if(count < 2) throw new IllegalArgumentException("interpolate: illegal count!");
        int[] array = new int[count + 1];
        for(int i = 0; i <= count; ++i) array[i] = (int)(start + i * (end - start) / count);
        return array;
    }

    @SideOnly(value = Side.CLIENT)
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event){
        event.getItemStack();
        NBTTagList nbttaglist = EnumInfusionEnchantmentII.getInfusionEnchantmentTagList(event.getItemStack());
        if(nbttaglist != null){
            for(int j = 0; j < nbttaglist.tagCount(); ++j){
                short k = nbttaglist.getCompoundTagAt(j).getShort("id");
                short l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
                if(k < 0 || k >= EnumInfusionEnchantmentII.values().length)
                    continue;
                String s = TextFormatting.GOLD + I18n.translateToLocal("enchantment.infusion." + EnumInfusionEnchantmentII.values()[k].toString());
                if(EnumInfusionEnchantmentII.values()[k].maxLevel > 1){
                    s = s + " " + I18n.translateToLocal("enchantment.level." + l);
                }
                event.getToolTip().add(1, s);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDropsInfusion(LivingDropsEvent event){
        if(event.getSource().getTrueSource() instanceof EntityPlayer){
            ItemStack item = ((EntityPlayer)event.getSource().getTrueSource()).getHeldItem(EnumHand.MAIN_HAND);
            int level = EnumInfusionEnchantmentII.getInfusionEnchantmentLevel(item, EnumInfusionEnchantmentII.TRANSMUTATIVE);
            if(level > 0){
                ArrayList<ItemStack> items = new ArrayList<>();
                for(EntityItem i : event.getDrops()){
                    ItemStack tem = i.getItem();
                    if(transmutations.containsKey(tem.getItem())){
                        items.add(new ItemStack(transmutations.get(tem.getItem()), tem.getCount()));
                    }else{
                        items.add(tem);
                    }
                }
                for(int i = 0; i < event.getDrops().size(); i++){
                    event.getDrops().get(i).setItem(items.get(i));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event){
        if(event.getAttackingPlayer() != null){
            event.getAttackingPlayer().getHeldItemMainhand();
            EntityPlayer p = event.getAttackingPlayer();
            ItemStack item = p.getHeldItemMainhand();
            int level = EnumInfusionEnchantmentII.getInfusionEnchantmentLevel(item, EnumInfusionEnchantmentII.AURAINFUSING);
            if(level > 0){
                int amount = event.getOriginalExperience();
                AuraHelper.addVis(p.getEntityWorld(), p.getPosition(), (float)(0.5 * amount));
                event.setCanceled(true);
            }
        }
    }

    static EnumGolemTrait createOtherEnumGolemTrait(String name, int ord, ResourceLocation icon){
        EnumGolemTrait enumValue;

        Class<EnumGolemTrait> monsterClass = EnumGolemTrait.class;
        // first we need to find our constructor, and make it accessible
        Constructor<?> constructor = monsterClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        Field constructorAccessorField;
        try{
            constructorAccessorField = Constructor.class.getDeclaredField("constructorAccessor");
            constructorAccessorField.setAccessible(true);
            // sun.reflect.ConstructorAccessor -> internal class, we should not use it, if you need use it, it would be better to actually not import it, but use it only via reflections. (as package may change, and will in java 9+)
            sun.reflect.ConstructorAccessor ca = (sun.reflect.ConstructorAccessor)constructorAccessorField.get(constructor);
            if(ca == null){
                Method acquireConstructorAccessorMethod = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");
                acquireConstructorAccessorMethod.setAccessible(true);
                ca = (sun.reflect.ConstructorAccessor)acquireConstructorAccessorMethod.invoke(constructor);
            }

            enumValue = (EnumGolemTrait)ca.newInstance(new Object[]{name, ord, icon});
            return enumValue;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Planar Artifice: Failed to add custom golem trait!");
            return null;
        }
    }
}