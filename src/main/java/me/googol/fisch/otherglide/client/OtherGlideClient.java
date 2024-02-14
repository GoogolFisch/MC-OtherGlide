package me.googol.fisch.otherglide.client;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.blocks.ModBlocks;
import me.googol.fisch.otherglide.entitys.ModEntity;
import me.googol.fisch.otherglide.gui.hud.GlideHud;
import me.googol.fisch.otherglide.items.ModItemGroup;
import me.googol.fisch.otherglide.items.ModItems;
import me.googol.fisch.otherglide.networking.ModMessages;
import me.googol.fisch.otherglide.server.OtherGlideServer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class OtherGlideClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static boolean inGlide = false;
    public static boolean isStuck = false;
    public static GlideHud glideHud = null;
    public static Vec3d stuckPos = Vec3d.ZERO;
    @Override
    public void onInitializeClient() {
        OtherGlide.LOGGER.info("Client start");
        //EntityRendererRegistry.register(new TestingEntity(), new TestingRenderer());
        ModItemGroup.registerItemClientGroup();
        ModEntity.registerModClientEntity();
        ModBlocks.registerModClientBlocks();
        ModItems.registerModItems();
        ModEntity.registerModEntity();


        ModMessages.registerS2CPacket();

        // for server!!!?!
        OtherGlideServer.onClientInit();
    }
}
