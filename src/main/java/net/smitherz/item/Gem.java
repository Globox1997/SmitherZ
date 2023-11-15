package net.smitherz.item;

import com.google.common.collect.Multimap;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public class Gem extends Item {

    private final Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers;
    private final float linkChance;
    @Nullable
    private final TagKey<Item> restrictionTag;

    public Gem(Settings settings, Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers, float linkChance, @Nullable TagKey<Item> restrictionTag) {
        super(settings);
        this.entityAttributeModifiers = entityAttributeModifiers;
        this.linkChance = linkChance;
        this.restrictionTag = restrictionTag;
    }

    public float getLinkChance() {
        return this.linkChance;
    }

    // @Override
    // public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
    // return super.getAttributeModifiers(slot);
    // }

    public boolean canLinkToItemStack(ItemStack stack) {
        if (this.restrictionTag != null && !stack.isIn(this.restrictionTag)) {
            return false;
        }
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (slot.equals(LivingEntity.getPreferredEquipmentSlot(stack))) {
            return entityAttributeModifiers;
        } else {
            return super.getAttributeModifiers(stack, slot);
        }
    }

}
