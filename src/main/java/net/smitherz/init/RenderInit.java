package net.smitherz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.libz.registry.TabRegistry;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.smitherz.screen.GrinderScreen;
import net.smitherz.screen.GrinderScreenHandler;
import net.smitherz.screen.SmitherScreen;
import net.smitherz.screen.SmitherScreenHandler;
import net.smitherz.screen.widget.*;
import net.smitherz.tooltip.SmitherTooltipComponent;
import net.smitherz.tooltip.SmitherTooltipData;

@Environment(EnvType.CLIENT)
public class RenderInit {

    private static final Identifier SMITHING_TAB_ICON = new Identifier("smitherz:textures/gui/smithing_tab_icon.png");
    private static final Identifier SMITHER_TAB_ICON = new Identifier("smitherz:textures/gui/smither_tab_icon.png");

    private static final Identifier GRINDSTONE_TAB_ICON = new Identifier("smitherz:textures/gui/grindstone_tab_icon.png");
    private static final Identifier GRINDER_TAB_ICON = new Identifier("smitherz:textures/gui/grinder_tab_icon.png");

    public static void init() {
        HandledScreens.<SmitherScreenHandler, SmitherScreen>register(ScreenInit.SMITHER_SCREEN_HANDLER_TYPE, SmitherScreen::new);
        HandledScreens.<GrinderScreenHandler, GrinderScreen>register(ScreenInit.GRINDER_SCREEN_HANDLER_TYPE, GrinderScreen::new);

        TabRegistry.registerOtherTab(new SmithingTab(Text.translatable("container.upgrade"), SMITHING_TAB_ICON, 0, SmithingScreen.class), SmithingScreen.class);
        TabRegistry.registerOtherTab(new SmitherTab(Text.translatable("container.link"), SMITHER_TAB_ICON, 1, SmitherScreen.class), SmithingScreen.class);

        TabRegistry.registerOtherTab(new GrindstoneTab(Text.translatable("container.grindstone_title"), GRINDSTONE_TAB_ICON, 0, GrindstoneScreen.class), GrindstoneScreen.class);
        TabRegistry.registerOtherTab(new GrinderTab(Text.translatable("container.unlink"), GRINDER_TAB_ICON, 1, GrinderScreen.class), GrindstoneScreen.class);

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof SmitherTooltipData) {
                return new SmitherTooltipComponent((SmitherTooltipData) data);
            }
            return null;
        });
    }
}
