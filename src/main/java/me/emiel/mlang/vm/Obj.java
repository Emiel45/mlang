package me.emiel.mlang.vm;

import java.util.HashMap;
import java.util.Map;

public class Obj {
    // todo move to per vm
    public static final Obj objProto = new Obj();
    static {
        objProto.proto = objProto;
    }

    protected Obj proto = objProto;
    protected Map<Hash, Object> values = new HashMap<>();

    public void set(Hash key, Object value) {
        values.put(key, value);
    }

    public void set(Symbol key, Object object) {
        values.put(key.getHash(), object);
    }

    public void set(String key, Object object) {
        values.put(Hash.from(key), object);
    }

    public Object get(Hash key) {
        return values.get(key);
    }

    public Object get(Symbol symbol) {
        return get(symbol.getHash());
    }

    public Object get(String key) {
        return get(Hash.from(key));
    }

    public Obj getProto() {
        return proto;
    }

    public void setProto(Obj proto) {
        this.proto = proto;
    }

    @Override
    public String toString() {
        return String.valueOf(values);
    }

}
