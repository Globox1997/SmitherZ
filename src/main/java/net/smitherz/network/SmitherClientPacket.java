package net.smitherz.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.smitherz.network.packet.ScreenPacket;
import net.smitherz.network.packet.SmithPacket;
import net.smitherz.network.packet.SmithUpdatePacket;
import net.smitherz.screen.SmitherScreen;

@Environment(EnvType.CLIENT)
public class SmitherClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SmithUpdatePacket.PACKET_ID, (payload, context) -> {
            boolean disableButton = payload.disableButton();
            context.client().execute(() -> {
                if (context.client().currentScreen instanceof SmitherScreen smitherScreen) {
                    smitherScreen.getSmitherButton().setDisabled(disableButton);
                }
            });
        });
    }

    // screenId: 1 = smithing (vanilla), 2 = smither, 3 = grindstone, 4 = grinder
    public static void writeC2SScreenPacket(int mouseX, int mouseY, int screenId) {
        ClientPlayNetworking.send(new ScreenPacket(screenId, mouseX, mouseY));
    }

    public static void writeC2SSmitherPacket() {
        ClientPlayNetworking.send(new SmithPacket());
    }

}
