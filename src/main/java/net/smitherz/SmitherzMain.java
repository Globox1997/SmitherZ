package net.smitherz;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.smitherz.init.*;
import net.smitherz.network.SmitherServerPacket;

public class SmitherzMain implements ModInitializer {

    public static final Map<String, Integer> upgradeSlotMap = new HashMap<String, Integer>();

    public static final boolean isTieredLoaded = FabricLoader.getInstance().isModLoaded("tiered");

    // custom sound for smithing and unlinking
    // ---
    // should this be dependend on tieredz? cause of health issue and other things probably
    // ---
    // work on extraction part, use grindstone extra page with extraction hammer for single gem extraction or non hammer for all extraction with a chance
    // add extraction chance to datapack
    // ---
    // drop event for each existing gem, maybe add a drop chance to the datapack and maybe a loot table id (multiple)
    // numismatic compat datapack for money cost
    // ---

    // Level 1 – 41-45%
    // Level 2 – 33-37%
    // Level 3 – 25-29%
    // Level 4 – 9-13%
    // Level 5 – 1-5%
    // Dual – 9-13%

    // adjust socket height
    // tweak tooltip
    // do screen shot for discord
    // check tooltip shade

    // Can failing to link break my gear?Some lapis can break your gear if they fail. These include; Accessory, Sonic, Flash, Horizon, Absorption and level 7 debuff lapis. In order to save your gear
    // in the event of a failed link you need to have a lucky charm in your possession. The charm is only good for one linking attempt.
    // Note: There is an extremely small chance for any lapis to break your gear.

    // You get additional linking % every 20 levels on your character, guild house blacksmith upgrades also add additional %.

    @Override
    public void onInitialize() {
        ConfigInit.init();
        ItemInit.init();
        LoaderInit.init();
        TagInit.init();
        ScreenInit.init();
        EventInit.init();
        SmitherServerPacket.init();
    }

}
