package net.cleocyde.newrpgtest.stats;

import net.cleocyde.newrpgtest.BrObject;
import net.cleocyde.newrpgtest.EntityData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class Status extends BrObject {
    public final float DEFAULT_HEALTH = 60f;
    public final int MAXIMUM_LEVEL = 200;

    private float previousLevelToNext;

    public int level;

    public Attribute Vitality;
    public Attribute Agility;
    public Attribute Strength;
    public Attribute Intelligence;
    public Attribute Luck;

    public Resource HP;
    public Resource EXP;
    public EntityData entityData;

    public float MaximumHealth(){
        return DEFAULT_HEALTH + (level * 5f) + Vitality.GetValue();
    }
    public float LevelToNext(){
        if(level == 1) return 110;
        else if (level < 180)
            return this.previousLevelToNext + (level * 8 + (level * level) * 10);
        else if (level < 199)
            return this.previousLevelToNext + (level * 15 + (level * level) * 20);
        else if (level < MAXIMUM_LEVEL)
            return this.previousLevelToNext + (level * 75 + (level * level * level) * 50);
        else return 0;
    }
    public Status(EntityData entityData){
        this.entityData = entityData;
        this.level = 1;
        this.previousLevelToNext = 110;
        this.Vitality = new Attribute(10);
        this.Agility = new Attribute(0);
        this.Strength = new Attribute(0);
        this.Intelligence = new Attribute(0);
        this.Luck = new Attribute(0);

        this.HP = new Resource(MaximumHealth(), this::MaximumHealth);
        this.EXP = new Resource(0, this::LevelToNext, this::HandleLevelUp);
        print(HP);
    }

    public void HandleLevelUp(float addedValue, float maximumEXP){
        if(level == MAXIMUM_LEVEL){
            this.EXP.Set(0);
            return;
        }

        this.previousLevelToNext = LevelToNext();
        this.level++;
        this.EXP.Set(0);
        float overflow = addedValue - maximumEXP;
        if(overflow > 0){
            this.EXP.Add(overflow);
        }
    }


}