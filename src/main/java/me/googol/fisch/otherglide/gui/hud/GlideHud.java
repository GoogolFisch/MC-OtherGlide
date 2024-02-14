package me.googol.fisch.otherglide.gui.hud;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.client.OtherGlideClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.system.MathUtil;

@Environment(value=EnvType.CLIENT)
public class GlideHud
        extends DrawableHelper {
    private static final Identifier SPEED_METER_TEXTURE = new Identifier(OtherGlide.MOD_ID,"textures/gui/speedometer.png");
    private final MinecraftClient client;

    public GlideHud(MinecraftClient client) {
        this.client = client;
    }

    public void render(MatrixStack matrices) {
        if (!OtherGlideClient.inGlide) {
            return;
        }
        int scaleWidth  = this.client.getWindow().getScaledWidth();
        int scaleHeight = this.client.getWindow().getScaledHeight();
        Vec3d vel = this.client.player.getVelocity();
        double playerHSpeed = Math.sqrt(vel.x * vel.x + vel.z * vel.z) * 20.0d;
        double playerSpeed = Math.sqrt(vel.x * vel.x + vel.y * vel.y + vel.z * vel.z) * 20.0d;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, SPEED_METER_TEXTURE);
        this.renderSpeedometer(matrices,scaleWidth - 32,scaleHeight / 3 - 32,playerHSpeed);
        RenderSystem.setShaderTexture(0, SPEED_METER_TEXTURE);
        this.renderSpeedometer(matrices,scaleWidth - 32,scaleHeight / 3     ,playerSpeed);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        /*for (ClientBossBar clientBossBar : this.bossBars.values()) {
            int k = scaleWidth / 2 - 91;
            int l = j;
            RenderSystem.setShaderTexture(0, SPEED_METER_TEXTURE);
            this.renderBossBar(matrices, k, l, clientBossBar);
            Text text = clientBossBar.getName();
            int m = this.client.textRenderer.getWidth(text);
            int n = scaleWidth / 2 - m / 2;
            int o = l - 9;
            this.client.textRenderer.drawWithShadow(matrices, text, (float)n, (float)o, 0xFFFFFF);
            if ((j += 10 + this.client.textRenderer.fontHeight) < this.client.getWindow().getScaledHeight() / 3) continue;
            break;
        }*/
    }

    private void renderSpeedometer(MatrixStack matrices, int x, int y, double speed) {
        String speedStr = String.format("%.2f m/s",speed);
        int textOffset = this.client.textRenderer.getWidth(speedStr);
        //GlideHud.drawTexture(matrices, x, y, 0, 0,16,16,16,12 * 16);
        GlideHud.drawTexture(matrices, x, y, 0, (Math.min((int)speed / 5,11) * 16),16,16,16,12 * 16);
        this.client.textRenderer.drawWithShadow(matrices,speedStr,x - 16 - textOffset,y,0xffffff);
    }

    /*private void renderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar, int width, int height) {
        GlideHud.drawTexture(matrices, x, y, 0, bossBar.getColor().ordinal() * 5 * 2 + height, width, 5);
        if (bossBar.getStyle() != BossBar.Style.PROGRESS) {
            RenderSystem.enableBlend();
            GlideHud.drawTexture(matrices, x, y, 0, 80 + (bossBar.getStyle().ordinal() - 1) * 5 * 2 + height, width, 5);
            RenderSystem.disableBlend();
        }
    }*/

}


