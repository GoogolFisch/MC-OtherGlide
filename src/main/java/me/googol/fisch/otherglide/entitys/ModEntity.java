package me.googol.fisch.otherglide.entitys;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.entitys.client.BoosterEntityRenderer;
import me.googol.fisch.otherglide.entitys.client.ThermalEntityRenderer;
import me.googol.fisch.otherglide.entitys.custom.BoosterEntity;
///import me.googol.fisch.otherglide.entitys.custom.SuperSnowballEntity;
import me.googol.fisch.otherglide.entitys.custom.ThermalEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntity {
    public static final EntityType<BoosterEntity> BOOSTER_ENTITY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(OtherGlide.MOD_ID, "booster"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BoosterEntity::new)
                    .dimensions(EntityDimensions.changing(4f,4f)).build());
    public static final EntityType<ThermalEntity> SMOKING_ENTITY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(OtherGlide.MOD_ID, "smoking"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ThermalEntity::new)
                    .dimensions(EntityDimensions.changing(4f,4f)).build());
    /*public static final EntityType<Entity> SUPER_BALL_ENTITY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(OtherGlide.MOD_ID, "super_ball"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SuperSnowballEntity::new)
                    .dimensions(EntityDimensions.changing(0.25f,0.25f)).build());/**/
    public static void registerModEntity() {
        FabricDefaultAttributeRegistry.register(BOOSTER_ENTITY, BoosterEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(SMOKING_ENTITY, ThermalEntity.setAttributes());
    }
    public static void registerModClientEntity() {
        EntityRendererRegistry.register(BOOSTER_ENTITY, BoosterEntityRenderer::new);
        EntityRendererRegistry.register(SMOKING_ENTITY, ThermalEntityRenderer::new);
        //EntityRendererRegistry.register(SUPER_BALL_ENTITY, SuperSnowballEntityRenderer::new);

        //EntityRendererFactory<TestingEntity>
        /*EntityRendererRegistry.register(BOOSTER_ENTITY, (context) -> {
            return new BoosterEntityRenderer((EntityRendererFactory.Context)context);
        });/**/
        //EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, TestingEntityModel::getTexturedModelData);
    }
}
