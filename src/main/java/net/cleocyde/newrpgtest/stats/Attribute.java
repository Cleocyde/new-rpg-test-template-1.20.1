package net.cleocyde.newrpgtest.stats;

import net.cleocyde.newrpgtest.utils.StateSaverAndLoader;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;

public class Attribute extends Stat {

    private float multiplier;

    ///PARAM (previous value , new changed value)
    protected ArrayList<RunnableFloat2> onValueChanged;

    public Attribute(float value) {
        super(value);
        multiplier = 1;
    }

    void OnChangedCheck(float previousValue, float changedValue) {
        if (previousValue == changedValue) return;

        if (onValueChanged != null) {
            for (var func : onValueChanged) {
                func.run(previousValue, changedValue);
            }
        }
    }

    public void AttachOnChangedFunction(RunnableFloat2 callback) {
        if (onValueChanged == null) onValueChanged = new ArrayList<RunnableFloat2>();
        onValueChanged.add(callback);
    }

    public Attribute(float value, RunnableFloat2... callbacks) {
        super(value);

        for (var onChange : callbacks) {
            if (onValueChanged == null) onValueChanged = new ArrayList<RunnableFloat2>();
            onValueChanged.add(onChange);
        }
    }

    @Override
    public void ModifyMultiplier(float modifier) {
        var previousValue = GetValue();
        multiplier += modifier;
        OnChangedCheck(previousValue, GetValue());
    }

    @Override
    public void Add(float modifier) {
        var previousValue = GetValue();
        value += modifier;
        OnChangedCheck(previousValue, GetValue());
    }

    @Override
    public float GetValue() {
        return value * multiplier;
    }

    @Override
    public void Set(float newValue) {
        var previousValue = GetValue();
        value = newValue;
        OnChangedCheck(previousValue, GetValue());
    }

    @Override
    public String toString() {
        return GetValue() + "";
    }


}