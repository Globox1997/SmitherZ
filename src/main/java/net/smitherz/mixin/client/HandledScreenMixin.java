package net.smitherz.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.smitherz.screen.SmitherScreen;
import net.smitherz.screen.widget.GemSlot;
import net.smitherz.util.UpgradeHelper;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {

    private final boolean isSmitherScreen = (Object) this instanceof SmitherScreen;

    @Shadow
    @Mutable
    @Final
    protected T handler;;

    public HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void renderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info, int i, int j, int k, Slot slot) {
        if (isSmitherScreen && k >= 2 && slot instanceof GemSlot) {
            ItemStack stack = ((ScreenHandler) this.handler).slots.get(0).getStack();
            if (!stack.isEmpty()) {
                int gemSlotSize = UpgradeHelper.getGemSlotSize(stack);
                if (gemSlotSize > k - 2) {
                    context.drawTexture(SmitherScreen.TEXTURE, slot.x - 1, slot.y - 1, 0, 166, 18, 18);
                }
            }
        }
    }

}
