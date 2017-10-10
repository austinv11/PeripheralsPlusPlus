package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.tiles.TileEntitySpeaker;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SynthResponsePacket implements IMessage {

    private BlockPos pos;
    public String text;
	public int x,y,z;
	public World world;
	public TurtleSide side;
	
	public SynthResponsePacket(){}
	
	public SynthResponsePacket(String text, BlockPos pos, World world, TurtleSide side) {
		this.text = text;
		this.pos = pos;
		this.world = world;
		this.side = side;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		text = tag.getString("text");
        int[] posArray = tag.getIntArray("pos");
        pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		world = DimensionManager.getWorld(tag.getInteger("dim"));
		side = tag.getString("side").equals("null") ? null : TurtleSide.valueOf(tag.getString("side"));
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("text", text);
        tag.setIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
		tag.setInteger("dim", world.provider.getDimension());
		tag.setString("side", side == null ? "null" : side.name());
		ByteBufUtils.writeTag(buf, tag);
	}
	
	public static class SynthResponsePacketHandler implements IMessageHandler<SynthResponsePacket, IMessage> {
		
		@Override
		public IMessage onMessage(SynthResponsePacket message, MessageContext ctx) {
			if (message.side == null)
				((TileEntitySpeaker) message.world.getTileEntity(message.pos)).onSpeechCompletion(message.text, null);
			else
				try {
					ITurtleAccess turtle = ReflectionHelper.getTurtle(message.world.getTileEntity(message.pos));
					((TileEntitySpeaker)turtle.getPeripheral(message.side)).onSpeechCompletion(message.text, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			return null;
		}
	}
}
