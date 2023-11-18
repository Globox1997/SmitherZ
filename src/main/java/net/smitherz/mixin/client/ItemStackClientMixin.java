package net.smitherz.mixin.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.smitherz.init.TagInit;
import net.smitherz.item.Gem;
import net.smitherz.item.attribute.GemEntityAttributeModifier;

@SuppressWarnings("rawtypes")
@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class ItemStackClientMixin {

    private boolean isGem = false;

    @Inject(method = "getTooltip", at = @At("HEAD"))
    private void getTooltipMixin(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info) {
        this.isGem = getItem() instanceof Gem || ((ItemStack) (Object) this).isIn(TagInit.GEMS);
    }

    @ModifyExpressionValue(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private MutableText modifyTooltipEquipmentSlot(MutableText original) {
        if (this.isGem) {
            return Text.translatable("item.modifiers.linked").formatted(Formatting.GRAY);
        }
        return original;
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getTooltipMixin(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List list, MutableText mutableText, int i, EquipmentSlot var6[],
            int var7, int var8, EquipmentSlot equipmentSlot, Multimap<EntityAttribute, EntityAttributeModifier> multimap) {
        if (!multimap.isEmpty() && !this.isGem) {
            Iterator<Entry<EntityAttribute, EntityAttributeModifier>> iterator = multimap.entries().iterator();
            while (iterator.hasNext()) {
                Entry<EntityAttribute, EntityAttributeModifier> entry = iterator.next();
                if (entry.getValue() instanceof GemEntityAttributeModifier) {
                    multimap.remove(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Shadow
    public Item getItem() {
        return null;
    }

}
