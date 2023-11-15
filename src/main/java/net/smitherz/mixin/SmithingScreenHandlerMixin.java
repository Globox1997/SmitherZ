package net.smitherz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.smitherz.access.ScreenHandlerAccess;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin implements ScreenHandlerAccess {

    @Unique
    private BlockPos pos;

    @Inject(method = "Lnet/minecraft/screen/SmithingScreenHandler;<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void initMixin(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo info) {
        context.run((world, pos) -> {
            SmithingScreenHandlerMixin.this.setPos(pos);
        });

    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

}
