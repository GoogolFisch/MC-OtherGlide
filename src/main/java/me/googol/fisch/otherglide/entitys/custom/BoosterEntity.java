package me.googol.fisch.otherglide.entitys.custom;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.util.PlayerDataSaver;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.EnvironmentStateTweaker;

public class BoosterEntity extends PathAwareEntity {
    private static final TrackedData<Boolean> IS_DRAWN;
    private static final TrackedData<Float> SCALE_X;
    private static final TrackedData<Float> SCALE_Y;
    private static final TrackedData<Float> SCALE_Z;
    private static final TrackedData<Float> SCALE_L;
    private static final TrackedData<Float> SCALE_H;
    private static final TrackedData<Float> BOOST_X;
    private static final TrackedData<Float> BOOST_Y;
    private static final TrackedData<Float> BOOST_Z;
    private static final TrackedData<Float> BOOST_MAX;
    private float boostingX;
    private float boostingY;
    private float boostingZ;
    private float boostingMax;
    private float scalingX;
    private float scalingY;
    private float scalingZ;
    private float scalingH;
    private float scalingL;
    private double pointYaw;
    private double pointPitch;
    private int age;

    /*public TestingEntity(World world, double x, double y, double z) {
        this(ModEntity.TESTING_ENTITY, world);
        this.setPosition(x, y, z);
    }/**/

    public BoosterEntity(EntityType<? extends PathAwareEntity> entityEntityType, World world) {
        super(entityEntityType, world);
        this.age = 0;
        this.boostingX = 0.0F;
        this.boostingY = 0.0F;
        this.boostingZ = 0.0F;
        this.boostingMax = 0.0F;
        this.pointYaw   = 0.0F;
        this.pointPitch = 0.0F;
        this.scalingX = 2.5F;
        this.scalingY = 2.5F;
        this.scalingZ = 2.5F;
        this.scalingH = 5.0F;
        this.scalingL = 5.0F;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return (new DefaultAttributeContainer.Builder())
                .add(EntityAttributes.GENERIC_MAX_HEALTH,20F)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,0.0F);
    }/**/

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(SCALE_X, 1.0F);
        this.getDataTracker().startTracking(SCALE_Y, 1.0F);
        this.getDataTracker().startTracking(SCALE_Z, 1.0F);
        this.getDataTracker().startTracking(SCALE_H, 1.0F);
        this.getDataTracker().startTracking(SCALE_L, 1.0F);
        this.getDataTracker().startTracking(BOOST_X, 0.0F);
        this.getDataTracker().startTracking(BOOST_Y, 0.0F);
        this.getDataTracker().startTracking(BOOST_Z, 0.0F);
        this.getDataTracker().startTracking(BOOST_MAX, 0.0F);
        this.getDataTracker().startTracking(IS_DRAWN, true);
    }
    public void tick() {
        ++this.age;
        this.setVelocity(0,0,0);
        this.setVelocityClient(0,0,0);
        super.tick();
        this.setVelocity(0,0,0);
        this.setVelocityClient(0,0,0);

        for (PlayerEntity p: this.world.getPlayers()) {
            if(!p.isFallFlying())continue;
            double ax,ay,az,bx,by,bz;
            float boostX,boostY,boostZ,boostMax,scaleX,scaleY,scaleZ;
            boostX = getBoost(BOOST_X);
            boostY = getBoost(BOOST_Y);
            boostZ = getBoost(BOOST_Z);
            boostMax = getBoost(BOOST_MAX);
            scaleX = getScale(SCALE_X);
            scaleY = getScale(SCALE_Y);
            scaleZ = getScale(SCALE_Z);
            ax = p.getX(); ay = p.getY(); az = p.getZ();
            bx = this.getX(); by = this.getY() + 1.0; bz = this.getZ();

            if(!(bx - scaleX < ax && ax < bx + scaleX))continue;
            if(!(by - scaleY < ay && ay < by + scaleY))continue;
            if(!(bz - scaleZ < az && az < bz + scaleZ))continue;
            Vec3d playerVel = p.getVelocity();
            playerVel = playerVel.add(boostX, boostY, boostZ);
            p.setVelocity(playerVel);
            ((PlayerDataSaver) p).playerBoostMax(boostMax);
            /*double hSpeed = Math.sqrt(playerVel.x * playerVel.x + playerVel.z * playerVel.z);
            if(hSpeed < boostMax){
                float yawAngle = p.getYaw() * MathHelper.RADIANS_PER_DEGREE;
                p.setVelocity(playerVel.add(-MathHelper.sin(yawAngle),0.0,MathHelper.cos(yawAngle)));
            }/**/
        }
        //OtherGlide.LOGGER.info("tick Yaw: {} Pitch: {}",pointYaw,pointPitch);
        /*this.headYaw = (float) this.pointYaw;
        this.prevPitch = (float) this.pointPitch;
        this.prevYaw = (float) this.pointYaw;

        this.prevHeadYaw = (float) this.pointYaw;
        this.bodyYaw = (float) this.pointYaw;
        this.prevBodyYaw = (float) this.pointYaw;*/
        //this.serverPitch = this.pointPitch;
        //this.serverYaw = this.pointYaw;
        //this.serverHeadYaw = this.pointYaw;
    }
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.emitGameEvent(GameEvent.ENTITY_DIE);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsDraw", this.getIsDrawn());

        /*nbt.putFloat("BoostX", this.getBoost(BOOST_X));
        nbt.putFloat("BoostY", this.getBoost(BOOST_Y));
        nbt.putFloat("BoostZ", this.getBoost(BOOST_Z));

        nbt.putFloat("ScaleX", this.getScale(SCALE_X));
        nbt.putFloat("ScaleY", this.getScale(SCALE_Y));
        nbt.putFloat("ScaleZ", this.getScale(SCALE_Z));/**/

        nbt.putFloat("BoostX", this.boostingX);
        nbt.putFloat("BoostY", this.boostingY);
        nbt.putFloat("BoostZ", this.boostingZ);
        nbt.putFloat("BoostMax", this.boostingMax);

        nbt.putFloat("ScaleX", this.scalingX);
        nbt.putFloat("ScaleY", this.scalingY);
        nbt.putFloat("ScaleZ", this.scalingZ);
        nbt.putFloat("ScaleH", this.scalingH);
        nbt.putFloat("ScaleL", this.scalingL);
        //nbt.put("Rotation", this.toNbtList(this.pointYaw, this.pointPitch));
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        boolean isPirMove = false;
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("IsDraw", NbtCompound.BYTE_TYPE)) {this.setIsDrawn(nbt.getBoolean("IsDraw"));}

        /*if (nbt.contains("BoostX", NbtCompound.FLOAT_TYPE)) {this.setBoost(BOOST_X,nbt.getFloat("BoostX"));isPirMove = true;}
        if (nbt.contains("BoostY", NbtCompound.FLOAT_TYPE)) {this.setBoost(BOOST_Y,nbt.getFloat("BoostY"));isPirMove = true;}
        if (nbt.contains("BoostZ", NbtCompound.FLOAT_TYPE)) {this.setBoost(BOOST_Z,nbt.getFloat("BoostZ"));isPirMove = true;}
        if (nbt.contains("ScaleX", NbtCompound.FLOAT_TYPE)) {this.setScale(SCALE_X,nbt.getFloat("ScaleX"));}
        if (nbt.contains("ScaleY", NbtCompound.FLOAT_TYPE)) {this.setScale(SCALE_Y,nbt.getFloat("ScaleY"));}
        if (nbt.contains("ScaleZ", NbtCompound.FLOAT_TYPE)) {this.setScale(SCALE_Z,nbt.getFloat("ScaleZ"));}/**/

        if (nbt.contains("BoostX", NbtCompound.FLOAT_TYPE)) {this.boostingX = nbt.getFloat("BoostX");setBoost(BOOST_X,this.boostingX);isPirMove = true;}
        if (nbt.contains("BoostY", NbtCompound.FLOAT_TYPE)) {this.boostingY = nbt.getFloat("BoostY");setBoost(BOOST_Y,this.boostingY);isPirMove = true;}
        if (nbt.contains("BoostZ", NbtCompound.FLOAT_TYPE)) {this.boostingZ = nbt.getFloat("BoostZ");setBoost(BOOST_Z,this.boostingZ);isPirMove = true;}
        if (nbt.contains("BoostMax", NbtCompound.FLOAT_TYPE)) {this.boostingMax = nbt.getFloat("BoostMax");setBoost(BOOST_MAX,this.boostingMax);isPirMove = true;}

        if (nbt.contains("ScaleX", NbtCompound.FLOAT_TYPE)) {this.scalingX = nbt.getFloat("ScaleX");setScale(SCALE_X,this.scalingX);}
        if (nbt.contains("ScaleY", NbtCompound.FLOAT_TYPE)) {this.scalingY = nbt.getFloat("ScaleY");setScale(SCALE_Y,this.scalingY);}
        if (nbt.contains("ScaleZ", NbtCompound.FLOAT_TYPE)) {this.scalingZ = nbt.getFloat("ScaleZ");setScale(SCALE_Z,this.scalingZ);}
        if (nbt.contains("ScaleZ", NbtCompound.FLOAT_TYPE)) {this.scalingH = nbt.getFloat("ScaleH");setScale(SCALE_H,this.scalingH);}
        if (nbt.contains("ScaleZ", NbtCompound.FLOAT_TYPE)) {this.scalingL = nbt.getFloat("ScaleL");setScale(SCALE_L,this.scalingL);}
        if(isPirMove) {this.setPointingDirection();}
    }
    public float getBoost(TrackedData<Float> type) {return this.getDataTracker().get(type);}
    public void setBoost(TrackedData<Float> type,float test) {this.getDataTracker().set(type, test);}
    public float getScale(TrackedData<Float> type) {return this.getDataTracker().get(type);}
    public void setScale(TrackedData<Float> type,float test) {this.getDataTracker().set(type, test);}
    public float getScaleH() {return this.getDataTracker().get(SCALE_H);}
    public float getScaleL() {return this.getDataTracker().get(SCALE_L);}

    public void setIsDrawn(boolean test) {this.getDataTracker().set(IS_DRAWN, test);this.setInvisible(!test);}
    public boolean getIsDrawn() {return this.getDataTracker().get(IS_DRAWN);}

    public boolean shouldRender(double distance) {return distance < 1000;}

    public boolean damage(DamageSource source, float amount) {
        return true;
    }

    private void setPointingDirection(){
        /*double yaw   = MathHelper.atan2(this.getBoost(BOOST_Z),this.getBoost(BOOST_X));
        double pitch = MathHelper.atan2(this.getBoost(BOOST_Y),0);/**/
        float length = MathHelper.sqrt(this.boostingX * this.boostingX + this.boostingY * this.boostingY + this.boostingZ * this.boostingZ);
        this.pointYaw   = MathHelper.wrapDegrees(-MathHelper.atan2(-this.boostingZ,this.boostingX) * MathHelper.DEGREES_PER_RADIAN - 90.0F);
        this.pointPitch = MathHelper.wrapDegrees(MathHelper.atan2(this.boostingY,length) * MathHelper.DEGREES_PER_RADIAN);
        this.setHeadYaw((float) this.pointYaw);
        this.prevPitch = (float) this.pointPitch;
        this.prevYaw = (float) this.pointYaw;

        this.prevHeadYaw = this.headYaw;
        this.bodyYaw = this.headYaw;
        this.prevBodyYaw = this.bodyYaw;
        this.setBoundingBox(new Box(
                this.getX() - scalingX,this.getY() - scalingY,this.getZ() - scalingZ,
                this.getX() + scalingX,this.getY() + scalingY,this.getZ() + scalingZ
        ));

        //OtherGlide.LOGGER.info("Yaw: {} Pitch: {} with X: {} Y: {} Z: {}",this.pointYaw,this.pointPitch,boosting_X,boosting_Y,boosting_Z);
    }



    public void pushAwayFrom(Entity entity){}
    protected void pushAway(Entity entity){}
    public boolean isPushable(){return false;}
    public boolean canHit() {return false;}
    public boolean cannotDespawn(){return true;}
    public boolean isFireImmune(){return true;}
    public PistonBehavior getPistonBehavior() {return PistonBehavior.IGNORE;}
    protected Entity.MoveEffect getMoveEffect() {return MoveEffect.NONE;}
    static {
        IS_DRAWN = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        SCALE_X = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        SCALE_Y = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        SCALE_Z = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        SCALE_L = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        SCALE_H = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_X = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_Y = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_Z = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_MAX = DataTracker.registerData(BoosterEntity.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
