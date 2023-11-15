package net.smitherz.tooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class SmitherTooltipData implements TooltipData {

    private final DefaultedList<ItemStack> inventory;
    private final int gemSlotSize;

    public SmitherTooltipData(DefaultedList<ItemStack> inventory, int gemSlotSize) {
        this.inventory = inventory;
        this.gemSlotSize = gemSlotSize;
    }

    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    public int getGemSlotSize() {
        return this.gemSlotSize;
    }
}
