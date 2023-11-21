package net.smitherz.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.smitherz.init.ItemInit;
import net.smitherz.item.Gem;
import net.smitherz.item.attribute.GemEntityAttributeModifier;

public class GemLoader {

    public static void readGemFile(File file) {

        try (FileReader fileReader = new FileReader(file)) {
            JsonObject data = JsonParser.parseReader(fileReader).getAsJsonObject();

            Iterator<Entry<String, JsonElement>> iterator = data.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, JsonElement> entry = iterator.next();
                JsonObject jsonObject = (JsonObject) entry.getValue();

                Multimap<EntityAttribute, EntityAttributeModifier> map = ArrayListMultimap.create();
                JsonArray jsonArray = jsonObject.get("attributes").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject attributejsonObject = jsonArray.get(i).getAsJsonObject();
                    map.put(Registries.ATTRIBUTE.get(new Identifier(attributejsonObject.get("type").getAsString())),
                            new GemEntityAttributeModifier(attributejsonObject.get("type").getAsString(), attributejsonObject.get("modifier").getAsJsonObject().get("amount").getAsFloat(),
                                    EntityAttributeModifier.Operation.valueOf(attributejsonObject.get("modifier").getAsJsonObject().get("operation").getAsString().toUpperCase())));
                }
                ItemInit.register(entry.getKey(), new Gem(new Item.Settings(), map, jsonObject.get("link_chance").getAsFloat(), jsonObject.get("break_chance").getAsFloat(),
                        jsonObject.get("unlink_chance").getAsFloat(), jsonObject.has("tag") && jsonObject.get("tag") != null ? TagKey.of(RegistryKeys.ITEM, new Identifier("id")) : null));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
