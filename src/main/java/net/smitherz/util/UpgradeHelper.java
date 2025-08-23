package net.smitherz.util;

import draylar.tiered.Tiered;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.smitherz.SmitherzMain;
import net.smitherz.init.ConfigInit;
import net.smitherz.init.ItemInit;
import net.smitherz.init.TagInit;
import net.smitherz.item.Gem;
import net.smitherz.item.Hammer;
import net.smitherz.item.Upgradeable;
import net.smitherz.item.component.GemComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class UpgradeHelper {

    public static final Random RANDOM = new Random();

    public static void spawnItemContents(ItemEntity itemEntity, Stream<ItemStack> contents) {
        World world = itemEntity.getWorld();
        if (world.isClient()) {
            return;
        }
        contents.forEach(stack -> world.spawnEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), (ItemStack) stack)));
    }

    public static Stream<ItemStack> getGemStacks(ItemStack stack) {
        int gemSlots = getGemSlotSize(stack);
        if (gemSlots == 0) {
            return Stream.empty();
        }

        if (stack.get(ItemInit.GEMS) == null) {
            return Stream.empty();
        }
        return stack.get(ItemInit.GEMS).gems().stream();
    }

    // Only gems can be added
    public static boolean tryAddStackToUpgradeable(ItemStack upgradeable, ItemStack gemStack, @Nullable ItemStack hammer) {
        if (gemStack.isEmpty() || !(upgradeable.getItem() instanceof Upgradeable)) {
            return false;
        }
        float linkChance = 0.0f;
        if (gemStack.getItem() instanceof Gem gem) {
            if (!gem.canLinkToItemStack(upgradeable)) {
                return false;
            }
            if (!ConfigInit.CONFIG.canLinkSameGem && upgradeable.get(ItemInit.GEMS) != null) {
                for (int i = 0; i < upgradeable.get(ItemInit.GEMS).gems().size(); i++) {
                    if (upgradeable.get(ItemInit.GEMS).gems().get(i).isOf(gemStack.getItem())) {
                        return false;
                    }
                }
            }
            linkChance = gem.getLinkChance();
        } else {
            // If non gem items are trying to get added, change the linkChance here
            // I guess this is not needed but may be a feature request
            // Non gem items can only get into the smithing table when they are in the gems item tag
            linkChance = 0.1f;
        }

        boolean hasHammer = hammer != null && !hammer.isEmpty() && hammer.isIn(TagInit.BONUS_ITEMS);
        if (hasHammer) {
            if (hammer.getItem() instanceof Hammer hammerItem) {
                linkChance = linkChance + hammerItem.getBonusChance();
            } else {
                linkChance = linkChance + ConfigInit.CONFIG.bonusItemExtraChance;
            }
            hammer.decrement(1);
        }


        if (linkChance >= RANDOM.nextFloat()) {
            addStackToUpgradeable(upgradeable, gemStack);
            gemStack.decrement(1);
            return true;
        } else if (!hasHammer && gemStack.getItem() instanceof Gem gem && gem.getLinkBreakChance() > 0.00001f && RANDOM.nextFloat() <= gem.getLinkBreakChance()) {
            upgradeable.decrement(1);
        }
        if (!upgradeable.isEmpty() && ConfigInit.CONFIG.linkBreakChance > 0.00001f && RANDOM.nextFloat() < ConfigInit.CONFIG.linkBreakChance) {
            upgradeable.decrement(1);
        }
        gemStack.decrement(1);
        return false;
    }

    public static void addStackToUpgradeable(ItemStack upgradeable, ItemStack gemStack) {
        if (getGemSlotSize(upgradeable) < getGemStacks(upgradeable).toList().size() + 1) {
            return;
        }
        GemComponent gemComponent = upgradeable.getOrDefault(ItemInit.GEMS, GemComponent.DEFAULT);
        List<ItemStack> gems = new ArrayList<ItemStack>(gemComponent.gems());
        gems.add(gemStack.copy());
        upgradeable.set(ItemInit.GEMS, new GemComponent(gems, gemComponent.size()));
    }

    public static List<ItemStack> tryRemoveStackFromUpgradeable(ItemStack upgradeable, @Nullable ItemStack hammer) {
        if (!(upgradeable.getItem() instanceof Upgradeable) || getGemStacks(upgradeable).toList().isEmpty()) {
            return List.of(ItemStack.EMPTY);
        }

        ItemStack itemStack2 = upgradeable.copy();
        if (itemStack2.get(ItemInit.GEMS) != null) {

            float unlinkChance = 0.0f;
            boolean hasHammer = hammer != null && !hammer.isEmpty() && hammer.isIn(TagInit.EXTRACTION_ITEMS);
            if (hasHammer) {
                if (hammer.getItem() instanceof Hammer hammerItem) {
                    unlinkChance += hammerItem.getBonusChance();
                } else {
                    unlinkChance += ConfigInit.CONFIG.bonusItemExtraChance;
                }
            }

            List<ItemStack> list = new ArrayList<ItemStack>();
            if (hammer != null && !hammer.isEmpty()) {
                ItemStack stack = itemStack2.get(ItemInit.GEMS).gems().getLast().copy();
                if (stack.getItem() instanceof Gem gem) {
                    unlinkChance = gem.getUnlinkChance() + (hasHammer ? (hammer.getItem() instanceof Hammer hammerItem ? hammerItem.getBonusChance() : ConfigInit.CONFIG.bonusItemExtraChance) : 0.0f);
                    if (RANDOM.nextFloat() <= unlinkChance) {
                        list.add(stack);
                    }
                } else if (unlinkChance >= RANDOM.nextFloat()) {
                    list.add(stack);
                }
                List<ItemStack> remainingGems = new ArrayList<ItemStack>(itemStack2.get(ItemInit.GEMS).gems());
                remainingGems.removeLast();
                itemStack2.set(ItemInit.GEMS, new GemComponent(remainingGems, itemStack2.get(ItemInit.GEMS).size()));

            } else {
                List<ItemStack> remainingGems = new ArrayList<ItemStack>(itemStack2.get(ItemInit.GEMS).gems());
                for (ItemStack stack : remainingGems) {
                    if (stack.getItem() instanceof Gem gem) {
                        unlinkChance = gem.getUnlinkChance()
                                + (hasHammer ? (hammer.getItem() instanceof Hammer hammerItem ? hammerItem.getBonusChance() : ConfigInit.CONFIG.bonusItemExtraChance) : 0.0f);
                        if (RANDOM.nextFloat() <= unlinkChance) {
                            list.add(stack);
                        }
                    } else if (unlinkChance >= RANDOM.nextFloat()) {
                        list.add(stack);
                    }
                }
                itemStack2.set(ItemInit.GEMS, new GemComponent(new ArrayList<>(), itemStack2.get(ItemInit.GEMS).size()));
            }
            list.add(0, itemStack2);
            return list;
        }

        return List.of(ItemStack.EMPTY);
    }

    public static boolean removeStackFromUpgradeable(ItemStack upgradeable, ItemStack gemStack) {
        if (getGemStacks(upgradeable).toList().isEmpty()) {
            return false;
        }
        boolean hasGemStack = false;
        List<ItemStack> gems = new ArrayList<>();
        for (ItemStack stack : upgradeable.get(ItemInit.GEMS).gems()) {
            if (!ItemStack.areEqual(stack, gemStack)) {
                gems.add(stack);
            } else {
                hasGemStack = true;
            }
        }
        upgradeable.set(ItemInit.GEMS, new GemComponent(gems, upgradeable.get(ItemInit.GEMS).size()));
        return hasGemStack;
    }

    /**
     * Sets the gem slot count of an itemstack.<br>
     * <p>
     * Set count to -1 for TieredZ compat or skewed random count.
     */
    public static void setGemSlots(ItemStack itemStack, int count) {
        if (itemStack.getItem() instanceof Upgradeable) {
            GemComponent gemComponent = itemStack.getOrDefault(ItemInit.GEMS, GemComponent.DEFAULT);

            if (count >= 0) {
                gemComponent = new GemComponent(new ArrayList<>(), Math.min(count, ConfigInit.CONFIG.maxGemSlots));
            } else if (!SmitherzMain.isTieredLoaded) {
                if (ConfigInit.CONFIG.gemSlotRandomness) {
                    gemComponent = new GemComponent(new ArrayList<>(), skewedRandomInt(ConfigInit.CONFIG.maxGemSlots));
                } else {
                    gemComponent = new GemComponent(new ArrayList<>(), ConfigInit.CONFIG.maxGemSlots);
                }
            } else {
                if (itemStack.get(Tiered.TIER) != null) {
                    if (SmitherzMain.upgradeSlotMap.containsKey(itemStack.get(Tiered.TIER).tier())) {
                        gemComponent = new GemComponent(new ArrayList<>(), SmitherzMain.upgradeSlotMap.get(itemStack.get(Tiered.TIER).tier()));
                    } else {
                        for (String id : SmitherzMain.upgradeSlotMap.keySet()) {
                            if (itemStack.get(Tiered.TIER).tier().contains(id)) {
                                gemComponent = new GemComponent(new ArrayList<>(), SmitherzMain.upgradeSlotMap.get(id));
                                break;
                            }
                        }
                    }
                }
            }
            itemStack.set(ItemInit.GEMS, gemComponent);
        }
    }

    public static void addGemSlots(ItemStack itemStack, int count) {
        if (count > 0 && itemStack.getItem() instanceof Upgradeable) {
            GemComponent gemComponent = itemStack.getOrDefault(ItemInit.GEMS, GemComponent.DEFAULT);
            gemComponent = new GemComponent(gemComponent.gems(), Math.min(gemComponent.size() + count, ConfigInit.CONFIG.maxGemSlots));
            itemStack.set(ItemInit.GEMS, gemComponent);
        }
    }

    public static void removeGemSlots(ItemStack itemStack, int count) {
        if (count > 0 && itemStack.getItem() instanceof Upgradeable) {
            GemComponent gemComponent = itemStack.getOrDefault(ItemInit.GEMS, GemComponent.DEFAULT);
            if (count >= gemComponent.size()) {
                itemStack.remove(ItemInit.GEMS);
            } else {
                List<ItemStack> gems = new ArrayList<>();
                for (int i = 0; i < gemComponent.size() - count; i++) {
                    if (gemComponent.gems().isEmpty() || gemComponent.gems().size() <= i) {
                        break;
                    }
                    gems.add(gemComponent.gems().get(i));
                }
                gemComponent = new GemComponent(gems, Math.max(gemComponent.size() - count, 0));
                itemStack.set(ItemInit.GEMS, gemComponent);
            }
        }
    }

    public static int getGemSlotSize(ItemStack itemStack) {
        if (itemStack.get(ItemInit.GEMS) != null) {
            return itemStack.get(ItemInit.GEMS).size();
        }
        return 0;
    }

    public static int skewedRandomInt(int maxValue) {
        double exponent = 1.5; // Adjust this value to control skewness
        // Generate a random double between 0.0 (inclusive) and 1.0 (exclusive)
        double randomValue = RANDOM.nextDouble();
        // Apply the exponential function to skew the distribution
        double skewedValue = Math.pow(randomValue, exponent);
        // Map the skewed value to the desired range

        return (int) (skewedValue * (maxValue + 1));
    }

}
