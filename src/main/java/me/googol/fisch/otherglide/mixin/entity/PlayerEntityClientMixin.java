package me.googol.fisch.otherglide.mixin.entity;

import me.googol.fisch.otherglide.client.OtherGlideClient;
import me.googol.fisch.otherglide.util.PlayerDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityClientMixin extends LivingEntityMixin implements PlayerDataSaver {
    private double boostingMax = 0.0d;
    private double thermalHeight = 0.0d;

    public PlayerEntityClientMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void startFallFlying();


    @Shadow public abstract boolean isCreative();

    @Shadow protected abstract void increaseRidingMotionStats(double dx, double dy, double dz);

    @Inject(at=@At("TAIL"),method = "tick")
    public void tickInject(CallbackInfo ci){
        if(OtherGlideClient.isStuck && this.getUuid() == MinecraftClient.getInstance().player.getUuid()){
            this.setPosition(OtherGlideClient.stuckPos);
            this.setVelocity(0d,0d,0d);
        }
        // override for closer LCE
        if(this.isFallFlying()) {
            Vec3d playerVel = this.getVelocity();
            double velX = playerVel.x;
            double velY = playerVel.y;
            double velZ = playerVel.z;
            double hSpeed = Math.sqrt(velX * velX + velZ * velZ);
            if (hSpeed < this.boostingMax) { // booster
                float yawAngle = this.getYaw() * MathHelper.RADIANS_PER_DEGREE;
                velX += -MathHelper.sin(yawAngle) * 0.1;
                velZ += MathHelper.cos(yawAngle) * 0.1;
            } else {
                this.boostingMax = 0.0d;
            }
            if (this.getPos().y < this.thermalHeight) { // thermal
                velY += 0.1;
            } else {
                this.thermalHeight = -999999.0d;
            }
            this.setVelocity(velX, velY, velZ);
        }else{
            this.boostingMax = 0.0d;
            this.thermalHeight = -999999.0d;
        }
    }
    @Inject(at = @At("HEAD"),method = "checkFallFlying", cancellable = true)
    public void checkFallFlyingMixin(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack;
        if (!this.isOnGround() && OtherGlideClient.inGlide) {
            this.startFallFlying();
            cir.setReturnValue(true);
            return;
        }
    }

    @Override
    public boolean afterGlideTick(){
        if(OtherGlideClient.inGlide && !this.onGround && !this.hasVehicle()) {
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, true);
            return true;
        }
        return false;
    }


    public void playerBoostMax(double vel){boostingMax = Math.max(boostingMax,vel);}
    public void playerThermalHeight(double height){thermalHeight = Math.max(thermalHeight,height);}

    public void collidedSoftlyMixin(Vec3d adjustedMovement, CallbackInfoReturnable<Boolean> cir){
        if(OtherGlideClient.inGlide && !this.isCreative()){
            this.getCommandTags().add("glideGameHit");
        }
    }
}
