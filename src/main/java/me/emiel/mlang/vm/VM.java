package me.emiel.mlang.vm;

import java.util.Stack;

public class VM {
    private final Stack<Object> stack = new Stack<>();

    private final Stack<Integer> stackLimitStack = new Stack<>();
    private int stackLimit = 0;

    private final Stack<Scope> scopeStack = new Stack<>();
    private Scope globalScope = new Scope(null);
    private Scope scope = new Scope(globalScope);

    public int execute(Function function) {
        return function.execute(this);
    }

    protected int execute(Instruction instruction) {
        // System.out.println(this + " " + instruction + "\t\t" + scope + "\t\t" + stack);
        switch (instruction.getType()) {
            case POP:
                pop((Hash) instruction.getValue());
                break;

            case PUSH:
                push(instruction.getValue());
                break;

            case CALL:
                int argCount = (int) instruction.getValue();
                call(argCount);
                break;

            case RET:
                return (int) instruction.getValue();

            default:
                throw new UnsupportedOperationException();
        }
        return -1;
    }

    private void pop(Hash value) {
        scope.set(value, pop());
    }

    public void call(int argCount) {
        Object object = pop();

        if (object instanceof Function) {
            callFunction(argCount, (Function) object);
        }

        else if (object instanceof Obj) {
            Obj obj = (Obj) object;
            invokeObject(obj, obj.getProto(), argCount);
        }

        else if (object instanceof String) {
            invokeObject(object, (Obj) scope.get("String"), argCount);
        }

        else if (object instanceof Number) {
            invokeObject(object, (Obj) scope.get("Number"), argCount);
        }

        else {
            throw new Error("Calling " + object);
        }
    }

    private void invokeObject(Object object, Obj prototype, int argCount) {
        if (argCount < 1) {
            throw new Error("Invoke requires at least 1 argCount");
        }

        Symbol functionSymbol = (Symbol) popRaw();
        Function function = (Function) prototype.get(functionSymbol);

        push(object);

        callFunction(argCount - 1, function);
    }


    public int callFunction(int argCount, Function function) {
        pushScope(argCount);
        int retCount = function.execute(this);

        while (stack.size() > stackLimit + retCount) {
            pop();
        }


        for (int i = 0; i < retCount; i++) {
            int index = stack.size() - 1 - i;
            Object o = stack.get(index);
            if (o instanceof Symbol) {
                stack.set(index, scope.get(((Symbol) o).getHash()));
            }
        }

        popScope();
        return retCount;
    }

    public void pushScope(int argCount) {
        stackLimitStack.push(stackLimit);
        stackLimit = stack.size() - argCount;

        scopeStack.push(scope);
        scope = new Scope(scope);
    }

    public void popScope() {
        stackLimit = stackLimitStack.pop();
        scope = scopeStack.pop();
    }

    public Object popRaw() {
        if (stack.size() < stackLimit) {
            throw new StackUnderflowException();
        }
        return stack.pop();
    }

    public Object pop() {
        Object value = popRaw();
        if (value instanceof Symbol) {
            value = scope.get((Symbol) value);
        }
        return value;
    }

    public boolean argsAvailable() {
        return stack.size() > stackLimit;
    }

    public void push(Object value) {
        stack.push(value);
    }

    public int stackSize() {
        return stack.size();
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "stackSize: " + stackSize();
    }
}
