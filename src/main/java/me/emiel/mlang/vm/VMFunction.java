package me.emiel.mlang.vm;

import java.util.ArrayList;
import java.util.List;

public class VMFunction implements Function {
    private final List<Instruction> instructions;

    private Obj proto;

    public VMFunction(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Instruction get(int index) {
        return instructions.get(index);
    }

    public int size() {
        return instructions.size();
    }

    @Override
    public int execute(VM vm) {
        for (int i = 0; i < instructions.size(); i++) {
            int ret = vm.execute(instructions.get(i));
            if (ret != - 1) {
                return ret;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(instructions);
    }
}
