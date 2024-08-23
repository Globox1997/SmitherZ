package net.smitherz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SmithUpdatePacket(boolean disableButton) implements CustomPayload {

    public static final CustomPayload.Id<SmithUpdatePacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("smitherz", "smith_update_packet"));

    public static final PacketCodec<RegistryByteBuf, SmithUpdatePacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeBoolean(value.disableButton());
    }, buf -> new SmithUpdatePacket(buf.readBoolean()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}


