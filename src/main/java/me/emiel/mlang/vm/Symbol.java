package me.emiel.mlang.vm;

import java.util.HashMap;
import java.util.Map;

public class Symbol {
    private static final Map<String, Symbol> symbols = new HashMap<>();

    public static Symbol from(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol == null) {
            symbol = new Symbol(Hash.from(name));
            symbols.put(name, symbol);
        }
        return symbol;
    }

    private final Hash hash;

    private Symbol(Hash hash) {
        this.hash = hash;
    }

    public Hash getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "$" + hash.getName();
    }
}
