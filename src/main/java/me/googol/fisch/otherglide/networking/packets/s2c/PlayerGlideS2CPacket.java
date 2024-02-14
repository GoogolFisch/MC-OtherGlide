package me.googol.fisch.otherglide.networking.packets.s2c;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.client.OtherGlideClient;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class PlayerGlideS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        // only on client!
        OtherGlideClient.inGlide = buf.getBoolean(0);
        if(OtherGlideClient.inGlide)
            client.player.startFallFlying();
    }

    public static PacketByteBuf genBuffer(boolean inGlide){
        PacketByteBuf c = PacketByteBufs.create();
        c.writeBoolean(inGlide);
        return c;
    }
}
