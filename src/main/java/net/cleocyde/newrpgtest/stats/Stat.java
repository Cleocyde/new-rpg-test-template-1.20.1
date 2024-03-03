package net.cleocyde.newrpgtest.stats;

import net.cleocyde.newrpgtest.BrObject;

public abstract class Stat extends BrObject {

    protected float value;

    public Stat(float value) {
        this.value = value;
    }

    public float GetValue() {
        return value;
    }

    public void SetValue(float newValue) {
        value = newValue;
    }

    public abstract void ModifyMultiplier(float modifier);

    public abstract void Add(float modifier);

    public abstract void Set(float newValue);

    public void ModifyMultiplier(double modifier) {
        ModifyMultiplier((float) modifier);
    }

    public void Add(double modifier) {
        Add((float) modifier);
    }
}