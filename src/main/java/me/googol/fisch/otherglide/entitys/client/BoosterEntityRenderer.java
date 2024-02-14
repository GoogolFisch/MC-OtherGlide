package me.googol.fisch.otherglide.entitys.client;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.entitys.custom.BoosterEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BoosterEntityRenderer extends MobEntityRenderer<BoosterEntity, BoosterEntityModel> {
    /*public TestingRenderer(EntityRendererFactory.Context context, TestingEntityModel entityModel, float f) {
        super(context, entityModel, f);
    }/**/

    public BoosterEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx,new BoosterEntityModel(), 0.F);
    }/**/

    @Override
    public Identifier getTexture(BoosterEntity entity) {
        if(entity.getIsDrawn()) {
            return new Identifier(OtherGlide.MOD_ID, "textures/entity/booster.png");
        }else{
            return new Identifier(OtherGlide.MOD_ID, "textures/empty.png");
        }
    }
}
