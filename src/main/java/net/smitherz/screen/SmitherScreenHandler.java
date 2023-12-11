package net.smitherz.screen;

import java.util.List;

import net.libz.access.ScreenHandlerAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import net.smitherz.init.ConfigInit;
import net.smitherz.init.ScreenInit;
import net.smitherz.init.TagInit;
import net.smitherz.item.Gem;
import net.smitherz.item.Upgradeable;
import net.smitherz.network.SmitherServerPacket;
import net.smitherz.screen.widget.GemSlot;
import net.smitherz.util.UpgradeHelper;

public class SmitherScreenHandler extends ScreenHandler implements ScreenHandlerAccess {

    private final Inventory inventory = new SimpleInventory(ConfigInit.CONFIG.maxGemSlots + 2) {
        @Override
        public void markDirty() {
            super.markDirty();
            SmitherScreenHandler.this.onContentChanged(this);
        }
    };

    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private BlockPos pos;

    public SmitherScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenInit.SMITHER_SCREEN_HANDLER_TYPE, syncId);

        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new Slot(this.inventory, 0, 80, 19) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof Upgradeable;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getWorld().isClient()) {
                    for (int i = 0; i < this.inventory.size() - 2; i++) {
                        if (!getSlot(i + 2).getStack().isEmpty()) {
                            if (((GemSlot) getSlot(i + 2)).isLinked()) {
                                getSlot(i + 2).setStack(ItemStack.EMPTY);
                                ((GemSlot) getSlot(i + 2)).setLinked(false);
                            } else {
                                playerInventory.offerOrDrop(getSlot(i + 2).getStack());
                            }
                        }
                    }
                }

                super.onTakeItem(player, stack);
            }

            @Override
            public void setStack(ItemStack stack) {
                if (!stack.isEmpty()) {
                    updateGemSlots(stack);
                }
                super.setStack(stack);
            }

        });
        this.addSlot(new Slot(this.inventory, 1, 106, 19) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(TagInit.BONUS_ITEMS);
            }
        });

        for (int i = 2; i < this.inventory.size(); i++) {
            this.addSlot(new GemSlot(this.inventory, i, 89 - (this.inventory.size() / 2 * 18) + i * 18 - 18, 43));
            ((GemSlot) this.getSlot(i)).setEnabled(false);
        }
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.context.run((world, pos) -> {
            SmitherScreenHandler.this.setPos(pos);
        });
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.inventory) {
            this.updateResult();
        }
    }

    private void updateGemSlots(ItemStack upgradeable) {
        int gemSlots = ((Upgradeable) upgradeable.getItem()).gemSlots(upgradeable);
        List<ItemStack> linkedStacks = UpgradeHelper.getGemStacks(upgradeable).toList();
        for (int i = 0; i < this.inventory.size() - 2; i++) {
            ((GemSlot) getSlot(i + 2)).setEnabled(i < gemSlots);
            boolean linked = i < linkedStacks.size();
            ((GemSlot) getSlot(i + 2)).setLinked(linked);
            if (linked) {
                getSlot(i + 2).setStack(linkedStacks.get(i));
            }
        }
    }

    private void updateResult() {
        if (!this.player.getWorld().isClient()) {
            boolean disableButton = true;
            boolean hasUpgradeable = this.getSlot(0).hasStack() && this.getSlot(0).getStack().getItem() instanceof Upgradeable;

            if (hasUpgradeable && hasUnlinkedGem()) {
                disableButton = false;
            }
            if (!disableButton && !canLinkSameGem()) {
                disableButton = true;
            }
            SmitherServerPacket.writeS2CSmitherReadyPacket((ServerPlayerEntity) this.player, disableButton);
        }
    }

    public void smith() {
        if (this.getSlot(0).hasStack() && !getUnlickedGem().isEmpty() && this.getSlot(0).getStack().getItem() instanceof Upgradeable) {
            UpgradeHelper.addStackToUpgradeable(this.getSlot(0).getStack(), getUnlickedGem(), this.getSlot(1).getStack());
            updateGemSlots(this.getSlot(0).getStack());
            context.run((world, pos) -> {
                world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0);
            });
            if (!this.getSlot(0).hasStack()) {
                context.run((world, pos) -> {
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f, serverWorld.getRandom().nextLong());
                    }
                });
            }
        }

    }

    private ItemStack getUnlickedGem() {
        for (int i = 2; i < this.inventory.size(); i++) {
            if (!this.getSlot(i).getStack().isEmpty() && !((GemSlot) this.getSlot(i)).isLinked()) {
                return this.getSlot(i).getStack();
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean canLinkSameGem() {
        if (!ConfigInit.CONFIG.canLinkSameGem) {
            if (this.getSlot(0).getStack().hasNbt() && this.getSlot(0).getStack().getNbt().contains(UpgradeHelper.GEMS_KEY)) {
                NbtList nbtList = this.getSlot(0).getStack().getNbt().getList(UpgradeHelper.GEMS_KEY, NbtElement.COMPOUND_TYPE);
                Item gem = getUnlickedGem().getItem();
                for (int i = 0; i < nbtList.size(); i++) {
                    if (ItemStack.fromNbt(nbtList.getCompound(i)).isOf(gem)) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    private boolean hasUnlinkedGem() {
        for (int i = 2; i < this.inventory.size(); i++) {
            if (!((GemSlot) this.getSlot(i)).isEnabled()) {
                continue;
            }
            if (!this.getSlot(i).getStack().isEmpty() && !((GemSlot) this.getSlot(i)).isLinked()) {
                return true;
            }
        }
        return false;
    }

    private boolean isUnlinkedGem(int slot) {
        if (slot == 0 || slot == 1) {
            return true;
        }
        if (this.getSlot(slot) instanceof GemSlot && !((GemSlot) this.getSlot(slot)).isLinked()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @Override
    protected void dropInventory(PlayerEntity player, Inventory inventory) {
        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).isDisconnected()) {
            for (int i = 0; i < inventory.size(); ++i) {
                if (isUnlinkedGem(i)) {
                    player.dropItem(inventory.removeStack(i), false);
                }
            }
            return;
        }
        for (int i = 0; i < inventory.size(); ++i) {
            PlayerInventory playerInventory = player.getInventory();
            if (!(playerInventory.player instanceof ServerPlayerEntity))
                continue;
            if (isUnlinkedGem(i)) {
                playerInventory.offerOrDrop(inventory.removeStack(i));
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> {
            return player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0;
        }, true);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!this.insertItem(itemStack2, 8, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index > 0 && index < 8) {
                if (!this.insertItem(itemStack2, 8, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 8 && index < 44) {
                if (itemStack.isIn(TagInit.BONUS_ITEMS) && !this.insertItem(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
                if (this.getSlot(0).getStack().isEmpty()) {
                    if (itemStack.getItem() instanceof Upgradeable && !this.insertItem(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (itemStack.getItem() instanceof Gem && !this.insertGem(itemStack2, 2, 8)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    private boolean insertGem(ItemStack stack, int startIndex, int endIndex) {
        ItemStack itemStack;
        Slot slot;
        boolean bl = false;
        int i = startIndex;
        if (!stack.isEmpty()) {
            i = startIndex;
            while (i < endIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }
                    slot.markDirty();
                    bl = true;
                    break;
                }
                ++i;
            }
        }
        return bl;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        if (slot.getIndex() != 0 && (this.getSlot(0).getStack().isEmpty() || UpgradeHelper.getGemSlotSize(this.getSlot(0).getStack()) < slot.getIndex())) {
            return false;
        }
        return slot.inventory != this.inventory && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

}
