package net.cleocyde.newrpgtest.stats;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Supplier;

public class Resource extends Stat{

    ///(modified value, maximumValue)
    private RunnableFloat2 OnHitMaximum;
    private Supplier<Float> maximumValue;
    public Resource(float defaultValue, Supplier<Float> defaultMaximumValue){
        super(defaultValue);
        maximumValue = defaultMaximumValue;
        OnHitMaximum = null;
    }
    public Resource(float defaultValue, Supplier<Float> defaultMaximumValue, RunnableFloat2 onHitMaximum){
        super(defaultValue);
        maximumValue = defaultMaximumValue;
        OnHitMaximum = onHitMaximum;
    }

    public float GetMaximumValue(){
        return maximumValue.get();
    }
    public void SetMaximumValue(Supplier<Float> value){
        maximumValue = value;
    }

    @Override
    public void ModifyMultiplier(float modifier){
        System.out.println("don't multiply resources?");
        return;
    }
    @Override
    public void Set(float newValue){
        value = newValue;
    }
    @Override
    public void Add(float modifier){
        float newValue = value + modifier;
        float maxValue = maximumValue.get();
        if(newValue > maxValue) {
            value = maxValue;
        }
        if(newValue >= maxValue && OnHitMaximum != null){
            OnHitMaximum.run(modifier, maxValue);
        }
        else if(newValue < 0) value = 0;
        else value += modifier;
    }
    @Override
    public String toString(){
        return GetValue() + "/" + maximumValue.get();
    }
}
