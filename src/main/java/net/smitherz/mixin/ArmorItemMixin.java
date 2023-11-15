package net.smitherz.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.smitherz.init.ConfigInit;
import net.smitherz.item.Upgradeable;
import net.smitherz.tooltip.SmitherTooltipData;
import net.smitherz.util.UpgradeHelper;

@Mixin(ArmorItem.class)
public class ArmorItemMixin extends Item implements Upgradeable {

    public ArmorItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        int gemSlotSize = UpgradeHelper.getGemSlotSize(stack);
        if (gemSlotSize > 0) {
            DefaultedList<ItemStack> defaultedList = DefaultedList.of();
            UpgradeHelper.getGemStacks(stack).forEach(defaultedList::add);
            return Optional.of(new SmitherTooltipData(defaultedList, UpgradeHelper.getGemSlotSize(stack)));
        }
        return Optional.empty();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (ConfigInit.CONFIG.showZeroSlotsInfo && UpgradeHelper.getGemSlotSize(stack) <= 0) {
            tooltip.add(Text.translatable("item.smitherz.zero_slots"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
