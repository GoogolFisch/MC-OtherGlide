package me.googol.fisch.otherglide.entitys.custom;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ThermalEntity extends PathAwareEntity {
    private static final TrackedData<Boolean> IS_DRAWN;
    private static final TrackedData<Float> SCALE_X;
    private static final TrackedData<Float> SCALE_Y;
    private static final TrackedData<Float> SCALE_Z;
    private static final TrackedData<Float> BOOST_X;
    private static final TrackedData<Float> BOOST_Y;
    private static final TrackedData<Float> BOOST_Z;
    private static final TrackedData<Float> THERMAL_HEIGHT;
    private float boostingX;
    private float boostingY;
    private float boostingZ;
    private float thermalHeight;
    private float scalingX;
    private float scalingY;
    private float scalingZ;
    private int age;

    /*public TestingEntity(World world, double x, double y, double z) {
        this(ModEntity.TESTING_ENTITY, world);
        this.setPosition(x, y, z);
    }/**/

    public ThermalEntity(EntityType<? extends PathAwareEntity> entityEntityType, World world) {
        super(entityEntityType, world);
        this.age = 0;
        this.boostingX = 0.0F;
        this.boostingY = 0.0F;
        this.boostingZ = 0.0F;
        this.thermalHeight = 0.0F;
        this.scalingX = 2.5F;
        this.scalingY = 2.5F;
        this.scalingZ = 2.5F;
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
        this.getDataTracker().startTracking(BOOST_X, 0.0F);
        this.getDataTracker().startTracking(BOOST_Y, 0.0F);
        this.getDataTracker().startTracking(BOOST_Z, 0.0F);
        this.getDataTracker().startTracking(THERMAL_HEIGHT, -512.0F);
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
            float boostX,boostY,boostZ,thermHeight,scaleX,scaleY,scaleZ;
            boostX = getBoost(BOOST_X);
            boostY = getBoost(BOOST_Y);
            boostZ = getBoost(BOOST_Z);
            thermHeight = getBoost(THERMAL_HEIGHT);
            scaleX = getScale(SCALE_X);
            scaleY = getScale(SCALE_Y);
            scaleZ = getScale(SCALE_Z);
            ax = p.getX(); ay = p.getY() + 1.0; az = p.getZ();
            bx = this.getX(); by = this.getY(); bz = this.getZ();

            if(!(bx - scaleX < ax && ax < bx + scaleX))continue;
            if(!(by - scaleY < ay && ay < by + scaleY))continue;
            if(!(bz - scaleZ < az && az < bz + scaleZ))continue;
            p.setVelocity(new Vec3d(p.getVelocity().x + boostX, Math.min(p.getVelocity().y + boostY,Math.max(2.0,p.getVelocity().y)), p.getVelocity().z + boostZ));
            ((PlayerDataSaver) p).playerThermalHeight(thermHeight);
        }
    }
    public void kill() {
        this.remove(RemovalReason.KILLED);
        this.emitGameEvent(GameEvent.ENTITY_DIE);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsDraw", this.getIsDrawn());


        nbt.putFloat("BoostX", this.boostingX);
        nbt.putFloat("BoostY", this.boostingY);
        nbt.putFloat("BoostZ", this.boostingZ);
        nbt.putFloat("ThermalHeight", this.thermalHeight);

        nbt.putFloat("ScaleX", this.scalingX);
        nbt.putFloat("ScaleY", this.scalingY);
        nbt.putFloat("ScaleZ", this.scalingZ);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("IsDraw", NbtCompound.BYTE_TYPE)) {this.setIsDrawn(nbt.getBoolean("IsDraw"));}


        if (nbt.contains("BoostX", NbtCompound.FLOAT_TYPE)) {this.boostingX = nbt.getFloat("BoostX");setBoost(BOOST_X,this.boostingX);}
        if (nbt.contains("BoostY", NbtCompound.FLOAT_TYPE)) {this.boostingY = nbt.getFloat("BoostY");setBoost(BOOST_Y,this.boostingY);}
        if (nbt.contains("BoostZ", NbtCompound.FLOAT_TYPE)) {this.boostingZ = nbt.getFloat("BoostZ");setBoost(BOOST_Z,this.boostingZ);}
        if (nbt.contains("ThermalHeight", NbtCompound.FLOAT_TYPE)) {this.thermalHeight = nbt.getFloat("ThermalHeight");setBoost(THERMAL_HEIGHT,this.thermalHeight);}

        if (nbt.contains("ScaleX", NbtCompound.FLOAT_TYPE)) {this.scalingX = nbt.getFloat("ScaleX");setScale(SCALE_X,this.scalingX);}
        if (nbt.contains("ScaleY", NbtCompound.FLOAT_TYPE)) {this.scalingY = nbt.getFloat("ScaleY");setScale(SCALE_Y,this.scalingY);}
        if (nbt.contains("ScaleZ", NbtCompound.FLOAT_TYPE)) {this.scalingZ = nbt.getFloat("ScaleZ");setScale(SCALE_Z,this.scalingZ);}
    }

    private void setBoost(TrackedData<Float> type,float test) {this.getDataTracker().set(type, test);}
    public float getBoost(TrackedData<Float> type) {return this.getDataTracker().get(type);}

    public float getScale(TrackedData<Float> type) {return this.getDataTracker().get(type);}
    public void setScale(TrackedData<Float> type,float test) {this.getDataTracker().set(type, test);}
    public float getScaleX() {return this.getDataTracker().get(SCALE_X);}
    public float getScaleY() {return this.getDataTracker().get(SCALE_Y);}
    public float getScaleZ() {return this.getDataTracker().get(SCALE_Z);}

    public void setIsDrawn(boolean test) {this.getDataTracker().set(IS_DRAWN, test);this.setInvisible(!test);}
    public boolean getIsDrawn() {return this.getDataTracker().get(IS_DRAWN);}

    public boolean shouldRender(double distance) {return distance < 1000;}

    public boolean damage(DamageSource source, float amount) {
        return true;
    }


    public void pushAwayFrom(Entity entity){}
    protected void pushAway(Entity entity){}
    public boolean isPushable(){return false;}
    public boolean canHit() {return false;}
    public boolean cannotDespawn(){return true;}
    public boolean isFireImmune(){return true;}
    public PistonBehavior getPistonBehavior() {return PistonBehavior.IGNORE;}
    protected MoveEffect getMoveEffect() {return MoveEffect.NONE;}
    static {
        IS_DRAWN = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        SCALE_X = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
        SCALE_Y = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
        SCALE_Z = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_X = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_Y = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOOST_Z = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
        THERMAL_HEIGHT = DataTracker.registerData(ThermalEntity.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
