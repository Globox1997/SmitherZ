package net.smitherz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.libz.registry.TabRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.smitherz.screen.SmitherScreen;
import net.smitherz.screen.SmitherScreenHandler;
import net.smitherz.screen.widget.SmitherTab;
import net.smitherz.screen.widget.SmithingTab;

@Environment(EnvType.CLIENT)
public class RenderInit {

    private static final Identifier SMITHING_TAB_ICON = new Identifier("smitherz:textures/gui/smithing_tab_icon.png");
    private static final Identifier SMITHER_TAB_ICON = new Identifier("smitherz:textures/gui/smither_tab_icon.png");

    public static void init() {
        HandledScreens.<SmitherScreenHandler, SmitherScreen>register(ScreenInit.SMITHER_SCREEN_HANDLER_TYPE, SmitherScreen::new);

        TabRegistry.registerOtherTab(new SmithingTab(Text.translatable("container.upgrade"), SMITHING_TAB_ICON, 0, SmithingScreen.class), SmithingScreen.class);
        TabRegistry.registerOtherTab(new SmitherTab(Text.translatable("container.link"), SMITHER_TAB_ICON, 1, SmitherScreen.class), SmithingScreen.class);
    }
}
