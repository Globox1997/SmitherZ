package net.smitherz.init;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.UnaryOperator;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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
import net.smitherz.item.component.GemComponent;

public class ItemInit {

    // Item Group
    public static final RegistryKey<ItemGroup> SMITHERZ_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("smitherz", "item_group"));
    // Lists
    public static final ArrayList<Item> ITEMS = new ArrayList<Item>();

    public static final Item SMITHER_HAMMER_1 = register("smither_hammer_1", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 5f));
    public static final Item SMITHER_HAMMER_2 = register("smither_hammer_2", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 10f));
    public static final Item SMITHER_HAMMER_3 = register("smither_hammer_3", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 20f));
    public static final Item SMITHER_HAMMER_4 = register("smither_hammer_4", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 50f));

    public static final Item EXTRACTION_HAMMER_1 = register("extraction_hammer_1", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 5f));
    public static final Item EXTRACTION_HAMMER_2 = register("extraction_hammer_2", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 10f));
    public static final Item EXTRACTION_HAMMER_3 = register("extraction_hammer_3", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 20f));
    public static final Item EXTRACTION_HAMMER_4 = register("extraction_hammer_4", new Hammer(new Item.Settings().rarity(Rarity.UNCOMMON), 50f));

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

    public static final ComponentType<GemComponent> GEMS = registerComponent("smitherz:gems", builder -> builder.codec(GemComponent.CODEC).packetCodec(GemComponent.PACKET_CODEC));


    public static Item register(String id, Item item) {
        ITEMS.add(item);
        return register(Identifier.of("smitherz", id), item);
    }

    private static Item register(Identifier id, Item item) {
        ItemGroupEvents.modifyEntriesEvent(SMITHERZ_ITEM_GROUP).register(entries -> entries.add(item));
        return Registry.register(Registries.ITEM, id, item);
    }

    private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, builderOperator.apply(ComponentType.builder()).build());
    }

    public static void init() {
        if (ConfigInit.CONFIG.defaultGems) {
            STRENGTH_I = register("strength_1_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getKey().get().getValue(), 2, Operation.ADD_VALUE)),
                    0.45f, 0.0f, 0.5f, ItemTags.SWORDS));
            STRENGTH_II = register("strength_2_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getKey().get().getValue(), 4, Operation.ADD_VALUE)),
                    0.33f, 0.0f, 0.3f, ItemTags.SWORDS));
            STRENGTH_III = register("strength_3_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getKey().get().getValue(), 6, Operation.ADD_VALUE)),
                    0.25f, 0.0f, 0.2f, ItemTags.SWORDS));
            STRENGTH_IV = register("strength_4_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getKey().get().getValue(), 8, Operation.ADD_VALUE)),
                    0.09f, 0.01f, 0.05f, ItemTags.SWORDS));
            STRENGTH_V = register("strength_5_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getKey().get().getValue(), 12, Operation.ADD_VALUE)),
                    0.01f, 0.05f, 0.01f, ItemTags.SWORDS));

            DEFENSE_I = register("defense_1_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getKey().get().getValue(), 2, Operation.ADD_VALUE)), 0.45f,
                            0.0f, 0.5f, ItemTags.ARMOR_ENCHANTABLE));
            DEFENSE_II = register("defense_2_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getKey().get().getValue(), 4, Operation.ADD_VALUE)), 0.33f,
                            0.0f, 0.3f, ItemTags.ARMOR_ENCHANTABLE));
            DEFENSE_III = register("defense_3_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getKey().get().getValue(), 6, Operation.ADD_VALUE)), 0.25f,
                            0.0f, 0.2f, ItemTags.ARMOR_ENCHANTABLE));
            DEFENSE_IV = register("defense_4_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getKey().get().getValue(), 8, Operation.ADD_VALUE)), 0.09f,
                            0.01f, 0.05f, ItemTags.ARMOR_ENCHANTABLE));
            DEFENSE_V = register("defense_5_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(EntityAttributes.GENERIC_ARMOR.getKey().get().getValue(), 10, Operation.ADD_VALUE)), 0.01f,
                            0.05f, 0.01f, ItemTags.ARMOR_ENCHANTABLE));

            HEALTH_I = register("health_1_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getKey().get().getValue(), 2, Operation.ADD_VALUE)),
                            0.45f, 0.0f, 0.5f, ItemTags.ARMOR_ENCHANTABLE));
            HEALTH_II = register("health_2_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getKey().get().getValue(), 4, Operation.ADD_VALUE)),
                            0.33f, 0.0f, 0.3f, ItemTags.ARMOR_ENCHANTABLE));
            HEALTH_III = register("health_3_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getKey().get().getValue(), 6, Operation.ADD_VALUE)),
                            0.25f, 0.0f, 0.2f, ItemTags.ARMOR_ENCHANTABLE));
            HEALTH_IV = register("health_4_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getKey().get().getValue(), 8, Operation.ADD_VALUE)),
                            0.09f, 0.01f, 0.05f, ItemTags.ARMOR_ENCHANTABLE));
            HEALTH_V = register("health_5_gem",
                    new Gem(new Item.Settings(),
                            Map.of(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getKey().get().getValue(), 12, Operation.ADD_VALUE)),
                            0.01f, 0.05f, 0.01f, ItemTags.ARMOR_ENCHANTABLE));

            SPEED_I = register("speed_1_gem", new Gem(new Item.Settings(), Map.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getKey().get().getValue(), 0.05D, Operation.ADD_VALUE)), 0.45f, 0.0f, 0.5f, ItemTags.ARMOR_ENCHANTABLE));
            SPEED_II = register("speed_2_gem", new Gem(new Item.Settings(), Map.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getKey().get().getValue(), 0.1D, Operation.ADD_VALUE)), 0.33f, 0.0f, 0.3f, ItemTags.ARMOR_ENCHANTABLE));
            SPEED_III = register("speed_3_gem", new Gem(new Item.Settings(), Map.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getKey().get().getValue(), 0.15D, Operation.ADD_VALUE)), 0.25f, 0.0f, 0.2f, ItemTags.ARMOR_ENCHANTABLE));
            SPEED_IV = register("speed_4_gem", new Gem(new Item.Settings(), Map.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getKey().get().getValue(), 0.2D, Operation.ADD_VALUE)), 0.09f, 0.01f, 0.05f, ItemTags.ARMOR_ENCHANTABLE));
            SPEED_V = register("speed_5_gem", new Gem(new Item.Settings(), Map.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED.getKey().get().getValue(), 0.3D, Operation.ADD_VALUE)), 0.01f, 0.05f, 0.01f, ItemTags.ARMOR_ENCHANTABLE));

            ATTACK_SPEED_I = register("attack_speed_1_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getKey().get().getValue(), 0.05D, Operation.ADD_VALUE)),
                    0.45f, 0.0f, 0.5f, ItemTags.BREAKS_DECORATED_POTS));
            ATTACK_SPEED_II = register("attack_speed_2_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getKey().get().getValue(), 0.1D, Operation.ADD_VALUE)),
                    0.33f, 0.0f, 0.3f, ItemTags.BREAKS_DECORATED_POTS));
            ATTACK_SPEED_III = register("attack_speed_3_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getKey().get().getValue(), 0.15D, Operation.ADD_VALUE)),
                    0.25f, 0.0f, 0.2f, ItemTags.BREAKS_DECORATED_POTS));
            ATTACK_SPEED_IV = register("attack_speed_4_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getKey().get().getValue(), 0.2D, Operation.ADD_VALUE)),
                    0.09f, 0.01f, 0.05f, ItemTags.BREAKS_DECORATED_POTS));
            ATTACK_SPEED_V = register("attack_speed_5_gem", new Gem(new Item.Settings(),
                    Map.of(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getKey().get().getValue(), 0.3D, Operation.ADD_VALUE)),
                    0.01f, 0.05f, 0.01f, ItemTags.BREAKS_DECORATED_POTS));
        }
        Registry.register(Registries.ITEM_GROUP, SMITHERZ_ITEM_GROUP,
                FabricItemGroup.builder().icon(() -> new ItemStack(SMITHER_HAMMER_4)).displayName(Text.translatable("item.smitherz.item_group")).build());
    }

}
