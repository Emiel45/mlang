package me.emiel.mlang.vm;

public class Scope extends Obj {
    private Scope parent;

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Object get(Hash key) {
        Object value = super.get(key);
        if (value != null) {
            return value;
        }
        if (parent != null) {
            return parent.get(key);
        }
        return null;
    }

    public Scope getParent() {
        return parent;
    }
}
