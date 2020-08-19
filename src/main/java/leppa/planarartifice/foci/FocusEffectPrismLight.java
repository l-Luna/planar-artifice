package leppa.planarartifice.foci;

import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.aspects.PAAspects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

public class FocusEffectPrismLight extends FocusEffect{
	
	PotionEffect onHitEffect = new PotionEffect(Potion.getPotionFromResourceLocation("blindness"), 1200);
	
	@Override
	public String getKey(){
		return "planarartifice.PRISMLIGHT";
	}
	
	@Override
	public String getResearch(){
		return "FOCUSLIGHT";
	}
	
	@Override
	public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num){
		PacketHandler.INSTANCE.sendToAllAround((IMessage)new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{this.getKey()}), new NetworkRegistry.TargetPoint(this.getPackage().world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0));
		if(target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit != null){
			float damage = this.getDamageForDisplay(finalPower);
			target.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)(target.entityHit != null ? target.entityHit : this.getPackage().getCaster()), (Entity)this.getPackage().getCaster()), damage);
			if(target.entityHit instanceof EntityLivingBase && ((EntityLivingBase)target.entityHit).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
				target.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)(target.entityHit != null ? target.entityHit : this.getPackage().getCaster()), (Entity)this.getPackage().getCaster()), damage);
			}
			if(target.entityHit instanceof EntityPlayer){
				((EntityPlayer)target.entityHit).addPotionEffect(onHitEffect);
			}
		}
		return false;
	}
	
	@Override
	public void renderParticleFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ){
		FXDispatcher.GenPart pp = new FXDispatcher.GenPart();
		pp.grav = -0.2f;
		pp.age = 10;
		pp.alpha = new float[]{0.7f};
		pp.partStart = 640;
		pp.partInc = 1;
		pp.partNum = 10;
		pp.slowDown = 0.75;
		pp.scale = new float[]{(float)(1.5 + world.rand.nextGaussian() * 0.20000000298023224)};
		FXDispatcher.INSTANCE.drawGenericParticles(posX, posY, posZ, motionX, motionY, motionZ, pp);
	}
	
	@Override
	public NodeSetting[] createSettings(){
		return new NodeSetting[]{new NodeSetting("power", "focus.common.power", new NodeSetting.NodeSettingIntRange(1, 5))};
	}
	
	@Override
	public float getDamageForDisplay(float finalPower){
		return (float)(3 + this.getSettingValue("power")) * finalPower;
	}
	
	@Override
	public int getComplexity(){
		return this.getSettingValue("power") * 3;
	}
	
	@Override
	public Aspect getAspect(){
		return Aspect.LIGHT;
	}
}