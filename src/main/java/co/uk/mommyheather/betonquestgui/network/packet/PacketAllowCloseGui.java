package co.uk.mommyheather.betonquestgui.network.packet;

import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketAllowCloseGui
{
    public static void encode(PacketCloseGui packet, FriendlyByteBuf buffer)
    {
    }

    public static PacketAllowCloseGui decode(FriendlyByteBuf buffer)
    {
        return new PacketAllowCloseGui();
    }

    public static class Handler
    {
        public static void handle(PacketAllowCloseGui packet, CustomPayloadEvent.Context context)
        {
            context.enqueueWork(PacketHandler::handleAllowCloseGui);
            context.setPacketHandled(true);
        }
    }
}
