package net.smitherz.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.smitherz.SmitherzMain;

public class SoundInit {

    public static SoundEvent LINKAGE_SUCCESS_EVENT = register("linkage_success");
    public static SoundEvent LINKAGE_FAILURE_EVENT = register("linkage_failure");

    private static SoundEvent register(String id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(SmitherzMain.identifierOf(id)));
    }

    public static void init() {
    }
}
