package co.uk.mommyheather.betonquestgui.network.packet;

import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketCreateGui
{
    public static PacketCreateGui decode(FriendlyByteBuf buffer)
    {
        return new PacketCreateGui();
    }

    public static class Handler
    {
        public static void handle(PacketCreateGui packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(PacketHandler::handleCreateGui);
            context.setPacketHandled(true);
        }
    }
}
