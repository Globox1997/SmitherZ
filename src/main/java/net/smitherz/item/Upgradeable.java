package net.smitherz.item;

import net.minecraft.item.ItemStack;
import net.smitherz.util.UpgradeHelper;

public interface Upgradeable {

    default int gemSlots(ItemStack itemStack) {
        return UpgradeHelper.getGemSlotSize(itemStack);
    }
}
