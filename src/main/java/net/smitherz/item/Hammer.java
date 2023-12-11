package net.smitherz.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.smitherz.init.TagInit;

public class Hammer extends Item {

    private final float chance;

    public Hammer(Settings settings, float chance) {
        super(settings);
        this.chance = chance;
    }

    public float getBonusChance() {
        return this.chance / 100f;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.isIn(TagInit.BONUS_ITEMS)) {
            tooltip.add(Text.translatable("item.smitherz.link_bonus", chance).formatted(Formatting.BLUE));
        } else if (stack.isIn(TagInit.EXTRACTION_ITEMS)) {
            tooltip.add(Text.translatable("item.smitherz.extraction_bonus", chance).formatted(Formatting.BLUE));
        }
    }

}
