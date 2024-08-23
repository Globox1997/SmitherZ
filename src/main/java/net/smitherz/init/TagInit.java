package net.smitherz.init;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagInit {

    public static final TagKey<Item> GEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of("smitherz", "gems"));
    public static final TagKey<Item> BONUS_ITEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of("smitherz", "bonus_items"));
    public static final TagKey<Item> EXTRACTION_ITEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of("smitherz", "extraction_items"));

    public static void init() {
    }

}
