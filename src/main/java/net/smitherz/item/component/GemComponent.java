package net.smitherz.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.ArrayList;
import java.util.List;

public record GemComponent(List<ItemStack> gems, int size) {

    public static final GemComponent DEFAULT = new GemComponent(new ArrayList<ItemStack>(), 0);

    public static final PacketCodec<RegistryByteBuf, GemComponent> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        ItemStack.OPTIONAL_LIST_PACKET_CODEC.encode(buf, value.gems());
        buf.writeInt(value.size());
    }, buf -> new GemComponent(ItemStack.OPTIONAL_LIST_PACKET_CODEC.decode(buf), buf.readInt()));

    public static final Codec<GemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(ItemStack.OPTIONAL_CODEC.listOf().fieldOf("gems").forGetter(GemComponent::gems),
            Codec.INT.fieldOf("size").forGetter(GemComponent::size)).apply(instance, GemComponent::new));

}
