package net.smitherz.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.smitherz.data.GemLoader;

public class EventInit {

    public static void init() {
        Path directoryPath = Paths.get(FabricLoader.getInstance().getGameDir().toString(), "global_packs", "required_datapacks");
        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
                for (Path filePath : directoryStream) {

                    if (filePath.toString().endsWith(".zip")) {
                        try (ZipFile zipFile = new ZipFile(filePath.toString())) {
                            Enumeration<? extends ZipEntry> entries = zipFile.entries();

                            while (entries.hasMoreElements()) {
                                ZipEntry entry = entries.nextElement();
                                if (!entry.isDirectory() && entry.getName().startsWith("data/smitherz/gems")) {
                                    File fileInstance = extractFileFromZip(zipFile, entry);
                                    GemLoader.readGemFile(fileInstance);
                                    fileInstance.delete();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Path gemDirectoryPath = Paths.get(filePath.toString(), "data", "smitherz", "gems");
                        if (Files.exists(gemDirectoryPath)) {
                            Files.list(gemDirectoryPath).forEach((path) -> {
                                GemLoader.readGemFile(path.toFile());
                            });
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (LootTables.END_CITY_TREASURE_CHEST.equals(key)) {
                LootPool pool = LootPool.builder().with(ItemEntry.builder(ItemInit.EXTRACTION_HAMMER_4).build()).rolls(BinomialLootNumberProvider.create(1, 0.005F))
                        .with(ItemEntry.builder(ItemInit.SMITHER_HAMMER_4).build()).rolls(BinomialLootNumberProvider.create(1, 0.005F)).build();
                tableBuilder.pool(pool);
            } else if (LootTables.BURIED_TREASURE_CHEST.equals(key) || LootTables.SHIPWRECK_TREASURE_CHEST.equals(key)) {
                LootPool pool = LootPool.builder().with(ItemEntry.builder(ItemInit.EXTRACTION_HAMMER_1).build()).rolls(BinomialLootNumberProvider.create(1, 0.1F))
                        .with(ItemEntry.builder(ItemInit.EXTRACTION_HAMMER_2).build()).rolls(BinomialLootNumberProvider.create(1, 0.05F)).with(ItemEntry.builder(ItemInit.EXTRACTION_HAMMER_3).build())
                        .rolls(BinomialLootNumberProvider.create(1, 0.01F)).build();
                tableBuilder.pool(pool);
            } else if (LootTables.BASTION_TREASURE_CHEST.equals(key)) {
                LootPool pool = LootPool.builder().with(ItemEntry.builder(ItemInit.SMITHER_HAMMER_1).build()).rolls(BinomialLootNumberProvider.create(1, 0.1F))
                        .with(ItemEntry.builder(ItemInit.SMITHER_HAMMER_2).build()).rolls(BinomialLootNumberProvider.create(1, 0.05F)).with(ItemEntry.builder(ItemInit.SMITHER_HAMMER_3).build())
                        .rolls(BinomialLootNumberProvider.create(1, 0.01F)).build();
                tableBuilder.pool(pool);
            }
        });
    }

    private static File extractFileFromZip(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        File tempFile = File.createTempFile("temp_gem", ".json");

        try (InputStream inputStream = zipFile.getInputStream(zipEntry); FileOutputStream fos = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

}
