package co.uk.mommyheather.betonquestgui.network.packet;

import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketOpenGui
{
    public static PacketOpenGui decode(FriendlyByteBuf buffer)
    {
        return new PacketOpenGui();
    }

    public static class Handler
    {
        public static void handle(PacketOpenGui packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(PacketHandler::handleOpenGui);
            context.setPacketHandled(true);
        }
    }
}
