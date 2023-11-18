package net.smitherz.tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.smitherz.init.ConfigInit;

@Environment(EnvType.CLIENT)
public class SmitherTooltipComponent implements TooltipComponent {

    public static final Identifier TEXTURE = new Identifier("smitherz:textures/gui/gem_slots.png");
    private final DefaultedList<ItemStack> inventory;
    private final int gemSlotSize;

    public SmitherTooltipComponent(SmitherTooltipData data) {
        this.inventory = data.getInventory();
        this.gemSlotSize = data.getGemSlotSize();
    }

    @Override
    public int getHeight() {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340)) {
            return 25 + getTooltipText().size() * 9;
        } else if (!ConfigInit.CONFIG.showHoldShiftInfo) {
            return 11;
        }
        return 23;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340)) {
            int minTextWidth = 0;
            List<Text> list = getTooltipText();
            for (int i = 0; i < list.size(); i++) {
                if (minTextWidth < textRenderer.getWidth(list.get(i)) + 12) {
                    minTextWidth = textRenderer.getWidth(list.get(i)) + 12;
                }
            }
            return Math.max(this.gemSlotSize * 18, minTextWidth);
        }
        return this.gemSlotSize * 18;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        boolean shiftKeyPressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340);
        if (ConfigInit.CONFIG.showHoldShiftInfo && !shiftKeyPressed) {
            context.drawTexture(TEXTURE, x, y + 20, 0, 18, 9, 10, 128, 128);
            context.drawText(textRenderer, Text.translatable("item.modifiers.shift"), x + 12, y + 22, 0, true);
        }
        if (shiftKeyPressed) {
            context.drawTexture(TEXTURE, x, y + 20, 0, 18, 9, 10, 128, 128);
            context.drawText(textRenderer, Text.translatable("item.modifiers.pressed_shift"), x + 12, y + 22, 0, true);

            List<Text> list = getTooltipText();
            if (!list.isEmpty()) {
                int o = 33;
                for (int i = 0; i < list.size(); i++) {
                    context.drawText(textRenderer, list.get(i), x + 12, y + o, 0, true);
                    o += 9;
                }
            }
        }
        if (this.gemSlotSize > 0) {
            for (int i = 0; i < this.gemSlotSize; i++) {
                this.drawSlot(x + i * 18, y, i, context, textRenderer);
            }
        }

    }

    private List<Text> getTooltipText() {
        ArrayList<Text> list = Lists.newArrayList();
        Map<EntityAttribute, List<Object>> map = new HashMap<EntityAttribute, List<Object>>();
        for (int i = 0; i < this.inventory.size(); i++) {
            if (!this.inventory.get(i).isEmpty()) {

                Multimap<EntityAttribute, EntityAttributeModifier> multimap = this.inventory.get(i).getAttributeModifiers(EquipmentSlot.MAINHAND);
                if (multimap.isEmpty()) {
                    continue;
                }
                for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
                    EntityAttributeModifier entityAttributeModifier = entry.getValue();
                    double d = entityAttributeModifier.getValue();
                    double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE
                            || entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? d * 100.0
                                    : (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);

                    if (map.containsKey(entry.getKey())) {
                        double totalValue = e + ((Boolean) map.get(entry.getKey()).get(2) ? (Double) map.get(entry.getKey()).get(1) : -(Double) map.get(entry.getKey()).get(1));
                        if (totalValue > 0.0D) {
                            list.set((Integer) map.get(entry.getKey()).get(0), Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(),
                                    ItemStack.MODIFIER_FORMAT.format(totalValue), Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
                        } else {
                            list.set((Integer) map.get(entry.getKey()).get(0), Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(),
                                    ItemStack.MODIFIER_FORMAT.format(totalValue *= -1.0), Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
                        }
                        map.put(entry.getKey(), List.of((Integer) map.get(entry.getKey()).get(0), totalValue, totalValue > 0.0D));
                    } else {
                        if (d > 0.0D) {
                            list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e),
                                    Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
                        } else {
                            list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e *= -1.0),
                                    Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
                        }
                        map.put(entry.getKey(), List.of(list.size() - 1, e, d > 0.0D));
                    }
                }
            }
        }
        return list;
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        context.drawTexture(TEXTURE, x, y, 0, 0, 18, 18, 128, 128);
        if (this.inventory.size() > index) {
            ItemStack itemStack = this.inventory.get(index);

            context.drawItem(itemStack, x + 1, y + 1, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
        }
    }

}
