package net.smitherz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SmithPacket() implements CustomPayload {

    public static final CustomPayload.Id<SmithPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("smitherz", "smith_packet"));

    public static final PacketCodec<RegistryByteBuf, SmithPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
    }, buf -> new SmithPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}


