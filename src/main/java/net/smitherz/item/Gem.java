package net.smitherz.item;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class Gem extends Item {

    private final Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers;
    private final float linkChance;
    private final float linkBreakChance;
    private final float unlinkChance;
    @Nullable
    private final TagKey<Item> restrictionTag;

    public Gem(Settings settings, Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers, float linkChance, float linkBreakChance, float unlinkChance,
            @Nullable TagKey<Item> restrictionTag) {
        super(settings);
        this.entityAttributeModifiers = entityAttributeModifiers;
        this.linkChance = linkChance;
        this.linkBreakChance = linkBreakChance;
        this.restrictionTag = restrictionTag;
        this.unlinkChance = unlinkChance;
    }

    public float getLinkChance() {
        return this.linkChance;
    }

    public float getLinkBreakChance() {
        return this.linkBreakChance;
    }

    public float getUnlinkChance() {
        return this.unlinkChance;
    }

    public boolean canLinkToItemStack(ItemStack stack) {
        if (this.restrictionTag != null && !stack.isIn(this.restrictionTag)) {
            return false;
        }
        return true;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getGemAttributeModifiers() {
        return this.entityAttributeModifiers;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (!stack.isOf(this) && slot.equals(LivingEntity.getPreferredEquipmentSlot(stack))) {
            return this.entityAttributeModifiers;
        } else if (slot.equals(EquipmentSlot.MAINHAND)) {
            return ImmutableMultimap.of();
        } else {
            return super.getAttributeModifiers(stack, slot);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340)) {
            if (getLinkBreakChance() < 0.00001f) {
                tooltip.add(Text.translatable("item.smitherz.gem.tooltip", String.format("%.1f", getLinkChance() * 100f)).formatted(Formatting.GRAY));
            } else {
                tooltip.add(Text.translatable("item.smitherz.gem.tooltip_2", String.format("%.1f", getLinkChance() * 100f), String.format("%.1f", getLinkBreakChance() * 100f))
                        .formatted(Formatting.GRAY));
            }
            tooltip.add(Text.translatable("item.smitherz.gem.tooltip_3", String.format("%.1f", getUnlinkChance() * 100f)).formatted(Formatting.GRAY));
        }
    }

}
