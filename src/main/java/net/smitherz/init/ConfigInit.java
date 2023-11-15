package net.smitherz.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.smitherz.config.SmitherConfig;

public class ConfigInit {
    public static SmitherConfig CONFIG = new SmitherConfig();

    public static void init() {
        AutoConfig.register(SmitherConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(SmitherConfig.class).getConfig();
    }

}
