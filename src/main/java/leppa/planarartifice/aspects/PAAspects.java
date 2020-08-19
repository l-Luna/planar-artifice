package leppa.planarartifice.aspects;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.google.common.primitives.Ints;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.blocks.PABlocks;
import leppa.planarartifice.items.PAItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

public class PAAspects{
	
	// Spatium
	public static Aspect DIMENSIONS;
	// Tempus
	public static Aspect TIME;
	// Tinctura
	public static Aspect COLOUR;
	
	public static ArrayList<Color> tincturaColours = new ArrayList<>();
	
	static Class clazz = Aspect.class;
	static Field f;
	
	public static void setupColours(int x){
		try{
			Field f = clazz.getDeclaredField("color");
			f.setAccessible(true);
			f.set(COLOUR, tincturaColours.get(x % tincturaColours.size()).getRGB());
			Aspect.aspects.put("Tinctura", COLOUR);
			// System.out.println(TIME.getColor());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("A thing with colors went very wrong!");
		}
	}
	
	public static void init(){
		DIMENSIONS = new Aspect("Spatio", 0x4AF755, new Aspect[]{Aspect.VOID, Aspect.ENTROPY}, new ResourceLocation("planarartifice", "textures/aspects/spatium.png"), 1);
		TIME = new Aspect("Tempus", 0xD6DB43, new Aspect[]{DIMENSIONS, Aspect.EXCHANGE}, new ResourceLocation("planarartifice", "textures/aspects/tempus.png"), 1);
		COLOUR = new Aspect("Tinctura", 0xFFFFFF, new Aspect[]{Aspect.EXCHANGE, Aspect.SENSES}, new ResourceLocation("planarartifice", "textures/aspects/tinctura.png"), 1);
		
		int timeToChange = 50;
		
		ArrayList<Integer> tincturaColoursRed = new ArrayList<Integer>();
		ArrayList<Integer> tincturaColoursBlue = new ArrayList<Integer>();
		ArrayList<Integer> tincturaColoursGreen = new ArrayList<Integer>();
		
		tincturaColoursRed.addAll(Ints.asList(PlanarArtifice.interpolateInt(0, 255, timeToChange)));
		tincturaColoursGreen.addAll(Ints.asList(PlanarArtifice.interpolateInt(255, 0, timeToChange)));
		tincturaColoursBlue.addAll(Ints.asList(PlanarArtifice.interpolateInt(255, 255, timeToChange)));
		
		tincturaColoursRed.addAll(Ints.asList(PlanarArtifice.interpolateInt(255, 255, timeToChange)));
		tincturaColoursGreen.addAll(Ints.asList(PlanarArtifice.interpolateInt(0, 255, timeToChange)));
		tincturaColoursBlue.addAll(Ints.asList(PlanarArtifice.interpolateInt(255, 0, timeToChange)));
		
		tincturaColoursRed.addAll(Ints.asList(PlanarArtifice.interpolateInt(255, 0, timeToChange)));
		tincturaColoursGreen.addAll(Ints.asList(PlanarArtifice.interpolateInt(255, 255, timeToChange)));
		tincturaColoursBlue.addAll(Ints.asList(PlanarArtifice.interpolateInt(0, 255, timeToChange)));
		
		for(int g = 3 * timeToChange; g > 0; g--){
			Color Q = new Color(tincturaColoursRed.get(g).intValue(), tincturaColoursGreen.get(g).intValue(), tincturaColoursBlue.get(g).intValue());
			tincturaColours.add(Q);
		}
	}
	
	public static void addAspectsToItems(){
		addAspectsToItem(new ItemStack(Items.CLOCK), new AspectList().add(PAAspects.TIME, 10));
		addAspectsToItem(new ItemStack(Items.ENDER_PEARL), new AspectList().add(PAAspects.DIMENSIONS, 10).add(PAAspects.TIME, 5));
		addAspectsToItem(new ItemStack(Blocks.ENDER_CHEST), new AspectList().add(PAAspects.DIMENSIONS, 12));
		addAspectsToItem(new ItemStack(PABlocks.flux_scrubber), new AspectList().add(PAAspects.DIMENSIONS, 6));
		addAspectsToItem(new ItemStack(PAItems.dimensional_curiosity), new AspectList().add(PAAspects.DIMENSIONS, 25));
		addAspectsToItem(new ItemStack(PAItems.bismuth_ingot), new AspectList().add(PAAspects.DIMENSIONS, 3));
		addAspectsToItem(new ItemStack(PAItems.dimensional_singularity), new AspectList().add(PAAspects.DIMENSIONS, 30).add(PAAspects.TIME, 30));
		addAspectsToItem(new ItemStack(Items.BONE), new AspectList().add(PAAspects.COLOUR, 5));
		addAspectsToItem(new ItemStack(Items.GLOWSTONE_DUST), new AspectList().add(PAAspects.COLOUR, 5));
		addAspectsToItem(new ItemStack(Blocks.WOOL), new AspectList().add(PAAspects.COLOUR, 5));
		addAspectsToItem(new ItemStack(Blocks.LAPIS_BLOCK), new AspectList().add(PAAspects.COLOUR, 180));
		addAspectsToItem(new ItemStack(Blocks.LAPIS_ORE), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.RED_FLOWER), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.YELLOW_FLOWER), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.CACTUS), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.DOUBLE_PLANT), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.GLOWSTONE), new AspectList().add(PAAspects.COLOUR, 15));
		
		addAspectsToItem(new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.RED_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.GRAY_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		addAspectsToItem(new ItemStack(Blocks.SILVER_GLAZED_TERRACOTTA), new AspectList().add(PAAspects.COLOUR, 15));
		
		for(int i = 0; i < 16; i++){
			addAspectsToItem(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i), new AspectList().add(PAAspects.COLOUR, 10));
			addAspectsToItem(new ItemStack(Items.DYE, 1, i), new AspectList().add(PAAspects.COLOUR, 20));
			addAspectsToItem(new ItemStack(Blocks.STAINED_GLASS, 1, i), new AspectList().add(PAAspects.COLOUR, 15));
		}
		
		setItemAspects(new ItemStack(PAItems.condensed_crystal_cluster), new AspectList().add(Aspect.FIRE, 12).add(Aspect.AIR, 12).add(Aspect.EARTH, 12).add(Aspect.ORDER, 12).add(Aspect.ENTROPY, 12).add(Aspect.MAGIC, 12).add(Aspect.ENERGY, 12).add(Aspect.WATER, 12));
	}
	
	public static void addAspectsToItem(ItemStack itemstack, AspectList aspects){
		ThaumcraftApi.registerObjectTag(itemstack, AspectHelper.getObjectAspects(itemstack).add(aspects));
	}
	
	public static void setItemAspects(ItemStack itemstack, AspectList aspects){
		ThaumcraftApi.registerObjectTag(itemstack, aspects);
	}
}