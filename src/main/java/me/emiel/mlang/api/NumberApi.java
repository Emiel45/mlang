package me.emiel.mlang.api;

import me.emiel.mlang.vm.Function;
import me.emiel.mlang.vm.Obj;
import me.emiel.mlang.vm.Scope;
import me.emiel.mlang.vm.VM;

public class NumberApi {
    public static void register(Scope scope) {
        Obj numberProto = new Obj();
        numberProto.set("<", (Function) NumberApi::lt);
        numberProto.set("<=", (Function) NumberApi::lte);
        numberProto.set("-", (Function) NumberApi::minus);
        numberProto.set("+", (Function) NumberApi::plus);
        numberProto.set("*", (Function) NumberApi::mul);
        scope.set("Number", numberProto);
    }

    private static int mul(VM vm) {
        Number a = (Number) vm.pop();
        Number b = (Number) vm.pop();

        if ((a instanceof Double) || (b instanceof Double)) {
            vm.push(a.doubleValue() * b.doubleValue());
        } else {
            vm.push(a.intValue() * b.intValue());
        }

        return 1;
    }

    private static int minus(VM vm) {
        Number a = (Number) vm.pop();
        Number b = (Number) vm.pop();

        if ((a instanceof Double) || (b instanceof Double)) {
            vm.push(a.doubleValue() - b.doubleValue());
        } else {
            vm.push(a.intValue() - b.intValue());
        }

        return 1;
    }

    private static int lte(VM vm) {
        Number a = (Number) vm.pop();
        Number b = (Number) vm.pop();

        if ((a instanceof Double) || (b instanceof Double)) {
            vm.push(a.doubleValue() <= b.doubleValue());
        } else {
            vm.push(a.intValue() <= b.intValue());
        }
        return 1;
    }

    private static int lt(VM vm) {
        Number a = (Number) vm.pop();
        Number b = (Number) vm.pop();

        if ((a instanceof Double) || (b instanceof Double)) {
            vm.push(a.doubleValue() < b.doubleValue());
        } else {
            vm.push(a.intValue() < b.intValue());
        }
        return 1;
    }

    private static int plus(VM vm) {
        Number a = (Number) vm.pop();
        Number b = (Number) vm.pop();

        if ((a instanceof Double) || (b instanceof Double)) {
            vm.push(a.doubleValue() + b.doubleValue());
        } else {
            vm.push(a.intValue() + b.intValue());
        }

        return 1;
    }
}
