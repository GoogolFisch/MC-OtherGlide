package me.googol.fisch.otherglide.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.util.PlayerDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class playerStuckCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                (
                        CommandManager
                                .literal("stuck")
                                .requires(source -> source.hasPermissionLevel(2))
                ).then(
                                CommandManager
                                        .literal("go")
                                        .executes(playerStuckCommand::runPlayerGoStuck)
                        ).then(
                                CommandManager
                                        .literal("un")
                                        .executes(playerStuckCommand::runPlayerUnStuck)
                        )
                        .executes(playerStuckCommand::run)
        );
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.stopPlayerStuck()){
            context.getSource().sendFeedback(Text.literal("Player Move!"), true);
        }else{
            player.startPlayerStuck();
            context.getSource().sendFeedback(Text.literal("Player Stuck!"), true);
        }
        return 0;
    }
    private static int runPlayerGoStuck(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.startPlayerStuck()){
            context.getSource().sendFeedback(Text.literal("Player Stuck!"), true);
        }else{
            context.getSource().sendFeedback(Text.literal("Player is Stuck!"), true);
        }
        return 0;
    }
    private static int runPlayerUnStuck(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.stopPlayerStuck()){
            context.getSource().sendFeedback(Text.literal("Player un Stuck!"), true);
        }else{
            context.getSource().sendFeedback(Text.literal("Player is un Stuck!"), true);
        }
        return 0;
    }
}
