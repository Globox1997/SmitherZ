package net.smitherz.item.attribute;

import java.util.UUID;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public class GemEntityAttributeModifier extends EntityAttributeModifier {

    public GemEntityAttributeModifier(String name, double value, Operation operation) {
        super(name, value, operation);
    }

    public GemEntityAttributeModifier(UUID uuid, String name, double value, Operation operation) {
        super(uuid, name, value, operation);
    }

}
