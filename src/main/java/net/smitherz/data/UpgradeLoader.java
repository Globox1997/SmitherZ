package net.smitherz.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.smitherz.SmitherzMain;
import net.smitherz.init.ConfigInit;

public class UpgradeLoader implements SimpleSynchronousResourceReloadListener {

    private static final Logger LOGGER = LogManager.getLogger("SmitherZ");

    @Override
    public Identifier getFabricId() {
        return Identifier.of("smitherz", "upgrade_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        SmitherzMain.upgradeSlotMap.clear();
        resourceManager.findResources("gem_upgrades", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
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
        SmitherzMain.gemDropMap.clear();
        SmitherzMain.gemRpgDropMap.clear();
        if (ConfigInit.CONFIG.mobsCanDropGems) {
            resourceManager.findResources("gem_drops", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
                try {
                    InputStream stream = resourceRef.getInputStream();
                    JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                    for (String s : data.keySet()) {
                        JsonObject jsonObject = data.get(s).getAsJsonObject();
                        List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>();

                        for (JsonElement element : jsonObject.getAsJsonArray("mob_types")) {
                            String mobType = element.getAsString();
                            if (Registries.ENTITY_TYPE.get(Identifier.of(mobType)).toString().equals("entity.minecraft.pig")) {
                                LOGGER.info("Resource {} was not loaded cause {} is not a valid entity identifier", id.toString(), mobType);
                                return;
                            }
                            entityTypes.add(Registries.ENTITY_TYPE.get(Identifier.of(mobType)));
                        }

                        for (EntityType<?> entityType : entityTypes) {
                            int rarityGroup = jsonObject.get("rarity_group").getAsInt();
                            float difficultyMultiplier = jsonObject.has("difficulty_multiplier") ? jsonObject.get("difficulty_multiplier").getAsFloat() : 0.0f;
                            Map<Item, Float> itemChanceMap = new HashMap<>();
                            float chance = jsonObject.get("drop_chance").getAsFloat();
                            for (JsonElement jsonElement : jsonObject.getAsJsonArray("items")) {
                                String item = jsonElement.getAsString();
                                if (Registries.ITEM.get(Identifier.of(item)).toString().equals("air")) {
                                    LOGGER.info("{} is not a valid item identifier", item);
                                    continue;
                                }
                                itemChanceMap.put(Registries.ITEM.get(Identifier.of(item)), chance);
                            }

                            if (SmitherzMain.gemDropMap.containsKey(entityType)) {
                                if (SmitherzMain.gemDropMap.get(entityType).containsKey(rarityGroup)) {
                                    SmitherzMain.gemDropMap.get(entityType).get(rarityGroup).putAll(itemChanceMap);
                                    SmitherzMain.gemRpgDropMap.get(entityType).get(difficultyMultiplier).putAll(itemChanceMap);
                                } else {
                                    SmitherzMain.gemDropMap.get(entityType).put(rarityGroup, itemChanceMap);
                                    SmitherzMain.gemRpgDropMap.get(entityType).put(difficultyMultiplier, itemChanceMap);
                                }
                            } else {
                                LinkedHashMap<Integer, Map<Item, Float>> gemDrops = new LinkedHashMap<Integer, Map<Item, Float>>();
                                gemDrops.put(rarityGroup, itemChanceMap);
                                SmitherzMain.gemDropMap.put(entityType, gemDrops);

                                LinkedHashMap<Float, Map<Item, Float>> gemRpgDrops = new LinkedHashMap<Float, Map<Item, Float>>();
                                gemRpgDrops.put(difficultyMultiplier, itemChanceMap);
                                SmitherzMain.gemRpgDropMap.put(entityType, gemRpgDrops);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
                }
            });
        }
    }

}
