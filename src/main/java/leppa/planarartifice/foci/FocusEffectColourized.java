package leppa.planarartifice.foci;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import leppa.planarartifice.PlanarArtifice;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.FocusNode;
import thaumcraft.api.casters.IFocusElement;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;

public class FocusEffectColourized extends FocusEffect{
	
	public static Field settings;
	
	public FocusEffectColourized(){
		super();
		try{
			settings = FocusNode.class.getDeclaredField("settings");
			settings.setAccessible(true);
			HashmapColourizerSettingsHack hash = new HashmapColourizerSettingsHack();
			hash.putAll((HashMap)settings.get(this));
			settings.set(this, hash);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public String getKey(){
		return "planarartifice.FOCUSCOLOURED";
	}
	
	@Override
	public String getResearch(){
		return "FOCUSLIGHT";
	}
	
	@Override
	public boolean execute(RayTraceResult var1, Trajectory var2, float var3, int var4){
		PlanarArtifice.focusEffects.get(getSettingValue("focus")).setPackage(getPackage());
		PlanarArtifice.focusEffects.get(getSettingValue("focus")).setParent(getParent());
		PlanarArtifice.focusEffects.get(getSettingValue("focus"));
		return PlanarArtifice.focusEffects.get(getSettingValue("focus")).execute(var1, var2, var3, var4);
	}
	
	@Override
	public void renderParticleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12){
		PlanarArtifice.focusEffects.get(getSettingValue("focus")).renderParticleFX(var1, var2, var4, var6, var8, var10, var12);
	}
	
	@Override
	public int getComplexity(){
		return PlanarArtifice.focusEffects.get(getSettingValue("focus")).getComplexity();
	}
	
	@Override
	public Aspect getAspect(){
		return PlanarArtifice.focusEffects.get(getSettingValue("focus")).getAspect();
	}
	
	@Override
	public NodeSetting[] createSettings(){
		//return new NodeSetting[]{new NodeSettingBaseFocus().new NodeSettingFocusBase("focus", "focus.colourizer.focus"), new NodeSetting("customSettingOne", "focus.colourizer.invisible", new NodeSettingIgnore()), new NodeSetting("customSettingTwo", "focus.colourizer.invisible", new NodeSettingIgnore())};
		return new NodeSetting[]{new NodeSettingBaseFocus().new NodeSettingFocusBase("focus", "focus.colourizer.focus")};
	}
}