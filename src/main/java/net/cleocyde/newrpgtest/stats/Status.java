package net.cleocyde.newrpgtest.stats;

import net.cleocyde.newrpgtest.BrObject;
import net.cleocyde.newrpgtest.EntityData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class Status extends BrObject {
    public final float DEFAULT_HEALTH = 60f;
    public final int MAXIMUM_LEVEL = 200;

    private float previousLevelToNext;
    public static int level;

    public static Attribute Vitality;
    public static Attribute Agility;
    public static Attribute Strength;
    public static Attribute Intelligence;
    public static Attribute Luck;

    public static Resource HP;
    public static Resource EXP;
    public static EntityData entityData;

    public float MaximumHealth() {
        return DEFAULT_HEALTH + (level * 5f) + Vitality.GetValue();
    }

    public float LevelToNext() {
        if (level == 1) return 110;
        else if (level < 180)
            return previousLevelToNext + (level * 8 + (level * level) * 10);
        else if (level < 199)
            return previousLevelToNext + (level * 15 + (level * level) * 20);
        else if (level < MAXIMUM_LEVEL)
            return previousLevelToNext + (level * 75 + (level * level * level) * 50);
        else return 0;
    }

    public Status(EntityData entityData) {
        this.entityData = entityData;

        level = 1;
        previousLevelToNext = 110;
        Vitality = new Attribute(0);
        Agility = new Attribute(0);
        Strength = new Attribute(0);
        Intelligence = new Attribute(0);
        Luck = new Attribute(0);

        HP = new Resource(MaximumHealth(), this::MaximumHealth);
        HP.AttachOnChangedFunction(this::UpdateMinecraftHealthBar);
        EXP = new Resource(0, this::LevelToNext, this::HandleLevelUp, this::UpdateMinecraftEXPBar);

    }


    public void HandleLevelUp(float addedValue, float maximumEXP) {
        if (level == MAXIMUM_LEVEL) {
            EXP.Set(0);
            return;
        }

        previousLevelToNext = LevelToNext();
        level++;
        entityData.status.HP.Add(5);
        EXP.Set(0);
        float overflow = addedValue - maximumEXP;
        if (overflow > 0) {
            EXP.Add(overflow);
        }
    }

    public void RefreshHealthBar(PlayerEntity player) {
        var value = this.HP.GetValue();
        var maxValue = this.HP.GetMaximumValue();

        if (value > maxValue) {
            value = maxValue;
        } else if (value <= 0) {
            player.setHealth(0f);
        } else if (value < maxValue * 0.05f) {
            player.setHealth(0.5f);
        } else {
            player.setHealth(20.0f * value / maxValue);
        }
    }

    public void UpdateMinecraftHealthBar(float previousValue, float newValue, float maximumValue) {
        //does shit.

    }

    public void updateActionBar(PlayerEntity player) {
        Text message = Text.literal("HP: " + this.HP);
        player.sendMessage(message, true);
    }

    public void UpdateMinecraftEXPBar(float previousValue, float newValue, float maximumValue) {
        var player = entityData.entity;
        player.experienceProgress = EXP.value / LevelToNext();
        player.experienceLevel = this.level;

        player.addExperienceLevels(0);
        //does shit to update
    }

}