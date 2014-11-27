package me.emiel.mlang.api;

import me.emiel.mlang.vm.*;

public class GlobalApi {
    private static final Object _else = new Object();

    public static void register(Scope scope) {
        scope.set("var", (Function) GlobalApi::var);
        scope.set("print", (Function) GlobalApi::print);
        scope.set("if", (Function) GlobalApi::_if);
        scope.set("else", _else);

        scope.set("Obj", Obj.objProto);
        scope.set("Hash", Hash.hashProto);

        NumberApi.register(scope);
        StringApi.register(scope);
        MapApi.register(scope);
    }

    private static int proto(VM vm) {
        Obj obj = (Obj) vm.pop();
        Object value = vm.pop();

        return 0;
    }

    public static int var(VM vm) {
        Hash key = (Hash) vm.pop();
        Object value = vm.pop();

        Scope scope = vm.getScope().getParent();
        scope.set(key, value);

        //vm.push(value);
        return 0;
    }

    public static int _if(VM vm) {
        Object condition = vm.pop();
        Function trueFunction = (Function) vm.pop();
        Function falseFunction = null;
        if (vm.argsAvailable()) {
            Object obj = vm.pop();
            if (obj == _else) {
                obj = vm.pop();
            }
            falseFunction = (Function) obj;
        }

        if (condition == null || condition == Boolean.FALSE || condition == new Integer(0)) {
            if (falseFunction != null) {
                return vm.callFunction(0, falseFunction);
            }
        } else {
            return vm.callFunction(0, trueFunction);
        }
        return 0;
    }

    private static int print(VM vm) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (vm.argsAvailable()) {
            if (!first) {
                sb.append(' ');
            } else {
                first = false;
            }
            sb.append(vm.pop());
        }
        System.out.println(sb.toString());
        return 0;
    }
}
