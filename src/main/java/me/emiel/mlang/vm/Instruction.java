package me.emiel.mlang.vm;

public class Instruction {
    private InstructionType type;
    private Object value;

    public Instruction(InstructionType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Instruction(InstructionType type) {
        this.type = type;
    }

    public InstructionType getType() {
        return type;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        if (value != null) {
            sb.append(':');
            sb.append(value);
        }
        return sb.toString();
    }
}
