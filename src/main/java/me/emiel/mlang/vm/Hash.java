package me.emiel.mlang.vm;

import me.emiel.mlang.api.HashApi;

import java.util.HashMap;
import java.util.Map;

public class Hash extends Obj {
    private static final Map<String, Hash> hashes = new HashMap<>();
    public static final Obj hashProto = HashApi.get();

    public static Hash from(String name) {
        Hash hash = hashes.get(name);
        if (hash == null) {
            hash = new Hash(name);
            hashes.put(name, hash);
        }
        return hash;
    }

    private final String name;
    private final int hash;

    private Hash(String name) {
        this.proto = hashProto;
        this.name = name;
        this.hash = name.hashCode();
    }

    public String getName() {
        return name;
    }

    public int getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hash hash1 = (Hash) o;

        if (hash != hash1.hash) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('#');
        sb.append(name);
        /*
        sb.append(':');
        sb.append(hash);
        */
        return sb.toString();
    }
}
