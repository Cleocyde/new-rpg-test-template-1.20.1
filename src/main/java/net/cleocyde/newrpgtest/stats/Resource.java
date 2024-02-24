package net.cleocyde.newrpgtest.stats;

import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Resource extends Stat{

    ///(modified value, maximumValue)
    private RunnableFloat2 OnHitMaximum;

    ///(previous value, new value, maximum value)
    protected ArrayList<RunnableFloat3> onValueChanged;

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
    public Resource(float defaultValue, Supplier<Float> defaultMaximumValue, RunnableFloat2 onHitMaximum, RunnableFloat3... valueChangedCallbacks){
        super(defaultValue);
        maximumValue = defaultMaximumValue;
        OnHitMaximum = onHitMaximum;

        for(var function : valueChangedCallbacks)
        {
            if(onValueChanged == null) onValueChanged = new ArrayList<RunnableFloat3>();
            onValueChanged.add(function);
        }
    }

    public void AttachOnChangedFunction(RunnableFloat3 callback){
        if(onValueChanged == null) onValueChanged = new ArrayList<RunnableFloat3>();
        onValueChanged.add(callback);
    }


    void OnChangedCheck(float previousValue, float changedValue){
        if(previousValue == changedValue) return;

        if(onValueChanged != null){
            for(var func : onValueChanged){
                func.run(previousValue, changedValue, maximumValue.get());
            }
        }
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
        var previousValue = GetValue();

        if(newValue > maximumValue.get()) value = maximumValue.get();
        else if(newValue < 0) value = 0;
        else value = newValue;

        OnChangedCheck(previousValue, GetValue());
    }

    @Override
    public void Add(float modifier){
        var previousValue = GetValue();

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

        OnChangedCheck(previousValue, GetValue());
    }
    @Override
    public String toString(){
        return GetValue() + "/" + maximumValue.get();
    }
}