package me.googol.fisch.otherglide.mixin.entity;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.networking.ModMessages;
import me.googol.fisch.otherglide.networking.packets.s2c.PlayerGlideS2CPacket;
import me.googol.fisch.otherglide.networking.packets.s2c.PlayerStuckS2CPacket;
import me.googol.fisch.otherglide.util.PlayerDataSaver;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
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
public abstract class ServerPlayerEntityMixin extends LivingEntityMixin implements PlayerDataSaver {
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    public boolean inGlide = false;
    public boolean isStuck = false;
    private static final TrackedData<Boolean> TRACK_IN_GLIDE;
    private static final TrackedData<Boolean> TRACK_IS_STUCK;
    private static final TrackedData<NbtCompound> TRACK_STUCK_POS;
    private double boostingMax = 0.0d;
    private double thermalHeight = 0.0d;
    public Vec3d stuckPos = Vec3d.ZERO;
    public Vec3d returnPos = Vec3d.ZERO;

    @Shadow public abstract void startFallFlying();


    @Shadow public abstract boolean isCreative();

    @Shadow protected abstract void increaseRidingMotionStats(double dx, double dy, double dz);

    @Shadow public abstract String getEntityName();

    @Shadow public abstract Text getName();

    @Inject(at=@At("TAIL"),method = "initDataTracker")
    public void initDataTrackerInject(CallbackInfo ci){
        this.dataTracker.startTracking(TRACK_IN_GLIDE, inGlide);
        this.dataTracker.startTracking(TRACK_IS_STUCK, isStuck);
        NbtCompound nbtStuckPos = new NbtCompound();
        nbtStuckPos.putDouble("x",0d);
        nbtStuckPos.putDouble("y",0d);
        nbtStuckPos.putDouble("z",0d);
        this.dataTracker.startTracking(TRACK_STUCK_POS, nbtStuckPos);
    }

    @Inject(at=@At("HEAD"),method = "tick")
    public void tickInject(CallbackInfo ci){
        ServerWorld world = (ServerWorld) this.world;
        if(inGlide) {
            boolean stopGlide = this.getCommandTags().contains("glideGameFinish");
            if(stopGlide){
                this.getCommandTags().remove("glideGameFinish");
                this.finishGlideMode();
                this.dataTracker.set(TRACK_IN_GLIDE,inGlide);
            }
            if(!this.isFallFlying() && !this.isCreative()){
                this.setHealth(20.0F);
                this.getCommandTags().add("glideGameReset");
            }
            //this.collidedSoftlyMixin(new Vec3d(0.0,0.0,0.0));
        }else{
            boolean startGlide = this.getCommandTags().contains("glideGameStart");
            if(startGlide){
                this.getCommandTags().remove("glideGameStart");
                this.startGlideMode();
                this.dataTracker.set(TRACK_IN_GLIDE,inGlide);
            }
        }
        if(isStuck){
            boolean stopStuck = this.getCommandTags().contains("playerUnStuck");
            if(stopStuck){
                this.getCommandTags().remove("playerUnStuck");
                this.stopPlayerStuck();
                this.dataTracker.set(TRACK_IS_STUCK,isStuck);
            }
        }else{
            boolean startStuck = this.getCommandTags().contains("playerGoStuck");
            if(startStuck){
                this.getCommandTags().remove("playerGoStuck");
                this.startPlayerStuck();
                this.dataTracker.set(TRACK_IS_STUCK,isStuck);
            }
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
                this.thermalHeight = -512.0d;
            }
            this.setVelocity(velX, velY, velZ);
        }else{
            this.boostingMax = 0.0d;
            this.thermalHeight = -512.0d;
        }
    }
    @Inject(at = @At("HEAD"),method = "checkFallFlying", cancellable = true)
    public void checkFallFlyingMixin(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack;
        if (!this.isOnGround() && this.inGlide) {
            this.startFallFlying();
            cir.setReturnValue(true);
            return;
        }
    }
    @Override
    public boolean afterGlideTick(){
        if(this.hasVehicle()){
            this.finishGlideMode();
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, false);
            return true;
        }
        if(this.inGlide && !this.onGround && !this.hasVehicle()) {
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, true);
            return true;
        }
        return false;
    }

    public void collidedSoftlyMixin(Vec3d adjustedMovement, CallbackInfoReturnable<Boolean> cir){
        if(this.inGlide && !this.isCreative()){
            this.getCommandTags().add("glideGameHit");
        }
    }

    @Inject(at = @At("TAIL"),method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        /*nbt.putFloat("BoostX", this.getBoost(BOOST_X));
        nbt.putFloat("BoostY", this.getBoost(BOOST_Y));
        nbt.putFloat("BoostZ", this.getBoost(BOOST_Z));

        nbt.putFloat("ScaleX", this.getScale(SCALE_X));
        nbt.putFloat("ScaleY", this.getScale(SCALE_Y));
        nbt.putFloat("ScaleZ", this.getScale(SCALE_Z));/**/


        nbt.putBoolean("inGlide", this.inGlide);
        nbt.putBoolean("isStuck", this.isStuck);
        NbtCompound nbtStuckPos = nbt.getCompound("StuckPos");
        nbtStuckPos.putDouble("x",this.stuckPos.x);
        nbtStuckPos.putDouble("y",this.stuckPos.y);
        nbtStuckPos.putDouble("z",this.stuckPos.z);
        //nbt.put("Rotation", this.toNbtList(this.pointYaw, this.pointPitch));
    }

    @Inject(at = @At("TAIL"),method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        /*if (nbt.contains("BoostX", NbtCompound.FLOAT_TYPE)) {this.setBoost(BOOST_X,nbt.getFloat("BoostX"));isPirMove = true;}
        if (nbt.contains("BoostY", NbtCompound.FLOAT_TYPE)) {this.setBoost(BOOST_Y,nbt.getFloat("BoostY"));isPirMove = true;}
        if (nbt.contains("BoostZ", NbtCompound.FLOAT_TYPE)) {this.setBoost(BOOST_Z,nbt.getFloat("BoostZ"));isPirMove = true;}
        if (nbt.contains("ScaleX", NbtCompound.FLOAT_TYPE)) {this.setScale(SCALE_X,nbt.getFloat("ScaleX"));}
        if (nbt.contains("ScaleY", NbtCompound.FLOAT_TYPE)) {this.setScale(SCALE_Y,nbt.getFloat("ScaleY"));}
        if (nbt.contains("ScaleZ", NbtCompound.FLOAT_TYPE)) {this.setScale(SCALE_Z,nbt.getFloat("ScaleZ"));}/**/

        if (nbt.contains("inGlide", NbtCompound.BYTE_TYPE)) {this.inGlide = nbt.getBoolean("inGlide");OtherGlide.LOGGER.info("New inGlide! {}",this.inGlide);}
        if (nbt.contains("isStuck", NbtCompound.BYTE_TYPE)) {this.isStuck = nbt.getBoolean("isStuck");OtherGlide.LOGGER.info("New isStuck! {}",this.isStuck);}
        if(nbt.contains("StuckPos",NbtCompound.COMPOUND_TYPE)) {
            NbtCompound nbtStuckPos = nbt.getCompound("StuckPos");
            if (nbtStuckPos.contains("x", NbtCompound.DOUBLE_TYPE)) {
                x = nbtStuckPos.getDouble("x");
            }
            if (nbtStuckPos.contains("y", NbtCompound.DOUBLE_TYPE)) {
                y = nbtStuckPos.getDouble("y");
            }
            if (nbtStuckPos.contains("z", NbtCompound.DOUBLE_TYPE)) {
                z = nbtStuckPos.getDouble("z");
            }
            this.stuckPos = new Vec3d(x, y, z);
            OtherGlide.LOGGER.info("Got new Stuck Pos {} {} {}",x,y,z);
        }
    }

    public boolean startGlideMode(){
        boolean prevState = inGlide;
        inGlide = true;
        String name = this.getName().getString();
        ServerPlayNetworking.send(
                (ServerPlayerEntity) world.getPlayers().stream().filter(x -> name.contains(x.getName().getString())).toList().get(0),
                ModMessages.PLAYER_GLIDE_ID, PlayerGlideS2CPacket.genBuffer(inGlide)
        );
        this.startFallFlying();
        return prevState != inGlide;
    }
    public boolean finishGlideMode(){
        boolean prevState = inGlide;
        inGlide = false;
        String name = this.getName().getString();
        ServerPlayNetworking.send(
                (ServerPlayerEntity) world.getPlayers().stream().filter(x -> name.contains(x.getName().getString())).toList().get(0),
                ModMessages.PLAYER_GLIDE_ID, PlayerGlideS2CPacket.genBuffer(inGlide)
        );
        return prevState != inGlide;
    }

    public boolean startPlayerStuck(){
        boolean prevState = isStuck;
        isStuck = true;
        stuckPos = this.getPos();
        NbtCompound nbtStuckPos = this.dataTracker.get(TRACK_STUCK_POS);
        nbtStuckPos.putDouble("x",stuckPos.x);
        nbtStuckPos.putDouble("y",stuckPos.y);
        nbtStuckPos.putDouble("z",stuckPos.z);
        String name = this.getName().getString();

        ServerPlayNetworking.send(
                (ServerPlayerEntity) world.getPlayers().stream().filter(x -> name.contains(x.getName().getString())).toList().get(0),
                ModMessages.PLAYER_STUCK_ID, PlayerStuckS2CPacket.genBuffer(isStuck,this.stuckPos)
        );
        return prevState != isStuck;
    }
    public boolean stopPlayerStuck(){
        boolean prevState = isStuck;
        isStuck = false;
        String name = this.getName().getString();
        ServerPlayNetworking.send(
                (ServerPlayerEntity) world.getPlayers().stream().filter(x -> name.contains(x.getName().getString())).toList().get(0),
                ModMessages.PLAYER_STUCK_ID, PlayerStuckS2CPacket.genBuffer(isStuck,this.stuckPos)
        );
        return prevState != isStuck;
    }

    public void playerBoostMax(double vel){boostingMax = Math.max(boostingMax,vel);}
    public void playerThermalHeight(double height){thermalHeight = Math.max(thermalHeight,height);}

    public void storePlayerGoto(Vec3d pos){
        this.returnPos = pos;
    }
    public boolean playerGotoStore() {this.teleport(this.returnPos.x,this.returnPos.y,this.returnPos.z);return true;}
    static {
        TRACK_IN_GLIDE = DataTracker.registerData(ServerPlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
        TRACK_IS_STUCK = DataTracker.registerData(ServerPlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
        TRACK_STUCK_POS = DataTracker.registerData(ServerPlayerEntityMixin.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    }
}
