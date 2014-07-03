package vswe.stevesvehicles.upgrade;


import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public class EffectType {
    private Class<? extends BaseEffect> clazz;
    private Object[] params;


    public EffectType(Class<? extends BaseEffect> clazz, Object[] params) {
        this.clazz = clazz;
        this.params = params;
    }

    public Class<? extends BaseEffect> getClazz() {
        return clazz;
    }

    public Object[] getParams() {
        return params;
    }
}
