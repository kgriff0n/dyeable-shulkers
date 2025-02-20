package io.github.kgriff0n.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import io.github.kgriff0n.Config;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConfigCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("dyeable-shulkers")
                    .requires(context -> context.hasPermissionLevel(4))
                    .executes(context -> display(context.getSource()))
                    .then(literal("canDyeBlock")
                            .then(argument("boolean", BoolArgumentType.bool())
                                    .executes(context -> setDyeBlock(context.getSource(), BoolArgumentType.getBool(context, "boolean")))))
                    .then(literal("canDyeMob")
                            .then(argument("boolean", BoolArgumentType.bool())
                                    .executes(context -> setDyeMob(context.getSource(), BoolArgumentType.getBool(context, "boolean")))))
                    .then(literal("canRenameBlock")
                            .then(argument("boolean", BoolArgumentType.bool())
                                    .executes(context -> setRenameBlock(context.getSource(), BoolArgumentType.getBool(context, "boolean")))))
                    .then(literal("canCleanShulker")
                            .then(argument("boolean", BoolArgumentType.bool())
                                    .executes(context -> setCleanShulker(context.getSource(), BoolArgumentType.getBool(context, "boolean")))))
            );
        });
    }

    private static int display(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        player.sendMessage(Text.literal("Dyeable Shulkers Config").formatted(Formatting.DARK_GRAY));

        MutableText canDyeBlockText = Text.literal("canDyeBlock=").formatted(Formatting.GRAY);
        if (Config.canDyeBlock) {
            canDyeBlockText.append("§2true");
        } else {
            canDyeBlockText.append("§4false");
        }
        player.sendMessage(canDyeBlockText);

        MutableText canDyeMobText = Text.literal("canDyeMob=").formatted(Formatting.GRAY);
        if (Config.canDyeMob) {
            canDyeMobText.append("§2true");
        } else {
            canDyeMobText.append("§4false");
        }
        player.sendMessage(canDyeMobText);

        MutableText canRenameBlockText = Text.literal("canRenameBlock=").formatted(Formatting.GRAY);
        if (Config.canRenameBlock) {
            canRenameBlockText.append("§2true");
        } else {
            canRenameBlockText.append("§4false");
        }
        player.sendMessage(canRenameBlockText);

        MutableText canCleanShulkerText = Text.literal("canCleanShulker=").formatted(Formatting.GRAY);
        if (Config.canCleanShulker) {
            canCleanShulkerText.append("§2true");
        } else {
            canCleanShulkerText.append("§4false");
        }
        player.sendMessage(canCleanShulkerText);

        return Command.SINGLE_SUCCESS;
    }

    private static int setDyeBlock(ServerCommandSource source, boolean b) {
        Config.canDyeBlock = b;
        display(source);
        return Command.SINGLE_SUCCESS;
    }

    private static int setDyeMob(ServerCommandSource source, boolean b) {
        Config.canDyeMob = b;
        display(source);
        return Command.SINGLE_SUCCESS;
    }

    private static int setRenameBlock(ServerCommandSource source, boolean b) {
        Config.canRenameBlock = b;
        display(source);
        return Command.SINGLE_SUCCESS;
    }

    private static int setCleanShulker(ServerCommandSource source, boolean b) {
        Config.canCleanShulker = b;
        display(source);
        return Command.SINGLE_SUCCESS;
    }
}