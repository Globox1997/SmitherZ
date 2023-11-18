package net.smitherz.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.smitherz.screen.SmitherScreen;

@Environment(EnvType.CLIENT)
public class SmitherClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SmitherServerPacket.SMITHER_READY, (client, handler, buf, sender) -> {
            boolean disableButton = buf.readBoolean();
            client.execute(() -> {
                if (client.currentScreen instanceof SmitherScreen) {
                    ((SmitherScreen) client.currentScreen).smitherButton.setDisabled(disableButton);
                }
            });
        });
    }

    // screenId: 1 = smithing (vanilla), 2 = smither, 3 = grindstone, 4 = grinder
    public static void writeC2SScreenPacket(int mouseX, int mouseY, int screenId) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(mouseX);
        buf.writeInt(mouseY);
        buf.writeInt(screenId);
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(SmitherServerPacket.SET_SCREEN, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
    }

    public static void writeC2SSmitherPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(SmitherServerPacket.SMITH, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
    }

}
