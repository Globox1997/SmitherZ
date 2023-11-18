package net.smitherz.screen.widget;

import org.jetbrains.annotations.Nullable;

import net.libz.api.InventoryTab;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.smitherz.network.SmitherClientPacket;

public class GrindstoneTab extends InventoryTab {

    public GrindstoneTab(Text title, @Nullable Identifier texture, int preferedPos, Class<?>... screenClasses) {
        super(title, texture, preferedPos, screenClasses);
    }

    @Override
    public void onClick(MinecraftClient client) {
        SmitherClientPacket.writeC2SScreenPacket((int) client.mouse.getX(), (int) client.mouse.getY(), 3);
    }
}
