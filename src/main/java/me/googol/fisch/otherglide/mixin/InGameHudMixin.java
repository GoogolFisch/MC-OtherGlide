package me.googol.fisch.otherglide.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.googol.fisch.otherglide.client.OtherGlideClient;
import me.googol.fisch.otherglide.gui.hud.GlideHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initMixin(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci){
        OtherGlideClient.glideHud = new GlideHud(client);
    }/**/

    @Inject(at = @At("HEAD"),method = "render")
    public void renderMixin(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        if(OtherGlideClient.inGlide || true){
            this.client.getProfiler().push("glide");
            RenderSystem.enableBlend();
            OtherGlideClient.glideHud.render(matrices);
            RenderSystem.disableBlend();
            this.client.getProfiler().pop();
        }
    }
}
