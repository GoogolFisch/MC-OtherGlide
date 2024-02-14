package me.googol.fisch.otherglide.networking.packets.s2c;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.client.OtherGlideClient;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

public class PlayerStuckS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        // only on client!
        OtherGlideClient.isStuck = buf.getBoolean(0);
        double x,y,z;
        x = buf.getDouble(1);
        y = buf.getDouble(1 + Double.BYTES);
        z = buf.getDouble(1 + Double.BYTES * 2);
        OtherGlideClient.stuckPos = new Vec3d(x,y,z);
    }
    public static PacketByteBuf genBuffer(boolean isStuck,Vec3d stuckPos){
        PacketByteBuf c = PacketByteBufs.create();
        c.writeBoolean(isStuck);
        c.writeDouble(stuckPos.x);
        c.writeDouble(stuckPos.y);
        c.writeDouble(stuckPos.z);
        return c;
    }
}
