package me.googol.fisch.otherglide.mixin;

import me.googol.fisch.otherglide.OtherGlide;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Date;

@Mixin(MinecraftClient.class)
public class ClientTickMixin {
    @Shadow @Nullable public ClientPlayerEntity player;
    boolean gimmeHit = false;
    boolean justFly = false;
    boolean crtlDown = false;
    double xPos = 0.0;
    double yPos = 0.0;
    double zPos = 0.0;
    @Inject(at=@At("TAIL"),method = "tick")
    public void tickInject(CallbackInfo ci){
        Date a = new Date();
        int hours   = a.getHours  ();
        int minutes = a.getMinutes();
        if(hours == 0 && minutes == 30 && MinecraftClient.getInstance().player != null){
            if(!gimmeHit) {
                MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("Gimme, gimme, gimme a man after midnight")
                );
                OtherGlide.LOGGER.error("""
                        Half-past twelve
                        And I'm watching the late show in my flat, all alone
                        How I hate to spend the evening on my own
                        Autumn winds
                        Blowing outside the window as I look around the room
                        And it makes me so depressed to see the gloom""");
            }
            gimmeHit = true;
        }else{gimmeHit = false;}

        if(this.player == null || !this.player.isFallFlying() || !this.player.isCreative())justFly = false;
        else if(justFly){
            this.player.setVelocity(0d,0d,0d);
            double dx = xPos;
            double dy = yPos;
            double dz = zPos;
            double anYaw = this.player.getYaw() * MathHelper.RADIANS_PER_DEGREE;
            double anPit = this.player.getPitch() * MathHelper.RADIANS_PER_DEGREE;
            double mv = 0;
            double vm = 0;
            if(MinecraftClient.getInstance().options.jumpKey.isPressed())   dy += 0.5d;
            if(MinecraftClient.getInstance().options.sneakKey.isPressed())  dy -= 0.5d;
            if(MinecraftClient.getInstance().options.forwardKey.isPressed())mv  = 0.6;
            if(MinecraftClient.getInstance().options.backKey.isPressed())   mv -= 0.6;
            if(MinecraftClient.getInstance().options.leftKey.isPressed())   vm  = 0.6;
            if(MinecraftClient.getInstance().options.rightKey.isPressed())  vm -= 0.6;
            dx += -MathHelper.sin((float) anYaw) * mv * MathHelper.cos((float) anPit) + MathHelper.cos((float) anYaw) * vm;
            dz += MathHelper.cos((float) anYaw) * mv * MathHelper.cos((float) anPit)  + MathHelper.sin((float) anYaw) * vm;
            dy += -MathHelper.sin((float) anPit) * mv;
            xPos = dx;
            yPos = dy;
            zPos = dz;
            this.player.setPos(xPos,yPos,zPos);
        }
        if(MinecraftClient.getInstance().options.sprintKey.isPressed() && !crtlDown){
            crtlDown = true;
            justFly = !justFly;
            xPos = this.player.getX();
            yPos = this.player.getY();
            zPos = this.player.getZ();
        }
        else if(crtlDown && !MinecraftClient.getInstance().options.sprintKey.isPressed())crtlDown = false;
    }
}
