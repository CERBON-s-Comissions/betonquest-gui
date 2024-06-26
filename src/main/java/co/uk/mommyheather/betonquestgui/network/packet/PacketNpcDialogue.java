package co.uk.mommyheather.betonquestgui.network.packet;

import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketNpcDialogue
{
    private final String npcName;
    private final String text;

    public PacketNpcDialogue(String npcName, String text)
    {
        this.npcName = npcName;
        this.text = text;
    }

    public static PacketNpcDialogue decode(FriendlyByteBuf buffer)
    {
        byte[] data = new byte[buffer.capacity()];
        buffer.getBytes(0, data);
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        input.readByte();
        return new PacketNpcDialogue(input.readUTF(), input.readUTF());
    }

    public static class Handler
    {
        public static void handle(PacketNpcDialogue packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(() -> PacketHandler.handleNpcDialogue(packet.npcName, packet.text));
            context.setPacketHandled(true);
        }
    }
}
