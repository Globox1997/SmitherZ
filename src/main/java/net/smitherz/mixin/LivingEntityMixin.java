package net.smitherz.mixin;

import java.util.Map;

import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.smitherz.SmitherzMain;
import net.smitherz.init.ItemInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.smitherz.util.LootHelper;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

//    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;applyAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", shift = Shift.AFTER, ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
//    private void getEquipmentChangesMixin(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> info, Map map, EquipmentSlot var2[], int var3, int var4, EquipmentSlot equipmentSlot, ItemStack itemStack, ItemStack itemStack2) {
//        if (!SmitherzMain.isTieredLoaded && itemStack.get(ItemInit.GEMS) != null && (Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
//            this.setHealth(this.getHealth() > this.getMaxHealth() ? this.getMaxHealth() : this.getHealth());
//            serverPlayerEntity.networkHandler.sendPacket(new HealthUpdateS2CPacket(this.getHealth(), serverPlayerEntity.getHungerManager().getFoodLevel(), serverPlayerEntity.getHungerManager().getSaturationLevel()));
//            ((ServerPlayerEntityAccessor)serverPlayerEntity).setSyncedHealth(serverPlayerEntity.getHealth());
//        }
//    }

//    @Shadow
//    public float getHealth() {
//        return 0f;
//    }
//
//    @Shadow
//    public final float getMaxHealth() {
//        return 0;
//    }
//
//    @Shadow
//    public void setHealth(float health) {
//    }


    @Inject(method = "dropLoot", at = @At("TAIL"))
    protected void dropLootMixin(DamageSource damageSource, boolean causedByPlayer, CallbackInfo info) {
        LootHelper.dropGemLoot((LivingEntity) (Object) this, damageSource, causedByPlayer);
    }
}
