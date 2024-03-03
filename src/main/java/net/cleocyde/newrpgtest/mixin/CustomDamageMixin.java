package net.cleocyde.newrpgtest.mixin;

import net.cleocyde.newrpgtest.EntityData;
import net.cleocyde.newrpgtest.NewRPGTest;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class CustomDamageMixin {
    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private void onApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        // Get the player entity
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Get the CustomHealthSystem instance for the player
        EntityData entityData = NewRPGTest.getEntityDatas().get(player);
        if (entityData != null) {
            if (source == player.getDamageSources().outOfWorld()) {
                return;
            } else {

                // Apply the damage to the custom health system
                entityData.status.HP.Add(-amount);
                entityData.status.updateActionBar(player);
                System.out.println(entityData.status.HP);
                System.out.println(player.getHealth());
                // Cancel the original damage
                ci.cancel();

                // Apply null amount of damage for knockback
                player.damage(player.getDamageSources().generic(), 0f);
            }
        }
    }

}