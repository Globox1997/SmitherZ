package net.smitherz.init;

import java.util.Iterator;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModelInit {

    public static RuntimeResourcePack ARRP_RESOURCE_PACK;

    public static void init() {
        ARRP_RESOURCE_PACK = RuntimeResourcePack.create("smitherz:gems");
        // Generate models
        Iterator<String> items = ItemInit.ITEM_IDS.iterator();
        while (items.hasNext()) {
            String item = items.next();
            if (item.contains("hammer")) {
                ARRP_RESOURCE_PACK.addModel(JModel.model().parent("item/handheld").textures(JModel.textures().layer0("smitherz:item/" + item)), new Identifier("smitherz", "item/" + item));
            } else {
                ARRP_RESOURCE_PACK.addModel(JModel.model().parent("item/generated").textures(JModel.textures().layer0("smitherz:item/" + item)), new Identifier("smitherz", "item/" + item));
            }
        }
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(ARRP_RESOURCE_PACK));
    }

}
