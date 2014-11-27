package me.emiel.mlang.api;

import me.emiel.mlang.vm.Function;
import me.emiel.mlang.vm.Obj;
import me.emiel.mlang.vm.Scope;
import me.emiel.mlang.vm.VM;

public class MapApi {
    public static void register(Scope scope) {
        // numberPrototype.set("+", (Function) NumberApi::number_plus);
        scope.set("Map", (Function) MapApi::create);
    }

    private static int create(VM vm) {

        return 0;
    }
}
