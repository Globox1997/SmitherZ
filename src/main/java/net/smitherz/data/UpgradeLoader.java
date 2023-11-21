package net.smitherz.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        return new Identifier("smitherz", "upgrade_loader");
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

                    Iterator<String> keyIterator = data.keySet().iterator();
                    while (keyIterator.hasNext()) {
                        JsonObject jsonObject = data.get(keyIterator.next()).getAsJsonObject();
                        List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>();

                        Iterator<JsonElement> mobTypeIterator = jsonObject.getAsJsonArray("mob_types").iterator();
                        while (mobTypeIterator.hasNext()) {
                            String mobType = mobTypeIterator.next().getAsString();
                            if (Registries.ENTITY_TYPE.get(new Identifier(mobType)).toString().equals("entity.minecraft.pig")) {
                                LOGGER.info("Resource {} was not loaded cause {} is not a valid entity identifier", id.toString(), mobType);
                                return;
                            }
                            entityTypes.add(Registries.ENTITY_TYPE.get(new Identifier(mobType)));
                        }

                        for (int i = 0; i < entityTypes.size(); i++) {
                            int rarityGroup = jsonObject.get("rarity_group").getAsInt();
                            float difficultyMultiplier = jsonObject.has("difficulty_multiplier") ? jsonObject.get("difficulty_multiplier").getAsFloat() : 0.0f;
                            Map<Item, Float> itemChanceMap = new HashMap<>();
                            float chance = jsonObject.get("drop_chance").getAsFloat();
                            Iterator<JsonElement> itemIterator = jsonObject.getAsJsonArray("items").iterator();
                            while (itemIterator.hasNext()) {
                                String item = itemIterator.next().getAsString();
                                if (Registries.ITEM.get(new Identifier(item)).toString().equals("air")) {
                                    LOGGER.info("{} is not a valid item identifier", item);
                                    continue;
                                }
                                itemChanceMap.put(Registries.ITEM.get(new Identifier(item)), chance);
                            }

                            if (SmitherzMain.gemDropMap.containsKey(entityTypes.get(i))) {
                                if (SmitherzMain.gemDropMap.get(entityTypes.get(i)).containsKey(rarityGroup)) {
                                    SmitherzMain.gemDropMap.get(entityTypes.get(i)).get(rarityGroup).putAll(itemChanceMap);
                                    SmitherzMain.gemRpgDropMap.get(entityTypes.get(i)).get(difficultyMultiplier).putAll(itemChanceMap);
                                } else {
                                    SmitherzMain.gemDropMap.get(entityTypes.get(i)).put(rarityGroup, itemChanceMap);
                                    SmitherzMain.gemRpgDropMap.get(entityTypes.get(i)).put(difficultyMultiplier, itemChanceMap);
                                }
                            } else {
                                LinkedHashMap<Integer, Map<Item, Float>> gemDrops = new LinkedHashMap<Integer, Map<Item, Float>>();
                                gemDrops.put(rarityGroup, itemChanceMap);
                                SmitherzMain.gemDropMap.put(entityTypes.get(i), gemDrops);

                                LinkedHashMap<Float, Map<Item, Float>> gemRpgDrops = new LinkedHashMap<Float, Map<Item, Float>>();
                                gemRpgDrops.put(difficultyMultiplier, itemChanceMap);
                                SmitherzMain.gemRpgDropMap.put(entityTypes.get(i), gemRpgDrops);
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
