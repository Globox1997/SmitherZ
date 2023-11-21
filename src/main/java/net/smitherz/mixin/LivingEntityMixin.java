package net.smitherz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.smitherz.util.LootHelper;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "dropLoot", at = @At("TAIL"))
    protected void dropLootMixin(DamageSource damageSource, boolean causedByPlayer, CallbackInfo info) {
        LootHelper.dropGemLoot((LivingEntity) (Object) this, damageSource, causedByPlayer);
    }
}
