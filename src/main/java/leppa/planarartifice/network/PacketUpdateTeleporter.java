package leppa.planarartifice.network;

import io.netty.buffer.ByteBuf;
import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateTeleporter implements IMessage{
	
	private BlockPos pos;
	private ItemStack stack;
	private long lastChangeTime;
	
	public PacketUpdateTeleporter(BlockPos pos, ItemStack stack, long lastChangeTime){
		this.pos = pos;
		this.stack = stack;
		this.lastChangeTime = lastChangeTime;
	}
	
	public PacketUpdateTeleporter(TileTeleporter te){
		this(te.getPos(), te.crystal.getStackInSlot(0), te.lastChangeTime);
	}
	
	public PacketUpdateTeleporter(){}
	
	@Override
	public void fromBytes(ByteBuf buf){
		pos = BlockPos.fromLong(buf.readLong());
		stack = ByteBufUtils.readItemStack(buf);
		lastChangeTime = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeLong(lastChangeTime);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateTeleporter, IMessage>{
		
		@Override
		public IMessage onMessage(PacketUpdateTeleporter message, MessageContext ctx){
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileTeleporter te = (TileTeleporter)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.crystal.setStackInSlot(0, message.stack);
				te.lastChangeTime = message.lastChangeTime;
			});
			return null;
		}
		
	}
}
