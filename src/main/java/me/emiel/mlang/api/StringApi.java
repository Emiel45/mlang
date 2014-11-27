package me.emiel.mlang.api;

import me.emiel.mlang.vm.Function;
import me.emiel.mlang.vm.Obj;
import me.emiel.mlang.vm.Scope;
import me.emiel.mlang.vm.VM;

public class StringApi {
    public static void register(Scope scope) {
        Obj stringProto = new Obj();
        stringProto.set("+", (Function) StringApi::plus);
        stringProto.set("length", (Function) StringApi::length);
        scope.set("String", stringProto);
    }

    private static int plus(VM vm) {
        String s = (String) vm.pop();
        vm.push(s + vm.pop());
        return 1;
    }

    private static int length(VM vm) {
        String s = (String) vm.pop();
        vm.push(s.length());
        return 1;
    }
}
