package leppa.planarartifice.potion;

import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.Arrays;

public enum PotionCatalouges{

    piston(
            Potion.getPotionFromResourceLocation("speed"), Potion.getPotionFromResourceLocation("slowness"),
            Potion.getPotionFromResourceLocation("haste"), Potion.getPotionFromResourceLocation("jump_boost"),
            Potion.getPotionFromResourceLocation("levitation")),
    ice(
            Potion.getPotionFromResourceLocation("mining_fatigue"),
            Potion.getPotionFromResourceLocation("resistance"),
            Potion.getPotionFromResourceLocation("fire_resistance")),
    iron(
            Potion.getPotionFromResourceLocation("strength"),
            Potion.getPotionFromResourceLocation("weakness")),
    apple(
            Potion.getPotionFromResourceLocation("instant_health"),
            Potion.getPotionFromResourceLocation("instant_damage"),
            Potion.getPotionFromResourceLocation("regeneration"),
            Potion.getPotionFromResourceLocation("health_boost"),
            Potion.getPotionFromResourceLocation("saturation")),
    pufferfish(
            Potion.getPotionFromResourceLocation("nausea"),
            Potion.getPotionFromResourceLocation("water_breathing"),
            Potion.getPotionFromResourceLocation("blindness"),
            Potion.getPotionFromResourceLocation("hunger")),
    glass(
            Potion.getPotionFromResourceLocation("invisibility"),
            Potion.getPotionFromResourceLocation("night_vision"),
            Potion.getPotionFromResourceLocation("absorption")),
    rotten_flesh(
            Potion.getPotionFromResourceLocation("poison"),
            Potion.getPotionFromResourceLocation("wither")),
    nitor(
            Potion.getPotionFromResourceLocation("glowing")),
    gold(
            Potion.getPotionFromResourceLocation("luck"),
            Potion.getPotionFromResourceLocation("bad_luck"));

    public ArrayList<Potion> potions = new ArrayList<>();

    PotionCatalouges(Potion... catalouge){
		potions.addAll(Arrays.asList(catalouge));
    }
}