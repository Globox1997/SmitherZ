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
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.smitherz.item.*;
import net.smitherz.item.attribute.GemEntityAttributeModifier;

public class ItemInit {

    // Item Group
    public static final RegistryKey<ItemGroup> SMITHERZ_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("smitherz", "item_group"));
    // Lists
    public static final ArrayList<String> ITEM_IDS = new ArrayList<String>();

    public static final Item SMITHER_HAMMER = register("smither_hammer", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item EXTRACTION_HAMMER = register("extraction_hammer", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static Item STRENGTH_I;
    public static Item STRENGTH_II;
    public static Item STRENGTH_III;
    public static Item STRENGTH_IV;
    public static Item STRENGTH_V;

    public static Item DEFENSE_I;
    public static Item DEFENSE_II;
    public static Item DEFENSE_III;
    public static Item DEFENSE_IV;
    public static Item DEFENSE_V;

    public static Item HEALTH_I;
    public static Item HEALTH_II;
    public static Item HEALTH_III;
    public static Item HEALTH_IV;
    public static Item HEALTH_V;

    public static Item SPEED_I;
    public static Item SPEED_II;
    public static Item SPEED_III;
    public static Item SPEED_IV;
    public static Item SPEED_V;

    public static Item ATTACK_SPEED_I;
    public static Item ATTACK_SPEED_II;
    public static Item ATTACK_SPEED_III;
    public static Item ATTACK_SPEED_IV;
    public static Item ATTACK_SPEED_V;

    public static final ArrayList<ArrayList<Item>> ITEM_CATEGORIES = new ArrayList<ArrayList<Item>>();

    public static Item register(String id, Item item) {
        ITEM_IDS.add(id);
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
                    0.45f, 0.0f, 0.5f, ItemTags.SWORDS));
            STRENGTH_II = register("strength_2_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 4, Operation.ADDITION)),
                    0.33f, 0.0f, 0.3f, ItemTags.SWORDS));
            STRENGTH_III = register("strength_3_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 6, Operation.ADDITION)),
                    0.25f, 0.0f, 0.2f, ItemTags.SWORDS));
            STRENGTH_IV = register("strength_4_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 8, Operation.ADDITION)),
                    0.09f, 0.01f, 0.05f, ItemTags.SWORDS));
            STRENGTH_V = register("strength_5_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey(), 12, Operation.ADDITION)),
                    0.01f, 0.05f, 0.01f, ItemTags.SWORDS));

            DEFENSE_I = register("defense_1_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_ARMOR, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getTranslationKey(), 2, Operation.ADDITION)), 0.45f,
                            0.0f, 0.5f, TagInit.ARMOR));
            DEFENSE_II = register("defense_2_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_ARMOR, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getTranslationKey(), 4, Operation.ADDITION)), 0.33f,
                            0.0f, 0.3f, TagInit.ARMOR));
            DEFENSE_III = register("defense_3_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_ARMOR, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getTranslationKey(), 6, Operation.ADDITION)), 0.25f,
                            0.0f, 0.2f, TagInit.ARMOR));
            DEFENSE_IV = register("defense_4_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_ARMOR, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getTranslationKey(), 8, Operation.ADDITION)), 0.09f,
                            0.01f, 0.05f, TagInit.ARMOR));
            DEFENSE_V = register("defense_5_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_ARMOR, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getTranslationKey(), 10, Operation.ADDITION)), 0.01f,
                            0.05f, 0.01f, TagInit.ARMOR));

            HEALTH_I = register("health_1_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_MAX_HEALTH, new GemEntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getTranslationKey(), 2, Operation.ADDITION)),
                            0.45f, 0.0f, 0.5f, TagInit.ARMOR));
            HEALTH_II = register("health_2_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_MAX_HEALTH, new GemEntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getTranslationKey(), 4, Operation.ADDITION)),
                            0.33f, 0.0f, 0.3f, TagInit.ARMOR));
            HEALTH_III = register("health_3_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_MAX_HEALTH, new GemEntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getTranslationKey(), 6, Operation.ADDITION)),
                            0.25f, 0.0f, 0.2f, TagInit.ARMOR));
            HEALTH_IV = register("health_4_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_MAX_HEALTH, new GemEntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getTranslationKey(), 8, Operation.ADDITION)),
                            0.09f, 0.01f, 0.05f, TagInit.ARMOR));
            HEALTH_V = register("health_5_gem",
                    new Gem(new Item.Settings(),
                            ImmutableMultimap.of(EntityAttributes.GENERIC_MAX_HEALTH, new GemEntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getTranslationKey(), 12, Operation.ADDITION)),
                            0.01f, 0.05f, 0.01f, TagInit.ARMOR));

            SPEED_I = register("speed_1_gem", new Gem(new Item.Settings(), ImmutableMultimap.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new GemEntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getTranslationKey(), 0.05D, Operation.ADDITION)), 0.45f, 0.0f, 0.5f, TagInit.ARMOR));
            SPEED_II = register("speed_2_gem", new Gem(new Item.Settings(), ImmutableMultimap.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new GemEntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getTranslationKey(), 0.1D, Operation.ADDITION)), 0.33f, 0.0f, 0.3f, TagInit.ARMOR));
            SPEED_III = register("speed_3_gem", new Gem(new Item.Settings(), ImmutableMultimap.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new GemEntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getTranslationKey(), 0.15D, Operation.ADDITION)), 0.25f, 0.0f, 0.2f, TagInit.ARMOR));
            SPEED_IV = register("speed_4_gem", new Gem(new Item.Settings(), ImmutableMultimap.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new GemEntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getTranslationKey(), 0.2D, Operation.ADDITION)), 0.09f, 0.01f, 0.05f, TagInit.ARMOR));
            SPEED_V = register("speed_5_gem", new Gem(new Item.Settings(), ImmutableMultimap.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new GemEntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getTranslationKey(), 0.3D, Operation.ADDITION)), 0.01f, 0.05f, 0.01f, TagInit.ARMOR));

            ATTACK_SPEED_I = register("attack_speed_1_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_SPEED, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getTranslationKey(), 0.05D, Operation.ADDITION)),
                    0.45f, 0.0f, 0.5f, ItemTags.TOOLS));
            ATTACK_SPEED_II = register("attack_speed_2_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_SPEED, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getTranslationKey(), 0.1D, Operation.ADDITION)),
                    0.33f, 0.0f, 0.3f, ItemTags.TOOLS));
            ATTACK_SPEED_III = register("attack_speed_3_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_SPEED, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getTranslationKey(), 0.15D, Operation.ADDITION)),
                    0.25f, 0.0f, 0.2f, ItemTags.TOOLS));
            ATTACK_SPEED_IV = register("attack_speed_4_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_SPEED, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getTranslationKey(), 0.2D, Operation.ADDITION)),
                    0.09f, 0.01f, 0.05f, ItemTags.TOOLS));
            ATTACK_SPEED_V = register("attack_speed_5_gem", new Gem(new Item.Settings(),
                    ImmutableMultimap.of(EntityAttributes.GENERIC_ATTACK_SPEED, new GemEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getTranslationKey(), 0.3D, Operation.ADDITION)),
                    0.01f, 0.05f, 0.01f, ItemTags.TOOLS));
        }
        Registry.register(Registries.ITEM_GROUP, SMITHERZ_ITEM_GROUP,
                FabricItemGroup.builder().icon(() -> new ItemStack(SMITHER_HAMMER)).displayName(Text.translatable("item.smitherz.item_group")).build());
    }

}
