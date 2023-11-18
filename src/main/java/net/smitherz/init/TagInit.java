package net.smitherz.init;

import io.github.apace100.autotag.common.TagIdentifiers;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagInit {
    // smitherz
    public static final TagKey<Item> GEMS = TagKey.of(RegistryKeys.ITEM, new Identifier("smitherz", "gems"));
    public static final TagKey<Item> BONUS_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier("smitherz", "bonus_items"));
    public static final TagKey<Item> EXTRACTION_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier("smitherz", "extraction_items"));
    // autotag
    public static final TagKey<Item> ARMOR = TagKey.of(RegistryKeys.ITEM, TagIdentifiers.Items.ARMOR);

    public static void init() {
    }

}
