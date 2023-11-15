package net.smitherz.screen.widget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.smitherz.init.TagInit;
import net.smitherz.item.Gem;

public class GemSlot extends Slot {

    private boolean enabled;
    private boolean linked;

    public GemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public boolean isLinked() {
        return this.linked;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        if (this.linked) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (stack.getItem() instanceof Gem && !((Gem) stack.getItem()).canLinkToItemStack(stack)) {
            return false;
        }
        return this.enabled && !this.linked && (stack.getItem() instanceof Gem || stack.isIn(TagInit.GEMS));
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

}
