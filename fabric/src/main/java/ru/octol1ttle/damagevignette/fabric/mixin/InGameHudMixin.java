package ru.octol1ttle.damagevignette.fabric.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.octol1ttle.damagevignette.common.DamageVignetteEvents;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderVignetteOverlay", at = @At(value = "HEAD"),cancellable = true)
    private void onVignetteRender(DrawContext context, @Nullable Entity entity, CallbackInfo ci) {
        DamageVignetteEvents.RENDER_HUD.renderHud(context);
    }
}
