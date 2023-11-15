package net.smitherz.util;

import java.util.Iterator;
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
    private static final Random RANDOM = new Random();

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

    // @Nullable
    // public static List<ItemStack> getTest(ItemStack stack) {
    // NbtCompound nbtCompound = stack.getNbt();
    // if (nbtCompound == null) {
    // return null;
    // }
    // int gemSlots = getGemSlotSize(stack);
    // if (gemSlots == 0) {
    // return null;
    // }
    // ArrayList<ItemStack> gemStacks = new ArrayList<>();
    // NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
    // Iterator<ItemStack> iterator = nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt).iterator();
    // while (iterator.hasNext()) {
    // gemStacks.add(iterator.next());
    // }
    // if (gemStacks.size() < gemSlots) {
    // for (int i = 0; i < gemStacks.size() - gemSlots; i++) {
    // gemStacks.add(ItemStack.EMPTY);
    // }
    // }
    // return gemStacks;
    // }

    public static boolean addStackToUpgradeable(ItemStack upgradeable, ItemStack stack, @Nullable ItemStack hammer) {
        if (stack.isEmpty() || !(upgradeable.getItem() instanceof Upgradeable)) {
            return false;
        }
        float linkChance = 0.0f;
        if (stack.getItem() instanceof Gem) {
            if (!((Gem) stack.getItem()).canLinkToItemStack(upgradeable)) {
                return false;
            }
            linkChance = ((Gem) stack.getItem()).getLinkChance();
        } else if (ConfigInit.CONFIG.defaultLinkChance <= RANDOM.nextFloat()) {
            linkChance = ConfigInit.CONFIG.defaultLinkChance;
        }
        if (hammer != null && !hammer.isEmpty() && hammer.isIn(TagInit.BONUS_ITEMS)) {
            linkChance = linkChance * (1.0f + ConfigInit.CONFIG.hammerExtraChance);
            hammer.decrement(1);
        }

        if (linkChance <= RANDOM.nextFloat()) {
            NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
            if (!nbtCompound.contains(GEMS_KEY)) {
                nbtCompound.put(GEMS_KEY, new NbtList());
            }

            NbtList nbtList = nbtCompound.getList(GEMS_KEY, NbtElement.COMPOUND_TYPE);
            ItemStack itemStack2 = stack.copy();
            NbtCompound nbtCompound3 = new NbtCompound();
            itemStack2.writeNbt(nbtCompound3);
            nbtList.add(nbtCompound3);
        }
        stack.decrement(1);
        return true;
    }

    public static void setGemSlots(ItemStack itemStack) {
        if (itemStack.getItem() instanceof Upgradeable) {
            NbtCompound nbtCompound = new NbtCompound();
            if (itemStack.hasNbt()) {
                nbtCompound = itemStack.getNbt().copy();
            }

            if (!SmitherzMain.isTieredLoaded) {
                nbtCompound.putInt("GemSlots", RANDOM.nextInt(ConfigInit.CONFIG.maxGemSlots + 1));
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

}
