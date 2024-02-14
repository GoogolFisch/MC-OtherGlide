package me.googol.fisch.otherglide.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow @Final public int defaultMaxHealth;


    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean isFallFlying();

    public boolean afterGlideTick(){return false;}

    @Shadow public int stuckArrowTimer;

    @Inject(at = @At("TAIL"),method = "tickFallFlying",cancellable = true)
    public void tickFallFlyingInject(CallbackInfo ci){if(this.afterGlideTick())ci.cancel();}

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


}