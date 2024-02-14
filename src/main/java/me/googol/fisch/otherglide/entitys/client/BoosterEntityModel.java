package me.googol.fisch.otherglide.entitys.client;

import com.google.common.collect.ImmutableList;
import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.client.OtherGlideClient;
import me.googol.fisch.otherglide.entitys.custom.BoosterEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

public class BoosterEntityModel extends EntityModel<BoosterEntity> {
    private final ModelData modelData;
    private final ModelPart body;
    private float scaleX;
    private float scaleY;
    private float scaleZ;

    public BoosterEntityModel() {
        modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                "body",
                ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                ModelTransform.of(0F, 0F, 0F,0F, 0F, 0F)
        );
        modelPartData.addChild(//+y
                "py",
                ModelPartBuilder.create().uv(9,42).cuboid( -9.4F,  -5F, 9.985F, 16.0F, 1.0F, 6.0F),
                ModelTransform.of(0F, 0F, 0F,0.3054F, 0.0F, 0F)
        );
        modelPartData.addChild(//-y
                "ny",
                ModelPartBuilder.create().uv(9,42).cuboid( -9.4F, -5F, 9.985F, 16.0F, 1.0F, 6.0F),
                ModelTransform.of(0F, 0F, 0F,0.3054F, 0.0F, 3.1416F)
        );
        modelPartData.addChild(//-x
                "nx",
                ModelPartBuilder.create().uv(9,42).cuboid(-9.4F,  -5F, 9.985F, 16.0F, 1.0F, 6.0F),
                ModelTransform.of(0F, 0F, 0F,0.3054F, 0.0F,  -1.5708F)
        );
        modelPartData.addChild(//+x
                "px",
                ModelPartBuilder.create().uv(9,42).cuboid(-9.4F,  -5F, 9.985F, 16.0F, 1.0F, 6.0F),
                ModelTransform.of(0F, 0F, 0F,0.3054F, 0.0F,  1.5708F)
        );
        body = this.modelData.getRoot().createPart(64,64);
        body.setPivot(0,0,0);
        scaleX = 1.0F;
        scaleY = 1.0F;
        scaleZ = 1.0F;
    }
    public BoosterEntityModel(ModelPart modelPart){
        //this.base = modelPart.getChild(EntityModelPartNames.CUBE);
        this();
    }
    //private final ModelPart base;
    @Override
    public void setAngles(BoosterEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        body.pitch = entity.getPitch();
        body.yaw = entity.getYaw();
        scaleX = entity.getScaleH();
        scaleY = entity.getScaleH();
        scaleZ = entity.getScaleL();
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
