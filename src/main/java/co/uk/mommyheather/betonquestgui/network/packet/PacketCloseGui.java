package co.uk.mommyheather.betonquestgui.network.packet;

import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketCloseGui
{
    public static void encode(PacketCloseGui packet, FriendlyByteBuf buffer)
    {
    }

    public static PacketCloseGui decode(FriendlyByteBuf buffer)
    {
        return new PacketCloseGui();
    }

    public static class Handler
    {
        public static void handle(PacketCloseGui packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(PacketHandler::handleCloseGui);
            context.setPacketHandled(true);
        }
    }
}
