package me.emiel.mlang.parse;

import me.emiel.mlang.token.Token;
import me.emiel.mlang.token.TokenType;
import me.emiel.mlang.vm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Parser {
    public static final Object NESTED_CALL = new Object();
    private final List<Token> tokens;
    private int index = 0;

    private final List<Instruction> instructions = new ArrayList<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        instructions.addAll(parseScope(tokens));
    }

    private List<Instruction> parseScope(List<Token> tokens) {
        List<Instruction> instructions = new ArrayList<>();
        ListIterator<Token> iterator = tokens.listIterator();
        while (iterator.hasNext()) {
            List<Token> statement = getStatement(iterator);
            instructions.addAll(parseStatement(statement));
        }
        return instructions;
    }

    public List<Instruction> parseStatement(List<Token> tokens) {
        List<Instruction> instructions = new ArrayList<>();

        List<List<Instruction>> args = new ArrayList<>();

        boolean isRet = false;

        ListIterator<Token> iterator = tokens.listIterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();

            List<Instruction> arg = new ArrayList<>();

            switch (token.getType()) {
                case LITERAL:
                case SYMBOL:
                case HASH:
                    arg.add(new Instruction(InstructionType.PUSH, token.getValue()));
                    break;

                case LSCOPE:
                    arg.add(new Instruction(InstructionType.PUSH, new VMFunction(parseScope(getScope(iterator)))));
                    break;

                case LPAREN:
                    List<Token> nestedStatement = getNestedStatement(iterator);
                    boolean isScope = false;

                    boolean symbolOnly = true;
                    for (Token param : nestedStatement) {
                        if (param.getType() != TokenType.SYMBOL) {
                            symbolOnly = false;
                        }
                    }

                    if (iterator.hasNext() && symbolOnly) {
                        if (iterator.next().getType() == TokenType.LSCOPE) {
                            isScope = true;
                        } else {
                            iterator.previous();
                        }
                    }

                    if (!isScope) {
                        arg.addAll(parseStatement(nestedStatement));
                    } else {
                        List<Instruction> functionInstructions = new ArrayList<>();
                        List<Token> functionBody = getScope(iterator);

                        for (Token param : nestedStatement) {
                            if (param.getType() != TokenType.SYMBOL) {
                                throw new Error("Unexpected token: " + token);
                            }

                            functionInstructions.add(new Instruction(InstructionType.POP, ((Symbol) param.getValue()).getHash()));
                        }
                        functionInstructions.addAll(parseScope(functionBody));

                        arg.add(new Instruction(InstructionType.PUSH, new VMFunction(functionInstructions)));
                    }

                    break;

                case RET:
                    if (args.size() != 0 && !isRet) {
                        throw new Error("Unexpected token: " + token);
                    }
                    isRet = true;
                    break;

                case COMMENT:
                    break;

                default:
                    throw new Error("Unexpected token: " + token);
            }

            if (!arg.isEmpty()) {
                args.add(arg);
            }
        }

        if (args.size() == 0) {
            return instructions;
        }

        for (int i = args.size() - 1; i >= 0; i--) {
            List<Instruction> arg = args.get(i);
            instructions.addAll(arg);
        }

        if (isRet) {
            instructions.add(new Instruction(InstructionType.RET, args.size()));
        } else {
            instructions.add(new Instruction(InstructionType.CALL, args.size() - 1));
        }


        return instructions;
    }

    private List<Token> getScope(ListIterator<Token> iterator) {
        List<Token> tokens = new ArrayList<>();
        int depth = 1;
        Token token;
        while (iterator.hasNext()) {
            token = iterator.next();
            if (token.getType() == TokenType.LSCOPE) {
                depth++;
            }

            if (token.getType() == TokenType.RSCOPE) {
                depth--;
                if (depth == 0) {
                    break;
                }
            }

            tokens.add(token);
        }
        return tokens;
    }

    public List<Token> getNestedStatement(ListIterator<Token> iterator) {
        List<Token> tokens = new ArrayList<>();
        int depth = 1;
        Token token;
        while (iterator.hasNext()) {
            token = iterator.next();
            if (token.getType() == TokenType.LPAREN) {
                depth++;
            }

            if (token.getType() == TokenType.RPAREN) {
                depth--;
                if (depth == 0) {
                    break;
                }
            }

            tokens.add(token);
        }
        return tokens;
    }

    public List<Token> getStatement(ListIterator<Token> iterator) {
        List<Token> tokens = new ArrayList<>();
        int depth = 1;
        Token token;
        while (iterator.hasNext()) {
            token = iterator.next();
            if (token.getType() == TokenType.LSCOPE) {
                depth++;
            }

            if (token.getType() == TokenType.RSCOPE) {
                depth--;
                if (depth == 0) {
                    break;
                }
            }

            if (depth == 1 && token.getType() == TokenType.SEMICOLON) {
                break;
            }

            tokens.add(token);
        }
        return tokens;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        return String.valueOf(instructions);
    }
}
