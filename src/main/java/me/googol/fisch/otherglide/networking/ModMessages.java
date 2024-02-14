package me.googol.fisch.otherglide.networking;


import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.networking.packets.c2s.MoreC2SPacket;
import me.googol.fisch.otherglide.networking.packets.s2c.PlayerGlideS2CPacket;
import me.googol.fisch.otherglide.networking.packets.s2c.PlayerStuckS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier PLAYER_STUCK_ID = new Identifier(OtherGlide.MOD_ID,"player_stuck");
    public static final Identifier PLAYER_GLIDE_ID = new Identifier(OtherGlide.MOD_ID,"player_glide");
    public static final Identifier MORE_ID = new Identifier(OtherGlide.MOD_ID,"more");

    public static void registerC2SPacket(){

        ServerPlayNetworking.registerGlobalReceiver(MORE_ID, MoreC2SPacket::receive);
    }
    public static void registerS2CPacket(){
        ClientPlayNetworking.registerGlobalReceiver(PLAYER_STUCK_ID, PlayerStuckS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(PLAYER_GLIDE_ID, PlayerGlideS2CPacket::receive);
    }
}
