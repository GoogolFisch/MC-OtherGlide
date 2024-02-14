package me.googol.fisch.otherglide.server;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.blocks.ModBlocks;
import me.googol.fisch.otherglide.command.CommandInit;
import me.googol.fisch.otherglide.entitys.ModEntity;
import me.googol.fisch.otherglide.items.ModItemGroup;
import me.googol.fisch.otherglide.items.ModItems;
import me.googol.fisch.otherglide.networking.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;

public class OtherGlideServer implements DedicatedServerModInitializer {

    /**
     * Runs the mod initializer on the client environment.
     */

    @Override
    public void onInitializeServer() {
        OtherGlide.LOGGER.info("Server start");
        onInit();
    }
    public static void onInit(){
        ModItemGroup.registerItemServerGroup();
        ModItems.registerServerModItems();
        ModBlocks.registerModServerBlocks();
        ModEntity.registerModEntity();
        ModMessages.registerC2SPacket();
        CommandInit.initCommands();
    }
    public static void onClientInit() {
        /*ModItems.registerServerModItems();
        ModBlocks.registerModServerBlocks();
        ModEntity.registerModEntity();
        ModMessages.registerC2SPacket();
        CommandInit.initCommands();/**/
    }
}
