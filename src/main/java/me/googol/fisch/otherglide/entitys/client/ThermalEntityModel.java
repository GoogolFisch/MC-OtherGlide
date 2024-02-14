package me.googol.fisch.otherglide.entitys.client;

import com.google.common.collect.ImmutableList;
import me.googol.fisch.otherglide.entitys.custom.ThermalEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class ThermalEntityModel extends EntityModel<ThermalEntity> {
    private final ModelData modelData;
    private final ModelPart body;
    private float scaleX;
    private float scaleY;
    private float scaleZ;

    public ThermalEntityModel() {
        modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                "body",
                ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                ModelTransform.of(0F, 0F, 0F,0F, 0F, 0F)
        );
        body = this.modelData.getRoot().createPart(4,4);
        body.setPivot(0,0,0);
        scaleX = 1.0F;
        scaleY = 1.0F;
        scaleZ = 1.0F;
    }
    public ThermalEntityModel(ModelPart modelPart){
        //this.base = modelPart.getChild(EntityModelPartNames.CUBE);
        this();
    }
    //private final ModelPart base;
    @Override
    public void setAngles(ThermalEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        body.pitch = entity.getPitch();
        body.yaw = entity.getYaw();
        scaleX = entity.getScaleX() * 2.0F;
        scaleY = entity.getScaleY() * 2.0F;
        scaleZ = entity.getScaleZ() * 2.0F;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.scale(scaleX,scaleY,scaleZ);
        ImmutableList.of(body).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, 255, overlay, 1f, 1f, 1f, 1f);
        });
    }

    /*public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
        return new TexturedModelData(modelData,new TextureDimensions(4,4));
    }/**/
}
