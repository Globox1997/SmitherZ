package net.smitherz.init;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMultimap;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.smitherz.item.*;
import net.smitherz.item.attribute.GemEntityAttributeModifier;

public class ItemInit {

    // Item Group
    public static final RegistryKey<ItemGroup> SMITHERZ_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("smitherz", "item_group"));
    // Lists
    public static final ArrayList<String> ITEMS = new ArrayList<String>();

    public static final Item SMITHER_HAMMER = register("smither_hammer", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item EXTRACTION_HAMMER = register("extraction_hammer", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static Item STRENGTH_I; // = register("strength_1_gem", new Gem(new Item.Settings(), EntityAttributes.GENERIC_ATTACK_DAMAGE, 4, Operation.ADDITION));
    public static Item STRENGTH_II; // = register("strength_2_gem", new Gem(new Item.Settings(), EntityAttributes.GENERIC_ATTACK_DAMAGE, 6, Operation.ADDITION));
    public static Item STRENGTH_III;
    public static Item STRENGTH_IV;
    public static Item STRENGTH_V;

    public static Item register(String id, Item item) {
        ITEMS.add(id);
        return register(new Identifier("smitherz", id), item);
    }

    private static Item register(Identifier id, Item item) {
        ItemGroupEvents.modifyEntriesEvent(SMITHERZ_ITEM_GROUP).register(entries -> entries.add(item));
        return Registry.register(Registries.ITEM, id, item);
    }

    public static void init() {
        if (ConfigInit.CONFIG.defaultGems) {
            STRENGTH_I = register("strength_1_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 2, Operation.ADDITION)),
                    0.1f, TagInit.SWORDS));
            STRENGTH_II = register("strength_2_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 4, Operation.ADDITION)),
                    0.01f, TagInit.SWORDS));
            STRENGTH_III = register("strength_3_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 6, Operation.ADDITION)),
                    0.01f, TagInit.SWORDS));
            STRENGTH_IV = register("strength_4_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 8, Operation.ADDITION)),
                    0.01f, TagInit.SWORDS));
            STRENGTH_V = register("strength_5_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 10, Operation.ADDITION)),
                    0.01f, TagInit.SWORDS));
        }
        Registry.register(Registries.ITEM_GROUP, SMITHERZ_ITEM_GROUP,
                FabricItemGroup.builder().icon(() -> new ItemStack(SMITHER_HAMMER)).displayName(Text.translatable("item.smitherz.item_group")).build());
    }

}
