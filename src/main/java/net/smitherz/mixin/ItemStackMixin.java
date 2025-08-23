package net.smitherz.mixin;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.smitherz.init.ItemInit;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

//    @Inject(method = "Lnet/minecraft/item/ItemStack;applyAttributeModifier(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
//    private void applyAttributeModifierMixin(AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer, CallbackInfo info, AttributeModifiersComponent attributeModifiersComponent) {
//        applyGemAttributeModifier(attributeModifiersComponent, null, slot, attributeModifierConsumer);
//    }

    @Inject(method = "Lnet/minecraft/item/ItemStack;applyAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void applyAttributeModifiersMixin(EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer, CallbackInfo info, AttributeModifiersComponent attributeModifiersComponent) {
        applyGemAttributeModifier(attributeModifiersComponent, slot, null, attributeModifierConsumer);
    }

    @Unique
    private void applyGemAttributeModifier(@Nullable AttributeModifiersComponent attributeModifiersComponent, @Nullable EquipmentSlot equipmentSlot, @Nullable AttributeModifierSlot attributeModifierSlot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer) {
        ItemStack itemStack = (ItemStack) (Object) this;
        if (itemStack.get(ItemInit.GEMS) != null) {
            if (attributeModifiersComponent.modifiers().isEmpty()) {
                attributeModifiersComponent = itemStack.getItem().getAttributeModifiers();
            }
            if ((equipmentSlot != null && attributeModifiersComponent.modifiers().getFirst().slot().matches(equipmentSlot))|| (attributeModifierSlot != null && attributeModifiersComponent.modifiers().getFirst().slot().equals(attributeModifierSlot))) {
                for (int i = 0; i < itemStack.get(ItemInit.GEMS).gems().size(); i++) {
                    ItemStack gem = itemStack.get(ItemInit.GEMS).gems().get(i);
                    gem.getItem().getAttributeModifiers().modifiers().forEach(entry -> {
                        attributeModifierConsumer.accept(entry.attribute(), entry.modifier());
                    });
                }
            }

        }
    }
}
