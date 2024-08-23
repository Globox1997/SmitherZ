package net.smitherz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.smitherz.init.RenderInit;
import net.smitherz.network.SmitherClientPacket;

@Environment(EnvType.CLIENT)
public class SmitherzClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderInit.init();
        SmitherClientPacket.init();
    }

}
