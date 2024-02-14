package me.googol.fisch.otherglide;

import me.googol.fisch.otherglide.blocks.ModBlocks;
import me.googol.fisch.otherglide.entitys.ModEntity;
import me.googol.fisch.otherglide.items.ModItemGroup;
import me.googol.fisch.otherglide.items.ModItems;
import me.googol.fisch.otherglide.networking.ModMessages;
import me.googol.fisch.otherglide.server.OtherGlideServer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherGlide implements ModInitializer {
    public static final String MOD_ID = "other_glide";
    public static final Logger LOGGER = LoggerFactory.getLogger("other_glide");
    @Override
    public void onInitialize() {
        OtherGlide.LOGGER.info("Main start");
        //ModItemGroup.registerItemGroup();
        /*if( != null){
            OtherGlideServer.onInit();
        }/**/
    }
}
