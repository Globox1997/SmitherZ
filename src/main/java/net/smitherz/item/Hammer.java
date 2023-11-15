package net.smitherz.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.smitherz.init.ConfigInit;
import net.smitherz.init.TagInit;

public class Hammer extends Item {

    public Hammer(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.isIn(TagInit.BONUS_ITEMS)) {
            tooltip.add(Text.translatable("item.smitherz.link_bonus", ConfigInit.CONFIG.hammerExtraChance).formatted(Formatting.BLUE));
        } else if (stack.isIn(TagInit.EXTRACTION_ITEMS)) {
            tooltip.add(Text.translatable("item.smitherz.extraction_bonus", ConfigInit.CONFIG.hammerExtraChance).formatted(Formatting.BLUE));
        }
    }

}
