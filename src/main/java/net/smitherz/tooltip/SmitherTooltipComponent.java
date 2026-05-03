package net.smitherz.tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.smitherz.init.ConfigInit;

@Environment(EnvType.CLIENT)
public class SmitherTooltipComponent implements TooltipComponent {

    public static final Identifier TEXTURE = Identifier.of("smitherz:textures/gui/gem_slots.png");
    private final DefaultedList<ItemStack> inventory;
    private final int gemSlotSize;

    public SmitherTooltipComponent(SmitherTooltipData data) {
        this.inventory = data.getInventory();
        this.gemSlotSize = data.getGemSlotSize();
    }

    @Override
    public int getHeight() {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340)) {
            return 33 + getTooltipText().size() * 9;
        } else if (!ConfigInit.CONFIG.showHoldShiftInfo) {
            return 20;
        }
        return 34;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340)) {
            int minTextWidth = 0;
            List<Text> list = getTooltipText();
            for (Text text : list) {
                if (minTextWidth < textRenderer.getWidth(text) + 12) {
                    minTextWidth = textRenderer.getWidth(text) + 12;
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
                for (Text text : list) {
                    context.drawText(textRenderer, text, x + 12, y + o, 0, true);
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
        Map<RegistryEntry<EntityAttribute>, List<Object>> map = new HashMap<>();
        for (ItemStack itemStack : this.inventory) {
            if (!itemStack.isEmpty()) {
                List<AttributeModifiersComponent.Entry> entries = itemStack.getItem().getAttributeModifiers().modifiers();
                if (entries.isEmpty()) {
                    continue;
                }
                for (AttributeModifiersComponent.Entry entry : entries) {
                    EntityAttributeModifier entityAttributeModifier = entry.modifier();
                    double d = entityAttributeModifier.value();
                    double e = entityAttributeModifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            || entityAttributeModifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL ? d * 100.0
                            : (entry.attribute().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);

                    if (map.containsKey(entry.attribute())) {
                        double totalValue = e + ((Boolean) map.get(entry.attribute()).get(2) ? (Double) map.get(entry.attribute()).get(1) : -(Double) map.get(entry.attribute()).get(1));
                        if (totalValue > 0.0D) {
                            list.set((Integer) map.get(entry.attribute()).get(0), Text.translatable("attribute.modifier.plus." + entityAttributeModifier.operation().getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(totalValue), Text.translatable(entry.attribute().value().getTranslationKey())).formatted(Formatting.BLUE));
                        } else {
                            list.set((Integer) map.get(entry.attribute()).get(0), Text.translatable("attribute.modifier.take." + entityAttributeModifier.operation().getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(totalValue *= -1.0), Text.translatable(entry.attribute().value().getTranslationKey())).formatted(Formatting.RED));
                        }
                        map.put(entry.attribute(), List.of((Integer) map.get(entry.attribute()).get(0), totalValue, totalValue > 0.0D));
                    } else {
                        if (d > 0.0D) {
                            list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                    Text.translatable(entry.attribute().value().getTranslationKey())).formatted(Formatting.BLUE));
                        } else {
                            list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(e *= -1.0),
                                    Text.translatable(entry.attribute().value().getTranslationKey())).formatted(Formatting.RED));
                        }
                        map.put(entry.attribute(), List.of(list.size() - 1, e, d > 0.0D));
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
