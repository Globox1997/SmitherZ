package net.smitherz;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.smitherz.init.*;
import net.smitherz.network.SmitherServerPacket;
import net.smitherz.tooltip.SmitherTooltipComponent;
import net.smitherz.tooltip.SmitherTooltipData;

public class SmitherzMain implements ModInitializer {

    public static final Map<String, Integer> upgradeSlotMap = new HashMap<String, Integer>();

    public static final boolean isTieredLoaded = FabricLoader.getInstance().isModLoaded("tiered");

    @Override
    public void onInitialize() {
        ConfigInit.init();
        ItemInit.init();
        LoaderInit.init();
        TagInit.init();
        ScreenInit.init();
        EventInit.init();
        SmitherServerPacket.init();
        TooltipComponentCallback.EVENT.register(data -> {
            return new SmitherTooltipComponent((SmitherTooltipData) data);
        });
    }

}
