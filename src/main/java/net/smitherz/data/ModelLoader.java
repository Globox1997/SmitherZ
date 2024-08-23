package net.smitherz.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.smitherz.init.ItemInit;
import net.smitherz.item.Hammer;

public class ModelLoader extends FabricModelProvider {

    public ModelLoader(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (int i = 0; i < ItemInit.ITEMS.size(); i++) {
            if (ItemInit.ITEMS.get(i) instanceof Hammer) {
                itemModelGenerator.register(ItemInit.ITEMS.get(i), Models.HANDHELD);
            } else {
                itemModelGenerator.register(ItemInit.ITEMS.get(i), Models.GENERATED);
            }
        }
    }
}
