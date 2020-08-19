package leppa.planarartifice.foci;

import leppa.planarartifice.PlanarArtifice;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.IFocusElement;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.NodeSetting.INodeSettingType;

public class NodeSettingBaseFocus implements INodeSettingType{
	
	@Override
	public int getDefault(){
		return 0;
	}
	
	@Override
	public int clamp(int var1){
		if(var1 < 0){
			var1 = PlanarArtifice.focusEffects.size() - 1;
		}
		return var1 % PlanarArtifice.focusEffects.size();
	}
	
	@Override
	public int getValue(int var1){
		return var1;
	}
	
	@Override
	public String getValueText(int var1){
		return PlanarArtifice.proxy.localize(PlanarArtifice.focusEffects.get(var1).getKey() + ".name");
	}
	
	public class NodeSettingFocusBase extends NodeSetting{
		
		int value;
		
		public NodeSettingFocusBase(String key, String description){
			super(key, description, new NodeSettingBaseFocus());
		}
		
		public void increment(){
			++value;
			value = getType().clamp(getValue());
			PlanarArtifice.currentFocusEffect = getValue();
		}
		
		public void decrement(){
			--value;
			value = getType().clamp(getValue());
			PlanarArtifice.currentFocusEffect = getValue();
		}
		
		public int getValue(){
			return getType().getValue(value);
		}
		
		public void setValue(int truevalue){
			int lv = -1;
			value = 0;
			while(getValue() != truevalue && lv != value){
				lv = value;
				increment();
			}
		}
		
		public String getValueText(){
			return I18n.translateToLocal((String)this.getType().getValueText(this.value));
		}
	}
}