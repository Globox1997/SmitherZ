package net.smitherz.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.rpgdifficulty.access.EntityAccess;
import net.smitherz.SmitherzMain;
import net.smitherz.init.ConfigInit;

public class LootHelper {

    private static final boolean isRpgDifficultyLoaded = FabricLoader.getInstance().isModLoaded("rpgdifficulty");

    public static void dropGemLoot(LivingEntity livingEntity, DamageSource damageSource, boolean causedByPlayer) {
        if (ConfigInit.CONFIG.mobsCanDropGems && causedByPlayer && livingEntity instanceof MobEntity mobEntity) {

            if (isRpgDifficultyLoaded && SmitherzMain.gemRpgDropMap.containsKey(mobEntity.getType())) {
                float mobHealthMultiplier = ((EntityAccess) mobEntity).getMobHealthMultiplier();
                float maxHealthMultiplier = 0.0f;
                Iterator<Map.Entry<Float, Map<Item, Float>>> iterator = SmitherzMain.gemRpgDropMap.get(mobEntity.getType()).entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Float, Map<Item, Float>> entry = iterator.next();
                    if (entry.getKey() <= mobHealthMultiplier && maxHealthMultiplier < entry.getKey()) {
                        maxHealthMultiplier = entry.getKey();
                    }
                }

                if (SmitherzMain.gemRpgDropMap.get(mobEntity.getType()).containsKey(maxHealthMultiplier)) {
                    Map<Item, Float> itemChance = SmitherzMain.gemRpgDropMap.get(mobEntity.getType()).get(maxHealthMultiplier);

                    List<Map.Entry<Item, Float>> entryList = new ArrayList<>(itemChance.entrySet());
                    Map.Entry<Item, Float> randomEntry = entryList.get(UpgradeHelper.RANDOM.nextInt(itemChance.size()));
                    if (UpgradeHelper.RANDOM.nextFloat() <= randomEntry.getValue()) {
                        livingEntity.dropStack(new ItemStack(randomEntry.getKey()));
                    }
                }

            } else if (SmitherzMain.gemDropMap.containsKey(mobEntity.getType())) {
                int rarity = 0;
                int maxValue = 0;
                Iterator<Integer> iterator = SmitherzMain.gemDropMap.get(mobEntity.getType()).keySet().iterator();
                while (iterator.hasNext()) {
                    int value = iterator.next();
                    if (value > maxValue) {
                        maxValue = value;
                    }
                }
                rarity = UpgradeHelper.skewedRandomInt(maxValue);

                if (SmitherzMain.gemDropMap.get(mobEntity.getType()).containsKey(rarity)) {
                    Map<Item, Float> itemChance = SmitherzMain.gemDropMap.get(mobEntity.getType()).get(rarity);

                    List<Map.Entry<Item, Float>> entryList = new ArrayList<>(itemChance.entrySet());
                    Map.Entry<Item, Float> randomEntry = entryList.get(UpgradeHelper.RANDOM.nextInt(itemChance.size()));
                    if (UpgradeHelper.RANDOM.nextFloat() <= randomEntry.getValue()) {
                        livingEntity.dropStack(new ItemStack(randomEntry.getKey()));
                    }
                }
            }

        }
    }

}
