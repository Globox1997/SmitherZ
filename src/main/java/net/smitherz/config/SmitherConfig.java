package net.smitherz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "smitherz")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class SmitherConfig implements ConfigData {

    public int maxGemSlots = 6;
    public boolean showZeroSlotsInfo = false;
    public boolean showHoldShiftInfo = true;

    @Comment("Used for items tagged with bonus_items")
    public float bonusItemExtraChance = 0.2f;
    @Comment("Use if gems provided by global datapack")
    public boolean defaultGems = true;
    public boolean canLinkSameGem = false;
    @Comment("Turn it off if you set drops via datapacks")
    public boolean mobsCanDropGems = true;
    @Comment("General chance to break the whole item")
    public float linkBreakChance = 0.0001f;
}
