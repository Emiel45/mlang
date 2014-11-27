package me.emiel.mlang.api;

import me.emiel.mlang.vm.Function;
import me.emiel.mlang.vm.Obj;

public class HashApi {
    public static Obj get() {
        Obj hashProto = new Obj();
        hashProto.set("=", (Function) GlobalApi::var);
        return hashProto;
    }
}
