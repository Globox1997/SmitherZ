package net.smitherz.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.libz.access.ScreenHandlerAccess;
import net.libz.network.LibzServerPacket;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.smitherz.network.packet.ScreenPacket;
import net.smitherz.network.packet.SmithPacket;
import net.smitherz.network.packet.SmithUpdatePacket;
import net.smitherz.screen.GrinderScreenHandler;
import net.smitherz.screen.SmitherScreenHandler;

public class SmitherServerPacket {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(SmithUpdatePacket.PACKET_ID, SmithUpdatePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ScreenPacket.PACKET_ID, ScreenPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SmithPacket.PACKET_ID, SmithPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ScreenPacket.PACKET_ID, (payload, context) -> {
            int mouseX = payload.mouseX();
            int mouseY = payload.mouseY();
            int screenId = payload.screenId();
            BlockPos pos = context.player().currentScreenHandler instanceof ScreenHandlerAccess ? ((ScreenHandlerAccess) context.player().currentScreenHandler).getPos() : null;

            if (pos != null) {
                context.server().execute(() -> {
                    if (screenId == 1) {
                        context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                            return new SmitherScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(playerx.getWorld(), pos));
                        }, Text.translatable("container.link")));
                    } else if (screenId == 2) {
                        context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                            return new SmithingScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(playerx.getWorld(), pos));
                        }, Text.translatable("container.upgrade")));
                    } else if (screenId == 3) {
                        context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                            return new GrindstoneScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(playerx.getWorld(), pos));
                        }, Text.translatable("container.grindstone_title")));
                    } else if (screenId == 4) {
                        context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                            return new GrinderScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(playerx.getWorld(), pos));
                        }, Text.translatable("container.unlink")));
                    }
                    LibzServerPacket.writeS2CMousePositionPacket(context.player(), mouseX, mouseY);
                });
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SmithPacket.PACKET_ID, (payload, context) -> {
            context.server().execute(() -> {
                if (context.player().currentScreenHandler instanceof SmitherScreenHandler smitherScreenHandler) {
                    smitherScreenHandler.smith();
                }
            });
        });
    }

    public static void writeS2CSmitherReadyPacket(ServerPlayerEntity serverPlayerEntity, boolean disableButton) {
        ServerPlayNetworking.send(serverPlayerEntity, new SmithUpdatePacket(disableButton));
    }

}
