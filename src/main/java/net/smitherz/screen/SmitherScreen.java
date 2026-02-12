package net.smitherz.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.libz.api.Tab;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.smitherz.SmitherzMain;
import net.smitherz.network.SmitherClientPacket;

@Environment(EnvType.CLIENT)
public class SmitherScreen extends HandledScreen<SmitherScreenHandler> implements ScreenHandlerListener, Tab {

    public static final Identifier TEXTURE = SmitherzMain.identifierOf("textures/gui/smither_screen.png");
    private SmitherScreen.SmitherButton smitherButton;

    public SmitherScreen(SmitherScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.titleX = 44;
    }

    @Override
    protected void init() {
        super.init();
        this.handler.addListener(this);

        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.smitherButton = this.addDrawableChild(new SmitherScreen.SmitherButton(i + 148, j + 18, (button) -> {
            if (button instanceof SmitherScreen.SmitherButton && !((SmitherScreen.SmitherButton) button).disabled) {
                SmitherClientPacket.writeC2SSmitherPacket();
            }
        }));
    }

    @Override
    public void removed() {
        super.removed();
        this.handler.removeListener(this);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    }

    @Override
    public Class<?> getParentScreenClass() {
        return SmithingScreen.class;
    }

    public SmitherButton getSmitherButton() {
        return this.smitherButton;
    }

    public class SmitherButton extends ButtonWidget {
        private boolean disabled;

        public SmitherButton(int x, int y, ButtonWidget.PressAction onPress) {
            super(x, y, 18, 18, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
            this.disabled = true;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int j = 176;
            if (this.disabled) {
                j += this.width * 2;
            } else if (this.isHovered()) {
                j += this.width;
            }
            context.drawTexture(TEXTURE, this.getX(), this.getY(), j, 0, this.width, this.height);
        }

        public void setDisabled(boolean disable) {
            this.disabled = disable;
        }

    }

}
