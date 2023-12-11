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
    @Comment("If non link chance is set via datapack, default 5% chance")
    public float defaultLinkChance = 0.05f;
    @Comment("Always used for unlinking")
    public float defaultUnlinkChance = 0.2f;
    @Comment("20% default bonus")
    public float hammerExtraChance = 0.2f;
    @Comment("Use if gems provided by global datapack")
    public boolean defaultGems = true;
    public boolean canLinkSameGem = false;
    @Comment("Turn it off if you set drops via datapacks")
    public boolean mobsCanDropGems = true;
    public float linkBreakChance = 0.0001f;
}
