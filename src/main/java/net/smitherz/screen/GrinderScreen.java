package net.smitherz.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.libz.api.Tab;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GrinderScreen extends HandledScreen<GrinderScreenHandler> implements Tab {

    public static final Identifier TEXTURE = new Identifier("smitherz", "textures/gui/grinder_screen.png");

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.drawBackground(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if ((((GrinderScreenHandler) this.handler).getSlot(0).hasStack() || ((GrinderScreenHandler) this.handler).getSlot(1).hasStack())
                && !((GrinderScreenHandler) this.handler).getSlot(2).hasStack()) {
            context.drawTexture(TEXTURE, i + 92, j + 31, this.backgroundWidth, 0, 28, 21);
        }
    }

    @Override
    public Class<?> getParentScreenClass() {
        return GrindstoneScreen.class;
    }

}
