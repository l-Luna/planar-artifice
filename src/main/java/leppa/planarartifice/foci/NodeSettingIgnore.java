package leppa.planarartifice.foci;

import thaumcraft.api.casters.NodeSetting.INodeSettingType;

public class NodeSettingIgnore implements INodeSettingType{
	
	@Override
	public int getDefault(){
		return 0;
	}
	
	@Override
	public int clamp(int var1){
		return 0;
	}
	
	@Override
	public int getValue(int var1){
		return 0;
	}
	
	@Override
	public String getValueText(int var1){
		return "N/A";
	}
}
