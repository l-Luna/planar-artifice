package leppa.planarartifice.foci;

import java.util.HashMap;
import java.util.Set;

import leppa.planarartifice.PlanarArtifice;

/**
 * DO NOT USE THIS OUTSIDE OF THE COLOURIZER.
 * 
 * @author Leppa
 *
 */
public class HashmapColourizerSettingsHack extends HashMap{
	
	public Object get(Object key){
		if(key instanceof String && key.equals("customSettingOne")){
			if(PlanarArtifice.focusEffects.get(PlanarArtifice.currentFocusEffect).createSettings().length >= 1){
				return PlanarArtifice.focusEffects.get(PlanarArtifice.currentFocusEffect).createSettings()[0];
			}
		}else if(key instanceof String && key.equals("customSettingTwo")){
			if(PlanarArtifice.focusEffects.get(PlanarArtifice.currentFocusEffect).createSettings().length >= 2){
				return PlanarArtifice.focusEffects.get(PlanarArtifice.currentFocusEffect).createSettings()[1];
			}
		}
		return super.get(key);
	}
}