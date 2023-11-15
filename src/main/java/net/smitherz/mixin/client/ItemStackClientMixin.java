package net.smitherz.mixin.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
@Mixin(value = ItemStack.class, priority = 999)
public class ItemStackClientMixin {

    private boolean isGem = false;

    @Inject(method = "getTooltip", at = @At("HEAD"))
    private void getTooltipMixin(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info) {
        this.isGem = getItem() instanceof Gem || ((ItemStack) (Object) this).isIn(TagInit.GEMS);
    }

    // tooltip needs work for attack damage like in tieredz - do this in the tooltip component with expandabel
    // should this be dependend on tieredz? cause of health issue and other things probably
    // ---
    // work on extraction part, use grindstone extra page with extraction hammer for single gem extraction or non hammer for all extraction with a chance
    // add extraction chance to datapack
    // ---
    // drop event for each existing gem, maybe add a drop chance to the datapack and maybe a loot table id (multiple)
    // numismatic compat datapack for money cost
    // ---
    // have to make this compatible with tieredz
    // this has require = 0!

    // Level 1 – 41-45%
    // Level 2 – 33-37%
    // Level 3 – 25-29%
    // Level 4 – 9-13%
    // Level 5 – 1-5%
    // Dual – 9-13%

    // Can failing to link break my gear?Some lapis can break your gear if they fail. These include; Accessory, Sonic, Flash, Horizon, Absorption and level 7 debuff lapis. In order to save your gear
    // in the event of a failed link you need to have a lucky charm in your possession. The charm is only good for one linking attempt.
    // Note: There is an extremely small chance for any lapis to break your gear.

    // You get additional linking % every 20 levels on your character, guild house blacksmith upgrades also add additional %.

    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 6), require = 0)
    private boolean modifyTooltipEquipmentSlot(List<Text> list, Object text) {
        if (this.isGem) {
            return list.add(Text.translatable("item.modifiers.linked").formatted(Formatting.GRAY));
        }
        return list.add((Text) text);
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
