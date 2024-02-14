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

public class gameGlideCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                (
                    CommandManager
                            .literal("glide")
                            .requires(source -> source.hasPermissionLevel(2))
                ).then(
                        CommandManager
                                .literal("start")
                                .executes(gameGlideCommand::runGlideStart)
                        ).then(
                                CommandManager
                                        .literal("finish")
                                        .executes(gameGlideCommand::runGlideFinish)
                        )
                        .executes(gameGlideCommand::run)
        );
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.finishGlideMode()){
            context.getSource().sendFeedback(Text.literal("Early finished player Glide!"), true);
        }else{
            player.startGlideMode();
            context.getSource().sendFeedback(Text.literal("Started player Glide!"), true);
        }
        return 0;
    }
    private static int runGlideStart(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.startGlideMode()){
            context.getSource().sendFeedback(Text.literal("Started player Glide!"), true);
        }else{
            context.getSource().sendFeedback(Text.literal("player Glide Running!"), true);
        }
        return 0;
    }
    private static int runGlideFinish(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.finishGlideMode()){
            context.getSource().sendFeedback(Text.literal("Started player Glide!"), true);
        }else{
            context.getSource().sendFeedback(Text.literal("player Glide Running!"), true);
        }
        return 0;
    }
}
