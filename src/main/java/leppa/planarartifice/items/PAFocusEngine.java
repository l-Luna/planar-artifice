package leppa.planarartifice.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import thaumcraft.api.casters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PAFocusEngine{

    // This is dumb

    private static ArrayList<String> damageResistList = new ArrayList<>();
    static HashMap<Integer, Long> cooldownServer = new HashMap<>();
    static HashMap<Integer, Long> cooldownClient = new HashMap<>();

    public static void castFocusPackageWithMultiplier(EntityLivingBase caster, FocusPackage focusPackage, boolean nocopy, float multiplier){
        FocusPackage focusPackageCopy = nocopy ? focusPackage : focusPackage.copy(caster);
        focusPackageCopy.initialize(caster);
        focusPackageCopy.setUniqueID(UUID.randomUUID());
        for(FocusEffect effect : focusPackageCopy.getFocusEffects()) effect.onCast(caster);
        PAFocusEngine.runFocusPackageWithEffectMultiplier(focusPackageCopy, null, null, multiplier);
    }

    public static void castFocusPackageWithMultiplier(EntityLivingBase caster, FocusPackage focusPackage, float multiplier){
        PAFocusEngine.castFocusPackageWithMultiplier(caster, focusPackage, false, multiplier);
    }

    public static void runFocusPackageWithEffectMultiplier(FocusPackage focusPackage, Trajectory[] trajectories, RayTraceResult[] targets, float multiplier){
        Trajectory[] prevTrajectories = trajectories;
        RayTraceResult[] prevTargets = targets;
        List<IFocusElement> list = focusPackage.nodes;
        synchronized(list){
            if(!(focusPackage.nodes.get(0) instanceof FocusMediumRoot)){
                focusPackage.nodes.add(0, new FocusMediumRoot(trajectories, targets));
            }
            for(int idx = 0; idx < focusPackage.nodes.size(); ++idx){
                focusPackage.setExecutionIndex(idx);
                IFocusElement node = focusPackage.nodes.get(idx);
                if(idx > 0 && ((FocusNode) node).getParent() == null){
                    IFocusElement nodePrev = focusPackage.nodes.get(idx - 1);
                    if(nodePrev instanceof FocusNode) ((FocusNode) node).setParent((FocusNode) nodePrev);
                }
                if(node instanceof FocusNode && ((FocusNode) node).getPackage() == null){
                    ((FocusNode) node).setPackage(focusPackage);
                }
                if(node instanceof FocusNode){
                    focusPackage.multiplyPower(((FocusNode) node).getPowerMultiplier());
                }
                if(node instanceof FocusPackage){
                    FocusEngine.runFocusPackage((FocusPackage) node, prevTrajectories, prevTargets);
                    break;
                }
                if(node instanceof FocusMedium){
                    FocusMedium medium = (FocusMedium) node;
                    if(prevTrajectories != null){
                        for(Trajectory trajectory : prevTrajectories){
                            medium.execute(trajectory);
                        }
                    }
                    if(medium.hasIntermediary()){
                        break;
                    }
                }else if(node instanceof FocusMod){
                    if(node instanceof FocusModSplit){
                        FocusModSplit split = (FocusModSplit) node;
                        for(FocusPackage sp : split.getSplitPackages()){
                            split.setPackage(sp);
                            sp.multiplyPower(focusPackage.getPower());
                            split.execute();
                            FocusEngine.runFocusPackage(sp, split.supplyTrajectories(), split.supplyTargets());
                        }
                        break;
                    }
                    ((FocusMod) node).execute();
                }else if(node instanceof FocusEffect){
                    focusPackage.multiplyPower(multiplier);
                    FocusEffect effect = (FocusEffect) node;
                    if(prevTargets != null){
                        int num = 0;
                        for(RayTraceResult target : prevTargets){
                            if(target.entityHit != null){
                                String k = "" + target.entityHit.getEntityId() + focusPackage.getUniqueID().toString();
                                if(damageResistList.contains(k) && target.entityHit.hurtResistantTime > 0) target.entityHit.hurtResistantTime = 0;
                                else{
                                    if(damageResistList.size() > 10) damageResistList.remove(0);
                                    damageResistList.add(k);
                                }
                            }
                            Trajectory tra = prevTrajectories != null ? (prevTrajectories.length == prevTargets.length ? prevTrajectories[num] : prevTrajectories[0]) : null;
                            effect.execute(target, tra, focusPackage.getPower(), num);
                            ++num;
                        }
                    }
                }
                if(!(node instanceof FocusNode))
                    continue;
                prevTrajectories = ((FocusNode) node).supplyTrajectories();
                prevTargets = ((FocusNode) node).supplyTargets();
            }
        }
    }

    public static void setCooldown(EntityLivingBase entityLiving, int cd){
        if(cd == 0){
            cooldownClient.remove(entityLiving.getEntityId());
            cooldownServer.remove(entityLiving.getEntityId());
        }else if(entityLiving.world.isRemote) cooldownClient.put(entityLiving.getEntityId(), System.currentTimeMillis() + (long) (cd * 50));
        else cooldownServer.put(entityLiving.getEntityId(), System.currentTimeMillis() + (long) (cd * 50));
    }

    static boolean isOnCooldown(EntityLivingBase entityLiving){
        if(entityLiving.world.isRemote && cooldownClient.containsKey(entityLiving.getEntityId())) return cooldownClient.get(entityLiving.getEntityId()) > System.currentTimeMillis();
        if(!entityLiving.world.isRemote && cooldownServer.containsKey(entityLiving.getEntityId())) return cooldownServer.get(entityLiving.getEntityId()) > System.currentTimeMillis();
        return false;
    }
}