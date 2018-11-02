import LEXER.Lexeme;
import LEXER.Lexer;
import LEXER.Token;

import java.util.*;

/**
 * Created by Андрюха on 11.10.2018.
 */
public class Parser {

    List<Token> tokenList = new ArrayList<>();
    int position = 0;
    Map<String, Lexeme> initMap = new HashMap<>(); // <Переменная, Лексема(тип)>
    Map<String, Boolean> varValue = new HashMap<>(); // <Переменная, значение>
    List<Token> poliz = new ArrayList<>();
    Stack<Token> stack = new Stack<>();
    Map<String, Integer> tableOfValues = new HashMap<>();
    String currentVar;
    Token currentToken;
    boolean initFlg = false;
    boolean rsquFlg = false;

    private Lexeme getLexeme() {

        return tokenList.get(position).getLexeme();
    }

    private String getLexemeValue()
    {
        return tokenList.get(position).getValue();
    }

    private Lexeme getLexemeDec()
    {
        return tokenList.get(position - 1).getLexeme();
    }

    private String getLexemeValueDec()
    {
        return tokenList.get(position - 1).getValue();
    }

    private Lexeme getLexemeInc() { return tokenList.get(position + 1).getLexeme(); }

    private String getLexemeValueInc() { return tokenList.get(position + 1).getValue(); }

    public Map<String, String> getTableOfValues()
    {
        return getTableOfValues();
    }

    private boolean equalityOfSQU()
    {
        boolean equalityOfSQU = false;
        int rCount = 0;
        int lCount = 0;
        for (Token token : tokenList)
        {
            if (token.getLexeme() == Lexeme.L_R_SQU) { lCount++; }
            if (token.getLexeme() == Lexeme.R_R_SQU) { rCount++; }
        }

        if (rCount == lCount)
        {
            equalityOfSQU = true;
        }
        else
        {
            System.err.println("Ошибка: неравное число скобок!"+position+"d");
            System.exit(11);
        }

        int rCountF = 0;
        int lCountF = 0;
        for (Token token : tokenList)
        {
            if (token.getLexeme() == Lexeme.L_F_SQU) { lCountF++; }
            if (token.getLexeme() == Lexeme.R_F_SQU) { rCountF++; }
        }

        if (rCountF == lCountF)
        {
            equalityOfSQU = true;
        }
        else
        {
            System.err.println("Ошибка: неравное число скобок!"+position + "l " + lCountF + "; r " + rCountF);
            System.exit(11);
        }
        return equalityOfSQU;
    }

    private int polizPriority(String op)
    {
        int priority;
        switch (op)
        {
            case "*":
                priority = 3;
                break;
            case "/":
                priority = 3;
                break;
            case "^":
                priority = 3;
                break;
            case "%":
                priority = 3;
                break;
            case "+":
                priority = 2;
                break;
            case "-":
                priority = 2;
                break;
            case "(":
                priority = 1;
                break;
            case "=":
                priority = 1;
                break;
            case ")":
                priority = 0;
                break;
            default:
                priority = -1;
                System.err.println("Ошибка в символе " + op);
                System.exit(8);
        }
        return priority;
    }

    private void toStack(Lexeme lexeme)
    {
        if (lexeme == Lexeme.ASSIGN_OP || lexeme == Lexeme.OP)
        {
            if (stack.empty() || (polizPriority(stack.peek().getValue())) <
                    polizPriority(tokenList.get(position).getValue()))
            {
                stack.push(tokenList.get(position));
            }
            else
            {
                if (!stack.empty() && (polizPriority(stack.peek().getValue()) >=
                        polizPriority(tokenList.get(position).getValue())))
                {
                    while (!stack.empty() && (polizPriority(stack.peek().getValue()) >=
                            polizPriority(tokenList.get(position).getValue())))
                    {
                        poliz.add(stack.pop());
                    }
                    stack.push(tokenList.get(position));
                }
            }
        }
        else
        {
            if (lexeme == Lexeme.L_R_SQU)
            {
                stack.push(tokenList.get(position));
                if (position < tokenList.size() - 1 && (getLexemeInc() == Lexeme.R_R_SQU || getLexemeDec() == Lexeme.R_R_SQU ||
                        getLexemeInc() == Lexeme.OP))
                {
                    System.err.println("Синтаксическая ошибка на позиции: " + position);
                    System.exit(12);
                }
            }
            if (lexeme == Lexeme.R_R_SQU)
            {
                while(!stack.empty() && polizPriority(stack.peek().getValue())  != 1)
                {
                    poliz.add(stack.pop());
                }
                stack.pop();
                if (position < tokenList.size() - 1 && (getLexemeDec() == Lexeme.L_R_SQU || getLexemeInc() == Lexeme.L_R_SQU ||
                        getLexemeDec() == Lexeme.OP))
                {
                    System.err.println("Синтаксическая ошибка на позиции: " + position);
                    System.exit(12);
                }
            }
        }
    }

    private void digitValue()
    {
        switch (getLexeme())
        {
            case DIGIT:
                initFlg = true;
                break;
            case VAR:
                if (varValue.containsKey(getLexemeValue()))
                {
                    initFlg = true;
                }
                else
                {
                    System.err.println("Ошибка: значение переменной неизвестно!");
                    initFlg = false;
                    System.exit(14);
                }
                break;
            default:
                System.err.println("Ошибка! Позиция: " + position);
                System.exit(13);
        }
    }

    private boolean value()
    {
        boolean value = false;

        if (getLexeme() == Lexeme.DIGIT || getLexeme() == Lexeme.VAR)
        {
            digitValue();
            if (position < tokenList.size() - 1 && (getLexemeInc() == Lexeme.DIGIT || getLexemeDec() == Lexeme.DIGIT ||
                    getLexemeInc() == Lexeme.VAR || getLexemeDec() == Lexeme.VAR))
            {
                System.err.println("Синтаксическая ошибка на позиции: " + position);
                System.exit(12);
            }
            poliz.add(tokenList.get(position));
            position ++;
            value = true;
        }
        return value;
    }

    private boolean op()
    {
        boolean op = false;

        if (getLexeme() == Lexeme.OP || getLexeme() == Lexeme.L_R_SQU || getLexeme() == Lexeme.R_R_SQU)
        {
            if (equalityOfSQU())
            {
                toStack(getLexeme());
                op = true;
                position++;
            }
        }
        return  op;
    }

    private boolean varHasValue() {
        boolean varHasValue = false;

        if (position < tokenList.size() && getLexemeInc() != Lexeme.SEM)
        poliz.add(tokenList.get(position));
        currentVar = getLexemeValue();
        position++;

        if (position < tokenList.size() && getLexeme() == Lexeme.ASSIGN_OP)
        {
            toStack(getLexeme());
            position++;
            while (position < tokenList.size() && getLexeme() != Lexeme.SEM)
            {
                if (value())
                {
                    varHasValue = true;
                }
                else
                {
                    if (op())
                    {
                       varHasValue = true;
                    }
                    else
                    {
                        System.err.println("Ошибка при инициализации переменной!");
                        System.exit(10);
                    }
                }
            }

            while(!stack.empty())
            {
                poliz.add(stack.pop());
            }
            varValue.put(currentVar, true);
            tableOfValues.put(currentVar, null);
        }
        else
        {
            if (position < tokenList.size() && getLexeme() == Lexeme.SEM)
            {
                varHasValue = true;
            }
        }
        return varHasValue;
    }

    private boolean varHasType() {
        boolean varHasType = false;
        if (!initMap.containsKey(getLexemeValue()))
        {
            if (position > 0)
            {
                if (getLexemeDec() != Lexeme.TYPE && tokenList.get(position-2).getLexeme() != Lexeme.SEM)
                {
                    System.err.println("Ошибка: переменная " + getLexemeValue() +
                            " не была объявлена!");
                    System.exit(3);
                }
                else
                    {
                        initMap.put(getLexemeValue(), getLexemeDec());
                        if (varHasValue())
                    {
                        if(initFlg)
                        position++;
                        varHasType = true;
                    }
                }
            }
            else
                {
                System.err.println("Ошибка: переменная \'" + getLexemeValue() +
                        "\' не была объявлена!");
                System.exit(3);
            }
        }
            else
            {
                if (initMap.containsKey(getLexemeValue()) && initMap.containsValue(getLexemeDec()))
                {
                    System.err.println("Ошибка: переменная " + getLexemeValue() +
                            " уже была объявлена!");
                    System.exit(6);
                }
                else
                {
                    if (varHasValue())
                    {
                        position ++;
                        varHasType = true;
                    }
                }
            }
        return varHasType;
    }


    private boolean declare() {
        boolean declare = false;

        if (position < tokenList.size() && getLexeme() == Lexeme.VAR) {
            if (varHasType())
            {
                declare = true;
            }
        }
        else
        {
            declare = true;
            position++;
        }
        return declare;
    }

    private boolean while_body()
    {
        boolean body = false;

        if (position < tokenList.size() && getLexeme() == Lexeme.L_F_SQU)
        {
            position++;
            while (position < tokenList.size() && getLexeme() != Lexeme.R_F_SQU)
            {
                if (expr())
                {
                    body = true;
                }
                else
                {
                    System.err.println("Ошибка в теле цикла!");
                    System.exit(21);
                }
            }
            poliz.add(tokenList.get(position));
        }
        else
        {
            System.err.println("Ошибка: отсутствует тело цикла!");
            System.exit(20);
        }

        return body;
    }

    private boolean condition_poliz()
    {
        boolean condition_poliz = false;

        switch (getLexeme())
        {
            case DIGIT:
                if (getLexemeInc() == Lexeme.LOG_OP || getLexemeDec() == Lexeme.LOG_OP)
                {
                    poliz.add(tokenList.get(position));
                    if (!stack.empty())
                    {
                        poliz.add(stack.pop());
                    }
                    condition_poliz = true;
                }
                else
                {
                    System.err.println("Ошибка в условии цикла!");
                    System.exit(19);
                }
                break;
            case VAR:
                if (getLexemeInc() == Lexeme.LOG_OP || getLexemeDec() == Lexeme.LOG_OP)
                {
                    if (tableOfValues.containsKey(tokenList.get(position).getValue())) {
                        poliz.add(tokenList.get(position));
                        if (!stack.empty()) {
                        poliz.add(stack.pop());
                    }
                    condition_poliz = true;
                }
                else {
                    System.err.println("Ошибка: значение переменной \'" + getLexemeValue() + "\' неизвестно!");
                    System.exit(18);
                    }
            }
                else
                {
                    System.err.println("Ошибка в условии цикла!");
                    System.exit(19);
                }
                break;
            case LOG_OP:
                if (getLexemeDec() == Lexeme.DIGIT || getLexemeDec() == Lexeme.VAR || getLexemeInc() == Lexeme.DIGIT ||
                        getLexemeDec() == Lexeme.VAR) {
                    stack.push(tokenList.get(position));
                    condition_poliz = true;
                }
                else {
                    System.err.println("Ошибка в условии цикла!");
                    System.exit(19);
                }
                break;
            default:
                System.err.println("Ошибка в условии цикла!");
                System.exit(19);
                break;
        }

        return condition_poliz;
    }

    private boolean while_condition()
    {
        boolean condition = false;

        if (position < tokenList.size() && getLexeme() == Lexeme.L_R_SQU && getLexemeInc() != Lexeme.R_R_SQU)
        {
            position++;
            while(position < tokenList.size() && getLexeme() != Lexeme.R_R_SQU)
            {
                condition_poliz();
                position++;
            }
            poliz.add(currentToken);
        }
        else
        {
            System.err.println("Ошибка! Отсутствует условие для цикла!");
            System.exit(17);
        }
        position++;
        if (while_body())
        {
            condition = true;
        }

        return condition;
    }

    private boolean while_loop()
    {
        boolean while_loop = false;

        if (position < tokenList.size() && getLexeme() == Lexeme.WHILE)
        {
            currentToken = tokenList.get(position);
            position++;
            if(while_condition())
            {
                while_loop = true;
            }
            else
            {
                System.err.println("Ошибка! Позиция: " + position);
                System.exit(16);
            }
        }

        return while_loop;
    }

    private boolean expr() {
        boolean expr = false;
        if(position < tokenList.size() && getLexeme() != Lexeme.WHILE) {
            if (declare()) {
                expr = true;
            }
        }
        else
        {
            if(while_loop())
            {
                expr = true;
            }
        }
        return expr;
    }


    public List<Token> lang(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.getLexeme() != Lexeme.WS) {
                tokenList.add(token);
            }
        }
        while(position < tokenList.size()) {
            if (expr()) {
            } else {
                System.err.println("Синтаксичкеская ошибка!"+position);
                System.exit(4);
            }
        }

        System.out.print("ОПЗ: " + "[ ");
        for (Token token : poliz)
        {
            System.out.print(token.getValue() + ", ");
        }
        System.out.print("]");
        System.out.print("\n");

        return poliz;
    }
}

