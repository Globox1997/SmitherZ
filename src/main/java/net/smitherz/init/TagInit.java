package net.smitherz.init;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.smitherz.SmitherzMain;

public class TagInit {

    public static final TagKey<Item> GEMS = TagKey.of(RegistryKeys.ITEM, SmitherzMain.identifierOf("gems"));
    public static final TagKey<Item> BONUS_ITEMS = TagKey.of(RegistryKeys.ITEM, SmitherzMain.identifierOf("bonus_items"));
    public static final TagKey<Item> EXTRACTION_ITEMS = TagKey.of(RegistryKeys.ITEM, SmitherzMain.identifierOf("extraction_items"));

    public static void init() {
    }

}
