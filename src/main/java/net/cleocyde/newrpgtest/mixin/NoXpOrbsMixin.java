package net.cleocyde.newrpgtest.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class NoXpOrbsMixin {
    @Inject(method = "dropXp", at = @At("HEAD"), cancellable = true)
    private void noXpOrbs(CallbackInfo ci) {
        ci.cancel();
    }
}
