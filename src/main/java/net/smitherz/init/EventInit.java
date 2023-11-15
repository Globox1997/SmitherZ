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
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.smitherz.data.GemLoader;
import net.smitherz.item.Upgradeable;
import net.smitherz.util.UpgradeHelper;

public class EventInit {

    public static void init() {
        ModifyItemAttributeModifiersCallback.EVENT.register((itemStack, slot, modifiers) -> {
            if (itemStack.getNbt() != null && itemStack.getItem() instanceof Upgradeable) {
                Iterator<ItemStack> iterator = UpgradeHelper.getGemStacks(itemStack).iterator();
                while (iterator.hasNext()) {
                    ItemStack stack = iterator.next();
                    modifiers.putAll(stack.getAttributeModifiers(slot));
                }
            }
        });

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
