package net.smitherz.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.libz.api.Tab;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;

@Environment(EnvType.CLIENT)
@Mixin(SmithingScreen.class)
public class SmithingScreenMixin implements Tab {

    @Override
    public @Nullable Class<?> getParentScreenClass() {
        return this.getClass();
    }
}
