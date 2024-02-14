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
import net.minecraft.util.math.BlockPos;

public class SetHomeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager
                        .literal("home")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(SetHomeCommand::run)
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        OtherGlide.LOGGER.info(context.getInput());
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendFeedback(Text.translatable("command.invalid.playerStuck"), true);
            return 0;
        }
        PlayerDataSaver player = (PlayerDataSaver) context.getSource().getPlayer();
        BlockPos playerPos = context.getSource().getPlayer().getBlockPos();
        String pos = "(" + playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ() + ")";

        player.storePlayerGoto(playerPos.toCenterPos());

        context.getSource().sendFeedback(Text.literal("Set home at " + pos), true);
        return 1;
    }
}