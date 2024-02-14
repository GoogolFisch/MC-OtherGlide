package me.googol.fisch.otherglide.entitys.client;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.entitys.custom.ThermalEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ThermalEntityRenderer extends MobEntityRenderer<ThermalEntity, ThermalEntityModel> {
    /*public TestingRenderer(EntityRendererFactory.Context context, TestingEntityModel entityModel, float f) {
        super(context, entityModel, f);
    }/**/

    public ThermalEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx,new ThermalEntityModel(), 0.F);
    }/**/

    @Override
    public Identifier getTexture(ThermalEntity entity) {
        if(entity.getIsDrawn()) {
            return new Identifier(OtherGlide.MOD_ID, "textures/entity/smoke.png");
        }else{
            return new Identifier(OtherGlide.MOD_ID, "textures/empty.png");
        }
    }
}
