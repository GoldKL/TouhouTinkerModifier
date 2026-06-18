package com.goldkl.touhoutinkermodifier.helper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DamageModifier {
    float baseamount;
    float add;
    float percent;
    float multiply;
    float fixed;

    public float getamount(){
        return (baseamount + add) * (1 + percent) * multiply + fixed;
    }
    public DamageModifier(float baseamount) {
        this.baseamount = baseamount;
        this.add = 0;
        this.percent = 0;
        this.multiply = 1;
        this.fixed = 0;
    }
    public DamageModifier(DamageModifier other) {
        this.baseamount = other.baseamount;
        this.add = other.add;
        this.percent = other.percent;
        this.multiply = other.multiply;
        this.fixed = other.fixed;
    }
    public void addAdd(float add) {
        this.add += add;
    }
    public void addPercent(float percent) {
        this.percent += percent;
    }
    public void addMultiply(float multiply) {
        this.multiply *= multiply;
    }
    public void addFixed(float fixed) {
        this.fixed += fixed;
    }
}
