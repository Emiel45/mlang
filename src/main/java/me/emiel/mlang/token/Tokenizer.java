package me.emiel.mlang.token;

import me.emiel.mlang.vm.Hash;
import me.emiel.mlang.vm.Symbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final List<Token> tokens = new ArrayList<Token>();

    private final BufferedReader reader;

    public Tokenizer(BufferedReader reader) {
        this.reader = reader;
    }

    private int peek() throws IOException {
        reader.mark(2);
        int c = reader.read();
        reader.reset();
        return c;
    }

    private int peek(int offset) throws IOException {
        reader.mark(2 + offset);
        reader.skip(offset);
        int c = reader.read();
        reader.reset();
        return c;
    }

    private void skip() throws IOException {
        reader.skip(1);
    }

    private void skip(int n) throws IOException {
        reader.skip(2);
    }

    private void add(Token token) {
        tokens.add(token);
    }

    public void tokenize() throws IOException {
        int p;
        while ((p = peek()) != -1) {
            if (isWhitespace(p)) {
                skip();
                continue;
            }

            if (p == '/' && peek(1) == '*') {
                skip(2);
                add(readComment());
                continue;
            }


            if (Character.isDigit(p)) {
                add(readNumber());
                continue;
            }

            switch (p) {

                case '#':
                    skip();
                    add(readHash());
                    break;

                case ';':
                    skip();
                    add(new Token(TokenType.SEMICOLON));
                    break;

                case '"':
                    skip();
                    add(readString());
                    break;

                case '(':
                    skip();
                    add(new Token(TokenType.LPAREN));
                    break;

                case ')':
                    skip();
                    add(new Token(TokenType.RPAREN));
                    break;

                case '{':
                    skip();
                    add(new Token(TokenType.LSCOPE));
                    break;

                case '}':
                    skip();
                    add(new Token(TokenType.RSCOPE));
                    break;

                default:
                    add(readSymbol());
                    break;
            }
        }
    }

    private Token readComment() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while (true) {
            if (peek() == '*' && peek(1) == '/') {
                skip(2);
                break;
            }
            sb.append((char) peek());
            skip();
        }
        return new Token(TokenType.COMMENT, sb.toString());
    }

    private Token readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = peek()) != '"') {
            if (c == '\\') {
                skip();
                c = peek();
                switch (c) {
                    case '"':
                        break;
                    default:
                        throw new Error("Invalid escape character: " + (char) c);
                }
                skip();
                continue;
            }
            sb.append((char) c);
            skip();
        }
        skip();
        return new Token(TokenType.LITERAL, sb.toString());
    }

    private boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean isValidSymbolChar(int c) {
        return !(isWhitespace(c) || c == '(' || c == ')' || c == ';');
    }

    private String readSymbolText() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while (isValidSymbolChar(c = peek())) {
            sb.append((char) c);
            skip();
        }
        return sb.toString();
    }

    private Token readSymbol() throws IOException {
        String text = readSymbolText();

        if (text.equals("ret")) {
            return new Token(TokenType.RET);
        } else if (text.equals("true")) {
            return new Token(TokenType.LITERAL, true);
        } else if (text.equals("false")) {
            return new Token(TokenType.LITERAL, false);
        } else if (text.equals("null")) {
            return new Token(TokenType.LITERAL, null);
        }

        return new Token(TokenType.SYMBOL, Symbol.from(text));
    }

    private Token readHash() throws IOException {
        return new Token(TokenType.HASH, Hash.from(readSymbolText()));
    }

    private Token readNumber() throws IOException {
        String s = readSymbolText();
        Object value;
        if (s.contains(".")) {
            value = Double.parseDouble(s);
        } else {
            value = Integer.parseInt(s);
        }
        return new Token(TokenType.LITERAL, value);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return String.valueOf(tokens);
    }
}
