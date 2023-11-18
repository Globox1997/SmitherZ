package net.smitherz.screen;

import java.util.ArrayList;
import java.util.List;

import net.libz.access.ScreenHandlerAccess;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import net.smitherz.init.ScreenInit;
import net.smitherz.init.TagInit;
import net.smitherz.item.Upgradeable;
import net.smitherz.util.UpgradeHelper;

public class GrinderScreenHandler extends ScreenHandler implements ScreenHandlerAccess {

    private final Inventory result = new CraftingResultInventory();
    final Inventory input = new SimpleInventory(2) {

        @Override
        public void markDirty() {
            super.markDirty();
            GrinderScreenHandler.this.onContentChanged(this);
        }
    };
    private final ScreenHandlerContext context;
    private BlockPos pos;
    private List<ItemStack> unlinkedItemStacks = new ArrayList<ItemStack>();

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ScreenInit.GRINDER_SCREEN_HANDLER_TYPE, syncId);
        int i;
        this.context = context;

        this.addSlot(new Slot(this.input, 0, 49, 40) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof Upgradeable;
            }

        });
        this.addSlot(new Slot(this.input, 1, 49, 19) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(TagInit.EXTRACTION_ITEMS);
            }
        });
        this.addSlot(new Slot(this.result, 2, 129, 34) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                context.run((world, pos) -> {
                    world.syncWorldEvent(WorldEvents.GRINDSTONE_USED, pos, 0);
                });
                GrinderScreenHandler.this.input.setStack(0, ItemStack.EMPTY);
                GrinderScreenHandler.this.input.getStack(1).decrement(1);
                for (int i = 0; i < unlinkedItemStacks.size(); i++) {
                    playerInventory.offerOrDrop(unlinkedItemStacks.get(i));
                }
            }
        });
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.context.run((world, pos) -> {
            GrinderScreenHandler.this.setPos(pos);
        });
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.input) {
            this.updateResult();
        }
    }

    private void updateResult() {
        ItemStack upgradeable = this.input.getStack(0);
        this.unlinkedItemStacks.clear();

        if (!upgradeable.isEmpty()) {
            List<ItemStack> resultStacks = UpgradeHelper.removeStackFromUpgradeable(upgradeable, this.input.getStack(1));

            if (!resultStacks.isEmpty()) {
                this.result.setStack(0, resultStacks.get(0));
                if (resultStacks.size() > 1) {
                    for (int i = 1; i < resultStacks.size(); i++) {
                        this.unlinkedItemStacks.add(resultStacks.get(i));
                    }
                }
            }
        } else {
            this.result.setStack(0, ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return GrinderScreenHandler.canUse(this.context, player, Blocks.GRINDSTONE);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(slotId);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            ItemStack itemStack3 = this.input.getStack(0);
            ItemStack itemStack4 = this.input.getStack(1);
            if (slotId == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else {
                if (slotId == 0 || slotId == 1 ? !this.insertItem(itemStack2, 3, 39, false)
                        : (itemStack3.isEmpty() || itemStack4.isEmpty() ? !this.insertItem(itemStack2, 0, 2, false)
                                : (slotId >= 3 && slotId < 30 ? !this.insertItem(itemStack2, 30, 39, false) : slotId >= 30 && slotId < 39 && !this.insertItem(itemStack2, 3, 30, false)))) {
                    return ItemStack.EMPTY;
                }

                if (itemStack2.isIn(TagInit.EXTRACTION_ITEMS) && this.insertItem(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                } else if (this.input.getStack(0).isEmpty() && itemStack2.getItem() instanceof Upgradeable) {
                    this.input.setStack(0, itemStack2);
                    return ItemStack.EMPTY;
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

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }
}
