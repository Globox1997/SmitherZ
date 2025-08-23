package net.smitherz.init;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.smitherz.item.Gem;
import net.smitherz.item.Upgradeable;
import net.smitherz.util.UpgradeHelper;

import java.util.Collection;

public class CommandInit {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            dispatcher.register((CommandManager.literal("smither").requires((serverCommandSource) -> {
                        return serverCommandSource.hasPermissionLevel(2);
                    }))
                            .then(CommandManager.literal("slots").then(CommandManager.argument("targets", EntityArgumentType.players())

                                    .then(CommandManager.literal("add").then(CommandManager.argument("count", IntegerArgumentType.integer()).executes((commandContext) -> {
                                        return executeSlotCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"),
                                                IntegerArgumentType.getInteger(commandContext, "count"), 0);
                                    }))).then(CommandManager.literal("remove").then(CommandManager.argument("count", IntegerArgumentType.integer()).executes((commandContext) -> {
                                        return executeSlotCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"),
                                                IntegerArgumentType.getInteger(commandContext, "count"), 1);
                                    }))).then(CommandManager.literal("set").then(CommandManager.argument("count", IntegerArgumentType.integer()).executes((commandContext) -> {
                                        return executeSlotCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"),
                                                IntegerArgumentType.getInteger(commandContext, "count"), 2);
                                    })))

                            ))
                            .then(CommandManager.literal("gems").then(CommandManager.argument("targets", EntityArgumentType.players())

                                    .then(CommandManager.literal("add").then(CommandManager.argument("item", ItemStackArgumentType.itemStack(dedicated)).executes((commandContext) -> {
                                        return executeGemCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"),
                                                ItemStackArgumentType.getItemStackArgument(commandContext, "item"), 0);
                                    }))).then(CommandManager.literal("remove").then(CommandManager.argument("item", ItemStackArgumentType.itemStack(dedicated)).executes((commandContext) -> {
                                        return executeGemCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"),
                                                ItemStackArgumentType.getItemStackArgument(commandContext, "item"), 1);
                                    })))
                            ))
                    // Here to add different command
            );
        });
    }

    // code 0: add, 1: remove
    private static int executeGemCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, ItemStackArgument item, int code) {
        ItemStack gemStack = null;
        try {
            gemStack = item.createStack(1, true);
        } catch (CommandSyntaxException ignored) {
        }

        for (ServerPlayerEntity serverPlayerEntity : targets) {
            Text text;
            if (!serverPlayerEntity.getMainHandStack().isEmpty() && serverPlayerEntity.getMainHandStack().getItem() instanceof Upgradeable && (item.getItem() instanceof Gem || gemStack.isIn(TagInit.GEMS))) {
                // add
                ItemStack stack = serverPlayerEntity.getMainHandStack();
                if (code == 0) {
                    if (UpgradeHelper.getGemSlotSize(stack) >= UpgradeHelper.getGemStacks(stack).toList().size() + 1) {
                        UpgradeHelper.addStackToUpgradeable(stack, gemStack);
                        text = Text.translatable("commands.smitherz.gem_added", serverPlayerEntity.getDisplayName());
                    } else {
                        text = Text.translatable("commands.smitherz.gem_added_missing", serverPlayerEntity.getDisplayName());
                    }
                } else {
                    // remove
                    // if (code == 1) {
                    if (UpgradeHelper.removeStackFromUpgradeable(stack, gemStack)) {
                        text = Text.translatable("commands.smitherz.gem_removed", serverPlayerEntity.getDisplayName());
                    } else {
                        text = Text.translatable("commands.smitherz.gem_removed_missing", serverPlayerEntity.getDisplayName());
                    }
                    // }
                }
            } else {
                text = Text.translatable("commands.smitherz.gem_change_fail", serverPlayerEntity.getDisplayName());
            }
            source.sendFeedback(() -> text, true);
        }
        return 1;
    }

    // code 0: add, 1: remove, 2: set
    private static int executeSlotCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, int count, int code) {

        for (ServerPlayerEntity serverPlayerEntity : targets) {
            if (!serverPlayerEntity.getMainHandStack().isEmpty() && serverPlayerEntity.getMainHandStack().getItem() instanceof Upgradeable) {
                // add
                if (code == 0) {
                    if (UpgradeHelper.getGemSlotSize(serverPlayerEntity.getMainHandStack()) <= 0) {
                        UpgradeHelper.setGemSlots(serverPlayerEntity.getMainHandStack(), count);
                    } else {
                        UpgradeHelper.addGemSlots(serverPlayerEntity.getMainHandStack(), count);
                    }
                } else
                    // remove
                    if (code == 1) {
                        UpgradeHelper.removeGemSlots(serverPlayerEntity.getMainHandStack(), count);
                    } else
                        // set
                        if (code == 2) {
                            UpgradeHelper.setGemSlots(serverPlayerEntity.getMainHandStack(), count);
                        }
                source.sendFeedback(() -> Text.translatable("commands.smitherz.slot_change", serverPlayerEntity.getDisplayName()), true);
            } else {
                source.sendFeedback(() -> Text.translatable("commands.smitherz.slot_change_fail", serverPlayerEntity.getDisplayName()), true);
            }
        }

        return 1;
    }

}
