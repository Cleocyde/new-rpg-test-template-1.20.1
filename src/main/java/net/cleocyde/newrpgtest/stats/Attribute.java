package net.cleocyde.newrpgtest.stats;

public class Attribute extends Stat{

    private float multiplier;

    public Attribute(float value){
        super(value);
        multiplier = 1;
    }
    @Override
    public void ModifyMultiplier(float modifier){
        multiplier += modifier;
    }

    @Override
    public void Add(float modifier){
        value += modifier;
    }
    @Override
    public float GetValue(){
        return value * multiplier;
    }
    @Override
    public void Set(float newValue){
        value = newValue;
    }
    @Override
    public String toString(){
        return GetValue() + "";
    }
}

