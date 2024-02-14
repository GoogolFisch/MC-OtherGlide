package me.googol.fisch.otherglide.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.util.PlayerDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;

public class GoToHomeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager
                        .literal("q")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(GoToHomeCommand::run)
        );
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver)context.getSource().getPlayer();

        if(player.playerGotoStore()){
            context.getSource().sendFeedback(Text.literal("TP to pos!"), true);
            return 0;
        }else{
            context.getSource().sendFeedback(Text.literal("No Pos Set!"), true);
            return -1;
        }
    }
}
