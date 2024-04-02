package co.uk.mommyheather.betonquestgui.network.packet;

import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketTargetLocation
{
    private final int x;
    private final int y;
    private final int z;

    public PacketTargetLocation(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static PacketTargetLocation decode(FriendlyByteBuf buffer)
    {
        byte[] data = new byte[buffer.capacity()];
        buffer.getBytes(0, data);
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        input.readByte();
        return new PacketTargetLocation(input.readInt(), input.readInt(), input.readInt());
    }

    public static class Handler
    {
        public static void handle(PacketTargetLocation packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(() -> PacketHandler.handleTargetLocation(packet.x, packet.y, packet.z));
            context.setPacketHandled(true);
        }
    }
}
