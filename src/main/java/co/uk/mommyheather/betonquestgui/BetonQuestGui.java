package co.uk.mommyheather.betonquestgui;

import co.uk.mommyheather.betonquestgui.config.BQGConfig;
import co.uk.mommyheather.betonquestgui.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(BetonQuestGui.MOD_ID)
@Mod.EventBusSubscriber(modid = BetonQuestGui.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetonQuestGui
{
    public static final String MOD_ID = "betonquestgui";
    public static final int PROTOCOL_VERSION = 1;

    public BetonQuestGui() {
        BQGConfig.register();
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.addListener(BetonQuestGui::clientLogInToServer);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (incoming, isNetwork) -> true));
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PacketHandler::registerPackets);
        
    }
    
    public static void clientLogInToServer(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft.getInstance().player.connection.sendCommand("bqgui login");
    }
}
