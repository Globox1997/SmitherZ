package net.smitherz.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.smitherz.SmitherzMain;
import net.smitherz.init.ConfigInit;
import net.smitherz.init.TagInit;
import net.smitherz.item.Gem;
import net.smitherz.item.Upgradeable;

public class UpgradeHelper {

    private static final String GEMS_KEY = "Gems";
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
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return Stream.empty();
        }
        NbtList nbtList = nbtCompound.getList(GEMS_KEY, NbtElement.COMPOUND_TYPE);
        return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
    }

    public static boolean addStackToUpgradeable(ItemStack upgradeable, ItemStack gemStack, @Nullable ItemStack hammer) {
        if (gemStack.isEmpty() || !(upgradeable.getItem() instanceof Upgradeable)) {
            return false;
        }
        float linkChance = 0.0f;
        if (gemStack.getItem() instanceof Gem gem) {
            if (!gem.canLinkToItemStack(upgradeable)) {
                return false;
            }
            if (!ConfigInit.CONFIG.canLinkSameGem && upgradeable.hasNbt()) {
                if (upgradeable.getNbt().contains(GEMS_KEY)) {
                    NbtList nbtList = upgradeable.getNbt().getList(GEMS_KEY, 9);
                    for (int i = 0; i < nbtList.size(); i++) {
                        if (ItemStack.fromNbt(nbtList.getCompound(i)).isOf(gemStack.getItem())) {
                            return false;
                        }
                    }
                }
            }
            linkChance = gem.getLinkChance();
        } else {
            linkChance = ConfigInit.CONFIG.defaultLinkChance;
        }
        boolean hasHammer = hammer != null && !hammer.isEmpty() && hammer.isIn(TagInit.BONUS_ITEMS);
        if (hasHammer) {
            linkChance = linkChance * (1.0f + ConfigInit.CONFIG.hammerExtraChance);
            hammer.decrement(1);
        }

        if (linkChance >= RANDOM.nextFloat()) {
            NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
            if (!nbtCompound.contains(GEMS_KEY)) {
                nbtCompound.put(GEMS_KEY, new NbtList());
            }

            NbtList nbtList = nbtCompound.getList(GEMS_KEY, NbtElement.COMPOUND_TYPE);
            ItemStack itemStack2 = gemStack.copy();
            NbtCompound nbtCompound3 = new NbtCompound();
            itemStack2.writeNbt(nbtCompound3);
            nbtList.add(nbtCompound3);

        } else if (!hasHammer && gemStack.getItem() instanceof Gem gem && gem.getLinkBreakChance() > 0.00001f && RANDOM.nextFloat() <= gem.getLinkBreakChance()) {
            upgradeable.decrement(1);
        }
        if (!upgradeable.isEmpty() && ConfigInit.CONFIG.linkBreakChance > 0.00001f && RANDOM.nextFloat() < ConfigInit.CONFIG.linkBreakChance) {
            upgradeable.decrement(1);
        }
        gemStack.decrement(1);
        return true;
    }

    public static List<ItemStack> removeStackFromUpgradeable(ItemStack upgradeable, @Nullable ItemStack hammer) {
        if (!(upgradeable.getItem() instanceof Upgradeable) || getGemStacks(upgradeable).toList().isEmpty()) {
            return List.of(ItemStack.EMPTY);
        }

        ItemStack itemStack2 = upgradeable.copy();
        if (itemStack2.hasNbt() && itemStack2.getNbt().contains(GEMS_KEY)) {

            float unlinkChance = ConfigInit.CONFIG.defaultUnlinkChance;
            boolean hasHammer = hammer != null && !hammer.isEmpty() && hammer.isIn(TagInit.BONUS_ITEMS);
            if (hasHammer) {
                unlinkChance = unlinkChance * (1.0f + ConfigInit.CONFIG.hammerExtraChance);
            }

            if (hammer != null && !hammer.isEmpty()) {
                List<ItemStack> list = new ArrayList<ItemStack>();
                NbtList nbtList = itemStack2.getNbt().copy().getList(GEMS_KEY, NbtElement.COMPOUND_TYPE);
                itemStack2.getNbt().remove(GEMS_KEY);

                ItemStack stack = ItemStack.fromNbt(nbtList.getCompound(nbtList.size() - 1));
                if (stack.getItem() instanceof Gem gem) {
                    if (RANDOM.nextFloat() <= (gem.getUnlinkChance() * (hasHammer ? (1.0f + ConfigInit.CONFIG.hammerExtraChance) : 1.0f))) {
                        list.add(stack);
                    }
                } else if (unlinkChance >= RANDOM.nextFloat()) {
                    list.add(stack);
                }
                nbtList.remove(nbtList.size() - 1);
                itemStack2.getNbt().put(GEMS_KEY, nbtList);
                list.add(0, itemStack2);
                return list;
            } else {
                List<ItemStack> list = new ArrayList<ItemStack>();
                NbtList nbtList = itemStack2.getNbt().getList(GEMS_KEY, 9);
                for (int i = 0; i < nbtList.size(); i++) {
                    ItemStack stack = ItemStack.fromNbt(nbtList.getCompound(i));
                    if (stack.getItem() instanceof Gem gem) {
                        if (RANDOM.nextFloat() <= (gem.getUnlinkChance() * (hasHammer ? (1.0f + ConfigInit.CONFIG.hammerExtraChance) : 1.0f))) {
                            list.add(stack);
                        }
                    } else if (unlinkChance >= RANDOM.nextFloat()) {
                        list.add(ItemStack.fromNbt(nbtList.getCompound(i)));
                    }
                }
                itemStack2.getNbt().remove(GEMS_KEY);
                list.add(0, itemStack2);
                return list;
            }
        }

        return List.of(ItemStack.EMPTY);

    }

    public static void setGemSlots(ItemStack itemStack) {
        if (itemStack.getItem() instanceof Upgradeable) {
            NbtCompound nbtCompound = new NbtCompound();
            if (itemStack.hasNbt()) {
                nbtCompound = itemStack.getNbt().copy();
            }

            if (!SmitherzMain.isTieredLoaded) {
                nbtCompound.putInt("GemSlots", skewedRandomInt(ConfigInit.CONFIG.maxGemSlots));
            } else {
                if (!nbtCompound.isEmpty()) {
                    if (nbtCompound.contains("Tiered") && nbtCompound.getCompound("Tiered").contains("Tier")
                            && SmitherzMain.upgradeSlotMap.containsKey(nbtCompound.getCompound("Tiered").getString("Tier"))) {
                        nbtCompound.putInt("GemSlots", SmitherzMain.upgradeSlotMap.get(nbtCompound.getCompound("Tiered").getString("Tier")));
                    } else {
                        Iterator<String> iterator = SmitherzMain.upgradeSlotMap.keySet().iterator();
                        while (iterator.hasNext()) {
                            String id = iterator.next();
                            if (nbtCompound.contains(id)) {
                                nbtCompound.putInt("GemSlots", SmitherzMain.upgradeSlotMap.get(id));
                                break;
                            }
                        }
                    }
                }
            }
            itemStack.setNbt(nbtCompound);
        }
    }

    public static int getGemSlotSize(ItemStack itemStack) {
        if (itemStack.hasNbt() && itemStack.getNbt().contains("GemSlots")) {
            return itemStack.getNbt().getInt("GemSlots");
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
        int result = (int) (skewedValue * (maxValue + 1));

        return result;
    }

}
