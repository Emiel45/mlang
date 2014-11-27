package me.emiel.mlang;

import me.emiel.mlang.api.GlobalApi;
import me.emiel.mlang.parse.Parser;
import me.emiel.mlang.token.Tokenizer;
import me.emiel.mlang.vm.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().run();

    }

    private void run() throws Exception {
        FileInputStream fis = new FileInputStream("test.m");

        Tokenizer tokenizer = new Tokenizer(new BufferedReader(new InputStreamReader(fis)));
        tokenizer.tokenize();
        System.out.println(tokenizer);

        Parser parser = new Parser(tokenizer.getTokens());
        parser.parse();
        System.out.println(parser);

        VM vm = new VM();
        Scope global = vm.getGlobalScope();

        GlobalApi.register(global);

        int retCount = vm.execute(new VMFunction(parser.getInstructions()));
        Scope scope = vm.getScope();

        vm.push(Symbol.from("print"));
        vm.call(retCount);
    }
}
