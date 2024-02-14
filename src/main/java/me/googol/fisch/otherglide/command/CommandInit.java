package me.googol.fisch.otherglide.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandInit {
    public static void initCommands(){
        CommandRegistrationCallback.EVENT.register(GoToHomeCommand::register);
        CommandRegistrationCallback.EVENT.register(SetHomeCommand::register);
        CommandRegistrationCallback.EVENT.register(gameGlideCommand::register);
        CommandRegistrationCallback.EVENT.register(playerStuckCommand::register);
    }
}
