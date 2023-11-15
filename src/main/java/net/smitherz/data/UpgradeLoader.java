package net.smitherz.data;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.smitherz.SmitherzMain;
import net.smitherz.init.ConfigInit;

public class UpgradeLoader implements SimpleSynchronousResourceReloadListener {

    private static final Logger LOGGER = LogManager.getLogger("SmitherZ");

    @Override
    public Identifier getFabricId() {
        return new Identifier("smitherz", "upgrade_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        SmitherzMain.upgradeSlotMap.clear();
        resourceManager.findResources("upgrades", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                InputStream stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (int i = 0; i <= ConfigInit.CONFIG.maxGemSlots; i++) {
                    if (!data.has(String.valueOf(i))) {
                        continue;
                    }
                    JsonArray data2 = (JsonArray) data.getAsJsonArray(String.valueOf(i));
                    for (int u = 0; u < data2.size(); u++) {
                        SmitherzMain.upgradeSlotMap.put(data2.get(u).getAsString(), i);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });
    }

}
