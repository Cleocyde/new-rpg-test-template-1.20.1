package net.cleocyde.newrpgtest.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class NoXpDropMixin {
    @Redirect(method = "dropXp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean preventXpDrop(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule) {
        return false;
    }
}