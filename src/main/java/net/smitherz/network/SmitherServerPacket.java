package net.smitherz.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.libz.network.LibzServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.smitherz.access.ScreenHandlerAccess;
import net.smitherz.screen.SmitherScreenHandler;

public class SmitherServerPacket {

    public static final Identifier SET_SCREEN = new Identifier("smitherz", "set_screen");
    public static final Identifier SMITHER_READY = new Identifier("smitherz", "smither_ready");
    public static final Identifier SMITH = new Identifier("smitherz", "smith");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SET_SCREEN, (server, player, handler, buffer, sender) -> {
            int mouseX = buffer.readInt();
            int mouseY = buffer.readInt();
            Boolean smitherScreen = buffer.readBoolean();
            BlockPos pos = smitherScreen ? (player.currentScreenHandler instanceof SmithingScreenHandler ? ((ScreenHandlerAccess) player.currentScreenHandler).getPos() : null)
                    : (player.currentScreenHandler instanceof SmitherScreenHandler ? ((SmitherScreenHandler) player.currentScreenHandler).getPos() : null);
            if (pos != null) {
                server.execute(() -> {
                    if (smitherScreen) {
                        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                            return new SmitherScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(playerx.getWorld(), pos));
                        }, Text.translatable("container.link")));
                    } else {
                        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                            return new SmithingScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(playerx.getWorld(), pos));
                        }, Text.translatable("container.upgrade")));
                    }
                    LibzServerPacket.writeS2CMousePositionPacket(player, mouseX, mouseY);
                });
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SMITH, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                if (player.currentScreenHandler instanceof SmitherScreenHandler) {
                    ((SmitherScreenHandler) player.currentScreenHandler).smith();
                }
            });
        });
    }

    public static void writeS2CSmitherReadyPacket(ServerPlayerEntity serverPlayerEntity, boolean disableButton) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(disableButton);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SMITHER_READY, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

}
