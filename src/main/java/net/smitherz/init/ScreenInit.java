package net.smitherz.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.smitherz.screen.SmitherScreenHandler;

public class ScreenInit {

    public static ScreenHandlerType<SmitherScreenHandler> SMITHER_SCREEN_HANDLER_TYPE;

    public static void init() {
        SMITHER_SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, "smitherz:smither",
                new ScreenHandlerType<>((syncId, inventory) -> new SmitherScreenHandler(syncId, inventory, ScreenHandlerContext.EMPTY), FeatureFlags.VANILLA_FEATURES));
    }

}
