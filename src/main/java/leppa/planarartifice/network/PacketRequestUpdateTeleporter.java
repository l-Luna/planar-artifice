package leppa.planarartifice.network;

import io.netty.buffer.ByteBuf;
import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateTeleporter implements IMessage{
	
	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateTeleporter(BlockPos pos, int dimension){
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateTeleporter(TileTeleporter te){
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateTeleporter(){}
	
	@Override
	public void fromBytes(ByteBuf buf){
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateTeleporter, PacketUpdateTeleporter>{
		
		@Override
		public PacketUpdateTeleporter onMessage(PacketRequestUpdateTeleporter message, MessageContext ctx){
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileTeleporter te = (TileTeleporter)world.getTileEntity(message.pos);
			if(te != null){
				return new PacketUpdateTeleporter(te);
			}else{
				return null;
			}
		}
		
	}
}