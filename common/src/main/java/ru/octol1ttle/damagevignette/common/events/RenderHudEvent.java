package ru.octol1ttle.damagevignette.common.events;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;
import ru.octol1ttle.damagevignette.common.DamageVignetteCommon;
import ru.octol1ttle.damagevignette.common.api.Vignette;
import ru.octol1ttle.damagevignette.common.config.VignetteSettings;
import ru.octol1ttle.damagevignette.common.util.FloatColor;

public class RenderHudEvent {
    private static final Identifier VIGNETTE_TEXTURE = Identifier.of("damagevignette", "textures/misc/vignette.png");

    public void renderHud(DrawContext context) {
        FloatColor color = computeVignetteColor();
        if (color == null) {
            return;
        }

        renderVignetteOverlay(context, VIGNETTE_TEXTURE, color);
    }

    private void renderVignetteOverlay(DrawContext context, Identifier texture, FloatColor color) {
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.ZERO, DstFactor.ONE_MINUS_SRC_COLOR, SrcFactor.ONE, DstFactor.ZERO);

      context.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
      context.drawTexture(texture, 0, 0, -90, 0.0F, 0.0F, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
      
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

    private @Nullable FloatColor computeVignetteColor() {
        VignetteSettings activeSettings = null;
        float maxStrength = 0.0f;
        for (Vignette vignette : DamageVignetteCommon.vignettes) {
            VignetteSettings settings = vignette.settingsSupplier().get();
            float strength = settings.scale(vignette.getStrength(MinecraftClient.getInstance().player));

            if (strength > maxStrength) {
                activeSettings = settings;
                maxStrength = strength;
            }
        }

        if (activeSettings == null) {
            return null;
        }

        return new FloatColor(
                activeSettings.color.getRed() / 255.0f,
                activeSettings.color.getGreen() / 255.0f,
                activeSettings.color.getBlue() / 255.0f,
                maxStrength
        ).invert();
    }
}
