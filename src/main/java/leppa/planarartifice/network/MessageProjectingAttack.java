package leppa.planarartifice.network;

import io.netty.buffer.ByteBuf;
import leppa.planarartifice.PlanarArtifice;
import leppa.planarartifice.aspects.EnumInfusionEnchantmentII;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageProjectingAttack implements IMessage{
	private int entityId;
	
	public MessageProjectingAttack(){
		// need this constructor
	}
	
	public MessageProjectingAttack(int parEntityId){
		entityId = parEntityId;
		// DEBUG
		System.out.println("Constructor");
	}
	
	@Override
	public void fromBytes(ByteBuf buf){
		entityId = ByteBufUtils.readVarInt(buf, 4);
		// DEBUG
		System.out.println("fromBytes");
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		ByteBufUtils.writeVarInt(buf, entityId, 4);
		// DEBUG
		System.out.println("toBytes encoded");
	}
	
	public static class Handler implements IMessageHandler<MessageProjectingAttack, IMessage>{
		@Override
		public IMessage onMessage(final MessageProjectingAttack message, MessageContext ctx){
			// DEBUG
			System.out.println("Message received");
			// Know it will be on the server so make it thread-safe
			final EntityPlayerMP thePlayer = (EntityPlayerMP)PlanarArtifice.proxy.getPlayerEntityFromContext(ctx);
			System.out.println(thePlayer);
			thePlayer.getServer().addScheduledTask(new Runnable(){
				@Override
				public void run(){
					Entity theEntity = thePlayer.getEntityWorld().getEntityByID(message.entityId);
					// DEBUG
					System.out.println("Entity = " + theEntity);
					
					// Need to ensure that hackers can't cause trick kills,
					// so double check weapon type and reach
					if(itemProjectingLevel(thePlayer.getHeldItemMainhand()) > 0){
						System.out.println("Will attack!");
						int projectingLevel = itemProjectingLevel(thePlayer.getHeldItemMainhand()) + 5;
						double distanceSq = thePlayer.getDistanceSq(theEntity);
						double reachSq = (2*projectingLevel) * 2*(projectingLevel);
						if(reachSq >= distanceSq){
							thePlayer.attackTargetEntityWithCurrentItem(theEntity);
						}
					}
					return;
				}
			});
			return null;
		}
	}
	
	public static int itemProjectingLevel(ItemStack stack){
		int level = EnumInfusionEnchantmentII.getInfusionEnchantmentLevel(stack, EnumInfusionEnchantmentII.PROJECTING);
		return level;
	}
}